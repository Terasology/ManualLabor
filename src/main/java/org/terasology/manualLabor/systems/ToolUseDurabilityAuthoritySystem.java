// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.manualLabor.systems;

import org.terasology.durability.events.ReduceDurabilityEvent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.machines.events.RequirementUsedEvent;
import org.terasology.workstation.process.inventory.InventoryInputProcessPartSlotAmountsComponent;

@RegisterSystem(RegisterMode.AUTHORITY)
public class ToolUseDurabilityAuthoritySystem extends BaseComponentSystem {
    @ReceiveEvent
    public void onToolUse(RequirementUsedEvent event, EntityRef entity) {
        InventoryInputProcessPartSlotAmountsComponent slotAmounts =
                event.getProcessEntity().getComponent(InventoryInputProcessPartSlotAmountsComponent.class);
        if (slotAmounts != null) {
            int totalAmount = 0;
            for (Integer itemAmount : slotAmounts.slotAmounts.values()) {
                totalAmount += itemAmount;
            }
            entity.send(new ReduceDurabilityEvent(totalAmount));
        } else {
            entity.send(new ReduceDurabilityEvent(1));
        }
    }
}
