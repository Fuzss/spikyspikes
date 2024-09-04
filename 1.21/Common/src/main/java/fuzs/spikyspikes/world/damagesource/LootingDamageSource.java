package fuzs.spikyspikes.world.damagesource;

import fuzs.puzzleslib.api.init.v3.registry.LookupHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

import java.util.OptionalInt;

public class LootingDamageSource extends DamageSource {
    private final int lootingLevel;

    private LootingDamageSource(Holder<DamageType> holder, BlockPos blockPos, int lootingLevel) {
        super(holder, blockPos.getCenter());
        this.lootingLevel = lootingLevel;
    }

    public OptionalInt getLootingLevel() {
        return this.lootingLevel == 0 ? OptionalInt.empty() : OptionalInt.of(this.lootingLevel);
    }

    public static DamageSource source(ResourceKey<DamageType> resourceKey, Level level, BlockPos blockPos, int lootingLevel) {
        return new LootingDamageSource(LookupHelper.lookup(level, Registries.DAMAGE_TYPE, resourceKey), blockPos, lootingLevel);
    }
}
