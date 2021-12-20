/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *     cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "IronChest", name = "Iron Chests", version = IronChest.VERSION, dependencies = "required-after:Forge@[10.10,);required-after:FML@[7.2,)")
public class IronChest {
    public static BlockIronChest ironChestBlock;
    @SidedProxy(clientSide = "cpw.mods.ironchest.client.ClientProxy", serverSide = "cpw.mods.ironchest.CommonProxy")
    public static CommonProxy proxy;
    @Instance("IronChest")
    public static IronChest instance;
    public static boolean CACHE_RENDER = true;
    public static boolean OCELOTS_SITONCHESTS = true;
    public static final String VERSION = "GRADLETOKEN_VERSION";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
        try
        {
            cfg.load();
            ChestChangerType.buildItems(cfg);
            CACHE_RENDER = cfg.get(Configuration.CATEGORY_GENERAL, "cacheRenderingInformation", true).getBoolean(true);
            OCELOTS_SITONCHESTS = cfg.get(Configuration.CATEGORY_GENERAL, "ocelotsSitOnChests", true).getBoolean(true);
        }
        catch (Exception e)
        {
            FMLLog.log(Level.ERROR, e, "IronChest has a problem loading its configuration");
        }
        finally
        {
            if (cfg.hasChanged())
                cfg.save();
        }
        ironChestBlock = new BlockIronChest();
        GameRegistry.registerBlock(ironChestBlock, ItemIronChest.class, "BlockIronChest");
        PacketHandler.INSTANCE.ordinal();
    }

    @EventHandler
    public void load(FMLInitializationEvent evt)
    {
        for (IronChestType typ : IronChestType.values())
        {
            GameRegistry.registerTileEntityWithAlternatives(typ.clazz, "IronChest."+typ.name(), typ.name());
            proxy.registerTileEntitySpecialRenderer(typ);
        }
        OreDictionary.registerOre("chestWood", Blocks.chest);
        IronChestType.registerBlocksAndRecipes(ironChestBlock);
        ChestChangerType.generateRecipes();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        proxy.registerRenderInformation();
//        if (OCELOTS_SITONCHESTS)
//        {
//            MinecraftForge.EVENT_BUS.register(new OcelotsSitOnChestsHandler());
//        }
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void modsLoaded(FMLPostInitializationEvent evt)
    {
    }

    @Mod.EventHandler
    public void missingMapping(FMLMissingMappingsEvent event) {
        for (FMLMissingMappingsEvent.MissingMapping mapping : event.getAll()) {
            if (mapping.type == GameRegistry.Type.BLOCK) {
                System.out.println("block got a " + mapping.name);
                switch (mapping.name) {
                    case "IronChest:copperSilverUpgrade":
                        mapping.remap(Block.getBlockFromName(ChestChangerType.COPPERSTEEL.itemName));
                        break;
                    case "IronChest:silverGoldUpgrade":
                        mapping.remap(Block.getBlockFromName(ChestChangerType.STEELGOLD.itemName));
                        break;
                    default:
                        System.out.println("No block match for " + mapping.name);
                    }
            } else if (mapping.type == GameRegistry.Type.ITEM) {
                System.out.println("got a " + mapping.name);
                switch (mapping.name) {
                    case "IronChest:copperSilverUpgrade":
                        mapping.remap(Item.getItemFromBlock(Block.getBlockFromName(ChestChangerType.COPPERSTEEL.itemName)));
                        break;
                    case "IronChest:silverGoldUpgrade":
                        mapping.remap(Item.getItemFromBlock(Block.getBlockFromName(ChestChangerType.STEELGOLD.itemName)));
                        break;
                    default:
                        System.out.println("No item match for " + mapping.name);
                }
            } else {
                System.out.println("Hmmmm " + mapping.name);
            }
        }
    }
}
