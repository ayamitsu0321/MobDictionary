package ayamitsu.mobdictionary.server;

import ayamitsu.mobdictionary.AbstractProxy;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.io.File;

/**
 * Created by ayamitsu0321 on 2015/04/12.
 */
public class ServerProxy extends AbstractProxy {
    @Override
    public void preInit() {
    }

    @Override
    public void init() {
    }

    @Override
    public void postInit() {
    }

    @Override
    public boolean isDedicatedServer() {
        return FMLServerHandler.instance().getServer().isDedicatedServer();
    }

    @Override
    public File getSaveDirectory() {
        return this.isDedicatedServer() ? (new File(FMLServerHandler.instance().getSavesDirectory(), "world/playerdata/mobdictionary")).getAbsoluteFile() : (new File(Loader.instance().getConfigDir(), "/dictionary")).getAbsoluteFile();
    }


}
