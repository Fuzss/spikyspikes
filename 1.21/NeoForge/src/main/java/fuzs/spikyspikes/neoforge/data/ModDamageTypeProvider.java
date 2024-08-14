package fuzs.spikyspikes.neoforge.data;

import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.neoforge.api.data.v2.AbstractBuiltInDataProvider;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypeProvider extends AbstractBuiltInDataProvider.DamageTypes {

    public ModDamageTypeProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    protected void addBootstrap(BootstapContext<DamageType> bootstapContext) {
        this.add(ModRegistry.SPIKE_DAMAGE_TYPE, new DamageType("spike", 0.1F));
    }
}
