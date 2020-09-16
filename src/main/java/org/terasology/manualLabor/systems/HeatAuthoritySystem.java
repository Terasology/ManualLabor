// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.manualLabor.systems;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.machines.entityNetwork.Network;
import org.terasology.machines.entityNetwork.NetworkNode;
import org.terasology.machines.entityNetwork.components.EntityNetworkComponent;
import org.terasology.machines.entityNetwork.systems.EntityNetworkManager;
import org.terasology.manualLabor.components.HeatBlockNetworkComponent;
import org.terasology.manualLabor.components.HeatSourceComponent;
import org.terasology.manualLabor.components.HeatedComponent;
import org.terasology.workstation.event.WorkstationStateChanged;

@RegisterSystem(RegisterMode.AUTHORITY)
public class HeatAuthoritySystem extends BaseComponentSystem {
    @In
    EntityNetworkManager entityNetworkManager;

    public static float getAverageHeat(EntityRef entityRef, EntityNetworkManager entityNetworkManager) {
        int heatSources = 0;
        int heatSinks = 0;
        for (NetworkNode workstationNode : entityNetworkManager.getNodesForEntity(entityRef)) {
            if (workstationNode.getNetworkId().equals(HeatBlockNetworkComponent.NETWORK_ID)) {
                for (Network network : entityNetworkManager.getNetworks(workstationNode)) {
                    // loop through each of the nodes on this network to see how many are heat sources
                    for (NetworkNode siblingNode : entityNetworkManager.getNetworkNodes(network)) {
                        EntityRef siblingEntity = entityNetworkManager.getEntityForNode(siblingNode);
                        if (siblingEntity.hasComponent(HeatSourceComponent.class)) {
                            heatSources++;
                        }
                        if (siblingEntity.hasComponent(HeatedComponent.class)) {
                            heatSinks++;
                        }
                    }
                }
            }
        }

        return (float) (heatSources / (heatSinks + 1));
    }

    public static float getTotalHeat(EntityRef entityRef, EntityNetworkManager entityNetworkManager) {
        int heatSources = 0;
        for (NetworkNode workstationNode : entityNetworkManager.getNodesForEntity(entityRef)) {
            if (workstationNode.getNetworkId().equals(HeatBlockNetworkComponent.NETWORK_ID)) {
                for (Network network : entityNetworkManager.getNetworks(workstationNode)) {
                    // loop through each of the nodes on this network to see how many are heat sources
                    for (NetworkNode siblingNode : entityNetworkManager.getNetworkNodes(network)) {
                        EntityRef siblingEntity = entityNetworkManager.getEntityForNode(siblingNode);
                        if (siblingEntity.hasComponent(HeatSourceComponent.class)) {
                            heatSources++;
                        }
                    }
                }
            }
        }

        return (float) heatSources;
    }

    @ReceiveEvent
    public void onHeatedChanged(OnChangedComponent event, EntityRef entity,
                                HeatedComponent heatedComponent,
                                EntityNetworkComponent entityNetworkComponent) {
        notifyConnectedWorkstations(entity);
    }

    @ReceiveEvent
    public void onHeatedActivated(OnActivatedComponent event, EntityRef entity,
                                  HeatedComponent heatedComponent,
                                  EntityNetworkComponent entityNetworkComponent) {
        notifyConnectedWorkstations(entity);
    }

    private void notifyConnectedWorkstations(EntityRef entityRef) {
        for (NetworkNode networkNode : entityNetworkManager.getNodesForEntity(entityRef)) {
            for (Network network : entityNetworkManager.getNetworks(networkNode)) {
                for (NetworkNode connectedNode : entityNetworkManager.getNetworkNodes(network)) {
                    EntityRef connectedEntityRef = entityNetworkManager.getEntityForNode(connectedNode);
                    if (!connectedEntityRef.equals(entityRef)) {
                        connectedEntityRef.send(new WorkstationStateChanged());
                    }
                }
            }
        }
    }
}
