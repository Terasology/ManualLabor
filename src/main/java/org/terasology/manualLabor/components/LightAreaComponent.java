// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.manualLabor.components;

import com.google.common.collect.Sets;
import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.entitySystem.Owns;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.world.block.ForceBlockActive;

import java.util.Set;

@ForceBlockActive
public class LightAreaComponent implements Component {
    @Owns
    public Set<EntityRef> lights = Sets.newHashSet();
    public String lightPrefab;
}
