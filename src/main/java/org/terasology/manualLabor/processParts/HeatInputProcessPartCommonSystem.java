/*
 * Copyright 2016 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.manualLabor.processParts;

import org.terasology.entityNetwork.systems.EntityNetworkManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.machines.ui.OverlapLayout;
import org.terasology.manualLabor.components.HeatedComponent;
import org.terasology.manualLabor.systems.HeatAuthoritySystem;
import org.terasology.nui.widgets.UIImage;
import org.terasology.registry.In;
import org.terasology.utilities.Assets;
import org.terasology.workstation.process.ProcessPartDescription;
import org.terasology.workstation.processPart.ProcessEntityFinishExecutionEvent;
import org.terasology.workstation.processPart.ProcessEntityIsInvalidToStartEvent;
import org.terasology.workstation.processPart.ProcessEntityStartExecutionEvent;
import org.terasology.workstation.processPart.metadata.ProcessEntityGetInputDescriptionEvent;


@RegisterSystem
public class HeatInputProcessPartCommonSystem extends BaseComponentSystem {
    @In
    EntityNetworkManager entityNetworkManager;

    ///// Processing

    @ReceiveEvent
    public void validateToStartExecution(ProcessEntityIsInvalidToStartEvent event, EntityRef processEntity,
                                         HeatInputComponent heatInputComponent) {

        float averageHeat = HeatAuthoritySystem.getAverageHeat(event.getWorkstation(), entityNetworkManager);

        // compare to the average heat sources in the network
        if (averageHeat == 0) {
            event.consume();
        }
    }

    @ReceiveEvent
    public void startExecution(ProcessEntityStartExecutionEvent event, EntityRef processEntity,
                               HeatInputComponent heatInputComponent) {
        if (event.getWorkstation().hasComponent(HeatedComponent.class)) {
            event.getWorkstation().removeComponent(HeatedComponent.class);
        }
    }

    @ReceiveEvent
    public void finishExecution(ProcessEntityFinishExecutionEvent event, EntityRef entityRef,
                                HeatInputComponent heatInputComponent) {
        if (event.getWorkstation().hasComponent(HeatedComponent.class)) {
            event.getWorkstation().removeComponent(HeatedComponent.class);
        }
    }

    ///// Metadata

    @ReceiveEvent
    public void getInputDescriptions(ProcessEntityGetInputDescriptionEvent event, EntityRef processEntity,
                                     HeatInputComponent heatInputComponent) {
        String description = "Heat";
        UIImage image = new UIImage(Assets.getTextureRegion("ManualLabor:Manuallabor#Heat").get());
        OverlapLayout layout = new OverlapLayout();
        layout.addWidget(image);
        layout.setTooltip(description);
        layout.setTooltipDelay(0);
        event.addInputDescription(new ProcessPartDescription(null, description, layout));
    }
}
