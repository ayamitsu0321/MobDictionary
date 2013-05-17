package ayamitsu.dictionary.network;

import ayamitsu.dictionary.MobDatas;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if (packet.channel.equals(EnumChannel.ADDINFO_TO_CLIENT.getChannel()))
		{
			String name = new String(packet.data);
			MobDatas.addInfoClient(name);
		}
		else if (packet.channel.equals(EnumChannel.ADDINFO_TO_SERVER.getChannel()))
		{
			String name = new String(packet.data);
			MobDatas.addInfo(name, (EntityPlayer)player);
		}
	}

}
