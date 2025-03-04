/*******************************************************************************
 * Copyright (c) 2012 cpw. All rights reserved. This program and the accompanying materials are made available under the
 * terms of the GNU Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors: cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest.blocks.barrel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.tileentity.barrel.TileEntityIronBarrel;
import ganymedes01.etfuturum.blocks.BlockBarrel;

public class BlockIronBarrel extends BlockBarrel {

    private IIcon innerTopIcon;
    private IIcon bottomIcon;
    private IIcon topIcon;
    private final IronBarrelType ironBarrelType;

    public BlockIronBarrel(IronBarrelType type) {
        super();
        this.ironBarrelType = type;
        this.blockMaterial = Material.iron;
        setStepSound(Block.soundTypeMetal);
        setHarvestLevel("pickaxe", 1);
        setBlockTextureName(type.name().toLowerCase());
        setBlockName("ironchest:" + type.name());
        setHardness(3.0F);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return ironBarrelType.makeEntity();
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
            float subY, float subZ) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity te = world.getTileEntity(x, y, z);
            if (!(te instanceof TileEntityIronBarrel ironBarrel)) {
                return true;
            }
            player.openGui(IronChest.instance, ironBarrel.getType().ordinal(), world, x, y, z);
            return true;
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister i) {
        this.blockIcon = i.registerIcon("ironchest:" + this.getTextureName() + "_side");
        this.topIcon = i.registerIcon("ironchest:" + this.getTextureName() + "_top");
        this.innerTopIcon = i.registerIcon("ironchest:" + this.getTextureName() + "_top_open");
        this.bottomIcon = i.registerIcon("ironchest:" + this.getTextureName() + "_bottom");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        int k = BlockPistonBase.getPistonOrientation(meta);
        return k > 5 ? (meta > 7 ? this.innerTopIcon : this.topIcon)
                : (side == k ? (meta > 7 ? this.innerTopIcon : this.topIcon)
                        : (side == Facing.oppositeSide[k] ? this.bottomIcon : this.blockIcon));
    }
}
