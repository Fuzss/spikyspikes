package fuzs.spikyspikes.data;

import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(DataGenerator gen, String modid) {
        super(gen, modid, "en_us");
    }

    @Override
    protected void addTranslations() {
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
        this.addAdditional(ModRegistry.WOODEN_SPIKE_BLOCK.get(), "description", "Slowly damages mobs, but does not deal a killing blow.");
        this.addAdditional(ModRegistry.STONE_SPIKE_BLOCK.get(), "description", "Damages and kills mobs, but does not drop any loot or experience.");
        this.addAdditional(ModRegistry.IRON_SPIKE_BLOCK.get(), "description", "Damages and kills mobs, only drops normal loot without experience.");
        this.addAdditional(ModRegistry.GOLDEN_SPIKE_BLOCK.get(), "description", "Damages and kills mobs, only drops experience without any loot.");
        this.addAdditional(ModRegistry.DIAMOND_SPIKE_BLOCK.get(), "description", "Deals a lot of damage, mobs drop all loot like when killed by a player. Can use most sword enchantments, apply via an anvil.");
        this.addAdditional(ModRegistry.NETHERITE_SPIKE_BLOCK.get(), "description", "Deals a lot of damage, mobs drop all loot like when killed by a player. Can use most sword enchantments, apply via an anvil. Is resistant to explosions and cannot be destroyed by the wither boss. Deals no damage to players.");
        this.add("death.attack.spike", "%1$s now rests in a less spiky world");
        this.add("death.attack.spike.player", "%1$s oversaw a spike trying to flee from %2$s");
    }

    public void addAdditional(Block key, String additional, String name) {
        this.add(key.getDescriptionId() + "." + additional, name);
    }
}
