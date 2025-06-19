package fuzs.spikyspikes.data.client;

import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.client.renderer.util.SpikeTooltipHelper;
import fuzs.spikyspikes.init.ModRegistry;
import fuzs.spikyspikes.world.level.block.SpikeBlock;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.addCreativeModeTab(ModRegistry.CREATIVE_MODE_TAB, SpikySpikes.MOD_NAME);
        builder.add(ModRegistry.WOODEN_SPIKE_BLOCK.value(), "Wooden Spike");
        builder.add(ModRegistry.STONE_SPIKE_BLOCK.value(), "Stone Spike");
        builder.add(ModRegistry.IRON_SPIKE_BLOCK.value(), "Iron Spike");
        builder.add(ModRegistry.GOLDEN_SPIKE_BLOCK.value(), "Golden Spike");
        builder.add(ModRegistry.DIAMOND_SPIKE_BLOCK.value(), "Diamond Spike");
        builder.add(ModRegistry.NETHERITE_SPIKE_BLOCK.value(), "Netherite Spike");
        builder.add(SpikeTooltipHelper.TooltipComponent.ADDITIONAL.getTranslationKey(), "Hold %s for more information");
        builder.add(SpikeTooltipHelper.TooltipComponent.SHIFT.getTranslationKey(), "Shift");
        builder.add(SpikeTooltipHelper.TooltipComponent.DAMAGE.getTranslationKey(), "Damage: %s");
        builder.add(((SpikeBlock) ModRegistry.WOODEN_SPIKE_BLOCK.value()).getDescriptionComponent(),
                "Slowly damages mobs, but does not deal a killing blow.");
        builder.add(((SpikeBlock) ModRegistry.STONE_SPIKE_BLOCK.value()).getDescriptionComponent(),
                "Killed mobs do not drop any loot or experience.");
        builder.add(((SpikeBlock) ModRegistry.IRON_SPIKE_BLOCK.value()).getDescriptionComponent(),
                "Killed mobs only drop normal loot without experience.");
        builder.add(((SpikeBlock) ModRegistry.GOLDEN_SPIKE_BLOCK.value()).getDescriptionComponent(),
                "Killed mobs only drop experience without any loot.");
        builder.add(((SpikeBlock) ModRegistry.DIAMOND_SPIKE_BLOCK.value()).getDescriptionComponent(),
                "Killed mobs drop all loot like when killed by a player. Accepts most sword enchantments.");
        builder.add(((SpikeBlock) ModRegistry.NETHERITE_SPIKE_BLOCK.value()).getDescriptionComponent(),
                "Killed mobs drop all loot like when killed by a player. Accepts most sword enchantments. Resistant to explosions and the wither boss. Does not damage players.");
        builder.addGenericDamageType(ModRegistry.SPIKE_DAMAGE_TYPE, "%1$s now rests in a less spiky world");
        builder.addPlayerDamageType(ModRegistry.SPIKE_DAMAGE_TYPE, "%1$s oversaw a spike trying to flee from %2$s");
    }
}
