package net.minecraft.src.dictionary;

import net.minecraft.src.*;
import java.util.List;

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
    		ModLoader.openGUI(player , new GuiMobDictionary(player.inventory));
    	}
    	
        return is;
    }
	
	@Override
	public int getDamageVsEntity(Entity entity)
    {
    	if (!entity.worldObj.isRemote)
    	{
	    	if (entity instanceof EntityLiving)
	    	{
	    		//register Entity to Dictionary
		    	EntityPlayer player = ModLoader.getMinecraftInstance().thePlayer;
		    	String name = EntityList.getEntityString(entity);
		    	String msg = MobDictionary.addInfo(entity.getClass()) ? "register " + name : "already registered " + name;
		    	player.addChatMessage(msg);
	    	}
    	}
    	
        return 0;
    }
	
	//display completed value
	@Override
	public void addInformation(ItemStack itemstack, List list)
    {
    	String s1 = String.valueOf(MobDictionary.getDicRegisteredValue());
    	String s2 = String.valueOf(MobDictionary.getEntityValueOfTypes());
    	String inf = (new StringBuilder()).append(s1).append("/").append(s2).toString();
    	list.add("Completed : " + inf);
    	//list.add(inf);
    }
	
	@Override
	public int getColorFromDamage(int par1, int par2)
    {
    	//ûwîÁêF hiwada
        return 0x965036;
    }
}