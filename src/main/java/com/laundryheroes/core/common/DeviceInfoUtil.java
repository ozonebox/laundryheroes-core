package com.laundryheroes.core.common;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

public final class DeviceInfoUtil {

    private DeviceInfoUtil() {
    }

    public static String buildDeviceLabel(String userAgentHeader) {
        if (userAgentHeader == null || userAgentHeader.isBlank()) {
            return "Unknown device";
        }

        UserAgent ua = UserAgent.parseUserAgentString(userAgentHeader);
        OperatingSystem os = ua.getOperatingSystem();
        Browser browser = ua.getBrowser();

        String osName = os != null ? os.getName() : "Unknown OS";
        String browserName = browser != null ? browser.getName() : "Unknown browser";

        return osName + " · " + browserName;
    }
}
