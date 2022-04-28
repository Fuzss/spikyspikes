package fuzs.spikyspikes.world.damagesource;

import fuzs.spikyspikes.world.level.block.SpikeBlock;
import net.minecraft.world.damagesource.DamageSource;

public class SpikeDamageSource extends DamageSource {
    private final SpikeBlock.SpikeMaterial spikeMaterial;

    public SpikeDamageSource(String identifier, SpikeBlock.SpikeMaterial spikeMaterial) {
        super(identifier);
        this.spikeMaterial = spikeMaterial;
    }

    public boolean dropsLoot() {
        return this.spikeMaterial.dropsLoot();
    }
}
