package com.lww.common.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Servlet request utils.
 *
 * @author lww
 */
public class ServletRequestUtil {

    private static final String UNKNOWN = "unknown";

    private static final String[] CLIENT_IP_HEADERS = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    private ServletRequestUtil() {
    }

    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        for (String header : CLIENT_IP_HEADERS) {
            String ip = resolveHeaderIp(request.getHeader(header));
            if (isValidIp(ip)) {
                return ip;
            }
        }

        return request.getRemoteAddr();
    }

    private static String resolveHeaderIp(String headerValue) {
        if (!isValidIp(headerValue)) {
            return null;
        }

        String[] ips = headerValue.split(",");
        for (String ip : ips) {
            String trimmedIp = ip.trim();
            if (isValidIp(trimmedIp)) {
                return trimmedIp;
            }
        }
        return null;
    }

    private static boolean isValidIp(String ip) {
        return ip != null && !ip.isBlank() && !UNKNOWN.equalsIgnoreCase(ip);
    }
}
