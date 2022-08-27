package fuzs.spikyspikes.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import fuzs.spikyspikes.init.ModRegistry;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModLootTableProvider extends LootTableProvider {
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> subProviders = ImmutableList.of(Pair.of(ModBlockLoot::new, LootContextParamSets.BLOCK));
    private final String modId;

    public ModLootTableProvider(DataGenerator p_124437_, String modId) {
        super(p_124437_);
        this.modId = modId;
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return this.subProviders;
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {

    }

    private class ModBlockLoot extends BlockLoot {
        @Override
        protected void addTables() {
            this.dropSelf(ModRegistry.WOODEN_SPIKE_BLOCK.get());
            this.dropSelf(ModRegistry.STONE_SPIKE_BLOCK.get());
            this.dropSelf(ModRegistry.IRON_SPIKE_BLOCK.get());
            this.dropSelf(ModRegistry.GOLDEN_SPIKE_BLOCK.get());
            this.add(ModRegistry.DIAMOND_SPIKE_BLOCK.get(), (p_124114_) -> {
                return LootTable.lootTable().withPool(applyExplosionCondition(p_124114_, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124114_).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy(SpikeBlockEntity.ENCHANTMENTS_TAG, SpikeBlockEntity.ENCHANTMENTS_TAG).copy(SpikeBlockEntity.REPAIR_COST_TAG, SpikeBlockEntity.REPAIR_COST_TAG)))));
            });
            this.add(ModRegistry.NETHERITE_SPIKE_BLOCK.get(), (p_124114_) -> {
                return LootTable.lootTable().withPool(applyExplosionCondition(p_124114_, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124114_).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy(SpikeBlockEntity.ENCHANTMENTS_TAG, SpikeBlockEntity.ENCHANTMENTS_TAG).copy(SpikeBlockEntity.REPAIR_COST_TAG, SpikeBlockEntity.REPAIR_COST_TAG)))));
            });
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ForgeRegistries.BLOCKS.getEntries().stream()
                    .filter(e -> e.getKey().location().getNamespace().equals(ModLootTableProvider.this.modId))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toSet());
        }
    }
}
