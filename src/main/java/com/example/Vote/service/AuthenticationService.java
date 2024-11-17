package com.example.Vote.service;

import com.example.Vote.Exception.AppException;
import com.example.Vote.Exception.ErorrCode;
import com.example.Vote.dto.request.AuthenticatuionRequest;
import com.example.Vote.dto.request.IntrospectRequest;
import com.example.Vote.dto.request.LogoutRequest;
import com.example.Vote.dto.request.RefreshTokenRequest;
import com.example.Vote.dto.response.AuthenticationResponse;
import com.example.Vote.dto.response.IntrospectResponse;
import com.example.Vote.entity.InvalidatedToken;
import com.example.Vote.entity.User;
import com.example.Vote.repository.InvalidatedTokenRepository;
import com.example.Vote.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Log4j2
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService {
    @NonFinal
    String refresh;
    @NonFinal
    String access;
    @Autowired
    UserRepository userRepository;
    @Autowired
    InvalidatedTokenRepository invalidatedTokenRepository;
    protected static final String SIGNER_KEY = "25hdy5/xUIEI/A8Q8XOxlIP4AGJLjk9B83Tah5uWyKAZa6+a8I40qpC2cvrCr3V25hdy5/xUIEI/A8Q8XOxlIP4AGJLjk9B83Tah5uWyKAZa6+a8I40qpC2cvrCr3V";

    public AuthenticationResponse authenticate(AuthenticatuionRequest request) throws ParseException, JOSEException {
        User user = userRepository.findByUsername(request.getUsername())
             .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_EXIST));
        String token = generateToken(user, 30);
        String refreshToken = generateRefreshToken(user);
        refresh = refreshToken;
        access = token;
        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    public AuthenticationResponse getAcessTokenByRefreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        if(!request.getRefreshToken().equals(refresh)) throw new AppException(ErorrCode.UNAUTHORIZED);
        SignedJWT signedJTW = SignedJWT.parse(refresh);
        User user = userRepository.findByUsername(signedJTW.getJWTClaimsSet().getSubject())
             .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_EXIST));
        String token = generateToken(user, 30);
        String refreshToken = generateRefreshToken(user);
        SignedJWT verifyToken = verifyToken(access);
        String jti = verifyToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = verifyToken.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
        refresh = refreshToken;
        access = token;
        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }


    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        String token = request.getToken();
        boolean isValid = true;
        try{
            verifyToken(token);
        }catch (AppException e){
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT verifyToken = verifyToken(request.getToken());
        String jti = verifyToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = verifyToken.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }


    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJTW = SignedJWT.parse(token);
        var verified = signedJTW.verify(verifier);
        Date expiretime = signedJTW.getJWTClaimsSet().getExpirationTime();
        if(!verified && expiretime.after(new Date()))
            throw new AppException(ErorrCode.UNAUTHENTICATED);
        if(invalidatedTokenRepository.existsById(signedJTW.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErorrCode.UNAUTHENTICATED);
        return signedJTW;
    }

    private String generateToken(User user, int minute){
        JWSHeader jwsHeader= new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("SHB")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(minute, ChronoUnit.MINUTES).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try{
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }catch (JOSEException e){
            throw new RuntimeException(e);
        }
    }
    private String generateRefreshToken(User user){
        JWSHeader jwsHeader= new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(120, ChronoUnit.MINUTES).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try{
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }catch (JOSEException e){
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role -> {
                stringJoiner.add(role.getName());
            });
        }
        return stringJoiner.toString();
    }
}
