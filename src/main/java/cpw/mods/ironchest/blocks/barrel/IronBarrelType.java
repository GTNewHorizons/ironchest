/*******************************************************************************
 * Copyright (c) 2012 cpw. All rights reserved. This program and the accompanying materials are made available under the
 * terms of the GNU Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors: cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest.blocks.barrel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.gui.barrel.slot.ValidatingBarrelSlot;
import cpw.mods.ironchest.tileentity.barrel.TileEntityCopperBarrel;
import cpw.mods.ironchest.tileentity.barrel.TileEntityDarkSteelBarrel;
import cpw.mods.ironchest.tileentity.barrel.TileEntityDiamondBarrel;
import cpw.mods.ironchest.tileentity.barrel.TileEntityGoldBarrel;
import cpw.mods.ironchest.tileentity.barrel.TileEntityIronBarrel;
import cpw.mods.ironchest.tileentity.barrel.TileEntityNetheriteBarrel;
import cpw.mods.ironchest.tileentity.barrel.TileEntityObsidianBarrel;
import cpw.mods.ironchest.tileentity.barrel.TileEntitySilverBarrel;
import cpw.mods.ironchest.tileentity.barrel.TileEntitySteelBarrel;

public enum IronBarrelType {

    BARREL_COPPER(45, 9, 1, "Copper Barrel", TileEntityCopperBarrel.class, 0),
    BARREL_IRON(54, 9, 2, "Iron Barrel", TileEntityIronBarrel.class, 0),
    BARREL_STEEL(72, 9, 3, "Copper Barrel", TileEntitySteelBarrel.class, 0),
    BARREL_SILVER(72, 9, 3, "Silver Barrel", TileEntitySilverBarrel.class, 0),
    BARREL_GOLD(81, 9, 4, "Gold Barrel", TileEntityGoldBarrel.class, 0),
    BARREL_DIAMOND(108, 12, 5, "Diamond Barrel", TileEntityDiamondBarrel.class, 0),
    BARREL_OBSIDIAN(108, 12, 5, "Obsidian Barrel", TileEntityObsidianBarrel.class, 1),
    BARREL_NETHERITE(135, 15, 6, "Netherite Barrel", TileEntityNetheriteBarrel.class, 1),
    BARREL_DARKSTEEL(135, 15, 6, "Dark Steel Barrel", TileEntityDarkSteelBarrel.class, 0),
    BARREL_WOOD(0, 0, -1, "", null, 0),;

    public final int size;
    private final int rowLength;
    private final Integer tier;
    public final String friendlyName;
    public final Class<? extends TileEntityIronBarrel> clazz;
    private final Item itemFilter;
    private final int resistance;

    IronBarrelType(int size, int rowLength, int tier, String friendlyName, Class<? extends TileEntityIronBarrel> clazz,
            int resistance) {
        this(size, rowLength, tier, friendlyName, clazz, (Item) null, resistance);
    }

    IronBarrelType(int size, int rowLength, int tier, String friendlyName, Class<? extends TileEntityIronBarrel> clazz,
            Item itemFilter, int resistance) {
        this.size = size;
        this.rowLength = rowLength;
        this.tier = tier;
        this.friendlyName = friendlyName;
        this.clazz = clazz;
        this.itemFilter = itemFilter;
        this.resistance = resistance;
    }

    public boolean allowUpgradeFrom(IronBarrelType typ) {
        var name = typ.name() + ":" + this.name();

        for (String blocked : IronChest.blocklistUpgrades) {
            if (blocked.equals(name)) {
                return false;
            }
        }

        return true;
    }

    public static TileEntityIronBarrel makeEntity(int metadata) {
        // Compatibility
        int chesttype = validateMeta(metadata);
        if (chesttype == metadata) {
            try {
                return values()[chesttype].clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                // unpossible
                e.printStackTrace();
            }
        }
        return null;
    }

    public TileEntityIronBarrel makeEntity() {
        try {
            return this.clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            // unpossible
            e.printStackTrace();
        }
        return null;
    }

    public static IronBarrelType[] getAll() {
        return values();
    }

    public static IronBarrelType[] getAllSortedByTier() {
        IronBarrelType[] vals = getAll();

        Arrays.sort(vals, Comparator.comparing(a -> a.tier));

        return vals;
    }

    public static IronBarrelType[] getAllByTier(int i) {
        HashSet<IronBarrelType> vals = new HashSet<IronBarrelType>();

        for (IronBarrelType typ : values()) {
            if (typ.tier == i) {
                vals.add(typ);
            }
        }

        return vals.toArray(new IronBarrelType[vals.size()]);
    }

    public static Object translateOreName(String mat) {
        if (mat.equals("obsidian")) {
            return Blocks.obsidian;
        } else if (mat.equals("dirt")) {
            return Blocks.dirt;
        }
        return mat;
    }

    public int getRowCount() {
        return size / rowLength;
    }

    public int getRowLength() {
        return rowLength;
    }

    public static int validateMeta(int i) {
        if (i < values().length && values()[i].size > 0) {
            return i;
        } else {
            return 0;
        }
    }

    public boolean isValidForCreativeMode() {
        return validateMeta(ordinal()) == ordinal();
    }

    public boolean isExplosionResistant() {
        return this.resistance >= 1;
    }

    public Slot makeSlot(IInventory chestInventory, int index, int x, int y) {
        return new ValidatingBarrelSlot(chestInventory, index, x, y, this);
    }

    public boolean acceptsStack(ItemStack itemstack) {
        return itemFilter == null || itemstack == null || itemstack.getItem() == itemFilter;
    }
}
