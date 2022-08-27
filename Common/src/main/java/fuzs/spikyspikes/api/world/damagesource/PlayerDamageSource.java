package fuzs.spikyspikes.api.world.damagesource;

import net.minecraft.world.level.storage.loot.LootContext;

/**
 * attachment for any {@link net.minecraft.world.damagesource.DamageSource} to allow dropping player loot
 */
public interface PlayerDamageSource {

    /**
     * @param context loot context from {@link net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition}
     * @return should player loot be dropped
     */
    default boolean dropPlayerLoot(LootContext context) {
        return true;
    }

    /**
     * @return looting level this damage source kills entities with
     */
    default int lootingLevel() {
        return 0;
    }
}
