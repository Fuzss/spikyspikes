package fuzs.spikyspikes.world.damagesource;

import fuzs.puzzleslib.api.init.v3.registry.LookupHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

public class SpikeDamageSource extends DamageSource {
    private final boolean dropPlayerLoot;
    private final ItemEnchantments itemEnchantments;

    private SpikeDamageSource(Holder<DamageType> holder, BlockPos blockPos, boolean dropPlayerLoot, ItemEnchantments itemEnchantments) {
        super(holder, blockPos.getCenter());
        this.dropPlayerLoot = dropPlayerLoot;
        this.itemEnchantments = itemEnchantments;
    }

    public boolean dropPlayerLoot() {
        return this.dropPlayerLoot;
    }

    public ItemEnchantments getItemEnchantments() {
        return this.itemEnchantments;
    }

    public static DamageSource source(ResourceKey<DamageType> resourceKey, Level level, BlockPos blockPos) {
        return source(resourceKey, level, blockPos, false, ItemEnchantments.EMPTY);
    }

    public static DamageSource source(ResourceKey<DamageType> resourceKey, Level level, BlockPos blockPos, ItemEnchantments itemEnchantments) {
        return source(resourceKey, level, blockPos, true, itemEnchantments);
    }

    private static DamageSource source(ResourceKey<DamageType> resourceKey, Level level, BlockPos blockPos, boolean dropPlayerLoot, ItemEnchantments itemEnchantments) {
        return new SpikeDamageSource(LookupHelper.lookup(level, Registries.DAMAGE_TYPE, resourceKey), blockPos,
                dropPlayerLoot, itemEnchantments
        );
    }
}
