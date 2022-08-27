package fuzs.spikyspikes.world.damagesource;

import fuzs.spikyspikes.api.world.damagesource.PlayerDamageSource;
import net.minecraft.world.damagesource.DamageSource;

public class SpikePlayerDamageSource extends DamageSource implements PlayerDamageSource {
    private static final String SPIKE_DAMAGE_SOURCE_KEY = "spike";
    public static final DamageSource SPIKE_DAMAGE_SOURCE = new DamageSource(SPIKE_DAMAGE_SOURCE_KEY);

    private final int lootingLevel;

    private SpikePlayerDamageSource(int lootingLevel) {
        super(SPIKE_DAMAGE_SOURCE_KEY);
        this.lootingLevel = lootingLevel;
    }

    @Override
    public int lootingLevel() {
        return this.lootingLevel;
    }

    public static DamageSource spikePlayer(int lootingLevel) {
        return new SpikePlayerDamageSource(lootingLevel);
    }
}
