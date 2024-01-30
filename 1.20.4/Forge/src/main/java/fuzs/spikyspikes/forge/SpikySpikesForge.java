package fuzs.spikyspikes.forge;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.forge.init.ForgeModRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(SpikySpikes.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpikySpikesForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ForgeModRegistry.touch();
        ModConstructor.construct(SpikySpikes.MOD_ID, SpikySpikes::new);
    }
}
