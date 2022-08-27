package fuzs.spikyspikes.init;

import fuzs.puzzleslib.core.CoreServices;
import fuzs.puzzleslib.init.RegistryManager;
import fuzs.puzzleslib.init.RegistryReference;
import fuzs.puzzleslib.init.builder.ModBlockEntityTypeBuilder;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.world.item.SpikeItem;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModRegistry {
    private static final RegistryManager REGISTRY = CoreServices.FACTORIES.registration(SpikySpikes.MOD_ID);
    public static final RegistryReference<Block> WOODEN_SPIKE_BLOCK = REGISTRY.placeholder(Registry.BLOCK_REGISTRY, "wooden_spike");
    public static final RegistryReference<Block> STONE_SPIKE_BLOCK = REGISTRY.placeholder(Registry.BLOCK_REGISTRY, "stone_spike");
    public static final RegistryReference<Block> IRON_SPIKE_BLOCK = REGISTRY.placeholder(Registry.BLOCK_REGISTRY, "iron_spike");
    public static final RegistryReference<Block> GOLDEN_SPIKE_BLOCK = REGISTRY.placeholder(Registry.BLOCK_REGISTRY, "golden_spike");
    public static final RegistryReference<Block> DIAMOND_SPIKE_BLOCK = REGISTRY.placeholder(Registry.BLOCK_REGISTRY, "diamond_spike");
    public static final RegistryReference<Block> NETHERITE_SPIKE_BLOCK = REGISTRY.placeholder(Registry.BLOCK_REGISTRY, "netherite_spike");
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
