/*
 * This file is part of Baritone.
 *
 * Baritone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Baritone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Baritone.  If not, see <https://www.gnu.org/licenses/>.
 */

package baritone.command.defaults;

import baritone.api.IBaritone;
import baritone.api.command.Command;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.exception.CommandException;
import baritone.seedore.SeedOreService;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * Manual configuration and status command for SeedOre.
 *
 * The command deliberately does not initiate prediction until a version-matched
 * world-generation backend is registered. It never reads hidden server blocks.
 */
public final class SeedOreCommand extends Command {

    private final SeedOreService service = SeedOreService.getInstance();

    public SeedOreCommand(IBaritone baritone) {
        super(baritone, "seedore");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        if (!args.hasAny()) {
            printUsage();
            return;
        }

        String action = args.getString().toLowerCase(Locale.ROOT);
        switch (action) {
            case "set" -> {
                args.requireMin(1);
                String rawSeed = args.getString();
                args.requireMax(0);
                try {
                    service.setSeed(Long.parseLong(rawSeed));
                    logDirect("SeedOre: manual seed stored.");
                } catch (NumberFormatException exception) {
                    logDirect("SeedOre: seed must be a signed 64-bit integer.");
                }
            }
            case "radius" -> {
                args.requireMin(1);
                String rawRadius = args.getString();
                args.requireMax(0);
                try {
                    service.setRadius(Integer.parseInt(rawRadius));
                    logDirect("SeedOre: scan radius set to " + service.getConfig().radius() + " blocks.");
                } catch (IllegalArgumentException exception) {
                    logDirect("SeedOre: radius must be between 64 and 10000 blocks.");
                }
            }
            case "dimension" -> {
                args.requireMin(1);
                String dimension = args.getString();
                args.requireMax(0);
                try {
                    service.setDimension(dimension);
                    logDirect("SeedOre: dimension set to " + service.getConfig().dimension() + ".");
                } catch (IllegalArgumentException exception) {
                    logDirect("SeedOre: supported dimensions are overworld, nether and end.");
                }
            }
            case "authorize-server" -> {
                args.requireMin(1);
                String address = args.getString();
                args.requireMax(0);
                try {
                    service.authorizeServer(address);
                    logDirect("SeedOre: local authorization allowlist updated for " + address + ".");
                    logDirect("This is a local declaration, not proof of server-owner permission.");
                } catch (IllegalArgumentException exception) {
                    logDirect("SeedOre: enter a valid server hostname or IP address.");
                }
            }
            case "status" -> {
                args.requireMax(0);
                logDirect(service.describeStatus());
            }
            case "clear" -> {
                args.requireMax(0);
                service.clearSeed();
                logDirect("SeedOre: manual seed cleared.");
            }
            case "scan" -> {
                args.requireMax(0);
                if (!service.getConfig().hasSeed()) {
                    logDirect("SeedOre: set a seed first with #seedore set <seed>.");
                } else {
                    logDirect("SeedOre: no Minecraft 1.21.11 world-generation backend is registered yet.");
                    logDirect("No coordinates were fabricated and no hidden server blocks were inspected.");
                }
            }
            default -> printUsage();
        }
    }

    private void printUsage() {
        logDirect("SeedOre commands:");
        logDirect("#seedore set <seed> | radius <64-10000> | dimension <overworld|nether|end>");
        logDirect("#seedore authorize-server <host> | status | scan | clear");
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) {
        if (!args.hasAny()) {
            return Stream.of("set", "radius", "dimension", "authorize-server", "status", "scan", "clear");
        }
        if (args.hasExactly(1)) {
            String action = args.getArgs().getFirst().getValue().toLowerCase(Locale.ROOT);
            if ("dimension".equals(action)) {
                return Stream.of("overworld", "nether", "end");
            }
        }
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "Configure SeedOre seed-based world-generation analysis";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList(
                "SeedOre accepts a manually supplied world seed and configures a version-specific ore-generation analysis.",
                "It does not bypass anti-xray, inspect hidden server blocks, or infer coordinates from packets.",
                "",
                "Usage:",
                "> seedore set <seed> - Store a signed 64-bit world seed.",
                "> seedore radius <blocks> - Set a scan radius from 64 to 10000 blocks.",
                "> seedore dimension <overworld|nether|end> - Select a dimension.",
                "> seedore authorize-server <host> - Add a server to the local authorization allowlist.",
                "> seedore status - Display configuration and safety state.",
                "> seedore scan - Run analysis when a compatible backend is installed.",
                "> seedore clear - Remove the manually entered seed."
        );
    }
}
