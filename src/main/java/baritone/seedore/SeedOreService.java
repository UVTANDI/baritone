/*
 * This file is part of Baritone.
 * Licensed under the GNU Lesser General Public License v3 or later.
 */
package baritone.seedore;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Thread-safe configuration owner for SeedOre. */
public final class SeedOreService {
    private static final SeedOreService INSTANCE = new SeedOreService();

    private volatile SeedOreConfig config = SeedOreConfig.defaults();

    private SeedOreService() {}

    public static SeedOreService getInstance() {
        return INSTANCE;
    }

    public SeedOreConfig getConfig() {
        return config;
    }

    public synchronized void setSeed(long seed) {
        SeedOreConfig old = config;
        config = new SeedOreConfig(seed, old.dimension(), old.radius(), old.authorizedServers());
    }

    public synchronized void clearSeed() {
        SeedOreConfig old = config;
        config = new SeedOreConfig(null, old.dimension(), old.radius(), old.authorizedServers());
    }

    public synchronized void setRadius(int radius) {
        SeedOreConfig old = config;
        config = new SeedOreConfig(old.seed(), old.dimension(), radius, old.authorizedServers());
    }

    public synchronized void setDimension(String dimension) {
        String normalized = dimension == null ? "" : dimension.trim().toLowerCase();
        if (!Set.of("overworld", "nether", "end").contains(normalized)) {
            throw new IllegalArgumentException("unsupported dimension");
        }
        SeedOreConfig old = config;
        config = new SeedOreConfig(old.seed(), normalized, old.radius(), old.authorizedServers());
    }

    public synchronized void authorizeServer(String address) {
        String host = ServerAuthorization.normalizeHost(address);
        SeedOreConfig old = config;
        Set<String> hosts = new LinkedHashSet<>(old.authorizedServers());
        hosts.add(host);
        config = new SeedOreConfig(old.seed(), old.dimension(), old.radius(), hosts);
    }

    public List<String> getAuthorizedServers() {
        return config.authorizedServers().stream().sorted().toList();
    }

    public String describeStatus() {
        SeedOreConfig current = config;
        return "SeedOre | seed=" + (current.hasSeed() ? current.seed() : "not set")
                + " | dimension=" + current.dimension()
                + " | radius=" + current.radius()
                + " | authorized servers=" + current.authorizedServers().size()
                + " | prediction backend=not registered";
    }
}
