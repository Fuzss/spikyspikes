package fuzs.spikyspikes.init;

import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.world.item.SpikeItem;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.SpikeMaterial;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(SpikySpikes.MOD_ID);
    public static final Holder.Reference<Block> WOODEN_SPIKE_BLOCK = REGISTRIES.whenOnFabricLike()
            .registerBlock("wooden_spike",
                    () -> new SpikeBlock(SpikeMaterial.WOOD,
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.WOOD)
                                    .strength(2.0F, 3.0F)
                                    .sound(SoundType.WOOD)
                                    .pushReaction(PushReaction.DESTROY)));
    public static final Holder.Reference<Block> STONE_SPIKE_BLOCK = REGISTRIES.whenOnFabricLike()
            .registerBlock("stone_spike",
                    () -> new SpikeBlock(SpikeMaterial.STONE,
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.STONE)
                                    .requiresCorrectToolForDrops()
                                    .strength(2.0F, 6.0F)
                                    .pushReaction(PushReaction.DESTROY)));
    public static final Holder.Reference<Block> IRON_SPIKE_BLOCK = REGISTRIES.whenOnFabricLike()
            .registerBlock("iron_spike",
                    () -> new SpikeBlock(SpikeMaterial.IRON,
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.METAL)
                                    .requiresCorrectToolForDrops()
                                    .strength(5.0F, 6.0F)
                                    .sound(SoundType.METAL)
                                    .pushReaction(PushReaction.DESTROY)));
    public static final Holder.Reference<Block> GOLDEN_SPIKE_BLOCK = REGISTRIES.whenOnFabricLike()
            .registerBlock("golden_spike",
                    () -> new SpikeBlock(SpikeMaterial.GOLD,
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.GOLD)
                                    .requiresCorrectToolForDrops()
                                    .strength(3.0F, 6.0F)
                                    .sound(SoundType.METAL)
                                    .pushReaction(PushReaction.DESTROY)));
    public static final Holder.Reference<Block> DIAMOND_SPIKE_BLOCK = REGISTRIES.whenOnFabricLike()
            .registerBlock("diamond_spike",
                    () -> new SpikeBlock(SpikeMaterial.DIAMOND,
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.DIAMOND)
                                    .requiresCorrectToolForDrops()
                                    .strength(5.0F, 6.0F)
                                    .sound(SoundType.METAL)
                                    .pushReaction(PushReaction.DESTROY)));
    public static final Holder.Reference<Block> NETHERITE_SPIKE_BLOCK = REGISTRIES.whenOnFabricLike()
            .registerBlock("netherite_spike",
                    () -> new SpikeBlock(SpikeMaterial.NETHERITE,
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.COLOR_BLACK)
                                    .requiresCorrectToolForDrops()
                                    .strength(50.0F, 1200.0F)
                                    .sound(SoundType.NETHERITE_BLOCK)
                                    .pushReaction(PushReaction.DESTROY)));
    public static final Holder.Reference<Item> WOODEN_SPIKE_ITEM = REGISTRIES.registerItem("wooden_spike",
            () -> new SpikeItem(WOODEN_SPIKE_BLOCK.value(), new Item.Properties()));
    public static final Holder.Reference<Item> STONE_SPIKE_ITEM = REGISTRIES.registerItem("stone_spike",
            () -> new SpikeItem(STONE_SPIKE_BLOCK.value(), new Item.Properties()));
    public static final Holder.Reference<Item> IRON_SPIKE_ITEM = REGISTRIES.registerItem("iron_spike",
            () -> new SpikeItem(IRON_SPIKE_BLOCK.value(), new Item.Properties()));
    public static final Holder.Reference<Item> GOLDEN_SPIKE_ITEM = REGISTRIES.registerItem("golden_spike",
            () -> new SpikeItem(GOLDEN_SPIKE_BLOCK.value(), new Item.Properties()));
    public static final Holder.Reference<Item> DIAMOND_SPIKE_ITEM = REGISTRIES.registerItem("diamond_spike",
            () -> new SpikeItem(DIAMOND_SPIKE_BLOCK.value(),
                    new Item.Properties().component(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)));
    public static final Holder.Reference<Item> NETHERITE_SPIKE_ITEM = REGISTRIES.registerItem("netherite_spike",
            () -> new SpikeItem(NETHERITE_SPIKE_BLOCK.value(),
                    new Item.Properties().component(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)));
    public static final Holder.Reference<BlockEntityType<SpikeBlockEntity>> SPIKE_BLOCK_ENTITY_TYPE = REGISTRIES.registerBlockEntityType(
            "spike",
            () -> BlockEntityType.Builder.of(SpikeBlockEntity::new,
                    WOODEN_SPIKE_BLOCK.value(),
                    STONE_SPIKE_BLOCK.value(),
                    IRON_SPIKE_BLOCK.value(),
                    GOLDEN_SPIKE_BLOCK.value(),
                    DIAMOND_SPIKE_BLOCK.value(),
                    NETHERITE_SPIKE_BLOCK.value()));
    public static final ResourceKey<DamageType> SPIKE_DAMAGE_TYPE = REGISTRIES.registerDamageType("spike");

    static final TagFactory TAGS = TagFactory.make(SpikySpikes.MOD_ID);
    public static final TagKey<EntityType<?>> SPIKE_DAMAGE_IMMUNE_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "spike_damage_immune");

    public static void touch() {
        // NO-OP
    }
}
