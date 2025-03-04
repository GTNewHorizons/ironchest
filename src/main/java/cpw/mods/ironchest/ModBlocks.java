package cpw.mods.ironchest;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.ironchest.blocks.barrel.BlockIronBarrel;
import cpw.mods.ironchest.blocks.barrel.IronBarrelType;

// credit:
// https://github.com/Roadhog360/Et-Futurum-Requiem/blob/master/src/main/java/ganymedes01/etfuturum/ModBlocks.java
public enum ModBlocks {

    BARREL_COPPER(true, new BlockIronBarrel(IronBarrelType.BARREL_COPPER)),
    BARREL_IRON(true, new BlockIronBarrel(IronBarrelType.BARREL_IRON)),
    BARREL_STEEL(true, new BlockIronBarrel(IronBarrelType.BARREL_STEEL)),
    BARREL_SILVER(true, new BlockIronBarrel(IronBarrelType.BARREL_SILVER)),
    BARREL_GOLD(true, new BlockIronBarrel(IronBarrelType.BARREL_GOLD)),
    BARREL_DIAMOND(true, new BlockIronBarrel(IronBarrelType.BARREL_DIAMOND)),
    BARREL_OBSIDIAN(true, new BlockIronBarrel(IronBarrelType.BARREL_OBSIDIAN)),
    BARREL_NETHERITE(true, new BlockIronBarrel(IronBarrelType.BARREL_NETHERITE)),
    BARREL_DARKSTEEL(true, new BlockIronBarrel(IronBarrelType.BARREL_DARKSTEEL)),;

    public static final ModBlocks[] VALUES = values();

    public static void init() {
        for (ModBlocks block : VALUES) {
            if (block.isEnabled()) {
                if (block.getItemBlock() != null || !block.getHasItemBlock()) {
                    GameRegistry.registerBlock(block.get(), block.getItemBlock(), block.name().toLowerCase());
                    // This part is used if the getItemBlock() is not ItemBlock.class, so we register a custom ItemBlock
                    // class as the ItemBlock
                    // It is also used if the getItemBlock() == null and getHasItemBlock() is false, meaning we WANT to
                    // register it as null, making the block have no inventory item.
                } else {
                    GameRegistry.registerBlock(block.get(), block.name().toLowerCase());
                    // Used if getItemBlock() == null but getHasItemBlock() is true, registering it with a default
                    // inventory item.
                }
            }
        }
    }

    private final boolean isEnabled;
    private final Block theBlock;
    /**
     * null == default ItemBlock
     */
    private final Class<? extends ItemBlock> itemBlock;
    /**
     * Determines if we should register the block with an ItemBlock. Set to false when the constructor that specifies
     * the ItemBlock is specifically set to false.
     */
    private boolean hasItemBlock;

    ModBlocks(Boolean enabled, Block block) {
        this(enabled, block, null);
        hasItemBlock = true;
    }

    ModBlocks(Boolean enabled, Block block, Class<? extends ItemBlock> iblock) {
        isEnabled = enabled;
        theBlock = block;
        itemBlock = iblock;
        hasItemBlock = iblock != null;
    }

    /**
     * If this is false, the block is initialized without an inventory item, or ItemBlock.
     */
    public boolean getHasItemBlock() {
        return hasItemBlock;
    }

    public Block get() {
        return theBlock;
    }

    public Class<? extends ItemBlock> getItemBlock() {
        return itemBlock;
    }

    public Item getItem() {
        return Item.getItemFromBlock(get());
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public ItemStack newItemStack() {
        return newItemStack(1);
    }

    public ItemStack newItemStack(int count) {
        return newItemStack(count, 0);
    }

    public ItemStack newItemStack(int count, int meta) {
        return new ItemStack(this.get(), count, meta);
    }

}
