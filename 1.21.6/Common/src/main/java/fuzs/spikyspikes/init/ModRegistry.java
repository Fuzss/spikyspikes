package fuzs.spikyspikes.init;

import fuzs.puzzleslib.api.data.v2.AbstractDatapackRegistriesProvider;
import fuzs.puzzleslib.api.init.v3.registry.ContentRegistrationHelper;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.world.item.SpikeItem;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.SpikeMaterial;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.Set;

public class ModRegistry {
    public static final RegistrySetBuilder REGISTRY_SET_BUILDER = new RegistrySetBuilder().add(Registries.DAMAGE_TYPE,
            ModRegistry::bootstrapDamageTypes);

    static final RegistryManager REGISTRIES = RegistryManager.from(SpikySpikes.MOD_ID);
    public static final Holder.Reference<Block> WOODEN_SPIKE_BLOCK = REGISTRIES.whenOnFabricLike()
            .registerBlock("wooden_spike",
                    (BlockBehaviour.Properties properties) -> new SpikeBlock(SpikeMaterial.WOOD, properties),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WOOD)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.WOOD)
                            .pushReaction(PushReaction.DESTROY));
    public static final Holder.Reference<Block> STONE_SPIKE_BLOCK = REGISTRIES.whenOnFabricLike()
            .registerBlock("stone_spike",
                    (BlockBehaviour.Properties properties) -> new SpikeBlock(SpikeMaterial.STONE, properties),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .pushReaction(PushReaction.DESTROY));
    public static final Holder.Reference<Block> IRON_SPIKE_BLOCK = REGISTRIES.whenOnFabricLike()
            .registerBlock("iron_spike",
                    (BlockBehaviour.Properties properties) -> new SpikeBlock(SpikeMaterial.IRON, properties),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .sound(SoundType.METAL)
                            .pushReaction(PushReaction.DESTROY));
    public static final Holder.Reference<Block> GOLDEN_SPIKE_BLOCK = REGISTRIES.whenOnFabricLike()
            .registerBlock("golden_spike",
                    (BlockBehaviour.Properties properties) -> new SpikeBlock(SpikeMaterial.GOLD, properties),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GOLD)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .sound(SoundType.METAL)
                            .pushReaction(PushReaction.DESTROY));
    public static final Holder.Reference<Block> DIAMOND_SPIKE_BLOCK = REGISTRIES.whenOnFabricLike()
            .registerBlock("diamond_spike",
                    (BlockBehaviour.Properties properties) -> new SpikeBlock(SpikeMaterial.DIAMOND, properties),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DIAMOND)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .sound(SoundType.METAL)
                            .pushReaction(PushReaction.DESTROY));
    public static final Holder.Reference<Block> NETHERITE_SPIKE_BLOCK = REGISTRIES.whenOnFabricLike()
            .registerBlock("netherite_spike",
                    (BlockBehaviour.Properties properties) -> new SpikeBlock(SpikeMaterial.NETHERITE, properties),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .requiresCorrectToolForDrops()
                            .strength(50.0F, 1200.0F)
                            .sound(SoundType.NETHERITE_BLOCK)
                            .pushReaction(PushReaction.DESTROY));
    public static final Holder.Reference<Item> WOODEN_SPIKE_ITEM = REGISTRIES.registerBlockItem(WOODEN_SPIKE_BLOCK,
            SpikeItem::new,
            Item.Properties::new);
    public static final Holder.Reference<Item> STONE_SPIKE_ITEM = REGISTRIES.registerBlockItem(STONE_SPIKE_BLOCK,
            SpikeItem::new,
            Item.Properties::new);
    public static final Holder.Reference<Item> IRON_SPIKE_ITEM = REGISTRIES.registerBlockItem(IRON_SPIKE_BLOCK,
            SpikeItem::new,
            Item.Properties::new);
    public static final Holder.Reference<Item> GOLDEN_SPIKE_ITEM = REGISTRIES.registerBlockItem(GOLDEN_SPIKE_BLOCK,
            SpikeItem::new,
            Item.Properties::new);
    public static final Holder.Reference<Item> DIAMOND_SPIKE_ITEM = REGISTRIES.registerBlockItem(DIAMOND_SPIKE_BLOCK,
            SpikeItem::new,
            () -> new Item.Properties().component(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)
                    .enchantable(1));
    public static final Holder.Reference<Item> NETHERITE_SPIKE_ITEM = REGISTRIES.registerBlockItem(NETHERITE_SPIKE_BLOCK,
            SpikeItem::new,
            () -> new Item.Properties().component(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)
                    .enchantable(1));
    public static final Holder.Reference<CreativeModeTab> CREATIVE_MODE_TAB = REGISTRIES.registerCreativeModeTab(
            DIAMOND_SPIKE_ITEM);
    public static final Holder.Reference<BlockEntityType<SpikeBlockEntity>> SPIKE_BLOCK_ENTITY_TYPE = REGISTRIES.registerBlockEntityType(
            "spike",
            SpikeBlockEntity::new,
            () -> Set.of(WOODEN_SPIKE_BLOCK.value(),
                    STONE_SPIKE_BLOCK.value(),
                    IRON_SPIKE_BLOCK.value(),
                    GOLDEN_SPIKE_BLOCK.value(),
                    DIAMOND_SPIKE_BLOCK.value(),
                    NETHERITE_SPIKE_BLOCK.value()));
    public static final ResourceKey<DamageType> SPIKE_DAMAGE_TYPE = REGISTRIES.registerDamageType("spike");

    public static void bootstrap() {
        ContentRegistrationHelper.registerTransmuteRecipeSerializers(REGISTRIES);
    }

    static void bootstrapDamageTypes(BootstrapContext<DamageType> context) {
        AbstractDatapackRegistriesProvider.registerDamageType(context, SPIKE_DAMAGE_TYPE);
    }
}
