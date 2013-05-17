package ayamitsu.dictionary.item;

import java.util.List;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import ayamitsu.dictionary.MobDatas;
import ayamitsu.dictionary.gui.GuiMobDictionary;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemMobDictionary extends Item
{

	public ItemMobDictionary(int par1)
	{
		super(par1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player)
	{
		// open gui
		if (world.isRemote && player.isSneaking())
		{
			this.displayDictionary(player);// if server, remove this method by coremods
		}

		return is;
	}

	private void displayDictionary(EntityPlayer player)
	{
		FMLClientHandler.instance().displayGuiScreen(player, new GuiMobDictionary());
	}

	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List list, boolean par4)
	{
		StringBuilder sb = new StringBuilder(StatCollector.translateToLocal("mobdic.register.completed")).append(": ").append(MobDatas.getRegisteredValue()).append('/').append(MobDatas.getAllMobValue());
		list.add(sb.toString());
	}

	@Override
	public int getDamageVsEntity(Entity entity)
	{
		return 1;//0;
	}

	@Override
	public int getColorFromItemStack(ItemStack is, int layer)
	{
		return 0x965036;// 檜皮色 hiwada
	}

	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("book");
	}

}
