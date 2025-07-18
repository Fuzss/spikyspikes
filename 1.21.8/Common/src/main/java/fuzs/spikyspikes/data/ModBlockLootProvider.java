package fuzs.spikyspikes.data;

import fuzs.puzzleslib.api.data.v2.AbstractLootProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class ModBlockLootProvider extends AbstractLootProvider.Blocks {

    public ModBlockLootProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addLootTables() {
        this.dropSelf(ModRegistry.WOODEN_SPIKE_BLOCK.value());
        this.dropSelf(ModRegistry.STONE_SPIKE_BLOCK.value());
        this.dropSelf(ModRegistry.IRON_SPIKE_BLOCK.value());
        this.dropSelf(ModRegistry.GOLDEN_SPIKE_BLOCK.value());
        this.add(ModRegistry.DIAMOND_SPIKE_BLOCK.value(), (Block block) -> {
            return LootTable.lootTable().withPool(this.applyExplosionCondition(block, LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(block)
                            .apply(CopyComponentsFunction.copyComponents(
                                    CopyComponentsFunction.Source.BLOCK_ENTITY)))));
        });
        this.add(ModRegistry.NETHERITE_SPIKE_BLOCK.value(), (Block block) -> {
            return LootTable.lootTable().withPool(this.applyExplosionCondition(block, LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(block)
                            .apply(CopyComponentsFunction.copyComponents(
                                    CopyComponentsFunction.Source.BLOCK_ENTITY)))));
        });
    }
}
