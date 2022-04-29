package fuzs.spikyspikes.handler;

import fuzs.spikyspikes.world.damagesource.SpikeEntityDamageSource;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SpikeLootHandler {

    @SubscribeEvent
    public void onLootingLevel(final LootingLevelEvent evt) {
        if (evt.getDamageSource() instanceof SpikeEntityDamageSource source && source.lootingLevel > 0) {
            // our fake player does not use an item for killing, so this needs to be set manually
            evt.setLootingLevel(source.lootingLevel);
        }
    }
}
