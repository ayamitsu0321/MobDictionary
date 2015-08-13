package ayamitsu.mobdictionary.client;

import ayamitsu.mobdictionary.AbstractProxy;
import ayamitsu.mobdictionary.MobDictionary;
import ayamitsu.mobdictionary.client.gui.GuiMobDictionary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ayamitsu0321 on 2015/04/12.
 */
public class ClientProxy extends AbstractProxy {

    private static HashMap<Integer, Class<? extends GuiScreen>> guiMap = new HashMap<Integer, Class<? extends GuiScreen>>();

    private static Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void preInit() {
        guiMap.put(Integer.valueOf(MobDictionary.GUI_DICTIONARY), GuiMobDictionary.class);
        //ModelResourceLocation mrl = new ModelResourceLocation(new ResourceLocation(MobDictionary.MODID, "dictionary"), "inventory");
        //System.out.println(mrl.toString());
        ModelLoader.setCustomModelResourceLocation(MobDictionary.mobDictionary, 0, new ModelResourceLocation(new ResourceLocation(MobDictionary.MODID, "dictionary"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(MobDictionary.mobData, 0, new ModelResourceLocation(new ResourceLocation(MobDictionary.MODID, "data"), "inventory"));
    }

    @Override
    public void init() {
    }

    @Override
    public void postInit() {
    }

    @Override
    public void displayScreen(EntityPlayer player, int guiID) {
        GuiScreen screen = null;

        try {
            Class clazz = guiMap.get(Integer.valueOf(guiID));
            screen = (GuiScreen)clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (screen != null) {
            FMLClientHandler.instance().displayGuiScreen(player, screen);
        }
    }

    @Override
    public EntityPlayer getPlayerInstance() {
        return FMLClientHandler.instance().getClientPlayerEntity();
    }

    // load only
    @Override
    public File getSaveDirectory() {
        return (new File(Loader.instance().getConfigDir(), "/dictionary")).getAbsoluteFile();
    }

    // from EntityRenderer
    public MovingObjectPosition getMouseOver(EntityLivingBase viewingEntity, double reach) {
        float renderPartialTicks = ((Timer)ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, mc, 17)).renderPartialTicks;
        MovingObjectPosition mop = null;

        if (viewingEntity != null) {
            if (viewingEntity.worldObj != null) {
                mop = viewingEntity.rayTrace(reach, renderPartialTicks);
                Vec3 viewPosition = viewingEntity.getPositionEyes(renderPartialTicks);//viewingEntity.getPosition();
                double d1 = 0;

                if (mop != null) {
                    d1 = mop.hitVec.distanceTo(viewPosition);
                }

                Vec3 lookVector = viewingEntity.getLook(renderPartialTicks);
                Vec3 reachVector = viewPosition.addVector(lookVector.xCoord * reach, lookVector.yCoord * reach, lookVector.zCoord * reach);
                Vec3 vec33 = null;
                float f1 = 1.0F;
                @SuppressWarnings("unchecked")
                List<Entity> list = viewingEntity.worldObj.getEntitiesWithinAABBExcludingEntity(
                        viewingEntity,
                        viewingEntity.getEntityBoundingBox().addCoord(lookVector.xCoord * reach, lookVector.yCoord * reach, lookVector.zCoord * reach).expand(f1, f1, f1)
                );
                double d2 = d1;
                Entity pointedEntity = null;

                for (Entity entity : list) {
                    if (entity.canBeCollidedWith()) {
                        float collisionSize = entity.getCollisionBorderSize();
                        AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(collisionSize, collisionSize, collisionSize);
                        MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(viewPosition, reachVector);

                        if (axisalignedbb.isVecInside(viewPosition)) {
                            if (0.0D < d2 || d2 == 0.0D) {
                                pointedEntity = entity;
                                vec33 = movingobjectposition == null ? viewPosition : movingobjectposition.hitVec;
                                d2 = 0.0D;
                            }
                        } else if (movingobjectposition != null) {
                            double d3 = viewPosition.distanceTo(movingobjectposition.hitVec);

                            if (d3 < d2 || d2 == 0.0D) {
                                if (entity == viewingEntity.ridingEntity && !entity.canRiderInteract()) {
                                    if (d2 == 0.0D) {
                                        pointedEntity = entity;
                                        vec33 = movingobjectposition.hitVec;
                                    }
                                } else {
                                    pointedEntity = entity;
                                    vec33 = movingobjectposition.hitVec;
                                    d2 = d3;
                                }
                            }
                        }
                    }
                }

                if (pointedEntity != null && (d2 < d1 || mop == null)) {
                    mop = new MovingObjectPosition(pointedEntity, vec33);
                }
            }
        }

        return mop;
    }

    @Override
    public void printChatMessageClient(IChatComponent chatComponent) {
        mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
    }
}
