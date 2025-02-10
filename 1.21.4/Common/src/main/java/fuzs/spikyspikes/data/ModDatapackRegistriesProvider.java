package fuzs.spikyspikes.data;

import fuzs.puzzleslib.api.data.v2.AbstractDatapackRegistriesProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;

public class ModDatapackRegistriesProvider extends AbstractDatapackRegistriesProvider {

    public ModDatapackRegistriesProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBootstrap(RegistryBoostrapConsumer consumer) {
        consumer.add(Registries.DAMAGE_TYPE, ModDatapackRegistriesProvider::bootstrapDamageTypes);
    }

    static void bootstrapDamageTypes(BootstrapContext<DamageType> context) {
        registerDamageType(context, ModRegistry.SPIKE_DAMAGE_TYPE);
    }
}
