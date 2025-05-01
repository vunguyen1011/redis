package com.JPA.redis.Service;

import com.JPA.redis.DTO.Request.RefreshToken;
import com.JPA.redis.Exception.ErrorCode;
import com.JPA.redis.Exception.WebException;
import com.JPA.redis.DTO.Request.CreateUserRequest;
import com.JPA.redis.DTO.Response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public TokenResponse signIn(CreateUserRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String accessToken = jwtService.generateAccessToken(request.getUsername());
        String refreshToken = jwtService.generateRefreshToken(request.getUsername());


        String refreshJti = jwtService.extractTokenId(refreshToken);


        tokenService.saveRefreshToken(request.getUsername(), refreshJti);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenResponse refreshToken(RefreshToken request) {
        String refreshToken = request.getRefreshToken();
        String username = request.getUsername();

        if (!jwtService.isTokenValid(refreshToken, username)) {
            throw new WebException(ErrorCode.TOKEN_CAN_NOT_BE_REFRESHED);
        }


        String tokenJti = jwtService.extractTokenId(refreshToken);
        if (!tokenService.isRefreshTokenValid(username, tokenJti)) {
            throw new WebException(ErrorCode.TOKEN_REVOKED);
        }

        String newAccessToken = jwtService.generateAccessToken(username);


        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // vẫn dùng refresh token cũ
                .build();
    }

    public void logout(String token) {
        String username = jwtService.extractUsername(token);
        String jti = jwtService.extractTokenId(token);
        long expiration = jwtService.getJwtExpiration(token);
        long ttlMillis = expiration - System.currentTimeMillis();
        if (ttlMillis > 0) {
            tokenService.blacklistToken(jti, ttlMillis);
        }
        tokenService.revokeRefreshToken(username);
    }

}
