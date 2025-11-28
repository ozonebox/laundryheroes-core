package com.laundryheroes.core.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(FcmProperties.class)
public class FirebaseInitializer {

    private final FcmProperties props;

    @PostConstruct
    public void init() {
        try (InputStream is =
                     new ClassPathResource(props.getServiceAccountPath())
                             .getInputStream()) {

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(is))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (Exception e) {
            throw new IllegalStateException(
                "Failed to initialize Firebase. Check service account path.",
                e
            );
        }
    }
}
