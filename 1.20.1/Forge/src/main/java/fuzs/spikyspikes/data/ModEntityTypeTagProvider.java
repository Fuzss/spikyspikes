package fuzs.spikyspikes.data;

import fuzs.puzzleslib.api.data.v1.AbstractTagProvider;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagProvider extends AbstractTagProvider.EntityTypes {

    public ModEntityTypeTagProvider(PackOutput packOutput, ExistingFileHelper fileHelper, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, fileHelper, modId, lookupProvider);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ModRegistry.SPIKE_DAMAGE_IMMUNE_ENTITY_TYPE_TAG)
                .add(EntityType.ARMOR_STAND)
                .addOptional(new ResourceLocation("betterend:end_fish"));
    }
}