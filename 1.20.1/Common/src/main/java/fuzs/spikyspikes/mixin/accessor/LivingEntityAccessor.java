package fuzs.spikyspikes.mixin.accessor;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Invoker("dropExperience")
    void spikyspikes$callDropExperience();

    @Accessor("lastHurtByPlayerTime")
    void spikyspikes$setLastHurtByPlayerTime(int lastHurtByPlayerTime);
}
