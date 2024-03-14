/*******************************************************************************
 * Copyright (c) 2012 cpw. All rights reserved. This program and the accompanying materials are made available under the
 * terms of the GNU Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors: cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest;

import static cpw.mods.ironchest.IronChestType.*;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.registry.GameRegistry;

public enum ChestChangerType {

    IRONGOLD(IRON, GOLD, "ironGoldUpgrade", "Iron to Gold Chest Upgrade", "mmm", "msm", "mmm"),
    GOLDDIAMOND(GOLD, DIAMOND, "goldDiamondUpgrade", "Gold to Diamond Chest Upgrade", "GGG", "msm", "GGG"),
    COPPERSILVER(COPPER, SILVER, "copperSilverUpgrade", "Copper to Silver Chest Upgrade", "mmm", "msm", "mmm"),
    SILVERGOLD(SILVER, GOLD, "silverGoldUpgrade", "Silver to Gold Chest Upgrade", "mGm", "GsG", "mGm"),
    COPPERSTEEL(COPPER, STEEL, "copperSteelUpgrade", "Copper to Steel Chest Upgrade", "mmm", "msm", "mmm"),
    STEELGOLD(STEEL, GOLD, "steelGoldUpgrade", "Steel to Gold Chest Upgrade", "mGm", "GsG", "mGm"),
    COPPERIRON(COPPER, IRON, "copperIronUpgrade", "Copper to Iron Chest Upgrade", "mGm", "GsG", "mGm"),
    DIAMONDCRYSTAL(DIAMOND, CRYSTAL, "diamondCrystalUpgrade", "Diamond to Crystal Chest Upgrade", "GGG", "GOG", "GGG"),
    WOODIRON(WOOD, IRON, "woodIronUpgrade", "Normal chest to Iron Chest Upgrade", "mmm", "msm", "mmm"),
    WOODCOPPER(WOOD, COPPER, "woodCopperUpgrade", "Normal chest to Copper Chest Upgrade", "mmm", "msm", "mmm"),
    DIAMONDOBSIDIAN(DIAMOND, OBSIDIAN, "diamondObsidianUpgrade", "Diamond to Obsidian Chest Upgrade", "mmm", "mGm",
            "mmm"),
    OBSIDIANNETHERITE(OBSIDIAN, NETHERITE, "obsidianNetheriteUpgrade", "Obsidian to Netherite Chest Upgrade", "OOO",
            "msm", "OOO"),
    DIAMONDDARKSTEEL(DIAMOND, DARKSTEEL, "diamondDarkSteelUpgrade", "Diamond to Dark Steel Chest Upgrade", "OOO", "msm",
            "OOO"),
    CRYSTALDARKSTEEL(CRYSTAL, DARKSTEEL, "crystalDarkSteelUpgrade", "Crystal to Dark Steel Chest Upgrade", "OOO", "msm",
            "OOO"),
    OBSIDIANDARKSTEEL(OBSIDIAN, DARKSTEEL, "obsidianDarkSteelUpgrade", "Obsidian to Dark Steel Chest Upgrade", "OOO",
            "msm", "OOO"),
    IRONSTEEL(IRON, STEEL, "ironSteelUpgrade", "Iron to Steel Chest Upgrade", "mGm", "GsG", "mGm"),
    IRONSILVER(IRON, SILVER, "ironSilverUpgrade", "Iron to Silver Chest Upgrade", "mGm", "GsG", "mGm");

    private final IronChestType source;
    private final IronChestType target;
    public final String itemName;
    public final String descriptiveName;
    private ItemChestChanger item;
    private final String[] recipe;

    private ChestChangerType(IronChestType source, IronChestType target, String itemName, String descriptiveName,
            String... recipe) {
        this.source = source;
        this.target = target;
        this.itemName = itemName;
        this.descriptiveName = descriptiveName;
        this.recipe = recipe;
    }

    public boolean canUpgrade(IronChestType from) {
        return from == this.source;
    }

    public int getTarget() {
        return this.target.ordinal();
    }

    public boolean isAllowed() {
        return target.allowUpgradeFrom(source);
    }

    public ItemChestChanger buildItem(Configuration cfg) {
        item = new ItemChestChanger(this);
        GameRegistry.registerItem(item, itemName);
        return item;
    }

    public void addRecipes() {
        for (String sourceMat : source.getMatList()) {
            for (String targetMat : target.getMatList()) {
                Object targetMaterial = IronChestType.translateOreName(targetMat);
                Object sourceMaterial = IronChestType.translateOreName(sourceMat);
                // spotless:off
                IronChestType.addRecipe(new ItemStack(item), recipe,
                        'm', targetMaterial, 's', sourceMaterial,
                        'G', Blocks.glass, 'O', Blocks.obsidian);
                // spotless:on
            }
        }
    }

    public static void buildItems(Configuration cfg) {
        for (ChestChangerType type : values()) {
            if (type.isAllowed() && (type.source.isEnabled() && type.target.isEnabled())) {
                type.buildItem(cfg);
            }
        }
    }

    public static void generateRecipes() {
        if (IronChest.isGTNHLoaded) {
            return;
        }
        for (ChestChangerType item : values()) {
            if (item.isAllowed()) {
                item.addRecipes();
            }
        }
    }
}
