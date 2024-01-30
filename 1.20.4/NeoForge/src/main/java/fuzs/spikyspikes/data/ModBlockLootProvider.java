package fuzs.spikyspikes.data;

import fuzs.puzzleslib.api.data.v1.AbstractLootProvider;
import fuzs.spikyspikes.init.ModRegistry;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class ModBlockLootProvider extends AbstractLootProvider.Blocks {

    public ModBlockLootProvider(PackOutput packOutput, String modId) {
        super(packOutput, modId);
    }

    @Override
    public void generate() {
        this.dropSelf(ModRegistry.WOODEN_SPIKE_BLOCK.get());
        this.dropSelf(ModRegistry.STONE_SPIKE_BLOCK.get());
        this.dropSelf(ModRegistry.IRON_SPIKE_BLOCK.get());
        this.dropSelf(ModRegistry.GOLDEN_SPIKE_BLOCK.get());
        this.add(ModRegistry.DIAMOND_SPIKE_BLOCK.get(), (p_124114_) -> {
            return LootTable.lootTable().withPool(this.applyExplosionCondition(p_124114_, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124114_).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy(SpikeBlockEntity.ENCHANTMENTS_TAG, SpikeBlockEntity.ENCHANTMENTS_TAG).copy(SpikeBlockEntity.REPAIR_COST_TAG, SpikeBlockEntity.REPAIR_COST_TAG)))));
        });
        this.add(ModRegistry.NETHERITE_SPIKE_BLOCK.get(), (p_124114_) -> {
            return LootTable.lootTable().withPool(this.applyExplosionCondition(p_124114_, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124114_).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy(SpikeBlockEntity.ENCHANTMENTS_TAG, SpikeBlockEntity.ENCHANTMENTS_TAG).copy(SpikeBlockEntity.REPAIR_COST_TAG, SpikeBlockEntity.REPAIR_COST_TAG)))));
        });
    }
}
