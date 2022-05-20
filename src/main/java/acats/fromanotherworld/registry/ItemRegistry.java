package acats.fromanotherworld.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.item.AssimilationLiquidItem;
import acats.fromanotherworld.item.ImpostorDetectorItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {
    public static final Item ASSIMILATION_LIQUID = new AssimilationLiquidItem(new Item.Settings().group(ItemGroup.MISC).maxCount(64));
    public static void register(){
        registerSpawnEgg("crawler_spawn_egg", 0x651619, 0x965a3f, EntityRegistry.CRAWLER);
        registerSpawnEgg("juliette_thing_spawn_egg", 0xbe886c, 0x8c0a19, EntityRegistry.JULIETTE_THING);
        registerSpawnEgg("dogbeast_spawn_egg", 0x8c0a19, 0x480a2a, EntityRegistry.DOGBEAST);
        Registry.register(Registry.ITEM, new Identifier(FromAnotherWorld.MODID, "assimilation_liquid"), ASSIMILATION_LIQUID);
        Registry.register(Registry.ITEM, new Identifier(FromAnotherWorld.MODID, "impostor_detector"), new ImpostorDetectorItem(new Item.Settings().group(ItemGroup.MISC).maxCount(1)));
    }

    private static void registerSpawnEgg(String path, int primaryColour, int secondaryColour, EntityType<? extends MobEntity> entityType){
        Registry.register(Registry.ITEM, new Identifier(FromAnotherWorld.MODID, path), new SpawnEggItem(entityType, primaryColour, secondaryColour, new Item.Settings().group(ItemGroup.MISC)));
    }
}
