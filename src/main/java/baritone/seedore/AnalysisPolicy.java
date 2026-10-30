/*
 * This file is part of Baritone.
 * Licensed under the GNU Lesser General Public License v3 or later.
 */
package baritone.seedore;

/** Explicit environment policy for SeedOre analysis. */
public final class AnalysisPolicy {
    private AnalysisPolicy() {}

    /**
     * Single-player analysis is permitted. Multiplayer analysis requires the
     * current server hostname to be on the user's local allowlist.
     */
    public static boolean mayAnalyze(
            boolean singlePlayer,
            String serverAddress,
            SeedOreConfig config
    ) {
        if (config == null || !config.hasSeed()) {
            return false;
        }
        if (singlePlayer) {
            return true;
        }
        return ServerAuthorization.isAuthorized(config, serverAddress);
    }
}
