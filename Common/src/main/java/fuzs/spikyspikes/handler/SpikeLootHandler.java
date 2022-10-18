package fuzs.spikyspikes.handler;

import fuzs.spikyspikes.api.world.damagesource.PlayerDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.OptionalInt;

public class SpikeLootHandler {

    public static OptionalInt onLootingLevel(LivingEntity entity, @Nullable DamageSource damageSource, int lootingLevel) {
        if (damageSource instanceof PlayerDamageSource source && source.lootingLevel() > 0) {
            // our fake player does not use an item for killing, so this needs to be set manually
            return OptionalInt.of(source.lootingLevel());
        }
        return OptionalInt.empty();
    }
}
