// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.manualLabor.systems;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.internal.PojoEntityManager;
import org.terasology.engine.rendering.assets.material.Material;
import org.terasology.engine.rendering.assets.skeletalmesh.SkeletalMesh;
import org.terasology.engine.rendering.logic.SkeletalMeshComponent;
import org.terasology.gestalt.assets.management.AssetManager;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShearingSystemTest {
    ShearingSystem shearingSystem;
    private EntityRef entity;

    /**
     * Initialize the shearing system and entity to be used while testing. The entity is supposed to mock the animal entity.
     */
    @BeforeEach
    public void setUp() {
        shearingSystem = new ShearingSystem();
        entity = new PojoEntityManager().create();
        SkeletalMeshComponent skeletalMeshComponent = new SkeletalMeshComponent();
        entity.saveComponent(skeletalMeshComponent);
    }

    /**
     * Check whether switchPrefab() changes mesh and material attributes of SkeletalMeshComponent correctly in all scenarios
     */
    @Test
    public void testSwitchPrefabAssetChange() {
        SkeletalMesh expectedMesh = mock(SkeletalMesh.class);
        Material expectedMaterial = mock(Material.class);
        shearingSystem.assetManager = mock(AssetManager.class);
        when(shearingSystem.assetManager.getAsset("testMesh", SkeletalMesh.class)).thenReturn(Optional.of(expectedMesh));
        when(shearingSystem.assetManager.getAsset("testMaterial", Material.class)).thenReturn(Optional.of(expectedMaterial));

        shearingSystem.switchPrefab(entity, "testMesh", "testMaterial");
        SkeletalMeshComponent skeletalMeshComponent = entity.getComponent(SkeletalMeshComponent.class);

        Assertions.assertEquals(skeletalMeshComponent.mesh, expectedMesh);
        Assertions.assertEquals(skeletalMeshComponent.material, expectedMaterial);

        when(shearingSystem.assetManager.getAsset("empty", SkeletalMesh.class)).thenReturn(Optional.empty());
        when(shearingSystem.assetManager.getAsset("empty", Material.class)).thenReturn(Optional.empty());

        shearingSystem.switchPrefab(entity, "testMesh", "empty");
        skeletalMeshComponent = entity.getComponent(SkeletalMeshComponent.class);

        Assertions.assertEquals(skeletalMeshComponent.mesh, expectedMesh);
        Assertions.assertEquals(skeletalMeshComponent.material, expectedMaterial);

        shearingSystem.switchPrefab(entity, "empty", "testMaterial");
        skeletalMeshComponent = entity.getComponent(SkeletalMeshComponent.class);

        Assertions.assertEquals(skeletalMeshComponent.mesh, expectedMesh);
        Assertions.assertEquals(skeletalMeshComponent.material, expectedMaterial);

        shearingSystem.switchPrefab(entity, "empty", "empty");
        skeletalMeshComponent = entity.getComponent(SkeletalMeshComponent.class);

        Assertions.assertEquals(skeletalMeshComponent.mesh, expectedMesh);
        Assertions.assertEquals(skeletalMeshComponent.material, expectedMaterial);

    }
}
