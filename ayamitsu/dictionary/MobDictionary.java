package ayamitsu.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.StatCollector;

public final class MobDictionary
{
	private static final Set<String> nameList = new HashSet<String>();
	private static int allMobValue;

	private MobDictionary() {}

	/**
	 * if return true, already contains
	 */
	public static boolean addInfo(Class clazz)
	{
		if (EntityUtils.isLivingClass(clazz))
		{
			return !nameList.add(EntityUtils.getNameFromClass(clazz));
		}

		return false;
	}

	/**
	 * if return true, already contains
	 */
	public static boolean addInfo(String name)
	{
		if (EntityUtils.isLivingName(name))
		{
			return !nameList.add(name);
		}

		return false;
	}

	public static int registeredValue()
	{
		return nameList.size();
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

	public static String[] toArray()
	{
		return nameList.toArray(new String[0]);
	}

	public static String[] getDisplayNames()
	{
		String[] names = nameList.toArray(new String[0]);
		Arrays.sort(names);

		for (int i = 0; i < names.length; i++)
		{
			names[i] = StatCollector.translateToLocal("entity." + names[i] + ".name");
		}

		return names;
	}

	public static void load() throws IOException
	{
		nameList.clear();
		File file = getSaveFile();

		if (!file.exists() || !file.canRead())
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
		return (new File(Minecraft.getMinecraftDir(), "config/dictionary/mobdic.md")).getAbsoluteFile();
	}
}
