package ayamitsu.mobdictionary.network.packet.register;

import ayamitsu.mobdictionary.MobDatas;
import ayamitsu.mobdictionary.MobDictionary;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

/**
 * Created by ayamitsu0321 on 2015/08/07.
 */
public class MessageRegisterHandler implements IMessageHandler<MessageRegister, IMessage> {

    @Override
    public IMessage onMessage(MessageRegister message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        Entity entity = player.worldObj.getEntityByID(message.getEntityId());

        if (player.getUniqueID().toString().equals(message.getUUIDString()) && entity instanceof EntityLivingBase) {
            EntityLivingBase target = (EntityLivingBase)entity;
            //System.out.println(target.getName());

            if (MobDictionary.proxy.isDedicatedServer()) {
                MobDatas.addInfoOnDedicatedServer(target.getClass(), player);

                try {
                    MobDatas.saveOnDedicatedServer(player);// dedicated server
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                MobDatas.addInfo(target.getClass());

                try {
                    MobDatas.save();// integrated server
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

}
