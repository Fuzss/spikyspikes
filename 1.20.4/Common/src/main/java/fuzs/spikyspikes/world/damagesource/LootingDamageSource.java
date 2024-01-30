package fuzs.spikyspikes.world.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.LevelReader;

import java.util.OptionalInt;

public class LootingDamageSource extends DamageSource {
    private final int lootingLevel;

    private LootingDamageSource(Holder<DamageType> holder, int lootingLevel) {
        super(holder);
        this.lootingLevel = lootingLevel;
    }

    public OptionalInt getLootingLevel() {
        return this.lootingLevel == 0 ? OptionalInt.empty() : OptionalInt.of(this.lootingLevel);
    }

    public static DamageSource source(LevelReader level, ResourceKey<DamageType> resourceKey, int lootingLevel) {
        Holder.Reference<DamageType> holder = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(resourceKey);
        return new LootingDamageSource(holder, lootingLevel);
    }
}
