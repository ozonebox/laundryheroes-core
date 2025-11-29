package com.laundryheroes.core.notification;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fcm")
@Data
public class FcmProperties {

    private String projectId;
    private String serviceAccountJson;
}
