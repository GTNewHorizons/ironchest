/*******************************************************************************
 * Copyright (c) 2012 cpw. All rights reserved. This program and the accompanying materials are made available under the
 * terms of the GNU Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors: cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.ironchest.blocks.chest.IronChestType;
import cpw.mods.ironchest.gui.barrel.ContainerIronBarrel;
import cpw.mods.ironchest.gui.chest.ContainerIronChest;
import cpw.mods.ironchest.tileentity.barrel.TileEntityIronBarrel;
import cpw.mods.ironchest.tileentity.chest.TileEntityIronChest;

public class CommonProxy implements IGuiHandler {

    public void registerRenderInformation() {

    }

    public void registerTileEntitySpecialRenderer(IronChestType typ) {

    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int X, int Y, int Z) {
        TileEntity te = world.getTileEntity(X, Y, Z);
        if (te instanceof TileEntityIronChest ironChest) {
            return new ContainerIronChest(player.inventory, ironChest, ironChest.getType(), 0, 0);
        } else if (te instanceof TileEntityIronBarrel ironBarrel) {
            return new ContainerIronBarrel(player.inventory, ironBarrel, ironBarrel.getType(), 0, 0);
        } else {
            return null;
        }
    }

    public World getClientWorld() {
        return null;
    }

}
