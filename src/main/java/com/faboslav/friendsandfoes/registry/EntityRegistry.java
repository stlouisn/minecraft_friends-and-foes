package com.faboslav.friendsandfoes.registry;

import com.faboslav.friendsandfoes.config.Settings;
import com.faboslav.friendsandfoes.entity.passive.CopperGolemEntity;
import com.faboslav.friendsandfoes.entity.passive.MoobloomEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

import java.util.function.Predicate;

public class EntityRegistry
{
    private static final Predicate<BiomeSelectionContext> flowerForest = BiomeSelectors.includeByKey(BiomeKeys.FLOWER_FOREST);
    private static final SpawnRestriction.Location onGround = SpawnRestriction.Location.ON_GROUND;
    private static final Heightmap.Type motionBlocking = Heightmap.Type.MOTION_BLOCKING_NO_LEAVES;
    public static final EntityType<MoobloomEntity> MOOBLOOM;
    public static final EntityType<CopperGolemEntity> COPPER_GOLEM;

    static {
        MOOBLOOM = registerMoobloom();
        COPPER_GOLEM =  registerCopperGolem();
    }

    private static EntityType<CopperGolemEntity> registerCopperGolem() {
        EntityType<CopperGolemEntity> copperGolem = registerEntity(
                "copper_golem",
                CopperGolemEntity::new,
                CopperGolemEntity.createCopperGolemAttributes(),
                SpawnGroup.MISC,
                0.75F,
                1.375F
        );

        return copperGolem;
    }

    private static EntityType<MoobloomEntity> registerMoobloom() {
        SpawnGroup spawnGroup = SpawnGroup.CREATURE;

        EntityType<MoobloomEntity> type = FabricEntityTypeBuilder
                .createMob()
                .spawnGroup(spawnGroup)
                .entityFactory(MoobloomEntity::new)
                .spawnRestriction(onGround, motionBlocking, MoobloomEntity::canSpawn)
                .dimensions(EntityDimensions.fixed(0.9F, 1.4F))
                .build();

        EntityType<MoobloomEntity> moobloom = Registry.register(Registry.ENTITY_TYPE, Settings.makeID("moobloom"), type);

        // Register attributes
        FabricDefaultAttributeRegistry.register(moobloom, MoobloomEntity.createCowAttributes());

        // Register spawn restriction
        BiomeModifications.addSpawn(flowerForest, spawnGroup, moobloom, 32, 2, 4);

        return moobloom;
    }

    private static <T extends LivingEntity> EntityType<T> registerEntity(
            String name,
            EntityType.EntityFactory<T> entity,
            DefaultAttributeContainer.Builder attributes,
            SpawnGroup group,
            float width,
            float height
    ) {
        EntityType<T> type = FabricEntityTypeBuilder.create(group, entity).dimensions(EntityDimensions.fixed(width, height)).build();
        FabricDefaultAttributeRegistry.register(type, attributes);

        return Registry.register(Registry.ENTITY_TYPE, Settings.makeID(name), type);
    }

    public static void init() {}
}