package com.laundryheroes.core.notification;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fcm")
public class FcmProperties {

    /**
     * Path to firebase service account json
     * Example: firebase/firebase-service-account.json
     * (relative to src/main/resources)
     */
    private String serviceAccountPath;

    public String getServiceAccountPath() {
        return serviceAccountPath;
    }

    public void setServiceAccountPath(String serviceAccountPath) {
        this.serviceAccountPath = serviceAccountPath;
    }
}
