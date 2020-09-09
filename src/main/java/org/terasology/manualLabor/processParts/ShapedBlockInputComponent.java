// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.manualLabor.processParts;

import org.terasology.engine.entitySystem.Component;

public class ShapedBlockInputComponent implements Component {
    public String shape;
    public int amount = 1;
}
