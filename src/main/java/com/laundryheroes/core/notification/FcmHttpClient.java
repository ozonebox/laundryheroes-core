package com.laundryheroes.core.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class FcmHttpClient {

    private final GoogleOAuthService oauth;
    private final FcmProperties props;
    private final ObjectMapper mapper = new ObjectMapper();

    public void sendIosPush(
    String token,
    String title,
    String body,
    Map<String, Object> data // 👈 now accepts Object values
) {

    try {
        log.info("📤 Sending FCM iOS push to token {}", maskToken(token));

        String accessToken = oauth.getAccessToken();

        // 🔁 Convert Map<String, Object> → Map<String, String> for FCM
        Map<String, String> stringData =
            data == null
                ? Map.of()
                : data.entrySet().stream()
                    .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() == null
                            ? ""
                            : String.valueOf(e.getValue())
                    ));

        Map<String, Object> message = Map.of(
            "token", token,
            "notification", Map.of(
                "title", title,
                "body", body
            ),
            "apns", Map.of(
                "headers", Map.of(
                    "apns-push-type", "alert",
                    "apns-priority", "10"
                ),
                "payload", Map.of(
                    "aps", Map.of(
                        "sound", "default"
                    )
                )
            ),
            "data", stringData
        );

        Map<String, Object> payload = Map.of("message", message);

        byte[] json = mapper.writeValueAsBytes(payload);

        String url =
            "https://fcm.googleapis.com/v1/projects/"
                + props.getProjectId()
                + "/messages:send";

        HttpURLConnection conn =
            (HttpURLConnection) new URL(url).openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty(
            "Authorization", "Bearer " + accessToken
        );
        conn.setRequestProperty(
            "Content-Type", "application/json; charset=UTF-8"
        );

        conn.getOutputStream().write(json);

        int status = conn.getResponseCode();

        if (status >= 400) {
            String error = new String(
                conn.getErrorStream().readAllBytes(),
                StandardCharsets.UTF_8
            );
            log.error("❌ FCM HTTP error {}→{}", status, error);
            throw new IllegalStateException(error);
        }

        log.info("✅ FCM push sent successfully");

    } catch (Exception e) {
        log.error("❌ FCM HTTP send failed", e);
        throw new IllegalStateException("FCM HTTP send failed", e);
    }
}

    private String maskToken(String token) {
        if (token == null || token.length() < 10) return "***";
        return token.substring(0, 6) + "****" + token.substring(token.length() - 4);
    }
}
