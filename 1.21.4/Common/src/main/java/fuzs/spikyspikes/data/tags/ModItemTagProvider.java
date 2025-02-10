package fuzs.spikyspikes.data.tags;

import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

public class ModItemTagProvider extends AbstractTagProvider<Item> {

    public ModItemTagProvider(DataProviderContext context) {
        super(Registries.ITEM, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.SWORD_ENCHANTABLE)
                .add(ModRegistry.DIAMOND_SPIKE_ITEM.value(), ModRegistry.NETHERITE_SPIKE_ITEM.value());
        this.tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .add(ModRegistry.DIAMOND_SPIKE_ITEM.value(), ModRegistry.NETHERITE_SPIKE_ITEM.value());
    }
}