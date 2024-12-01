package org.huydd.bus_ticket_Ecommercial_platform.services;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Set;

@Service
@PropertySource("classpath:application.properties")
public class GoogleOauth2Service {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String authorizeUri;


    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenUri;


    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String userInfoUri;

    private Set<String> scopes = Set.of("https://www.googleapis.com/auth/userinfo.email",
                                        "https://www.googleapis.com/auth/userinfo.profile",
                                        "openid");

    public String createAuthorizationUrl(String state) {
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow .Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                clientId,
                clientSecret,
                scopes).setAccessType("offline").build();

        return flow.newAuthorizationUrl()
                .setState(state)
                .setRedirectUri(redirectUri)
                .build();
    }

    public UserDTO exchangeAuthorizationCode(String code) throws IOException, GeneralSecurityException {
        try {
            GoogleAuthorizationCodeTokenRequest tokenRequest = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    clientId,
                    clientSecret,
                    code,
                    redirectUri
            );
            GoogleTokenResponse tokenResponse = tokenRequest.execute();
            String token = tokenResponse.getIdToken();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            ).setAudience(Collections.singletonList(clientId)).build();
            GoogleIdToken idToken = verifier.verify(token);
            GoogleIdToken.Payload payload = idToken.getPayload();

            return UserDTO.builder()
                    .email(payload.getEmail())
                    .firstname((String) payload.get("given_name"))
                    .lastname((String) payload.get("family_name"))
                    .username(payload.getSubject())
                    .avatar((String) payload.get("picture"))
                    .build();
        }
        catch (RuntimeException e) {
            return null;
        }
    }

}
