// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.manualLabor.components;

import org.terasology.engine.entitySystem.Component;

/**
 * Attached to entities that can be sheared.
 */
public class ShearableComponent implements Component {

    /**
     * Stores the last time the shearing took place. Default -1 for no previous shearing event.
     */
    public long lastShearingTimestamp = -1;

    /**
     * Stores the current state of shearing i.e. whether sheared or not.
     */
    public boolean isSheared;
}
