package ayamitsu.mobdictionary.network;

import ayamitsu.mobdictionary.MobDictionary;
import ayamitsu.mobdictionary.network.packet.register.MessageRegister;
import ayamitsu.mobdictionary.network.packet.register.MessageRegisterHandler;
import ayamitsu.mobdictionary.network.packet.syncdata.MessageSyncData;
import ayamitsu.mobdictionary.network.packet.syncdata.MessageSyncDataHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by ayamitsu0321 on 2015/07/28.
 */
public class PacketHandler {

    public static PacketHandler INSTANCE = new PacketHandler();

    public static final SimpleNetworkWrapper DISPATCHER = NetworkRegistry.INSTANCE.newSimpleChannel(MobDictionary.CHANNEL);

    /*
        MessageのID
     */
    public static final int SYNC_DATA_ID = 0;
    public static final int REGISTER = 1;


    public void init() {
        DISPATCHER.registerMessage(MessageSyncDataHandler.class, MessageSyncData.class, SYNC_DATA_ID, Side.CLIENT);
        DISPATCHER.registerMessage(MessageRegisterHandler.class, MessageRegister.class, REGISTER, Side.SERVER);
    }

}
