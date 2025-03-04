/*******************************************************************************
 * Copyright (c) 2012 cpw. All rights reserved. This program and the accompanying materials are made available under the
 * terms of the GNU Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors: cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.ironchest.blocks.barrel.IronBarrelType;
import cpw.mods.ironchest.blocks.chest.IronChestType;
import cpw.mods.ironchest.gui.chest.ContainerIronChest;
import cpw.mods.ironchest.tileentity.barrel.TileEntityIronBarrel;
import cpw.mods.ironchest.tileentity.chest.TileEntityIronChest;

public class GUIChest extends GuiContainer {

    public enum ResourceList {

        IRON(new ResourceLocation("ironchest", "textures/gui/iron.png")),
        COPPER(new ResourceLocation("ironchest", "textures/gui/copper.png")),
        STEEL(new ResourceLocation("ironchest", "textures/gui/silver.png")),
        GOLD(new ResourceLocation("ironchest", "textures/gui/gold.png")),
        DIAMOND(new ResourceLocation("ironchest", "textures/gui/diamond.png")),
        NETHERITE(new ResourceLocation("ironchest", "textures/gui/netherite.png")),
        DARKSTEEL(new ResourceLocation("ironchest", "textures/gui/netherite.png")),
        SILVER(new ResourceLocation("ironchest", "textures/gui/silver.png")),
        DIRT(new ResourceLocation("ironchest", "textures/gui/dirt.png"));

        public final ResourceLocation location;

        private ResourceList(ResourceLocation loc) {
            this.location = loc;
        }
    }

    public enum GUI {

        IRON(184, 202, ResourceList.IRON, IronChestType.IRON, IronBarrelType.BARREL_IRON),
        GOLD(184, 256, ResourceList.GOLD, IronChestType.GOLD, IronBarrelType.BARREL_GOLD),
        DIAMOND(238, 256, ResourceList.DIAMOND, IronChestType.DIAMOND, IronBarrelType.BARREL_DIAMOND),
        COPPER(184, 184, ResourceList.COPPER, IronChestType.COPPER, IronBarrelType.BARREL_COPPER),
        STEEL(184, 238, ResourceList.STEEL, IronChestType.STEEL, IronBarrelType.BARREL_STEEL),
        CRYSTAL(238, 256, ResourceList.DIAMOND, IronChestType.CRYSTAL, IronBarrelType.BARREL_DIAMOND),
        OBSIDIAN(238, 256, ResourceList.DIAMOND, IronChestType.OBSIDIAN, IronBarrelType.BARREL_OBSIDIAN),
        DIRTCHEST9000(184, 184, ResourceList.DIRT, IronChestType.DIRTCHEST9000, IronBarrelType.BARREL_IRON),
        NETHERITE(292, 256, ResourceList.NETHERITE, IronChestType.NETHERITE, IronBarrelType.BARREL_NETHERITE),
        DARKSTEEL(292, 256, ResourceList.DARKSTEEL, IronChestType.DARKSTEEL, IronBarrelType.BARREL_DARKSTEEL),
        SILVER(184, 238, ResourceList.SILVER, IronChestType.SILVER, IronBarrelType.BARREL_SILVER);

        private final int xSize;
        private final int ySize;
        private final ResourceList guiResourceList;
        private final IronChestType mainType;
        private final IronBarrelType barrelType;

        private GUI(int xSize, int ySize, ResourceList guiResourceList, IronChestType mainType,
                IronBarrelType barrelType) {
            this.xSize = xSize;
            this.ySize = ySize;
            this.guiResourceList = guiResourceList;
            this.mainType = mainType;
            this.barrelType = barrelType;
        }

        protected Container makeContainer(IInventory player, IInventory chest) {
            return new ContainerIronChest(player, chest, mainType, xSize, ySize);
        }

        public static GUIChest buildGUI(int chestTypeIndex, IInventory playerInventory,
                TileEntityIronChest chestInventory) {
            for (GUI gui : values()) {
                if (gui.mainType.ordinal() == chestTypeIndex) {
                    return new GUIChest(gui, playerInventory, chestInventory);
                }
            }
            return null;
        }

        public static GUIChest buildGUI(int chestTypeIndex, IInventory playerInventory,
                TileEntityIronBarrel ironBarrel) {
            for (GUI gui : values()) {
                if (gui.barrelType.ordinal() == chestTypeIndex) {
                    return new GUIChest(gui, playerInventory, ironBarrel);
                }
            }
            return null;
        }
    }

    public int getRowLength() {
        return type.mainType.getRowLength();
    }

    private final GUI type;

    private GUIChest(GUI type, IInventory player, IInventory chest) {
        super(type.makeContainer(player, chest));
        this.type = type;
        this.xSize = type.xSize;
        this.ySize = type.ySize;
        this.allowUserInput = false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // new "bind tex"
        this.mc.getTextureManager().bindTexture(type.guiResourceList.location);

        if (type == GUI.NETHERITE || type == GUI.DARKSTEEL) {
            final Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(guiLeft, guiTop, 0, 0.0, 0.0);
            tessellator.addVertexWithUV(guiLeft, guiTop + ySize, 0, 0.0, 1.0);
            tessellator.addVertexWithUV(guiLeft + xSize, guiTop + ySize, 0, 1.0, 1.0);
            tessellator.addVertexWithUV(guiLeft + xSize, guiTop, 0, 1.0, 0.0);
            tessellator.draw();
            return;
        }

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

    }
}
