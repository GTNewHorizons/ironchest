package cpw.mods.ironchest.gui.chest.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import cpw.mods.ironchest.blocks.chest.IronChestType;

public class ValidatingChestSlot extends Slot {

    private final IronChestType type;

    public ValidatingChestSlot(IInventory par1iInventory, int par2, int par3, int par4, IronChestType type) {
        super(par1iInventory, par2, par3, par4);
        this.type = type;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack) {
        return type.acceptsStack(par1ItemStack);
    }
}
