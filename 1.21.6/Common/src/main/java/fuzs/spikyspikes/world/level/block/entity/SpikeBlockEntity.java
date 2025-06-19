package fuzs.spikyspikes.world.level.block.entity;

import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.init.ModRegistry;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.SpikeMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SpikeBlockEntity extends BlockEntity {
    public static final String ENCHANTMENTS_TAG = SpikySpikes.id("enchantments").toString();
    public static final String REPAIR_COST_TAG = SpikySpikes.id("repair_cost").toString();

    private ItemEnchantments enchantments = ItemEnchantments.EMPTY;
    private int repairCost;

    public SpikeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModRegistry.SPIKE_BLOCK_ENTITY_TYPE.value(), blockPos, blockState);
    }

    public static void attack(ServerLevel level, BlockPos pos, BlockState state, SpikeBlockEntity blockEntity, LivingEntity entity, SpikeMaterial material) {
        FakePlayerAttackHelper.attack(entity,
                level,
                pos,
                state.getValue(SpikeBlock.FACING),
                material.damageAmount(),
                blockEntity.enchantments,
                material.hurtsPlayers());
    }

    @Override
    public void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        this.enchantments = valueInput.read(ENCHANTMENTS_TAG, ItemEnchantments.CODEC).orElse(ItemEnchantments.EMPTY);
        this.repairCost = valueInput.getIntOr(REPAIR_COST_TAG, 0);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        if (!this.enchantments.isEmpty()) {
            valueOutput.store(ENCHANTMENTS_TAG, ItemEnchantments.CODEC, this.enchantments);
        }
        valueOutput.putInt(REPAIR_COST_TAG, this.repairCost);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter dataComponentGetter) {
        super.applyImplicitComponents(dataComponentGetter);
        this.enchantments = dataComponentGetter.getOrDefault(DataComponents.STORED_ENCHANTMENTS,
                ItemEnchantments.EMPTY);
        this.repairCost = dataComponentGetter.getOrDefault(DataComponents.REPAIR_COST, 0);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.STORED_ENCHANTMENTS, this.enchantments);
        components.set(DataComponents.REPAIR_COST, this.repairCost);
    }

    @Override
    public void removeComponentsFromTag(ValueOutput valueOutput) {
        super.removeComponentsFromTag(valueOutput);
        valueOutput.discard(ENCHANTMENTS_TAG);
        valueOutput.discard(REPAIR_COST_TAG);
    }
}
