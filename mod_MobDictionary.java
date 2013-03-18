package net.minecraft.src;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import ayamitsu.dictionary.GuiMobDictionary;
import ayamitsu.dictionary.ItemMobDictionary;
import ayamitsu.dictionary.MobDictionary;

public class mod_MobDictionary extends BaseMod {

	public static Item dictionary;

	@MLProp(info = "dictionary item id")
	public static int dictionaryId = 23356;

	@MLProp(info = "register Giant")
	public static boolean registerGiant = false;

	@MLProp(info = "if true, pressed B key open gui")
	public static boolean isKeySet = false;

	private GuiScreen prevGui;

	@Override
	public String getVersion()
	{
		return "1.5.0-v1.0.0";
	}

	@Override
	public void load()
	{
		try
		{
			MobDictionary.load();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		ModLoader.setInGUIHook(this, true, true);
		this.dictionary = new ItemMobDictionary(this.dictionaryId).setUnlocalizedName("dictionary").setCreativeTab(CreativeTabs.tabMisc);
		ModLoader.addName(this.dictionary, "Mob Dictionary");
		//add recipe
		ModLoader.addShapelessRecipe(new ItemStack(this.dictionary, 1),
				new Object[] {
				new ItemStack(Item.book,1),
				new ItemStack(Block.sapling, 1, 0),
				new ItemStack(Block.sapling, 1, 1),
				new ItemStack(Block.sapling, 1, 2),
				new ItemStack(Block.sapling, 1, 3)
			}
		);

		if (this.registerGiant)
		{
			MobDictionary.addInfo("Giant");
		}

		if (this.isKeySet)
		{
			ModLoader.registerKey(this, new KeyBinding("Open Dictionary", Keyboard.KEY_B), false);// B
		}
	}

	public void keyboardEvent(KeyBinding key)
	{
		if (ModLoader.getMinecraftInstance().theWorld != null && ModLoader.getMinecraftInstance().thePlayer != null)
		{
			ModLoader.getMinecraftInstance().displayGuiScreen(new GuiMobDictionary());
		}
	}

	@Override
	public boolean onTickInGUI(float f, Minecraft mc, GuiScreen guiScreen)
	{
		if (guiScreen == this.prevGui)
		{
			return true;
		}

		this.prevGui = guiScreen;

		if (this.prevGui instanceof GuiIngameMenu)
		{
			try
			{
				MobDictionary.save();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return true;
	}

	@Override
	public void modsLoaded()
	{
		MobDictionary.initAllMobValue();
	}
}
