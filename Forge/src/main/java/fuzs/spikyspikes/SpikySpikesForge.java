package fuzs.spikyspikes;

import fuzs.puzzleslib.core.CoreServices;
import fuzs.spikyspikes.data.*;
import fuzs.spikyspikes.handler.ItemCombinerHandler;
import fuzs.spikyspikes.handler.SpikeLootHandler;
import fuzs.spikyspikes.init.ForgeModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Optional;

@Mod(SpikySpikes.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpikySpikesForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CoreServices.FACTORIES.modConstructor(SpikySpikes.MOD_ID).accept(new SpikySpikes());
        ForgeModRegistry.touch();
        registerHandlers();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final LootingLevelEvent evt) -> {
            SpikeLootHandler.onLootingLevel(evt.getEntityLiving(), evt.getDamageSource(), evt.getLootingLevel()).ifPresent(evt::setLootingLevel);
        });
        MinecraftForge.EVENT_BUS.addListener((final AnvilUpdateEvent evt) -> {
            MutableObject<ItemStack> output = new MutableObject<>(evt.getOutput());
            MutableInt cost = new MutableInt(evt.getCost());
            MutableInt materialCost = new MutableInt(evt.getMaterialCost());
            Optional<Unit> result = ItemCombinerHandler.onAnvilUpdate(evt.getLeft(), evt.getRight(), output, evt.getName(), cost, materialCost, evt.getPlayer());
            if (result.isPresent()) {
                evt.setCanceled(true);
            } else {
                evt.setOutput(output.getValue());
                evt.setCost(cost.intValue());
                evt.setMaterialCost(materialCost.intValue());
            }
        });
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        final ExistingFileHelper existingFileHelper = evt.getExistingFileHelper();
        generator.addProvider(new ModLootTableProvider(generator, SpikySpikes.MOD_ID));
        generator.addProvider(new ModRecipeProvider(generator, SpikySpikes.MOD_ID));
        generator.addProvider(new ModBlockTagsProvider(generator, SpikySpikes.MOD_ID, existingFileHelper));
        generator.addProvider(new ModLanguageProvider(generator, SpikySpikes.MOD_ID));
        generator.addProvider(new ModBlockStateProvider(generator, SpikySpikes.MOD_ID, existingFileHelper));
        generator.addProvider(new ModItemModelProvider(generator, SpikySpikes.MOD_ID, existingFileHelper));
    }
}
