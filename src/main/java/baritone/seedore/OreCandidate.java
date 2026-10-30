/*
 * This file is part of Baritone.
 * Licensed under the GNU Lesser General Public License v3 or later.
 */
package baritone.seedore;

import java.util.Objects;

/** A candidate position emitted by a version-matched world-generation backend. */
public record OreCandidate(
        String dimension,
        String oreId,
        int x,
        int y,
        int z,
        String featureId,
        PredictionStatus status
) {
    public OreCandidate {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(oreId, "oreId");
        Objects.requireNonNull(featureId, "featureId");
        Objects.requireNonNull(status, "status");
    }
}
