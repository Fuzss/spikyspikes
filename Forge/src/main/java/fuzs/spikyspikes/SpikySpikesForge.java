package fuzs.spikyspikes;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.spikyspikes.data.*;
import fuzs.spikyspikes.handler.ItemCombinerHandler;
import fuzs.spikyspikes.handler.SpikeLootHandler;
import fuzs.spikyspikes.init.ForgeModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Mod(SpikySpikes.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpikySpikesForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(SpikySpikes.MOD_ID, SpikySpikes::new);
        ForgeModRegistry.touch();
        registerHandlers();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final LootingLevelEvent evt) -> {
            SpikeLootHandler.onLootingLevel(evt.getEntity(), evt.getDamageSource(), evt.getLootingLevel()).ifPresent(evt::setLootingLevel);
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
        final DataGenerator dataGenerator = evt.getGenerator();
        final PackOutput packOutput = dataGenerator.getPackOutput();
        final CompletableFuture<HolderLookup.Provider> lookupProvider = evt.getLookupProvider();
        final ExistingFileHelper fileHelper = evt.getExistingFileHelper();
        dataGenerator.addProvider(true, new ModBlockLootProvider(packOutput, SpikySpikes.MOD_ID));
        dataGenerator.addProvider(true, new ModRecipeProvider(packOutput));
        dataGenerator.addProvider(true, new ModBlockTagsProvider(packOutput, lookupProvider, SpikySpikes.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModLanguageProvider(packOutput, SpikySpikes.MOD_ID));
        dataGenerator.addProvider(true, new ModModelProvider(packOutput, SpikySpikes.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModItemModelProvider(packOutput, SpikySpikes.MOD_ID, fileHelper));
    }
}
