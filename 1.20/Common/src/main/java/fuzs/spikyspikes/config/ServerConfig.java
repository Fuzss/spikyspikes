package fuzs.spikyspikes.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
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
}
