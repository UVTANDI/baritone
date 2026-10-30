# SeedOre Intelligence (Minecraft 1.21.11)

SeedOre is an opt-in, manual-seed world-generation analysis module for Baritone.
The first integration milestone adds the command/configuration layer, explicit
server allowlisting, prediction data contracts and Baritone goal integration.

## Safety model

- Single-player analysis is permitted when a seed has been entered.
- Multiplayer analysis is disabled unless the user explicitly adds the server
  hostname to the local allowlist with `#seedore authorize-server <host>`.
- An allowlist is only a local declaration. It is not proof of server-owner
  permission and does not override server rules.
- No packet probing, hidden-block inspection, anti-xray bypass, server exploit,
  or client-side attempt to reveal concealed blocks is implemented.
- A prediction is not treated as verified until checked against legitimate
  local world data.

## Commands

- `#seedore set <signed-64-bit-seed>`
- `#seedore radius <64-10000>`
- `#seedore dimension <overworld|nether|end>`
- `#seedore authorize-server <hostname>`
- `#seedore status`
- `#seedore scan`
- `#seedore clear`

The scan command intentionally reports that no prediction backend is registered
until the version-specific adapter is implemented. It does not emit guessed
coordinates.

## Architecture

- `SeedOreConfig`: immutable manual seed, dimension, radius and server allowlist.
- `SeedOreService`: thread-safe configuration owner.
- `AnalysisPolicy` / `ServerAuthorization`: multiplayer safety gate.
- `WorldgenRequest`: complete deterministic scan input.
- `WorldgenPredictionBackend`: adapter contract for Minecraft 1.21.11 worldgen.
- `OreCandidate`: typed result with explicit trust status.
- `OreGoalController`: routes trusted candidates through Baritone's public goal process.
- `SeedOreCommand`: registers the `#seedore` command.

## Exact prediction requirements

A production backend must use the matching 1.21.11 registries and worldgen
pipeline: biome source, dimension settings, random state, configured features,
placed features and placement modifiers. It must also fingerprint relevant
datapack/registry settings and reject mismatched profiles. A generic Y-level
table or arbitrary seeded PRNG is not an exact ore predictor.

## Build target

This branch is configured for Fabric + Minecraft 1.21.11 + Java 21. The existing
repository uses the Unimined build plugin; the Fabric 1.21.11 toolchain migration
and full compile must be validated in CI before distributing a JAR. Forge and
NeoForge source directories are retained but excluded from this branch's build
until their loader-specific ports are completed.
