package fuzs.spikyspikes.world.level.block.entity;

import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.init.ModRegistry;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.SpikeMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SpikeBlockEntity extends BlockEntity {
    public static final String ENCHANTMENTS_TAG = SpikySpikes.id("enchantments").toString();
    public static final String REPAIR_COST_TAG = SpikySpikes.id("repair_cost").toString();

    private ItemEnchantments enchantments = ItemEnchantments.EMPTY;
    private int repairCost;

    public SpikeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModRegistry.SPIKE_BLOCK_ENTITY_TYPE.value(), blockPos, blockState);
    }

    public static void attack(ServerLevel level, BlockPos pos, BlockState state, SpikeBlockEntity blockEntity, LivingEntity entity, SpikeMaterial material) {
        FakePlayerAttackHelper.attack(entity, level, pos, state.getValue(SpikeBlock.FACING), material.damageAmount(), blockEntity.enchantments, material.hurtsPlayers());
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.enchantments = ItemEnchantments.CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag.get(ENCHANTMENTS_TAG)).resultOrPartial().orElse(ItemEnchantments.EMPTY);
        if (tag.contains(REPAIR_COST_TAG, Tag.TAG_INT)) {
            this.repairCost = tag.getInt(REPAIR_COST_TAG);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!this.enchantments.isEmpty()) {
            tag.put(ENCHANTMENTS_TAG, ItemEnchantments.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), this.enchantments).getOrThrow());
        }
        if (this.repairCost != 0) {
            tag.putInt(REPAIR_COST_TAG, this.repairCost);
        }
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
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        this.enchantments = componentInput.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        this.repairCost = componentInput.getOrDefault(DataComponents.REPAIR_COST, 0);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.STORED_ENCHANTMENTS, this.enchantments);
        components.set(DataComponents.REPAIR_COST, this.repairCost);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        super.removeComponentsFromTag(tag);
        tag.remove(ENCHANTMENTS_TAG);
        tag.remove(REPAIR_COST_TAG);
    }

    public boolean hasFoil() {
        return this.enchantments != null && !this.enchantments.isEmpty();
    }
}
