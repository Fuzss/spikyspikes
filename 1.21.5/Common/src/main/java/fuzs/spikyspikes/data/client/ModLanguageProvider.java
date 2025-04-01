package fuzs.spikyspikes.data.client;

import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.spikyspikes.SpikySpikes;
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
        builder.add(SpikeBlock.TooltipComponent.MORE.getTranslationKey(), "Hold %s for more information");
        builder.add(SpikeBlock.TooltipComponent.SHIFT.getTranslationKey(), "Shift");
        builder.add(SpikeBlock.TooltipComponent.DAMAGE.getTranslationKey(), "Damage: %s");
        builder.add(SpikeBlock.TooltipComponent.HEARTS.getTranslationKey(), "%s Heart(s)");
        builder.add(ModRegistry.WOODEN_SPIKE_BLOCK.value(),
                "description",
                "Slowly damages mobs, but does not deal a killing blow.");
        builder.add(ModRegistry.STONE_SPIKE_BLOCK.value(),
                "description",
                "Killed mobs do not drop any loot or experience.");
        builder.add(ModRegistry.IRON_SPIKE_BLOCK.value(),
                "description",
                "Killed mobs only drop normal loot without experience.");
        builder.add(ModRegistry.GOLDEN_SPIKE_BLOCK.value(),
                "description",
                "Killed mobs only drop experience without any loot.");
        builder.add(ModRegistry.DIAMOND_SPIKE_BLOCK.value(),
                "description",
                "Killed mobs drop all loot like when killed by a player. Accepts most sword enchantments via an anvil.");
        builder.add(ModRegistry.NETHERITE_SPIKE_BLOCK.value(),
                "description",
                "Killed mobs drop all loot like when killed by a player. Accepts most sword enchantments via an anvil. Resistant to explosions and the wither boss. Does not damage players.");
        builder.addGenericDamageType(ModRegistry.SPIKE_DAMAGE_TYPE, "%1$s now rests in a less spiky world");
        builder.addPlayerDamageType(ModRegistry.SPIKE_DAMAGE_TYPE, "%1$s oversaw a spike trying to flee from %2$s");
    }
}
