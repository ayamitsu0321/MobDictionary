package ayamitsu.mobdictionary.network.packet.syncdata;

import ayamitsu.mobdictionary.MobDatas;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by ayamitsu0321 on 2015/07/28.
 * server to client
 * synchronize
 * client only
 */
public class MessageSyncDataHandler implements IMessageHandler<MessageSyncData, IMessage> {

    @Override
    public IMessage onMessage(MessageSyncData message, MessageContext ctx) {
        MobDatas.clearNameList();
        //System.out.println("NameList cleared");

        for (String name : message.getNameList()) {
            MobDatas.addInfo(name);
        }

        //System.out.println(message.getNameList());
        //System.out.println("Name added");

        return null;
    }

}
