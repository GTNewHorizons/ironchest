package cpw.mods.ironchest;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.world.World;

public class IronChestAIOcelotSit extends EntityAIOcelotSit {

    public IronChestAIOcelotSit(EntityOcelot ocelot, double navigationSpeed) {
        super(ocelot, navigationSpeed);
    }

    @Override
    public boolean func_151486_a(World world, int x, int y, int z) {
        Block targetBlock = world.getBlock(x, y, z);
        if (targetBlock == IronChest.ironChestBlock) {
            TileEntityIronChest chest = (TileEntityIronChest) world.getTileEntity(x, y, z);
            if (chest.getNumUsingPlayers() == 0) {
                return true;
            }
        }
        return super.func_151486_a(world, x, y, z);
    }
}
