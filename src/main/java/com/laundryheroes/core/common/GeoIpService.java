package com.laundryheroes.core.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class GeoIpService {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Resolve country name from IP address.
     * Returns "Unknown country" if anything fails.
     */
    public String resolveCountry(String ipAddress) {
        if (ipAddress == null || ipAddress.isBlank()) {
            return "Unknown country";
        }

        try {
            // Simple, free-style API. For production, move URL to config.
            String url = "https://ipapi.co/" + ipAddress + "/country_name/";

            String response = restTemplate.getForObject(url, String.class);

            if (response == null || response.isBlank() || response.toLowerCase().contains("error")) {
                return "Unknown country";
            }

            return response.trim();

        } catch (RestClientException ex) {
            log.warn("Failed to resolve country for IP {}: {}", ipAddress, ex.getMessage());
            return "Unknown country";
        }
    }
}
