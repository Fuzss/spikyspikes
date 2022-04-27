package fuzs.spikyspikes.data;

import fuzs.spikyspikes.registry.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(DataGenerator gen, String modid) {
        super(gen, modid, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(ModRegistry.DIAMOND_SPIKE_BLOCK.get(), "Diamond Spike");
    }
}
