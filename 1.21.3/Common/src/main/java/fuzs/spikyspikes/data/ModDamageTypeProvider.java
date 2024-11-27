package fuzs.spikyspikes.data;

import fuzs.puzzleslib.api.data.v2.AbstractRegistriesDatapackGenerator;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypeProvider extends AbstractRegistriesDatapackGenerator<DamageType> {

    public ModDamageTypeProvider(DataProviderContext context) {
        super(Registries.DAMAGE_TYPE, context);
    }

    @Override
    public void addBootstrap(BootstrapContext<DamageType> context) {
        registerDamageType(context, ModRegistry.SPIKE_DAMAGE_TYPE);
    }
}
