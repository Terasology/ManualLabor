// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.manualLabor.processParts;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.inventory.logic.InventoryManager;
import org.terasology.manualLabor.components.AnalyzedMaterialComponent;
import org.terasology.substanceMatters.components.MaterialCompositionComponent;
import org.terasology.workstation.process.WorkstationInventoryUtils;
import org.terasology.workstation.process.inventory.InventoryInputProcessPartCommonSystem;
import org.terasology.workstation.process.inventory.InventoryOutputProcessPartCommonSystem;
import org.terasology.workstation.processPart.ProcessEntityFinishExecutionEvent;
import org.terasology.workstation.processPart.ProcessEntityIsInvalidToStartEvent;
import org.terasology.workstation.processPart.inventory.ProcessEntityIsInvalidForInventoryItemEvent;

@RegisterSystem
public class AnalyzeMaterialsProcessPartCommonSystem extends BaseComponentSystem {
    @In
    InventoryManager inventoryManager;

    ///// Processing

    @ReceiveEvent
    public void validateToStartExecution(ProcessEntityIsInvalidToStartEvent event, EntityRef processEntity,
                                         AnalyzeMaterialsComponent analyzeMaterialsComponent) {
        for (int inputSlot : WorkstationInventoryUtils.getAssignedInputSlots(event.getWorkstation(),
                InventoryInputProcessPartCommonSystem.WORKSTATIONINPUTCATEGORY)) {
            EntityRef inputItem = inventoryManager.getItemInSlot(event.getWorkstation(), inputSlot);
            if (inputItem.exists()) {
                return;
            }
        }
        event.consume();
    }

    @ReceiveEvent
    public void finishExecution(ProcessEntityFinishExecutionEvent event, EntityRef entityRef,
                                AnalyzeMaterialsComponent analyzeMaterialsComponent) {
        for (int inputSlot : WorkstationInventoryUtils.getAssignedInputSlots(event.getWorkstation(),
                InventoryInputProcessPartCommonSystem.WORKSTATIONINPUTCATEGORY)) {
            EntityRef inputItem = inventoryManager.getItemInSlot(event.getWorkstation(), inputSlot);
            if (inputItem.exists()) {
                if (!inputItem.hasComponent(AnalyzedMaterialComponent.class)) {
                    inputItem.addComponent(new AnalyzedMaterialComponent());
                }
                inventoryManager.moveItemToSlots(event.getInstigator(), event.getWorkstation(), inputSlot,
                        event.getWorkstation(),
                        WorkstationInventoryUtils.getAssignedOutputSlots(event.getWorkstation(),
                                InventoryOutputProcessPartCommonSystem.WORKSTATIONOUTPUTCATEGORY));
            }
        }
    }

    ///// Inventory

    @ReceiveEvent
    public void isValidInventoryItem(ProcessEntityIsInvalidForInventoryItemEvent event, EntityRef processEntity,
                                     AnalyzeMaterialsComponent analyzeMaterialsComponent) {
        if (WorkstationInventoryUtils.getAssignedInputSlots(event.getWorkstation(),
                InventoryInputProcessPartCommonSystem.WORKSTATIONINPUTCATEGORY).contains(event.getSlotNo())
                || WorkstationInventoryUtils.getAssignedOutputSlots(event.getWorkstation(),
                InventoryOutputProcessPartCommonSystem.WORKSTATIONOUTPUTCATEGORY).contains(event.getSlotNo())) {
            // let analyzed items go to the output,  and un-analyzed go to the input
            if (WorkstationInventoryUtils.getAssignedInputSlots(event.getWorkstation(),
                    InventoryInputProcessPartCommonSystem.WORKSTATIONINPUTCATEGORY).contains(event.getSlotNo())) {
                if (event.getItem().hasComponent(MaterialCompositionComponent.class) && !event.getItem().hasComponent(AnalyzedMaterialComponent.class)) {
                    return;
                }
            } else if (WorkstationInventoryUtils.getAssignedOutputSlots(event.getWorkstation(),
                    InventoryOutputProcessPartCommonSystem.WORKSTATIONOUTPUTCATEGORY).contains(event.getSlotNo())) {
                if (event.getItem().hasComponent(AnalyzedMaterialComponent.class)) {
                    return;
                }
            }

            event.consume();
        }
    }
}
