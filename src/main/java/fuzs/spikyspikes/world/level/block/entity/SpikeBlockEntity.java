package fuzs.spikyspikes.world.level.block.entity;

import com.mojang.authlib.GameProfile;
import fuzs.spikyspikes.registry.ModRegistry;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class SpikeBlockEntity extends BlockEntity {
    public static final String ENCHANTMENTS_TAG = "Enchantments";
    public static final String REPAIR_COST_TAG = "RepairCost";
    private static final UUID BASE_ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");

    @Nullable
    private ListTag enchantments;
    private int repairCost;
    @Nullable
    private WeakReference<Player> player;

    public SpikeBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModRegistry.SPIKE_BLOCK_ENTITY_TYPE.get(), p_155229_, p_155230_);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(ENCHANTMENTS_TAG, Tag.TAG_LIST)) {
            this.enchantments = tag.getList(ENCHANTMENTS_TAG, Tag.TAG_COMPOUND);
        }
        if (tag.contains(REPAIR_COST_TAG, Tag.TAG_INT)) {
            this.repairCost = tag.getInt(REPAIR_COST_TAG);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.enchantments != null) {
            tag.put(ENCHANTMENTS_TAG, this.enchantments);
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
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    private Player createPlayer(ServerLevel level) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), this.getBlockState().getBlock().getName().getString());
        Player player = new FakePlayer(level, profile) {

            @Override
            public float getAttackStrengthScale(float p_36404_) {
                return 1.0F;
            }
        };
        player.setItemInHand(InteractionHand.MAIN_HAND, this.createPlayerWeapon());
        return player;
    }

    private ItemStack createPlayerWeapon() {
        SpikeBlock.SpikeMaterial spikeMaterial = ((SpikeBlock) this.getBlockState().getBlock()).spikeMaterial;
        ItemStack stack = new ItemStack(spikeMaterial.swordItem());
        float damageAmount = spikeMaterial.damageAmount - 1.0F;
        stack.addAttributeModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", damageAmount, AttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
        stack.getOrCreateTag().putBoolean("Unbreakable", true);
        if (this.hasEnchantments()) {
            EnchantmentHelper.setEnchantments(EnchantmentHelper.deserializeEnchantments(this.enchantments), stack);
        }
        return stack;
    }

    public void attack(ServerLevel level, Entity entity) {
        Player player;
        if (this.player == null || this.player.get() == null) {
            player = this.createPlayer(level);
            this.player = new WeakReference<>(player);
        } else {
            player = this.player.get();
        }
        player.setSilent(true);
        player.attack(entity);
        this.resetMobTarget(entity);
    }

    private void resetMobTarget(Entity entity) {
        if (entity instanceof Mob mob) {
            mob.setLastHurtByMob(null);
            mob.setLastHurtByPlayer(null);
            mob.setTarget(null);
        }
    }

    public void setEnchantmentData(@Nullable ListTag enchantments, int repairCost) {
        this.enchantments = enchantments;
        this.repairCost = repairCost;
        this.setChanged();
    }

    public boolean hasEnchantments() {
        return this.enchantments != null && !this.enchantments.isEmpty();
    }
}
