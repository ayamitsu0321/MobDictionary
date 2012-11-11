package net.minecraft.src.dictionary;

import net.minecraft.src.*;
import net.minecraft.client.Minecraft;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;

public final class SaveManager
{
	
	private static boolean hasInit = false;
	static GuiScreen prevGui;
	static World prevWorld;
	
	public static void init()
	{
		if (hasInit)
		{
			return;
		}
		
		hasInit = true;
		readFile();
	}
	
	public static void reset()
	{
		hasInit = false;
		init();
	}
	
	public static void update(Minecraft mc, GuiScreen guiscreen)
	{
		World worlc = mc.theWorld;
		
		if (guiscreen == prevGui)
		{
			return;
		}
		
		prevGui = guiscreen;
		
		if (guiscreen instanceof GuiIngameMenu)
		{
			writeFile();
		}
	}
	
	private static void writeFile()
	{
		String path = getPath();
		File dir = new File(getPath());
		
		if (!dir.exists())
		{
			dir.mkdirs();
		}
		
		String[] names = MobDictionary.getNames_file();
		File file = new File(getFile());
		
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException ioexception)
			{
				ioexception.printStackTrace();
			}
		}
		
		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file.getName())));
			
			for (int i = 0; i < names.length; i++)
			{
				pw.println(names[i]);
			}
			
			pw.close();
		}
		catch (IOException ioexception)
		{
			ioexception.printStackTrace();
		}
	}
	
	private static void readFile()
	{
		String path = getPath();
		File dir = new File(path);
		
		if (!dir.exists())
		{
			dir.mkdirs();
			return;
		}
		
		File file = new File(getFile());
		List list = new ArrayList();
		
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getName())));
			String str;
			
			while ((str = br.readLine()) != null)
			{
				list.add(str);
			}
			
			br.close();
		}
		catch (IOException ioexception)
		{
			ioexception.printStackTrace();
		}
		
		if (!list.isEmpty())
		{
			String[] names = (String[])list.toArray(new String[0]);
			
			for (int i = 0; i < names.length; i++)
			{
				MobDictionary.addInfo(names[i]);
			}
		}
	}
	
	private static String getPath()
	{
		return ModLoader.getMinecraftInstance().getMinecraftDir() + "/config/dictionary";
	}
	
	private static String getFile()
	{
		return getPath() + "/mobdic.md";
	}
}