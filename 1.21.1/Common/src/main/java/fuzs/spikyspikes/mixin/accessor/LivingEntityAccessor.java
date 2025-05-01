package fuzs.spikyspikes.mixin.accessor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Invoker("dropExperience")
    void spikyspikes$callDropExperience(@Nullable Entity entity);

    @Accessor("lastHurtByPlayerTime")
    void spikyspikes$setLastHurtByPlayerTime(int lastHurtByPlayerTime);
}
