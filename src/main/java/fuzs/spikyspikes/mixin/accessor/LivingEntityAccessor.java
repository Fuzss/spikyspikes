package fuzs.spikyspikes.mixin.accessor;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Accessor
    void setLastHurtByPlayerTime(int lastHurtByPlayerTime);

    @Accessor
    int getLastHurtByPlayerTime();

    @Invoker
    void callDropExperience();
}
