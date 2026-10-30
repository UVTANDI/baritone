/*
 * This file is part of Baritone.
 * Licensed under the GNU Lesser General Public License v3 or later.
 */
package baritone.seedore;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.pathing.goals.GoalBlock;

/** Sends a trusted SeedOre candidate to Baritone's existing goal process. */
public final class OreGoalController {
    private OreGoalController() {}

    public static void navigateTo(OreCandidate candidate) {
        if (candidate == null) {
            throw new IllegalArgumentException("candidate is required");
        }
        if (candidate.status() == PredictionStatus.UNVERIFIED
                || candidate.status() == PredictionStatus.PROFILE_MISMATCH) {
            throw new IllegalStateException("Refusing to navigate to an untrusted SeedOre candidate");
        }

        IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
        baritone.getCustomGoalProcess().setGoalAndPath(
                new GoalBlock(candidate.x(), candidate.y(), candidate.z())
        );
    }
}
