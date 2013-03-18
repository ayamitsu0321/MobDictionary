package ayamitsu.dictionary;

import java.util.List;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;

public class ItemMobDictionary extends Item
{
	public ItemMobDictionary(int id)
	{
		super(id);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player)
	{
		if (is != null)
		{
			//opengui
			ModLoader.openGUI(player , new GuiMobDictionary());
		}

		return is;
	}

	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List list, boolean par4)
	{
		StringBuilder sb = new StringBuilder("Completed:").append(MobDictionary.registeredValue()).append('/').append(MobDictionary.getAllMobValue());
		list.add(sb.toString());
	}

	@Override
	public int getDamageVsEntity(Entity entity)
	{
		if (!entity.worldObj.isRemote)
		{
			if (entity instanceof EntityLiving)
			{
				EntityPlayer player = (EntityPlayer)entity.worldObj.playerEntities.get(0);
				String name = EntityList.getEntityString(entity);
				//register Mob to Dictionary
				String msg = (!MobDictionary.addInfo(name) ? "register " : "already registered ") + name;
				player.addChatMessage(msg);
			}
		}

		return 0;
	}

	@Override
	public int getColorFromItemStack(ItemStack is, int layer)
	{
		return 0x965036;// 檜皮色 hiwada
	}

	@Override
	public void func_94581_a(IconRegister par1IconRegister)
	{
		this.iconIndex = par1IconRegister.func_94245_a("book");
	}
}
