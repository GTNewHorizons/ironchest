/*******************************************************************************
 * Copyright (c) 2012 cpw. All rights reserved. This program and the accompanying materials are made available under the
 * terms of the GNU Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors: cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest.proxy;

import net.minecraft.client.renderer.tileentity.TileEntityRendererChestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.ironchest.blocks.chest.IronChestType;
import cpw.mods.ironchest.client.GUIChest;
import cpw.mods.ironchest.client.IronChestRenderHelper;
import cpw.mods.ironchest.client.TileEntityIronChestRenderer;
import cpw.mods.ironchest.tileentity.barrel.TileEntityIronBarrel;
import cpw.mods.ironchest.tileentity.chest.TileEntityIronChest;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderInformation() {
        TileEntityRendererChestHelper.instance = new IronChestRenderHelper();
    }

    @Override
    public void registerTileEntitySpecialRenderer(IronChestType typ) {
        ClientRegistry.bindTileEntitySpecialRenderer(typ.clazz, new TileEntityIronChestRenderer());
    }

    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityIronChest ironChest) {
            return GUIChest.GUI.buildGUI(ID, player.inventory, ironChest);
        } else if (te instanceof TileEntityIronBarrel ironBarrel) {
            return GUIChest.GUI.buildGUI(ID, player.inventory, ironBarrel);
        } else {
            return null;
        }
    }
}
