package fuzs.spikyspikes.world.level.block.entity;

import com.google.common.collect.Maps;
import fuzs.spikyspikes.init.ModRegistry;
import fuzs.spikyspikes.mixin.accessor.LivingEntityAccessor;
import fuzs.spikyspikes.world.damagesource.LootingDamageSource;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.SpikeMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.SweepingEdgeEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class SpikeBlockEntity extends BlockEntity {
    public static final String ENCHANTMENTS_TAG = "Enchantments";
    public static final String REPAIR_COST_TAG = "RepairCost";

    private Map<Enchantment, Integer> enchantments = Maps.newHashMap();
    private int repairCost;

    public SpikeBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModRegistry.SPIKE_BLOCK_ENTITY_TYPE.get(), p_155229_, p_155230_);
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

    public static void attackPlayerLike(Level level, BlockPos pos, BlockState state, SpikeBlockEntity blockEntity, LivingEntity entity, SpikeMaterial material) {
        attackPlayerLike(entity, material.damageAmount(), level, pos, state.getValue(SpikeBlock.FACING), blockEntity.enchantments, material.hurtsPlayers());
    }

    /**
     * adapted from {@link Player#attack}, most importantly removing all the knockback and sounds, also most particles
     */
    private static void attackPlayerLike(Entity target, float attackDamage, Level level, BlockPos pos, Direction direction, Map<Enchantment, Integer> enchantments, boolean hurtPlayers) {
        if (target.isAttackable()) {

            MobType mobType = target instanceof LivingEntity ? ((LivingEntity) target).getMobType() : MobType.UNDEFINED;

            float damageBonus = 0.0F;
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                damageBonus += entry.getKey().getDamageBonus(entry.getValue(), mobType);
            }

            if (attackDamage > 0.0F || damageBonus > 0.0F) {

                attackDamage += damageBonus;

                int fireAspect = enchantments.getOrDefault(Enchantments.FIRE_ASPECT, 0);
                boolean setOnFire = false;
                if (target instanceof LivingEntity) {

                    if (fireAspect > 0 && !target.isOnFire()) {

                        setOnFire = true;
                        target.setSecondsOnFire(1);
                    }
                }

                int looting = enchantments.getOrDefault(Enchantments.MOB_LOOTING, 0);
                Vec3 oldMovement = target.getDeltaMovement();
                if (hurtPlayerLike(level, target, attackDamage, looting)) {

                    // prevent any movement so entity simply stands still on spike (except when knockback enchantment is present)
                    target.setDeltaMovement(oldMovement);
                    int knockback = enchantments.getOrDefault(Enchantments.KNOCKBACK, 0);
                    if (knockback > 0) {
                        applyLivingKnockback(direction, target, knockback * 0.5F, level.getRandom());
                    }

                    int sweeping = enchantments.getOrDefault(Enchantments.SWEEPING_EDGE, 0);
                    if (sweeping > 0) {
                        applySweepingDamage(target, attackDamage, level, pos, direction, looting, knockback, sweeping, hurtPlayers);
                    }

                    if (damageBonus > 0.0F && level instanceof ServerLevel) {
                        ((ServerLevel) level).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, ClientboundAnimatePacket.MAGIC_CRITICAL_HIT));
                    }

                    // not possible without a player
//                    if (target instanceof LivingEntity) {
//                        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
//                            entry.getKey().doPostHurt((LivingEntity) target, player, entry.getValue());
//                        }
//                    }
//
//                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
//                        entry.getKey().doPostAttack(player, target, entry.getValue());
//                    }

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

    private static boolean hurtPlayerLike(Level level, Entity entity, float attackDamage, int looting) {
        if (entity instanceof Mob mob) {
            mob.setLastHurtByMob(null);
            ((LivingEntityAccessor) mob).spikyspikes$setLastHurtByPlayerTime(100);
            mob.setTarget(null);
        }
        return entity.hurt(LootingDamageSource.source(level, ModRegistry.SPIKE_DAMAGE_TYPE, looting), attackDamage);
    }

    private static void applyLivingKnockback(Direction direction, Entity target, float strength, RandomSource random) {
        if (target instanceof LivingEntity) {
            strength *= 1.0 - ((LivingEntity) target).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        }
        if (strength > 0.0) {
            Vec3 deltaMovement = target.getDeltaMovement();
            Vec3 normalVec = new Vec3(direction.getNormal().getX(), direction.getNormal().getY(), direction.getNormal().getZ());
            int axisStep = direction.getOpposite().getAxisDirection().getStep();
            Vec3 offsetVec = new Vec3(axisStep, axisStep, axisStep).add(normalVec).multiply(random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
            Vec3 knockbackVec = normalVec.add(offsetVec).normalize().scale(strength);
            target.setDeltaMovement(deltaMovement.x / 2.0 + knockbackVec.x, Math.min(0.4, deltaMovement.y / 2.0 + knockbackVec.y), deltaMovement.z / 2.0 + knockbackVec.z);
            target.hasImpulse = true;
        }
    }

    private static void applySweepingDamage(Entity target, float attackDamage, Level level, BlockPos pos, Direction direction, int looting, int knockback, int sweeping, boolean hurtPlayers) {
        float f3 = 1.0F + SweepingEdgeEnchantment.getSweepingDamageRatio(sweeping) * attackDamage;

        for (LivingEntity livingentity : level.getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(1.0D, 0.25D, 1.0D), hurtPlayers ? EntitySelector.NO_SPECTATORS : entity -> !(entity instanceof Player))) {
            if (livingentity != target && (!(livingentity instanceof ArmorStand) || !((ArmorStand) livingentity).isMarker()) && pos.distToCenterSqr(livingentity.position()) < 9.0D) {
                if (knockback > 0) {
                    applyLivingKnockback(direction, livingentity, 0.4F, level.getRandom());
                }
                livingentity.hurt(LootingDamageSource.source(level, ModRegistry.SPIKE_DAMAGE_TYPE, looting), f3);
            }
        }
        if (level instanceof ServerLevel) {
            BlockPos offsetPos = pos.relative(direction);
            ((ServerLevel) level).sendParticles(ParticleTypes.SWEEP_ATTACK, offsetPos.getX() + 0.5, offsetPos.getY() + 0.5, offsetPos.getZ() + 0.5, 0, 0.0, 0.0, 0.0, 0.0);
        }
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

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
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
