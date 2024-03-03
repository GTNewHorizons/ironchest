/*******************************************************************************
 * Copyright (c) 2012 cpw. All rights reserved. This program and the accompanying materials are made available under the
 * terms of the GNU Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors: cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public enum IronChestType {

    IRON(54, 9, 2, "Iron Chest", "ironchest.png", 0, Arrays.asList("ingotIron", "ingotRefinedIron"),
            TileEntityIronChest.class, null, "mGmGPGmGm", "mmmmPmmmm"),
    GOLD(81, 9, 4, "Gold Chest", "goldchest.png", 1, Arrays.asList("ingotGold"), TileEntityGoldChest.class, null,
            "mGmGPGmGm", "mmmmPmmmm"),
    DIAMOND(108, 12, 5, "Diamond Chest", "diamondchest.png", 2, Arrays.asList("gemDiamond"),
            TileEntityDiamondChest.class, null, "GGGmPmGGG", "GGGGPGmmm"),
    COPPER(45, 9, 1, "Copper Chest", "copperchest.png", 3, Arrays.asList("ingotCopper"), TileEntityCopperChest.class,
            null, "mmmmPmmmm", "mGmGPGmGm"),
    STEEL(72, 9, 3, "Steel Chest", "silverchest.png", 4, Arrays.asList("ingotSteel"), TileEntitySteelChest.class, null,
            "mGmGPGmGm", "mmmmPmmmm"),
    CRYSTAL(108, 12, 5, "Crystal Chest", "crystalchest.png", 5, Arrays.asList("blockGlass"),
            TileEntityCrystalChest.class, "mmmmPmmmm", null, null),
    OBSIDIAN(108, 12, 5, "Obsidian Chest", "obsidianchest.png", 6, Arrays.asList("obsidian"),
            TileEntityObsidianChest.class, "mmmmPmmmm", null, null),
    DIRTCHEST9000(1, 1, -1, "Dirt Chest 9000", "dirtchest.png", 7, Arrays.asList("dirt"), TileEntityDirtChest.class,
            Item.getItemFromBlock(Blocks.dirt), "mmmmCmmmm", null, null),
    NETHERITE(135, 15, 6, "Netherite Chest", "netheritechest.png", 2, Arrays.asList("ingotNetherite"),
            TileEntityNetheriteChest.class, null, "OOOmPmOOO", "OOOOPOmmm"),
    DARKSTEEL(135, 15, 6, "Dark Steel Chest", "darksteelchest.png", 2, Arrays.asList("ingotDarkSteel"),
            TileEntityDarkSteelChest.class, null, "OOOmPmOOO", "OOOOPOmmm"),
    SILVER(72, 9, 3, "Silver Chest", "silverchest.png", 4, Arrays.asList("ingotSilver"), TileEntitySilverChest.class,
            null, "mGmGPGmGm", "mmmmPmmmm"),
    WOOD(0, 0, -1, "", "", -1, Arrays.asList("plankWood"), null, null, null, null);

    final int size;
    private final int rowLength;
    public final String friendlyName;
    private final Integer tier;
    private final String modelTexture;
    private final int textureRow;
    public final Class<? extends TileEntityIronChest> clazz;
    private final String recipeDirect;
    private final String recipeUpgradeOneTier;
    private final String recipeUpgradeTwoTiers;
    private final ArrayList<String> matList;
    private final Item itemFilter;

    IronChestType(int size, int rowLength, int tier, String friendlyName, String modelTexture, int textureRow,
            List<String> mats, Class<? extends TileEntityIronChest> clazz, String recipeDirect,
            String recipeUpgradeOneTier, String recipeUpgradeTwoTiers) {
        this(
                size,
                rowLength,
                tier,
                friendlyName,
                modelTexture,
                textureRow,
                mats,
                clazz,
                (Item) null,
                recipeDirect,
                recipeUpgradeOneTier,
                recipeUpgradeTwoTiers);
    }

    IronChestType(int size, int rowLength, int tier, String friendlyName, String modelTexture, int textureRow,
            List<String> mats, Class<? extends TileEntityIronChest> clazz, Item itemFilter, String recipeDirect,
            String recipeUpgradeOneTier, String recipeUpgradeTwoTiers) {
        this.size = size;
        this.rowLength = rowLength;
        this.tier = tier;
        this.friendlyName = friendlyName;
        this.modelTexture = modelTexture;
        this.textureRow = textureRow;
        this.clazz = clazz;
        this.itemFilter = itemFilter;
        this.recipeDirect = recipeDirect;
        this.recipeUpgradeOneTier = recipeUpgradeOneTier;
        this.recipeUpgradeTwoTiers = recipeUpgradeTwoTiers;
        this.matList = new ArrayList<String>();
        matList.addAll(mats);
    }

    public String getModelTexture() {
        return modelTexture;
    }

    public int getTextureRow() {
        return textureRow;
    }

    public boolean isEnabled() {
        if (this == STEEL) {
            return IronChest.ENABLE_STEEL_CHESTS;
        } else if (this == SILVER) {
            return !IronChest.ENABLE_STEEL_CHESTS;
        } else if (this == DARKSTEEL) {
            return IronChest.ENABLE_DARK_STEEL_CHESTS;
        } else if (this == NETHERITE) {
            return !IronChest.ENABLE_DARK_STEEL_CHESTS;
        }
        return true;
    }

    public static TileEntityIronChest makeEntity(int metadata) {
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

    public static IronChestType[] getAll() {
        return values();
    }

    public static IronChestType[] getAllSortedByTier() {
        IronChestType[] vals = getAll();

        Arrays.sort(vals, new Comparator<IronChestType>() {

            public int compare(IronChestType a, IronChestType b) {
                return a.tier.compareTo(b.tier);
            }
        });

        return vals;
    }

    public static IronChestType[] getAllByTier(int i) {
        HashSet<IronChestType> vals = new HashSet<IronChestType>();

        for (IronChestType typ : values()) {
            if (typ.tier == i) {
                vals.add(typ);
            }
        }

        return vals.toArray(new IronChestType[vals.size()]);
    }

    public static void registerBlocksAndRecipes(BlockIronChest blockResult) {
        IronChestType[] vals = getAllSortedByTier();

        for (IronChestType typ : vals) {
            if (typ.isEnabled()) {
                ItemStack chest = new ItemStack(blockResult, 1, typ.ordinal());
                registerChest(typ, chest);
                if (typ.isValidForCreativeMode()) {
                    GameRegistry.registerCustomItemStack(typ.friendlyName, chest);
                }
            }
        }

        if (!IronChest.isGTNHLoaded) {
            for (IronChestType typ : vals) {
                if (typ.isEnabled()) {
                    generateRecipesForType(typ);
                }
            }
        }
    }

    public static void generateRecipesForType(IronChestType typ) {
        ItemStack chest = getRegistredChest(typ);

        for (String mat : typ.matList) {
            Object mainMaterial = translateOreName(mat);

            // spotless:off
            if (typ.recipeDirect != null) {
                Object[] curTiers = getRegistredChestsByTier(typ.tier);
                if (curTiers.length > 1) {
                    for (Object curTier : curTiers) { /* mainly meant for crystal and obsidian chests */
                        if (curTier != chest) {
                            addRecipe(chest, getRecipeSplitted(typ.recipeDirect),
                                'm', mainMaterial, 'P', curTier,
                                'G', "blockGlass", 'C', "chestWood", 'O', Blocks.obsidian
                            );
                        }
                    }
                }
                else {
                    addRecipe(chest, getRecipeSplitted(typ.recipeDirect),
                        'm', mainMaterial,
                        'G', "blockGlass", 'C', "chestWood", 'O', Blocks.obsidian
                    );
                }
            }
            if (typ.recipeUpgradeOneTier != null) {
                Object[] prevTiers = getRegistredChestsByTier(typ.tier - 1);
                for (Object prevTier : prevTiers) {
                    addRecipe(chest, getRecipeSplitted(typ.recipeUpgradeOneTier),
                        'm', mainMaterial, 'P', prevTier, /* previous tier of chest */
                        'G', "blockGlass", 'C', "chestWood", 'O', Blocks.obsidian
                    );
                }
            }
            if (typ.recipeUpgradeTwoTiers != null) {
                Object[] prevPrevTiers = getRegistredChestsByTier(typ.tier - 2);
                for (Object prevTier : prevPrevTiers) {
                    addRecipe(chest, getRecipeSplitted(typ.recipeUpgradeTwoTiers),
                        'm', mainMaterial, 'P', prevTier, /* previous tier of chest */
                        'G', "blockGlass", 'C', "chestWood", 'O', Blocks.obsidian
                    );
                }
            }
            // spotless:on
        }
    }

    public static String[] getRecipeSplitted(String recipe) {
        return new String[] { recipe.substring(0, 3), recipe.substring(3, 6), recipe.substring(6, 9) };
    }

    public static Object translateOreName(String mat) {
        if (mat.equals("obsidian")) {
            return Blocks.obsidian;
        } else if (mat.equals("dirt")) {
            return Blocks.dirt;
        }
        return mat;
    }

    public static void addRecipe(ItemStack is, Object... parts) {
        ShapedOreRecipe oreRecipe = new ShapedOreRecipe(is, parts);
        GameRegistry.addRecipe(oreRecipe);
    }

    public int getRowCount() {
        return size / rowLength;
    }

    public int getRowLength() {
        return rowLength;
    }

    public boolean isTransparent() {
        return this == CRYSTAL;
    }

    public List<String> getMatList() {
        return matList;
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
        return this == OBSIDIAN || this == NETHERITE;
    }

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    @SideOnly(Side.CLIENT)
    public void makeIcons(IIconRegister par1IconRegister) {
        if (isValidForCreativeMode()) {
            icons = new IIcon[3];
            int i = 0;
            for (String s : sideNames) {
                if (name().equalsIgnoreCase("steel")) {
                    icons[i++] = par1IconRegister.registerIcon(String.format("ironchest:silver_%s", s));
                } else {
                    icons[i++] = par1IconRegister
                            .registerIcon(String.format("ironchest:%s_%s", name().toLowerCase(), s));
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side) {

        return icons[sideMapping[side]];
    }

    private static final HashMap<IronChestType, ItemStack> registredChests = new HashMap<IronChestType, ItemStack>();
    private static final String[] sideNames = { "top", "front", "side" };
    private static final int[] sideMapping = { 0, 0, 2, 1, 2, 2, 2 };

    public Slot makeSlot(IInventory chestInventory, int index, int x, int y) {
        return new ValidatingSlot(chestInventory, index, x, y, this);
    }

    public boolean acceptsStack(ItemStack itemstack) {
        return itemFilter == null || itemstack == null || itemstack.getItem() == itemFilter;
    }

    public void adornItemDrop(ItemStack item) {
        if (this == DIRTCHEST9000) {
            item.setTagInfo("dirtchest", new NBTTagByte((byte) 1));
        }
    }

    private static void registerChest(IronChestType typ, ItemStack stack) {
        registredChests.put(typ, stack);
    }

    public static ItemStack getRegistredChest(IronChestType typ) {
        return registredChests.getOrDefault(typ, null);
    }

    public static Object[] getRegistredChestsByTier(int tier) {
        if (tier < 0) {
            return new Object[0];
        }

        if (tier == 0) {
            return new Object[] { "chestWood" };
        }

        IronChestType[] allTyps = getAllByTier(tier);
        HashSet<Object> result = new HashSet<Object>();

        for (IronChestType typ : allTyps) {
            if (typ.tier == tier) {
                ItemStack itemStack = getRegistredChest(typ);
                if (itemStack != null) {
                    result.add(itemStack);
                }
            }
        }

        return result.toArray(new Object[result.size()]);
    }
}
