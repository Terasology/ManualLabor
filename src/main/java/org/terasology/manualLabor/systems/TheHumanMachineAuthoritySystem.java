// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.manualLabor.systems;

import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.characters.CharacterComponent;
import org.terasology.engine.logic.players.event.OnPlayerSpawnedEvent;
import org.terasology.engine.registry.In;
import org.terasology.manualLabor.components.TheHumanMachineComponent;

@RegisterSystem(RegisterMode.AUTHORITY)
public class TheHumanMachineAuthoritySystem extends BaseComponentSystem {
    @In
    EntityManager entityManager;

    @ReceiveEvent(components = {CharacterComponent.class})
    public void onPlayerSpawn(OnPlayerSpawnedEvent event, EntityRef player) {
        addTheHumanMachine(player);
    }

    private TheHumanMachineComponent addTheHumanMachine(EntityRef player) {
        EntityRef machineEntity = entityManager.create("TheHumanMachine");
        TheHumanMachineComponent theHumanAutomaticProcessingComponent = new TheHumanMachineComponent();
        theHumanAutomaticProcessingComponent.machineEntity = machineEntity;
        player.addComponent(theHumanAutomaticProcessingComponent);
        return theHumanAutomaticProcessingComponent;
    }
}
