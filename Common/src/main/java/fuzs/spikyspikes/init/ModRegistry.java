package fuzs.spikyspikes.init;

import fuzs.puzzleslib.core.CoreServices;
import fuzs.puzzleslib.core.ModLoader;
import fuzs.puzzleslib.init.RegistryManager;
import fuzs.puzzleslib.init.RegistryReference;
import fuzs.puzzleslib.init.builder.ModBlockEntityTypeBuilder;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.world.item.SpikeItem;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.SpikeMaterial;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class ModRegistry {
    private static final RegistryManager REGISTRY = CoreServices.FACTORIES.registration(SpikySpikes.MOD_ID);
    public static final RegistryReference<Block> WOODEN_SPIKE_BLOCK = REGISTRY.whenNotOn(ModLoader.FORGE).registerBlock("wooden_spike", () -> new SpikeBlock(SpikeMaterial.WOOD, BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryReference<Block> STONE_SPIKE_BLOCK = REGISTRY.whenNotOn(ModLoader.FORGE).registerBlock("stone_spike", () -> new SpikeBlock(SpikeMaterial.STONE, BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
    public static final RegistryReference<Block> IRON_SPIKE_BLOCK = REGISTRY.whenNotOn(ModLoader.FORGE).registerBlock("iron_spike", () -> new SpikeBlock(SpikeMaterial.IRON, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryReference<Block> GOLDEN_SPIKE_BLOCK = REGISTRY.whenNotOn(ModLoader.FORGE).registerBlock("golden_spike", () -> new SpikeBlock(SpikeMaterial.GOLD, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryReference<Block> DIAMOND_SPIKE_BLOCK = REGISTRY.whenNotOn(ModLoader.FORGE).registerBlock("diamond_spike", () -> new SpikeBlock(SpikeMaterial.DIAMOND, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.DIAMOND).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryReference<Block> NETHERITE_SPIKE_BLOCK = REGISTRY.whenNotOn(ModLoader.FORGE).registerBlock("netherite_spike", () -> new SpikeBlock(SpikeMaterial.NETHERITE, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).sound(SoundType.NETHERITE_BLOCK)));
    public static final RegistryReference<Item> WOODEN_SPIKE_ITEM = REGISTRY.registerItem("wooden_spike", () -> new SpikeItem(WOODEN_SPIKE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryReference<Item> STONE_SPIKE_ITEM = REGISTRY.registerItem("stone_spike", () -> new SpikeItem(STONE_SPIKE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryReference<Item> IRON_SPIKE_ITEM = REGISTRY.registerItem("iron_spike", () -> new SpikeItem(IRON_SPIKE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryReference<Item> GOLDEN_SPIKE_ITEM = REGISTRY.registerItem("golden_spike", () -> new SpikeItem(GOLDEN_SPIKE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryReference<Item> DIAMOND_SPIKE_ITEM = REGISTRY.registerItem("diamond_spike", () -> new SpikeItem(DIAMOND_SPIKE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryReference<Item> NETHERITE_SPIKE_ITEM = REGISTRY.registerItem("netherite_spike", () -> new SpikeItem(NETHERITE_SPIKE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryReference<BlockEntityType<SpikeBlockEntity>> SPIKE_BLOCK_ENTITY_TYPE = REGISTRY.registerBlockEntityTypeBuilder("spike", () -> ModBlockEntityTypeBuilder.of(SpikeBlockEntity::new, WOODEN_SPIKE_BLOCK.get(), STONE_SPIKE_BLOCK.get(), IRON_SPIKE_BLOCK.get(), GOLDEN_SPIKE_BLOCK.get(), DIAMOND_SPIKE_BLOCK.get(), NETHERITE_SPIKE_BLOCK.get()));

    public static void touch() {

    }
}
