package fuzs.spikyspikes.world.level.block.entity;

import fuzs.puzzleslib.api.init.v3.registry.LookupHelper;
import fuzs.spikyspikes.init.ModRegistry;
import fuzs.spikyspikes.mixin.accessor.LivingEntityAccessor;
import fuzs.spikyspikes.world.damagesource.SpikeDamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public final class FakePlayerAttackHelper {

    private FakePlayerAttackHelper() {
        // NO-OP
    }

    /**
     * Adapted from {@link Player#attack}, most importantly removing all the knockback and sounds, also most particles.
     */
    public static void attack(Entity entity, ServerLevel level, BlockPos pos, Direction direction, float attackDamage, ItemEnchantments itemEnchantments, boolean hurtPlayers) {

        if (entity.isAttackable()) {

            DamageSource damageSource = SpikeDamageSource.source(ModRegistry.SPIKE_DAMAGE_TYPE, level, pos, itemEnchantments);
            float enchantedDamage = BlockEnchantmentHelper.modifyDamage(level, entity, damageSource, attackDamage,
                    itemEnchantments
            ) - attackDamage;

            if (attackDamage > 0.0F || enchantedDamage > 0.0F) {

                attackDamage += enchantedDamage;

                // workaround so that items from instant kills are still smelted
                boolean setOnFire = false;
                if (entity instanceof LivingEntity &&
                        itemEnchantments.keySet().stream().anyMatch(holder -> holder.is(EnchantmentTags.SMELTS_LOOT)) && !entity.isOnFire()) {

                    setOnFire = true;
                    entity.igniteForSeconds(1);
                }

                if (hurt(entity, attackDamage, damageSource)) {

                    float attackKnockback = (float) BlockEnchantmentHelper.getAttributeValue(
                            Attributes.ATTACK_KNOCKBACK, itemEnchantments);
                    attackKnockback = BlockEnchantmentHelper.modifyKnockback(level, entity, damageSource,
                            attackKnockback, itemEnchantments
                    );
                    knockback(entity, attackKnockback * 0.5F, direction, level.getRandom());

                    sweepAttack(entity, attackDamage, level, pos, direction, damageSource, hurtPlayers,
                            itemEnchantments
                    );

                    BlockEnchantmentHelper.doPostAttackEffects(level, entity, damageSource, itemEnchantments);

                    if (enchantedDamage > 0.0F) {
                        level.getChunkSource().broadcastAndSend(entity,
                                new ClientboundAnimatePacket(entity, ClientboundAnimatePacket.MAGIC_CRITICAL_HIT)
                        );
                    }
                } else if (setOnFire) {

                    entity.clearFire();
                }
            }
        }
    }

    public static void knockback(Entity entity, double knockbackStrength, Direction direction, RandomSource random) {

        if (entity instanceof LivingEntity) {
            knockbackStrength *= 1.0 - ((LivingEntity) entity).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        }
        if (knockbackStrength > 0.0) {
            Vec3 deltaMovement = entity.getDeltaMovement();
            Vec3 normalVec = new Vec3(direction.getNormal().getX(), direction.getNormal().getY(),
                    direction.getNormal().getZ()
            );
            int axisStep = direction.getOpposite().getAxisDirection().getStep();
            Vec3 offsetVec = new Vec3(axisStep, axisStep, axisStep).add(normalVec).multiply(random.nextGaussian(),
                    random.nextGaussian(), random.nextGaussian()
            );
            Vec3 knockbackVec = normalVec.add(offsetVec).normalize().scale(knockbackStrength);
            entity.setDeltaMovement(deltaMovement.x / 2.0 + knockbackVec.x,
                    Math.min(0.4, deltaMovement.y / 2.0 + knockbackVec.y), deltaMovement.z / 2.0 + knockbackVec.z
            );
            entity.hasImpulse = true;
        }
    }

    public static boolean hurt(Entity entity, float attackDamage, DamageSource damageSource) {

        Vec3 oldMovement = entity.getDeltaMovement();

        if (entity instanceof Mob mob) {
            mob.setLastHurtByMob(null);
            ((LivingEntityAccessor) mob).spikyspikes$setLastHurtByPlayerTime(100);
            mob.setTarget(null);
        }

        if (entity.hurt(damageSource, attackDamage)) {

            // prevent any movement so entity simply stands still on spike (except when knockback enchantment is present)
            entity.setDeltaMovement(oldMovement);
            return true;
        } else {

            return false;
        }
    }

    public static void sweepAttack(Entity entity, float attackDamage, ServerLevel level, BlockPos pos, Direction direction, DamageSource damageSource, boolean hurtPlayers, ItemEnchantments itemEnchantments) {

        double sweepingDamageRatio = BlockEnchantmentHelper.getAttributeValue(Attributes.SWEEPING_DAMAGE_RATIO,
                itemEnchantments
        );
        if (sweepingDamageRatio != Attributes.SWEEPING_DAMAGE_RATIO.value().getDefaultValue()) {

            float sweepingDamage = 1.0F + (float) sweepingDamageRatio * attackDamage;
            Predicate<? super LivingEntity> filter = hurtPlayers ? EntitySelector.NO_SPECTATORS : Predicate.not(
                    Player.class::isInstance);
            for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class,
                    entity.getBoundingBox().inflate(1.0D, 0.25D, 1.0D), filter
            )) {
                if (livingEntity != entity &&
                        (!(livingEntity instanceof ArmorStand) || !((ArmorStand) livingEntity).isMarker()) &&
                        pos.distToCenterSqr(livingEntity.position()) < 9.0D) {

                    float hurtAmount = BlockEnchantmentHelper.modifyDamage(level, livingEntity, damageSource,
                            sweepingDamage, itemEnchantments
                    );

                    // prevent any movement so entity simply stands still on spike (except when knockback enchantment is present)
                    if (itemEnchantments.getLevel(
                            LookupHelper.lookup(level, Registries.ENCHANTMENT, Enchantments.KNOCKBACK)) > 0) {
                        knockback(livingEntity, 0.4F, direction, level.getRandom());
                    }

                    livingEntity.hurt(damageSource, hurtAmount);
                    BlockEnchantmentHelper.doPostAttackEffects(level, livingEntity, damageSource, itemEnchantments);
                }
            }

            BlockPos onFrontPos = pos.relative(direction);
            level.playSound(null, onFrontPos.getX() + 0.5, onFrontPos.getY() + 0.5, onFrontPos.getZ() + 0.5,
                    SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.BLOCKS, 1.0F, 1.0F
            );
            level.sendParticles(ParticleTypes.SWEEP_ATTACK, onFrontPos.getX() + 0.5, onFrontPos.getY() + 0.5,
                    onFrontPos.getZ() + 0.5, 0, 0.0, 0.0, 0.0, 0.0
            );
        }
    }
}
