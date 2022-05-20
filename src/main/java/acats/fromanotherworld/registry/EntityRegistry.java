package acats.fromanotherworld.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import acats.fromanotherworld.entity.resultant.CrawlerEntity;
import acats.fromanotherworld.entity.resultant.DogBeastEntity;
import acats.fromanotherworld.entity.resultant.JulietteThingEntity;
import acats.fromanotherworld.entity.revealed.ChestSpitterEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityRegistry {

    public static final EntityType<CrawlerEntity> CRAWLER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(FromAnotherWorld.MODID, "crawler"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CrawlerEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).trackRangeBlocks(256).build()
    );

    public static final EntityType<ChestSpitterEntity> CHEST_SPITTER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(FromAnotherWorld.MODID, "chest_spitter"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ChestSpitterEntity::new).dimensions(EntityDimensions.fixed(1.0f, 0.375F)).build()
    );

    public static final EntityType<AssimilationLiquidEntity> ASSIMILATION_LIQUID = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(FromAnotherWorld.MODID, "assimilation_liquid"),
            FabricEntityTypeBuilder.<AssimilationLiquidEntity>create(SpawnGroup.MISC, AssimilationLiquidEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );

    public static final EntityType<JulietteThingEntity> JULIETTE_THING = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(FromAnotherWorld.MODID, "juliette_thing"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, JulietteThingEntity::new).dimensions(EntityDimensions.fixed(0.8f, 1.8f)).trackRangeBlocks(256).build()
    );

    public static final EntityType<DogBeastEntity> DOGBEAST = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(FromAnotherWorld.MODID, "dogbeast"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DogBeastEntity::new).dimensions(EntityDimensions.fixed(0.8f, 0.8f)).trackRangeBlocks(256).build()
    );

    public static void register(){
        FabricDefaultAttributeRegistry.register(CRAWLER, CrawlerEntity.createCrawlerAttributes());
        FabricDefaultAttributeRegistry.register(CHEST_SPITTER, ChestSpitterEntity.createChestSpitterAttributes());
        FabricDefaultAttributeRegistry.register(JULIETTE_THING, JulietteThingEntity.createJulietteThingAttributes());
        FabricDefaultAttributeRegistry.register(DOGBEAST, DogBeastEntity.createDogBeastAttributes());
    }
}
