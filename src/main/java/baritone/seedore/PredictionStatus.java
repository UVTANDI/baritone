/*
 * This file is part of Baritone.
 * Licensed under the GNU Lesser General Public License v3 or later.
 */
package baritone.seedore;

/** Trust state for a generated ore candidate. */
public enum PredictionStatus {
    SIMULATED,
    VERIFIED_IN_LOCAL_WORLD,
    UNVERIFIED,
    PROFILE_MISMATCH
}
