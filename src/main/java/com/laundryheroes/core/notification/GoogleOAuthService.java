package com.laundryheroes.core.notification;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthService {

    private final FcmProperties props;

    public String getAccessToken() {
        try {
            String json = props.getServiceAccountJson();

            if (json == null || json.isBlank()) {
                throw new IllegalStateException("FIREBASE_SERVICE_ACCOUNT_JSON / fcm.service-account-json is empty");
            }

            GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)))
                .createScoped(Collections.singletonList(
                    "https://www.googleapis.com/auth/firebase.messaging"
                ));

            AccessToken token = credentials.refreshAccessToken();

            if (token == null || token.getTokenValue() == null || token.getTokenValue().isBlank()) {
                throw new IllegalStateException("GoogleCredentials.refreshAccessToken() returned null/empty token");
            }

            log.info("✅ Google access token obtained");
            return token.getTokenValue();

        } catch (Exception e) {
            log.error("❌ Failed to obtain Google access token", e);
            throw new IllegalStateException("Failed to obtain Google access token", e);
        }
    }
}
