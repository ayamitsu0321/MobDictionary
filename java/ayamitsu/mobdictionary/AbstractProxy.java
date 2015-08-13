package ayamitsu.mobdictionary;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;

import java.io.File;

/**
 * Created by ayamitsu0321 on 2015/04/12.
 */
public abstract class AbstractProxy {

    public abstract void preInit();

    public abstract void init();

    public abstract void postInit();

    public void displayScreen(EntityPlayer player, int gui) {
    }

    public boolean isDedicatedServer() {
        return false;
    }

    public EntityPlayer getPlayerInstance() {
        return null;
    }

    public File getSaveDirectory() {
        return null;
    }

    public MovingObjectPosition getMouseOver(EntityLivingBase viewingEntity, double reach) {
        return null;
    }

    public void printChatMessageClient(IChatComponent chatComponent) {}

}

