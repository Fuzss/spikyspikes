package fuzs.spikyspikes.world.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.LevelReader;

import java.util.OptionalInt;

public class LootingDamageSource extends DamageSource {
    private final int lootingLevel;

    private LootingDamageSource(int lootingLevel) {
        super("spike");
        this.lootingLevel = lootingLevel;
    }

    public OptionalInt getLootingLevel() {
        return this.lootingLevel == 0 ? OptionalInt.empty() : OptionalInt.of(this.lootingLevel);
    }

    public static DamageSource source(LevelReader level, int lootingLevel) {
        return new LootingDamageSource(lootingLevel);
    }
}
