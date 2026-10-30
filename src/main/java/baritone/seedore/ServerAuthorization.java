/*
 * This file is part of Baritone.
 * Licensed under the GNU Lesser General Public License v3 or later.
 */
package baritone.seedore;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

/**
 * Local allowlist helper. An allowlist entry is the user's declaration that
 * they have permission to use seed analysis on that server; it cannot prove
 * permission or override server-side anti-xray rules.
 */
public final class ServerAuthorization {
    private ServerAuthorization() {}

    public static String normalizeHost(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("empty host");
        }

        String value = input.trim();
        if (!value.contains("://")) {
            value = "https://" + value;
        }

        try {
            URI uri = new URI(value);
            String host = uri.getHost();
            if (host == null || host.isBlank() || uri.getUserInfo() != null) {
                throw new IllegalArgumentException("invalid host");
            }
            return host.toLowerCase(Locale.ROOT);
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException("invalid host", exception);
        }
    }

    public static boolean isAuthorized(SeedOreConfig config, String serverAddress) {
        if (config == null || serverAddress == null || serverAddress.isBlank()) {
            return false;
        }
        try {
            return config.authorizedServers().contains(normalizeHost(serverAddress));
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }
}
