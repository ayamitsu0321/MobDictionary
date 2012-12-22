package net.minecraft.src;

import net.minecraft.src.dictionary.*;
import net.minecraft.client.Minecraft;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

public class mod_MobDictionary extends BaseMod
{
	//#009F00
	private static Item dictionary;
	@MLProp(info = "Dictionary's ItemID")
	public static int id = 23356;//dictionary's id
	public static double yaw = 0D;//use on gui, entity rotate
	public static double yaw2 = 0D;//use on gui, entity rotate
	@MLProp(info = "Register GiantZombie ?")
	public static boolean registerGiant = false;
	@MLProp(info = "Key Set ? not add Dictionary & recipe &, set key B")
	public static boolean isKeySet = false;
	
	World prevWorld;
	GuiScreen prevGui;
	
	public String getVersion()
	{
		return "1.4.6_v0.0.5";
	}
	
	public void load()
	{
		ModLoader.setInGameHook(this, true, false);
		ModLoader.setInGUIHook(this, true, true);
		
		if (isKeySet)
		{
			ModLoader.registerKey(this, new KeyBinding("MobDictionary", 48), false);//'B'
		}
		else
		{
			//addItem
			dictionary = (new ItemMobDictionary(id - 256)).setIconCoord(11, 3).setItemName("dictionary").setCreativeTab(CreativeTabs.tabMisc);;
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
		
		//register giant
		if (this.registerGiant)
		{
			MobDictionary.addInfo("Giant");
		}
		
		SaveManager.init();
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
	
	public boolean onTickInGUI(float f, Minecraft mc, GuiScreen guiscreen)
	{
		SaveManager.update(mc, guiscreen);
		return true;
	}
	
	public void modsLoaded()
	{
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