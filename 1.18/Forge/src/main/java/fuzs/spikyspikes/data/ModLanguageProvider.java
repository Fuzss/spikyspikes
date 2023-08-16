package fuzs.spikyspikes.data;

import fuzs.puzzleslib.api.data.v1.AbstractLanguageProvider;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void addTranslations() {
        this.addCreativeModeTab(SpikySpikes.MOD_NAME);
        this.add(ModRegistry.WOODEN_SPIKE_BLOCK.get(), "Wooden Spike");
        this.add(ModRegistry.STONE_SPIKE_BLOCK.get(), "Stone Spike");
        this.add(ModRegistry.IRON_SPIKE_BLOCK.get(), "Iron Spike");
        this.add(ModRegistry.GOLDEN_SPIKE_BLOCK.get(), "Golden Spike");
        this.add(ModRegistry.DIAMOND_SPIKE_BLOCK.get(), "Diamond Spike");
        this.add(ModRegistry.NETHERITE_SPIKE_BLOCK.get(), "Netherite Spike");
        this.add("item.spikyspikes.spike.tooltip.more", "Hold %s for more information");
        this.add("item.spikyspikes.spike.tooltip.shift", "Shift");
        this.add("item.spikyspikes.spike.tooltip.damage", "Damage: %s");
        this.add("item.spikyspikes.spike.tooltip.hearts", "%s Heart(s)");
        this.add(ModRegistry.WOODEN_SPIKE_BLOCK.get(), "description", "Slowly damages mobs, but does not deal a killing blow.");
        this.add(ModRegistry.STONE_SPIKE_BLOCK.get(), "description", "Killed mobs do not drop any loot or experience.");
        this.add(ModRegistry.IRON_SPIKE_BLOCK.get(), "description", "Killed mobs only drop normal loot without experience.");
        this.add(ModRegistry.GOLDEN_SPIKE_BLOCK.get(), "description", "Killed mobs only drop experience without any loot.");
        this.add(ModRegistry.DIAMOND_SPIKE_BLOCK.get(), "description", "Killed mobs drop all loot like when killed by a player. Accepts most sword enchantments via an anvil.");
        this.add(ModRegistry.NETHERITE_SPIKE_BLOCK.get(), "description", "Killed mobs drop all loot like when killed by a player. Accepts most sword enchantments via an anvil. Resistant to explosions and the wither boss. Does not damage players.");
        this.addDamageType(new DamageSource("spike"), "%1$s now rests in a less spiky world");
        this.addPlayerDamageType(new DamageSource("spike"), "%1$s oversaw a spike trying to flee from %2$s");
    }
}
