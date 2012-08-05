package net.minecraft.src.dictionary;

import net.minecraft.src.*;

public class RegisterDictionary extends PlayerBase
{
	public RegisterDictionary(PlayerAPI api)
	{
		super(api);
	}
	
	@Override
	public void onKillEntity(EntityLiving entityliving)
	{
		super.onKillEntity(entityliving);
	}
	
	@Override
	public void attackTargetEntityWithCurrentItem(Entity entity)
	{
		if (mod_MobDictionary.isKeySet)
		{
			if (entity instanceof EntityLiving)
			{
				//register entity
				if (MobDictionary.addInfo(entity.getClass()))
				{
					String name = EntityList.getEntityString(entity);
					if (!player.worldObj.isRemote)//not smp
					player.addChatMessage("register " + name);
				}
			}
		}
		
		super.attackTargetEntityWithCurrentItem(entity);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		//write to file
		//System.out.println("Write");
		MobDictionary.writeFile();
		super.writeEntityToNBT(nbt);
	}
}