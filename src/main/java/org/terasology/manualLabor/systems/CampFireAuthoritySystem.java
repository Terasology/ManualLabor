// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.manualLabor.systems;

import com.google.common.collect.Sets;
import org.terasology.engine.entitySystem.entity.EntityBuilder;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnAddedComponent;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.destruction.BeforeDestroyEvent;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.block.BlockComponent;
import org.terasology.manualLabor.components.LightAreaComponent;
import org.terasology.math.geom.Vector3f;

import java.util.Set;

@RegisterSystem(RegisterMode.AUTHORITY)
public class CampFireAuthoritySystem extends BaseComponentSystem {
    @In
    EntityManager entityManager;

    @ReceiveEvent
    public void addLightArea(OnAddedComponent event, EntityRef entity, LightAreaComponent lightAreaComponent,
                             LocationComponent location, BlockComponent block) {
        Set<Vector3f> lightRelativePositions = Sets.newHashSet();
        lightRelativePositions.add(new Vector3f(0, 0, 0));

        for (Vector3f relativePosition : lightRelativePositions) {
            EntityBuilder renderedEntityBuilder = entityManager.newBuilder(lightAreaComponent.lightPrefab);
            renderedEntityBuilder.setOwner(entity);
            LocationComponent locationComponent = new LocationComponent();
            Vector3f worldPosition = block.getPosition().toVector3f();
            worldPosition.add(relativePosition);
            locationComponent.setWorldPosition(worldPosition);
            renderedEntityBuilder.addComponent(locationComponent);
            EntityRef newLightEntity = renderedEntityBuilder.build();

            lightAreaComponent.lights.add(newLightEntity);
        }
        entity.saveComponent(lightAreaComponent);
    }

    @ReceiveEvent
    public void destroyLightArea(BeforeDestroyEvent event, EntityRef entityRef, LightAreaComponent lightAreaComponent
            , BlockComponent block) {
        for (EntityRef light : lightAreaComponent.lights) {
            light.destroy();
        }
    }
}
