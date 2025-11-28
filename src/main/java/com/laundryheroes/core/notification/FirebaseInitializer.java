package com.laundryheroes.core.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class FirebaseInitializer {

    private final FcmProperties props;
    private final Environment environment;
    @PostConstruct
    public void init() {
        try {
            if (isProfileActive("ci", "test")) {
            //log.info("Firebase initialization skipped (CI/Test profile)");
            return;
            }
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
    private boolean isProfileActive(String... profiles) {
        return Arrays.stream(environment.getActiveProfiles())
            .anyMatch(p -> Arrays.asList(profiles).contains(p));
    }
}
