package fuzs.spikyspikes.registry;

import fuzs.puzzleslib.registry.RegistryManager;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.world.item.SpikeBlockItem;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistry {
    public static final DamageSource SPIKE_DAMAGE_SOURCE = new DamageSource("spike");
    public static final DamageSource SPIKE_DOWN_DAMAGE_SOURCE = new DamageSource("spike_top").damageHelmet();

    private static final RegistryManager REGISTRY = RegistryManager.of(SpikySpikes.MOD_ID);
    public static final RegistryObject<Block> DIAMOND_SPIKE_BLOCK = REGISTRY.registerBlock("diamond_spike", () -> new SpikeBlock(SpikeBlock.SpikeMaterial.DIAMOND, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.DIAMOND).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Item> DIAMOND_SPIKE_ITEM = REGISTRY.registerItem("diamond_spike", () -> new SpikeBlockItem(DIAMOND_SPIKE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<BlockEntityType<SpikeBlockEntity>> DIAMOND_SPIKE_BLOCK_ENTITY_TYPE = REGISTRY.registerRawBlockEntityType("diamond_spike", () -> BlockEntityType.Builder.of(SpikeBlockEntity::new, DIAMOND_SPIKE_BLOCK.get()));

    public static void touch() {

    }
}
