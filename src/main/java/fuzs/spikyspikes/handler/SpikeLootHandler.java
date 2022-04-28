package fuzs.spikyspikes.handler;

import fuzs.spikyspikes.world.damagesource.SpikeDamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SpikeLootHandler {

    @SubscribeEvent
    public void onLivingDrops(final LivingDropsEvent evt) {
        // this also prevents equipment (e.g. saddles, not e.g. equipment from natural spawning) from dropping unfortunately and unlike doMobLoot
        // but probably better than messing with the game rule
        if (evt.getSource() instanceof SpikeDamageSource source && !source.dropsLoot()) {
            evt.setCanceled(true);
        }
    }
}
