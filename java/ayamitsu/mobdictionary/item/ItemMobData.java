package ayamitsu.mobdictionary.item;

import ayamitsu.mobdictionary.MobDatas;
import ayamitsu.mobdictionary.MobDictionary;
import ayamitsu.mobdictionary.util.EntityUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by ayamitsu0321 on 2015/08/05.
 */
public class ItemMobData extends Item {

    public ItemMobData() {
        super();
        this.setMaxStackSize(1);
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        NBTTagCompound nbt = itemStackIn.getTagCompound();

        if (nbt != null) {
            String name = ItemMobData.getEntityNameFromNBT(nbt);

            if (MobDictionary.proxy.isDedicatedServer()) {// dedicated server
                if (playerIn instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP)playerIn;

                    if (!MobDatas.containsOnDedicatedServer(name, playerMP)) {// 未登録
                        MobDatas.addInfoOnDedicatedServer(name, playerMP);

                        try {
                            MobDatas.saveOnDedicatedServer(playerMP);// save
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        itemStackIn.stackSize--;
                    }
                }
            } else {// client and integrated server
                if (!MobDatas.contains(name)) {// 未登録
                    MobDatas.addInfo(name);

                    if (worldIn.isRemote) {// client
                        // show chat
                        ItemMobDictionary.showChatMessage(ItemMobDictionary.EnumChatMessage.ACCEPT, StatCollector.translateToLocal("entity." + name + ".name"));
                    }

                    try {
                        MobDatas.save();// save
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    itemStackIn.stackSize--;
                } else {
                    if (worldIn.isRemote) {
                        // show chat
                        ItemMobDictionary.showChatMessage(ItemMobDictionary.EnumChatMessage.ALREADY, StatCollector.translateToLocal("entity." + name + ".name"));
                    }
                }
            }
        }

        return itemStackIn;
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
        NBTTagCompound nbt = stack.getTagCompound();
        StringBuilder sb;

        if (nbt != null) {
            String name = getEntityNameFromNBT(nbt);

            if (name != null && name.length() > 0) {
                if (EntityUtils.isLivingName(name)) {
                    // translate
                    name = StatCollector.translateToLocal(new StringBuilder().append("entity.").append(name).append(".name").toString());
                }

                sb = new StringBuilder().append(StatCollector.translateToLocal("mobdictionary.common.name")).append(":").append(name);
                tooltip.add(sb.toString());
            }
        }
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack item = new ItemStack(itemStack.getItem());

        if (itemStack.hasTagCompound()) {
            item.setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
        }

        return item;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return stack.getItem() instanceof ItemMobData;
    }

    public static String getEntityNameFromNBT(NBTTagCompound nbt) {
        return nbt.getString("Name");
    }

    public static void setEntityNameToNBT(String name, NBTTagCompound nbt) {
        nbt.setString("Name", name);
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        for (String name : (Set<String>)EntityUtils.getStringToClassMapping().keySet()) {
            if (EntityUtils.isLivingName(name)) {
                ItemStack item = new ItemStack(this);
                item.setTagCompound(new NBTTagCompound());
                setEntityNameToNBT(name, item.getTagCompound());
                subItems.add(item);
            }
        }
    }

}
