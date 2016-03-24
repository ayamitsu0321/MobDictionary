package ayamitsu.mobdictionary.item;

import ayamitsu.mobdictionary.MobDatas;
import ayamitsu.mobdictionary.MobDictionary;
import ayamitsu.mobdictionary.network.PacketHandler;
import ayamitsu.mobdictionary.network.packet.register.MessageRegister;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by ayamitsu0321 on 2015/04/13.
 */
public class ItemMobDictionary extends Item {

    public ItemMobDictionary() {
        super();
    }

    @Override
    //public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        // client side
        if (worldIn.isRemote) {
            // main_handのときだけ図鑑ひらける
            if (playerIn.isSneaking() && hand == EnumHand.MAIN_HAND) {
                this.displayDictionary(playerIn);
            } else {
                //距離延長してEntity取得
                RayTraceResult mop = MobDictionary.proxy.getMouseOver(playerIn, 20.0F);

                if (mop.entityHit instanceof EntityLivingBase) {
                    EntityLivingBase living = (EntityLivingBase)mop.entityHit;
                    Class clazz = living.getClass();
                    //System.out.println(living.getName());

                    if (MobDatas.contains(clazz)) {
                        if (hasItemPlayer(playerIn, Items.paper)) {
                            if (hasEmptyPlayer(playerIn)) {
                                consumeItemPlayer(playerIn, Items.paper);
                                ItemStack is = new ItemStack(MobDictionary.mobData);
                                NBTTagCompound nbt = new NBTTagCompound();
                                ItemMobData.setEntityNameToNBT(EntityList.getEntityString(living), nbt);
                                is.setTagCompound(nbt);
                                addItemStackPlayer(playerIn, is);
                            } else {
                                // show chat
                                showChatMessage(EnumChatMessage.ALREADY, I18n.translateToLocal("entity." + EntityList.getEntityString(living) + ".name"));
                            }
                        } else {
                            // show chat
                            showChatMessage(EnumChatMessage.ALREADY, I18n.translateToLocal("entity." + EntityList.getEntityString(living) + ".name"));
                        }
                    } else {
                        //client側での登録
                        MobDatas.addInfo(clazz);
                        // show chat
                        showChatMessage(EnumChatMessage.ACCEPT, I18n.translateToLocal("entity." + EntityList.getEntityString(living) + ".name"));
                        //server側での登録のためにMessage
                        PacketHandler.DISPATCHER.sendToServer(new MessageRegister(playerIn, living));
                    }
                }
            }
        }

        //if (playerIn instanceof EntityPlayerMP && MobDictionary.proxy.isDedicatedServer()) {
        //    System.out.println("registeredValue:" + MobDatas.getRegisteredValueOnDedicatedServer((EntityPlayerMP)playerIn));
        //} else {
        //    System.out.println("registeredValue:" + MobDatas.getRegisteredValue());
        //}

        return new ActionResult(EnumActionResult.PASS, itemStackIn);
    }

    @SideOnly(Side.CLIENT)
    private void displayDictionary(EntityPlayer player) {
        //FMLClientHandler.instance().displayGuiScreen(player, new GuiMobDictionary());
        //FMLCommonHandler.instance().showGuiScreen(new GuiMobDictionary());
        MobDictionary.proxy.displayScreen(player, MobDictionary.GUI_DICTIONARY);
    }

    private static boolean hasItemPlayer(EntityPlayer player, Item item) {
        InventoryPlayer inventory = player.inventory;
        return inventory.hasItemStack(new ItemStack(item));
    }

    private static boolean hasEmptyPlayer(EntityPlayer player) {
        InventoryPlayer inventory = player.inventory;
        return inventory.getFirstEmptyStack() > 0;
    }

    private static void consumeItemPlayer(EntityPlayer player, Item item) {
        InventoryPlayer inventory = player.inventory;
        int size = inventory.mainInventory.length;

        for (int i = 0; i < size; i++) {
            ItemStack is = inventory.mainInventory[i];

            if (is.getItem() == item) {
                if (--inventory.mainInventory[i].stackSize <= 0) {
                    inventory.mainInventory[i] = null;
                }

                break;
            }
        }
    }

    private static void addItemStackPlayer(EntityPlayer player, ItemStack itemStack) {
        InventoryPlayer inventory = player.inventory;
        inventory.addItemStackToInventory(itemStack);
    }

    // client
    public static void showChatMessage(EnumChatMessage chatMessage, Object... objects) {
        ITextComponent chatComponent = new TextComponentString(chatMessage.getTranslatedText(objects));
        MobDictionary.proxy.printChatMessageClient(chatComponent);
    }

    /*
        @Override
        public int getColorFromItemStack(ItemStack is, int layer) {
            return 0x965036;// 檜皮色 hiwada
        }
    */
    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
        StringBuilder sb = new StringBuilder(I18n.translateToLocal("mobdictionary.common.registered_value")).append(": ").append(MobDatas.getRegisteredValue()).append('/').append(MobDatas.getAllMobValue());
        tooltip.add(sb.toString());
    }

    static enum EnumChatMessage {

        ACCEPT("mobdictionary.common.register_accept"),
        ALREADY("mobdictionary.common.register_already"),
        ERROR("mobdictionary.common.register_error");

        String text;

        EnumChatMessage(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public String getTranslatedText(Object... objects) {
            return I18n.translateToLocalFormatted(this.text, objects);
        }

    }

}
