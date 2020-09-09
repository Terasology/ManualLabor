// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.manualLabor.processParts;

import org.terasology.engine.entitySystem.Component;

/**
 * Creates an material item containing the materials that it is composed of based on the original input items.  The item
 * will appear like the largest amount of substance.
 */
public class SiftedMaterialOutputComponent implements Component {
    public String item;
    public String smallItem;
    public float smallItemAmount = 2.5f;
    public float minimumSiftableAmount = 1f;
}
