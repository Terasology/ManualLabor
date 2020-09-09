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
import org.terasology.manualLabor.components.BurnableSubstanceComponent;
import org.terasology.substanceMatters.components.MaterialCompositionComponent;
import org.terasology.substanceMatters.components.MaterialItemComponent;
import org.terasology.workstation.process.WorkstationInventoryUtils;
import org.terasology.workstation.process.inventory.InventoryInputComponent;
import org.terasology.workstation.process.inventory.InventoryInputItemsComponent;
import org.terasology.workstation.process.inventory.InventoryInputProcessPartCommonSystem;
import org.terasology.workstation.processPart.ProcessEntityGetDurationEvent;
import org.terasology.workstation.processPart.inventory.ProcessEntityIsInvalidForInventoryItemEvent;

import java.util.Map;

@RegisterSystem
public class BurnableSubstanceRequirementProcessPartCommonSystem extends BaseComponentSystem {
    @In
    PrefabManager prefabManager;

    @ReceiveEvent
    public void isValidInventoryItem(ProcessEntityIsInvalidForInventoryItemEvent event, EntityRef processEntity,
                                     BurnableSubstanceRequirementComponent burnableSubstanceRequirementComponent,
                                     InventoryInputComponent inventoryInputComponent) {
        if (WorkstationInventoryUtils.getAssignedInputSlots(event.getWorkstation(),
                InventoryInputProcessPartCommonSystem.WORKSTATIONINPUTCATEGORY).contains(event.getSlotNo())) {
            if (getBurnTime(event.getItem()) == 0) {
                event.consume();
            }
        }
    }

    @ReceiveEvent
    public void getDuration(ProcessEntityGetDurationEvent event, EntityRef processEntity,
                            BurnableSubstanceRequirementComponent burnableSubstanceRequirementComponent,
                            InventoryInputItemsComponent inputItemsComponent) {
        long totalBurnTime = 0;
        for (EntityRef item : inputItemsComponent.items) {
            totalBurnTime += getBurnTime(item);
        }

        event.add(totalBurnTime / 1000f);
    }

    private long getBurnTime(EntityRef item) {
        MaterialItemComponent materialItemComponent = item.getComponent(MaterialItemComponent.class);
        MaterialCompositionComponent materialCompositionComponent =
                item.getComponent(MaterialCompositionComponent.class);
        if (materialItemComponent != null && materialCompositionComponent != null) {
            for (Map.Entry<String, Float> substanceEntry : materialCompositionComponent.contents.entrySet()) {
                String substanceUri = substanceEntry.getKey();
                Prefab substancePrefab = prefabManager.getPrefab(substanceUri);
                BurnableSubstanceComponent burnableSubstanceComponent =
                        substancePrefab.getComponent(BurnableSubstanceComponent.class);
                if (burnableSubstanceComponent != null) {
                    return (long) (burnableSubstanceComponent.burnTimePerSubstanceAmount * substanceEntry.getValue());
                }
            }
        }
        return 0;
    }
}
