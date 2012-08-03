package net.minecraft.src;

import net.minecraft.src.dictionary.*;
import net.minecraft.client.Minecraft;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

public class mod_MobDictionary extends BaseMod
{
	private static Item dictionary;
	@MLProp(info = "Dictionary's ItemID")
	public static int id = 23356;//dictionary's id
	public static double yaw = 0D;//use on gui, entity rotate
	public static double yaw2 = 0D;//use on gui, entity rotate
	@MLProp(info = "Register GiantZombie ?")
	public static boolean registerGiant = false;
	@MLProp(info = "Key Set ? not add Dictionary & recipe &, set key B")
	public static boolean isKeySet = false;
	
	public String getVersion()
	{
		return "1.2.5_v0.0.1";
	}
	
	public void load()
	{
		ModLoader.setInGameHook(this, true, false);
		
		if (isKeySet)
		{
			ModLoader.registerKey(this, new KeyBinding("MobDictionary", 48), false);//'B'
		}
		else
		{
			//addItem
			dictionary = (new ItemMobDictionary(id - 256)).setIconCoord(11, 3).setItemName("dictionary");
			ModLoader.addName(dictionary, "Mob Dictionary");
			//add recipe
			ModLoader.addShapelessRecipe(new ItemStack(dictionary, 1), new Object[]
				{
					new ItemStack(Item.book,1),
					new ItemStack(Block.sapling, 1, 0),
					new ItemStack(Block.sapling, 1, 1),
					new ItemStack(Block.sapling, 1, 2),
					new ItemStack(Block.sapling, 1, 3)
				});
		}
		
		//read file
		String fileName = ModLoader.getMinecraftInstance().getMinecraftDir() + "/config/dictionary/mobdic.md";
		File file = new File(fileName);
		
		//read from file
		if (file != null)
    	for (int i = 0; i < MobDictionary.readFile().length; i++)
		{
			MobDictionary.addInfo(MobDictionary.readFile()[i]);
		}
		
		//register giant
		if (registerGiant)
		{
			MobDictionary.addInfo("Giant");
		}
	}
	
	//on dictionary's gui, rotate entity
	public boolean onTickInGame(float f, Minecraft mc)
    {
    	yaw2 = yaw;
    	
    	for (yaw += 0.2F * 10F; yaw > 360D;)
    	{
        	yaw -= 360D;
        	yaw2 -= 360D;
    	}
    	
        return true;
    }
	
	public void modsLoaded()
	{
		//for save file
		PlayerAPI.register("RegisterDictionary", RegisterDictionary.class);
		//get kind of entity's value
		MobDictionary.setEntityValueOfTypes();
	}
	
	public void keyboardEvent(KeyBinding var1)
    {
    	if (isKeySet)
    	{
	        if (var1.keyDescription == "MobDictionary")
	        {
	        	ModLoader.openGUI(ModLoader.getMinecraftInstance().thePlayer, new GuiMobDictionary(ModLoader.getMinecraftInstance().thePlayer.inventory));
	        }
    	}
    }
}