package fuzs.spikyspikes.config;

import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.annotation.Config;
import fuzs.puzzleslib.config.serialization.EntryCollectionBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerConfig extends AbstractConfig {

    @Config(description = "Damage dealt by a wooden spike.")
    public double woodenSpikeDamage = 1.0;
    @Config(description = "Damage dealt by a stone spike.")
    public double stoneSpikeDamage = 2.0;
    @Config(description = "Damage dealt by a iron spike.")
    public double ironSpikeDamage = 4.0;
    @Config(description = "Damage dealt by a golden spike.")
    public double goldenSpikeDamage = 6.0;
    @Config(description = "Damage dealt by a diamond spike.")
    public double diamondSpikeDamage = 8.0;
    @Config(description = "Damage dealt by a netherite spike.")
    public double netheriteSpikeDamage = 12.0;

    public ServerConfig() {
        super("");
    }
}
