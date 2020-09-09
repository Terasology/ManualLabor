// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.manualLabor.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;

public class AnalyzedMaterialComponent implements Component {
    @Replicate
    public boolean IsAnalyzed = true;
}
