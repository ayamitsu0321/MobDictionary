package ayamitsu.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import ayamitsu.dictionary.network.EnumChannel;
import ayamitsu.util.entity.EntityUtils;

public final class MobDatas
{
	private static Set<String> nameList = new HashSet<String>();

	private static int allMobValue;

	public static void addInfo(Object obj, EntityPlayer player)
	{
		String name = null;

		// addinfo
		if (obj instanceof Class)
		{
			name = EntityUtils.getNameFromClass((Class)obj);
		}
		else if (obj instanceof String)
		{
			name = (String)obj;
		}

		if (name == null)
		{
			return;
		}

		// send chat message
		if (!player.worldObj.isRemote)
		{
			Boolean alreadyRegistered = obj instanceof Class ? addInfo((Class)obj) : obj instanceof String ? addInfo((String)obj) : null;
			String msg = alreadyRegistered == null ? "mobdic.register.error" : alreadyRegistered.booleanValue() ? "mobdic.register.already" : "mobdic.register.accept";

			if (alreadyRegistered != null && !alreadyRegistered.booleanValue())
			{
				FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendChatMsg(StatCollector.translateToLocalFormatted(msg, StatCollector.translateToLocal("entity." + name + ".name"), ((EntityPlayer)player).username));

				// add info other players
				PacketDispatcher.sendPacketToAllPlayers(new Packet250CustomPayload(EnumChannel.ADDINFO_TO_CLIENT.getChannel(), name.getBytes()));
			}
			else
			{
				FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendChatMsg(StatCollector.translateToLocalFormatted(msg, StatCollector.translateToLocal("entity." + name + ".name")));
			}
		}
		else
		{
			PacketDispatcher.sendPacketToServer(new Packet250CustomPayload(EnumChannel.ADDINFO_TO_SERVER.getChannel(), name.getBytes()));
		}
	}

	public static void addInfoClient(String name)
	{
		addInfo(name);
	}

	/**
	 * if return true, already contains
	 */
	private static Boolean addInfo(Class clazz)
	{
		if (EntityUtils.isLivingClass(clazz))
		{
			return !nameList.add(EntityUtils.getNameFromClass(clazz));
		}

		return null;
	}

	/**
	 * if return true, already contains
	 */
	private static Boolean addInfo(String name)
	{
		if (EntityUtils.isLivingName(name))
		{
			return !nameList.add(name);
		}

		return null;
	}

	public static boolean contains(Class clazz)
	{
		return contains(EntityUtils.getNameFromClass(clazz));
	}

	public static boolean contains(String name)
	{
		return nameList.contains(name);
	}

	public static void initAllMobValue()
	{
		Set set = new HashSet();
		Map classToStringMapping = EntityUtils.getClassToStringMapping();

		for (Object obj : classToStringMapping.keySet())
		{
			if (obj instanceof Class && EntityUtils.isLivingClass((Class)obj))
			{
				set.add(obj);
			}
		}

		allMobValue = set.size();
	}

	public static int getAllMobValue()
	{
		return allMobValue;
	}

	public static int getRegisteredValue()
	{
		return nameList.size();
	}

	public static String[] toArray()
	{
		return nameList.toArray(new String[0]);
	}

	public static void load() throws IOException
	{
		nameList.clear();
		File file = getSaveFile();

		if (!file.exists())
		{
			return;
		}

		if (!file.canRead())
		{
			throw new IOException("Can not read dictionary data:" + file.getPath());
		}

		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;

		while ((line = br.readLine()) != null)
		{
			addInfo(line.trim());
		}

		br.close();
	}

	public static void save() throws IOException
	{
		File file = getSaveFile();
		File dir = file.getParentFile();

		if (!dir.exists() && !dir.mkdirs())
		{
			throw new IOException("Can not write dictionary data:" + file.getPath());
		}

		if (!file.exists() && !file.createNewFile())
		{
			throw new IOException("Can not write dictionary data:" + file.getPath());
		}

		if (!file.canWrite())
		{
			throw new IOException("Can not write dictionary data:" + file.getPath());
		}

		PrintWriter pw = new PrintWriter(new FileOutputStream(file));

		for (String name : nameList)
		{
			pw.println(name);
		}

		pw.close();
	}

	public static File getSaveFile()
	{
		return (new File(Loader.instance().getConfigDir(), "dictionary/mobdic.md")).getAbsoluteFile();
	}
}
