// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.manualLabor.processParts;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.entitySystem.prefab.PrefabManager;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.manualLabor.components.MetalSubstanceComponent;
import org.terasology.substanceMatters.components.MaterialCompositionComponent;
import org.terasology.substanceMatters.components.MaterialItemComponent;
import org.terasology.workstation.process.WorkstationInventoryUtils;
import org.terasology.workstation.process.inventory.InventoryInputComponent;
import org.terasology.workstation.process.inventory.InventoryInputProcessPartCommonSystem;
import org.terasology.workstation.processPart.inventory.ProcessEntityIsInvalidForInventoryItemEvent;

@RegisterSystem
public class MetalSubstanceRequirementProcessPartCommonSystem extends BaseComponentSystem {
    @In
    PrefabManager prefabManager;

    @ReceiveEvent
    public void isValidInventoryItem(ProcessEntityIsInvalidForInventoryItemEvent event, EntityRef processEntity,
                                     MetalSubstanceRequirementComponent metalSubstanceRequirementComponent,
                                     InventoryInputComponent inventoryInputComponent) {
        if (WorkstationInventoryUtils.getAssignedInputSlots(event.getWorkstation(),
                InventoryInputProcessPartCommonSystem.WORKSTATIONINPUTCATEGORY).contains(event.getSlotNo())) {
            MaterialItemComponent materialItemComponent = event.getItem().getComponent(MaterialItemComponent.class);
            MaterialCompositionComponent materialCompositionComponent =
                    event.getItem().getComponent(MaterialCompositionComponent.class);
            if (materialItemComponent != null && materialCompositionComponent != null) {
                String primarySubstanceUri = materialCompositionComponent.getPrimarySubstance();
                Prefab primarySubstance = prefabManager.getPrefab(primarySubstanceUri);
                if (!primarySubstance.hasComponent(MetalSubstanceComponent.class)) {
                    event.consume();
                }
            }
        }
    }
}
