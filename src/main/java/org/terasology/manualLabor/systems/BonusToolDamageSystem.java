// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.manualLabor.systems;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.world.block.BlockComponent;
import org.terasology.engine.world.block.entity.damage.BlockDamageModifierComponent;
import org.terasology.engine.world.block.family.BlockFamily;
import org.terasology.health.logic.event.BeforeDamagedEvent;
import org.terasology.inventory.rendering.nui.layers.ingame.GetItemTooltip;
import org.terasology.manualLabor.components.BonusToolDamageComponent;
import org.terasology.nui.widgets.TooltipLine;

@RegisterSystem(RegisterMode.AUTHORITY)
public class BonusToolDamageSystem extends BaseComponentSystem {
    @ReceiveEvent(netFilter = RegisterMode.AUTHORITY)
    public void beforeDamage(BeforeDamagedEvent event, EntityRef entity, BlockComponent blockComponent) {
        if (event.getDamageType() != null) {
            BlockDamageModifierComponent blockDamage =
                    event.getDamageType().getComponent(BlockDamageModifierComponent.class);
            BonusToolDamageComponent bonusToolDamageComponent =
                    event.getDirectCause().getComponent(BonusToolDamageComponent.class);
            if (blockDamage != null && bonusToolDamageComponent != null) {
                BlockFamily blockFamily = blockComponent.getBlock().getBlockFamily();
                for (String category : blockFamily.getCategories()) {
                    if (blockDamage.materialDamageMultiplier.containsKey(category)) {
                        event.add(bonusToolDamageComponent.baseDamage);
                    }
                }
            }
        }
    }


    @ReceiveEvent
    public void getDurabilityItemTooltip(GetItemTooltip event, EntityRef entity,
                                         BonusToolDamageComponent bonusToolDamageComponent) {
        event.getTooltipLines().add(new TooltipLine("Bonus Damage: " + bonusToolDamageComponent.baseDamage));
    }
}
