package cpw.mods.ironchest.tileentity.barrel;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import cpw.mods.ironchest.blocks.barrel.IronBarrelType;
import ganymedes01.etfuturum.tileentities.TileEntityBarrel;

public class TileEntityIronBarrel extends TileEntityBarrel {

    private final IronBarrelType type;
    private ItemStack[] chestContents;

    public TileEntityIronBarrel() {
        this(IronBarrelType.BARREL_IRON);
    }

    protected TileEntityIronBarrel(IronBarrelType type) {
        this.type = type;
        chestContents = new ItemStack[type.size];
    }

    @Override
    public int getSizeInventory() {
        return type.size;
    }

    public IronBarrelType getType() {
        return type;
    }

    @Override
    public String getInventoryName() {
        return type.friendlyName;
    }

    public void readFromNBT(NBTTagCompound compound) {
        this.xCoord = compound.getInteger("x");
        this.yCoord = compound.getInteger("y");
        this.zCoord = compound.getInteger("z");

        NBTTagList nbttaglist = compound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        chestContents = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < chestContents.length) {
                chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    public void writeToNBT(NBTTagCompound compound) {
        String s = (String) classToNameMap.get(this.getClass());

        if (s == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        } else {
            compound.setString("id", s);
            compound.setInteger("x", this.xCoord);
            compound.setInteger("y", this.yCoord);
            compound.setInteger("z", this.zCoord);
        }
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < chestContents.length; i++) {
            if (chestContents[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                chestContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        compound.setTag("Items", nbttaglist);
    }

    public ItemStack getStackInSlot(int slotIn) {
        return this.chestContents[slotIn];
    }

    public ItemStack decrStackSize(int index, int count) {
        if (this.chestContents[index] != null) {
            if (this.chestContents[index].stackSize <= count) {
                ItemStack itemstack = this.chestContents[index];
                this.chestContents[index] = null;
                this.markDirty();
                return itemstack;
            } else {
                ItemStack itemstack = this.chestContents[index].splitStack(count);
                if (this.chestContents[index].stackSize == 0) {
                    this.chestContents[index] = null;
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index) {
        if (this.chestContents[index] != null) {
            ItemStack itemstack = this.chestContents[index];
            this.chestContents[index] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        this.chestContents[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }
}
