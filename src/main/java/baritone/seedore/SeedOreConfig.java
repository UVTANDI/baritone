/*
 * This file is part of Baritone.
 * Licensed under the GNU Lesser General Public License v3 or later.
 */
package baritone.seedore;

import java.util.Set;

/** Immutable SeedOre configuration. */
public record SeedOreConfig(
        Long seed,
        String dimension,
        int radius,
        Set<String> authorizedServers
) {
    public SeedOreConfig {
        dimension = dimension == null ? "overworld" : dimension;
        authorizedServers = authorizedServers == null ? Set.of() : Set.copyOf(authorizedServers);
        if (radius < 64 || radius > 10_000) {
            throw new IllegalArgumentException("radius must be between 64 and 10000");
        }
    }

    public boolean hasSeed() {
        return seed != null;
    }

    public static SeedOreConfig defaults() {
        return new SeedOreConfig(null, "overworld", 1_000, Set.of());
    }
}
