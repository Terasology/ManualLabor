// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.manualLabor.systems;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.EventPriority;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.network.ClientComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.NUIManager;
import org.terasology.inventory.input.binds.InventoryButton;
import org.terasology.manualLabor.components.TheHumanMachineComponent;
import org.terasology.nui.input.ButtonState;

@RegisterSystem(RegisterMode.CLIENT)
public class TheHumanMachineClientSystem extends BaseComponentSystem {
    @In
    NUIManager nuiManager;

    @In
    LocalPlayer localPlayer;


    @ReceiveEvent(components = ClientComponent.class, priority = EventPriority.PRIORITY_HIGH)
    public void onToggleInventory(InventoryButton event, EntityRef entity) {
        if (event.getState() == ButtonState.DOWN) {
            TheHumanMachineComponent theHumanMachine =
                    localPlayer.getCharacterEntity().getComponent(TheHumanMachineComponent.class);
            localPlayer.activateOwnedEntityAsClient(theHumanMachine.machineEntity);
            event.consume();
        }
    }
}
