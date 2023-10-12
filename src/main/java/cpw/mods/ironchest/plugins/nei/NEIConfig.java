package cpw.mods.ironchest.plugins.nei;

import codechicken.nei.DefaultBookmarkContainerHandler;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.client.GUIChest;

public class NEIConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        API.registerBookmarkContainerHandler(GUIChest.class, new DefaultBookmarkContainerHandler());
    }

    @Override
    public String getName() {
        return "Iron Chests";
    }

    @Override
    public String getVersion() {
        return IronChest.VERSION;
    }
}
