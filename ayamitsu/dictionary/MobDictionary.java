package ayamitsu.dictionary;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import ayamitsu.dictionary.item.ItemMobDictionary;
import ayamitsu.dictionary.network.EnumChannel;
import ayamitsu.util.io.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(
	modid = "ayamitsu.dictionary",
	name = "MobDictionary",
	version = "2.0.0"
)
@NetworkMod(
	clientSideRequired = true,
	serverSideRequired = true,
	connectionHandler = ayamitsu.dictionary.network.ConnectionHandler.class,
	packetHandler = ayamitsu.dictionary.network.PacketHandler.class,
	channels = { "mobdic.addinfo.0", "mobdic.addinfo.1" }
)
public class MobDictionary
{
	@Mod.Instance("ayamitsu.dictionary")
	public static MobDictionary instance;

	public static Item dictionaryItem;

	public static int dictionaryItemId;

	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration conf = new Configuration(event.getSuggestedConfigurationFile());

		try
		{
			conf.load();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		this.dictionaryItemId = conf.getProperty("dictionaryItemId", 23356).getInt();

		try
		{
			conf.save();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Mod.Init
	public void init(FMLInitializationEvent event)
	{
		this.dictionaryItem = new ItemMobDictionary(this.dictionaryItemId).setUnlocalizedName("dictionary").setCreativeTab(CreativeTabs.tabMisc);
		LanguageRegistry.instance().addNameForObject(this.dictionaryItem, "en_US", "Mob Dictionary");
		LanguageRegistry.instance().addNameForObject(this.dictionaryItem, "ja_JP", "モブ図鑑");

		GameRegistry.addShapelessRecipe(new ItemStack(this.dictionaryItem, 1),
				new Object[] {
				new ItemStack(Item.book,1),
				new ItemStack(Block.sapling, 1, 0),
				new ItemStack(Block.sapling, 1, 1),
				new ItemStack(Block.sapling, 1, 2),
				new ItemStack(Block.sapling, 1, 3)
			}
		);

		LanguageRegistry.instance().addStringLocalization("mobdic.register.accept", "en_US", "Registered %1$s to MobDictionary by %2$s !!");
		LanguageRegistry.instance().addStringLocalization("mobdic.register.already", "en_US", "Already registered %s !!");
		LanguageRegistry.instance().addStringLocalization("mobdic.register.error", "en_US", "Couldn't register %s, it's not mob !!");
		LanguageRegistry.instance().addStringLocalization("mobdic.register.accept", "ja_JP", "%2$sによってモブ図鑑に%1$sが登録されました!!");
		LanguageRegistry.instance().addStringLocalization("mobdic.register.already", "ja_JP", "モブ図鑑に%sは既に登録されています。");
		LanguageRegistry.instance().addStringLocalization("mobdic.register.error", "ja_JP", "%sは登録できませんでした。Mobじゃないでしょう。");
		LanguageRegistry.instance().addStringLocalization("mobdic.register.completed", "en_US", "Completed");
		LanguageRegistry.instance().addStringLocalization("mobdic.register.completed", "ja_JP", "登録数");
	}

	@Mod.PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		MobDatas.initAllMobValue();
	}

	@Mod.ServerStarted
	public void serverStarted(FMLServerStartedEvent event)
	{
		try
		{
			MobDatas.load();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Mod.ServerStopped
	public void serverStopped(FMLServerStoppedEvent event)
	{
		try
		{
			MobDatas.save();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 *  client only
	 *  call on PlayerControllerMP func_78768_b(EntityPlayer, Entity)
	 *  insert this method from coremods
	 */
	public static void interactWithEntity(EntityPlayer player, Entity entity)
	{
		if (entity instanceof EntityLiving)
		{
			ItemStack itemStack = player.inventory.getCurrentItem();

			if (!player.isSneaking() && itemStack != null && itemStack.itemID == dictionaryItem.itemID)
			{
				MobDatas.addInfo(EntityList.getEntityString(entity), player);
			}
		}
	}
}
