/*******************************************************************************
 * Copyright (c) 2012 cpw. All rights reserved. This program and the accompanying materials are made available under the
 * terms of the GNU Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors: cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemIronChest extends ItemBlock {

    public ItemIronChest(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int i) {
        return IronChestType.validateMeta(i);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return "tile.ironchest:" + IronChestType.values()[itemstack.getItemDamage()].name();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        switch (stack.getItemDamage()) {
            case 0:
                tooltip.add(StatCollector.translateToLocal("tile.ironchest:IRON.tooltip"));
                break;
            case 1:
                tooltip.add(StatCollector.translateToLocal("tile.ironchest:GOLD.tooltip"));
                break;
            case 2:
            case 5:
                tooltip.add(StatCollector.translateToLocal("tile.ironchest:DIAMOND.tooltip"));
                break;
            case 3:
                tooltip.add(StatCollector.translateToLocal("tile.ironchest:COPPER.tooltip"));
                break;
            case 4:
                tooltip.add(StatCollector.translateToLocal("tile.ironchest:STEEL.tooltip"));
                break;
            case 6:
                tooltip.add(StatCollector.translateToLocal("tile.ironchest:OBSIDIAN.tooltip"));
                break;
            case 9:
                tooltip.add(StatCollector.translateToLocal("tile.ironchest:DARKSTEEL.tooltip"));
                break;
        }
    }
}
