package fuzs.spikyspikes.data;

import fuzs.puzzleslib.api.data.v1.AbstractDamageTypeProvider;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModDamageTypeProvider extends AbstractDamageTypeProvider {

    public ModDamageTypeProvider(PackOutput packOutput, String modId, ExistingFileHelper fileHelper) {
        super(packOutput, modId, fileHelper);
    }

    @Override
    protected void addDamageSources() {
        this.add(ModRegistry.SPIKE_DAMAGE_TYPE, new DamageType("spike", 0.1F));
    }
}
