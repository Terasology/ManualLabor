// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.manualLabor.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.world.block.ForceBlockActive;
import org.terasology.machines.entityNetwork.components.BlockLocationNetworkNodeComponent;

@ForceBlockActive
public class HeatBlockNetworkComponent extends BlockLocationNetworkNodeComponent implements Component {
    public static final String NETWORK_ID = "ManualLabor:Heat";

    public HeatBlockNetworkComponent() {
        networkId = NETWORK_ID;
    }
}
