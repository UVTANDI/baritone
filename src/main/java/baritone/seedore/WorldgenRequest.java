/*
 * This file is part of Baritone.
 * Licensed under the GNU Lesser General Public License v3 or later.
 */
package baritone.seedore;

import java.util.Set;

/** Immutable request passed to a Minecraft-version-specific generation adapter. */
public record WorldgenRequest(
        long seed,
        String minecraftVersion,
        String dimension,
        int radius,
        Set<String> oreIds
) {
    public WorldgenRequest {
        if (!"1.21.11".equals(minecraftVersion)) {
            throw new IllegalArgumentException("SeedOre backend requires Minecraft 1.21.11");
        }
        if (radius < 64 || radius > 10_000) {
            throw new IllegalArgumentException("radius must be between 64 and 10000");
        }
        oreIds = Set.copyOf(oreIds);
    }
}
