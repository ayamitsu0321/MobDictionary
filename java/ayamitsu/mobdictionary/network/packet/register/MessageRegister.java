package ayamitsu.mobdictionary.network.packet.register;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

/**
 * Created by ayamitsu0321 on 2015/08/06.
 */
public class MessageRegister implements IMessage {

    private String uuidString;
    private int entityId;

    public MessageRegister() {
    }

    public MessageRegister(EntityPlayer player, EntityLivingBase living) {
        this.uuidString = player.getUniqueID().toString();
        this.entityId = living.getEntityId();
    }

    public String getUUIDString() {
        return this.uuidString;
    }

    public int getEntityId() {
        return this.entityId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.uuidString = ByteBufUtils.readUTF8String(buf);
        this.entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.uuidString);
        buf.writeInt(this.entityId);
    }
}
