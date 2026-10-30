/*
 * This file is part of Baritone.
 * Licensed under the GNU Lesser General Public License v3 or later.
 */
package baritone.seedore;

import java.util.List;
import java.util.function.DoubleConsumer;

/**
 * Adapter boundary for the exact 1.21.11 configured/placed-feature pipeline.
 * Implementations must use Minecraft's registered biome sources, chunk
 * generator, random-state and placed-feature definitions. Do not substitute
 * guessed coordinates or generic ore-height heuristics.
 */
public interface WorldgenPredictionBackend {
    List<OreCandidate> predict(WorldgenRequest request, DoubleConsumer progress);

    /** Stable identifier for the generator/registry profile used by this backend. */
    String profileFingerprint();
}
