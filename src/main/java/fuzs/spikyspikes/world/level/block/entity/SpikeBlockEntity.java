package fuzs.spikyspikes.world.level.block.entity;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import fuzs.spikyspikes.registry.ModRegistry;
import fuzs.spikyspikes.world.damagesource.SpikeEntityDamageSource;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.SweepingEdgeEnchantment;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.Map;
import java.util.UUID;

public class SpikeBlockEntity extends BlockEntity {
    public static final String ENCHANTMENTS_TAG = "Enchantments";
    public static final String REPAIR_COST_TAG = "RepairCost";

    private Map<Enchantment, Integer> enchantments;
    private int repairCost;
    private final UUID uuid = UUID.randomUUID();

    public SpikeBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModRegistry.SPIKE_BLOCK_ENTITY_TYPE.get(), p_155229_, p_155230_);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(ENCHANTMENTS_TAG, Tag.TAG_LIST)) {
            ListTag enchantments = tag.getList(ENCHANTMENTS_TAG, Tag.TAG_COMPOUND);
            this.enchantments = EnchantmentHelper.deserializeEnchantments(enchantments);
        } else {
            this.enchantments = Maps.newHashMap();
        }
        if (tag.contains(REPAIR_COST_TAG, Tag.TAG_INT)) {
            this.repairCost = tag.getInt(REPAIR_COST_TAG);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ListTag enchantments = serializeEnchantments(this.enchantments);
        if (!enchantments.isEmpty()) {
            tag.put(ENCHANTMENTS_TAG, enchantments);
        }
        if (this.repairCost != 0) {
            tag.putInt(REPAIR_COST_TAG, this.repairCost);
        }
    }

    public static ListTag serializeEnchantments(Map<Enchantment, Integer> p_44866_) {
        ListTag listtag = new ListTag();
        for (Map.Entry<Enchantment, Integer> entry : p_44866_.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment != null) {
                int i = entry.getValue();
                listtag.add(EnchantmentHelper.storeEnchantment(EnchantmentHelper.getEnchantmentId(enchantment), i));
            }
        }
        return listtag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public void attack(ServerLevel level, Entity target, float attackDamage) {
        GameProfile profile = new GameProfile(this.uuid, this.getBlockState().getBlock().getName().getString());
        FakePlayer player = FakePlayerFactory.get(level, profile);
        this.attack(player, target, attackDamage);
        if (target instanceof Mob mob) {
            mob.setLastHurtByMob(null);
            mob.setLastHurtByPlayer(null);
            mob.setTarget(null);
        }
    }

    /**
     * adapted from {@link Player#attack}, most importantly removing all the knockback and sounds, also most particles
     */
    private void attack(Player player, Entity target, float attackDamage) {
        // not sure how well other mods will handle a fake player here
//        if (!ForgeHooks.onPlayerAttackTarget(player, target)) return;
        if (target.isAttackable()) {
            if (!target.skipAttackInteraction(player)) {
                MobType mobType = target instanceof LivingEntity ? ((LivingEntity) target).getMobType() : MobType.UNDEFINED;
                float damageBonus = 0.0F;
                for (Map.Entry<Enchantment, Integer> entry : this.enchantments.entrySet()) {
                    damageBonus += entry.getKey().getDamageBonus(entry.getValue(), mobType);
                }
                if (attackDamage > 0.0F || damageBonus > 0.0F) {

                    attackDamage += damageBonus;

                    int fireAspect = this.enchantments.getOrDefault(Enchantments.FIRE_ASPECT, 0);
                    boolean setOnFire = false;
                    if (target instanceof LivingEntity) {
                        if (fireAspect > 0 && !target.isOnFire()) {
                            setOnFire = true;
                            target.setSecondsOnFire(1);
                        }
                    }

                    int looting = this.enchantments.getOrDefault(Enchantments.MOB_LOOTING, 0);
                    Vec3 oldMovement = target.getDeltaMovement();
                    boolean hurtTarget = target.hurt(SpikeEntityDamageSource.spikeEntity(player, looting), attackDamage);
                    if (hurtTarget) {

                        int sweeping = this.enchantments.getOrDefault(Enchantments.SWEEPING_EDGE, 0);
                        if (sweeping > 0) {
                            float f3 = 1.0F + SweepingEdgeEnchantment.getSweepingDamageRatio(sweeping) * attackDamage;

                            for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(1.0D, 0.25D, 1.0D))) {
                                if (livingentity != player && livingentity != target && (!(livingentity instanceof ArmorStand) || !((ArmorStand)livingentity).isMarker()) && this.getBlockPos().distToCenterSqr(livingentity.position()) < 9.0D) {
                                    livingentity.hurt(SpikeEntityDamageSource.spikeEntity(player, looting), f3);
                                }
                            }
                            if (this.level instanceof ServerLevel) {
                                Direction direction = this.getBlockState().getValue(SpikeBlock.FACING);
                                BlockPos pos = this.getBlockPos().relative(direction);
                                ((ServerLevel) this.level).sendParticles(ParticleTypes.SWEEP_ATTACK, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0.0, 0.0, 0.0, 0.0);
                            }

                        }

                        target.setDeltaMovement(oldMovement);

                        if (damageBonus > 0.0F && this.level instanceof ServerLevel) {
                            ((ServerLevel) this.level).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
                        }

                        if (target instanceof LivingEntity) {
                            for (Map.Entry<Enchantment, Integer> entry : this.enchantments.entrySet()) {
                                entry.getKey().doPostHurt((LivingEntity) target, player, entry.getValue());
                            }
                        }

                        for (Map.Entry<Enchantment, Integer> entry : this.enchantments.entrySet()) {
                            entry.getKey().doPostAttack(player, target, entry.getValue());
                        }

                        if (target instanceof LivingEntity) {
                            if (fireAspect > 0) {
                                target.setSecondsOnFire(fireAspect * 4);
                            }
                        }
                    } else if (setOnFire) {
                        target.clearFire();
                    }
                }
            }
        }
    }

    public void setEnchantmentData(Map<Enchantment, Integer> enchantments, int repairCost) {
        this.enchantments = enchantments;
        this.repairCost = repairCost;
        this.setChanged();
    }

    public boolean hasFoil() {
        return this.enchantments != null && !this.enchantments.isEmpty();
    }
}
