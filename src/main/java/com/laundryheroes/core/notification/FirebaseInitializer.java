package com.laundryheroes.core.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class FirebaseInitializer {

    private final FcmProperties props;

    @PostConstruct
    public void init() {
        try {
            String serviceAccountJson = props.getServiceAccountJson();

            if (serviceAccountJson == null || serviceAccountJson.isBlank()) {
                throw new IllegalStateException("Firebase service account JSON is not set");
            }

            GoogleCredentials credentials =
                    GoogleCredentials.fromStream(
                            new ByteArrayInputStream(
                                    serviceAccountJson.getBytes(StandardCharsets.UTF_8)
                            )
                    );

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to initialize Firebase Admin SDK",
                    e
            );
        }
    }
}
