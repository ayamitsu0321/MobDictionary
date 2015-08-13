package ayamitsu.mobdictionary.event;

import ayamitsu.mobdictionary.MobDatas;
import ayamitsu.mobdictionary.MobDictionary;
import ayamitsu.mobdictionary.network.PacketHandler;
import ayamitsu.mobdictionary.network.packet.syncdata.MessageSyncData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by ayamitsu0321 on 2015/07/28.
 */
public class PlayerLoggedInHandler {

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        //System.out.println("onPlayerLoggedIn");

        if (event.player instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)event.player;

            if (MobDictionary.proxy.isDedicatedServer()) {// dedicated server
                try {
                    MobDatas.loadOnDedicatedServer(player);// load
                } catch (IOException e) {
                    e.printStackTrace();
                }

                UUID uuid = player.getUniqueID();
                String[] nameList = MobDatas.toArrayOnDedicatedServer(player);

                MessageSyncData msg = new MessageSyncData(nameList);

                //System.out.println("dispatchMessage");


                PacketHandler.DISPATCHER.sendTo(msg, player);// to MessageSyncDataHandler
            } else {// integrated server
                try {
                    MobDatas.load();// load
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String[] nameList = MobDatas.toArray();
                MessageSyncData msg = new MessageSyncData(nameList);
                //System.out.println("dispatchMessage");

                PacketHandler.DISPATCHER.sendTo(msg, player);// to MessageSyncDataHandler
            }
        }
    }
}
