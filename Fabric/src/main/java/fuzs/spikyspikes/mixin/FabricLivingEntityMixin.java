package fuzs.spikyspikes.mixin;

import fuzs.spikyspikes.api.event.entity.living.LootingLevelCallback;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class FabricLivingEntityMixin extends Entity {

    public FabricLivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyVariable(method = "dropAllDeathLoot", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;lastHurtByPlayerTime:I"), ordinal = 0)
    protected int dropAllDeathLoot$modifyVariable$store$lootingLevel(int lootingLevel, DamageSource damageSource) {
        return LootingLevelCallback.EVENT.invoker().onLootingLevel((LivingEntity) (Object) this, damageSource, lootingLevel).orElseThrow();
    }
}
