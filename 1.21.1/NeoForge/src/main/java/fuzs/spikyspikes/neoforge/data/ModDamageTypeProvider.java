package fuzs.spikyspikes.neoforge.data;

import fuzs.puzzleslib.neoforge.api.data.v2.AbstractBuiltInDataProvider;
import fuzs.puzzleslib.neoforge.api.data.v2.core.NeoForgeDataProviderContext;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypeProvider extends AbstractBuiltInDataProvider.DamageTypes {

    public ModDamageTypeProvider(NeoForgeDataProviderContext context) {
        super(context);
    }

    @Override
    protected void addBootstrap(BootstrapContext<DamageType> bootstapContext) {
        this.add(ModRegistry.SPIKE_DAMAGE_TYPE, new DamageType("spike", 0.1F));
    }
}
