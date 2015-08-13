package ayamitsu.mobdictionary.network.packet.syncdata;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by ayamitsu0321 on 2015/07/28.
 */
public class MessageSyncData implements IMessage {

    String[] nameList = null;

    public MessageSyncData() {
    }

    public MessageSyncData(String[] names) {
        this.nameList = names;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int length = buf.readInt();
        this.nameList = new String[length];

        for (int i = 0; i < length; i++) {
            this.nameList[i] = ByteBufUtils.readUTF8String(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(nameList.length);

        for (String name : nameList) {
            ByteBufUtils.writeUTF8String(buf, name);
        }
    }

    public String[] getNameList() {
        return this.nameList;
    }
}
