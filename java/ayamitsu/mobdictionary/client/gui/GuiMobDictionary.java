package ayamitsu.mobdictionary.client.gui;

import ayamitsu.mobdictionary.MobDatas;
import ayamitsu.mobdictionary.MobDictionary;
import ayamitsu.mobdictionary.item.ItemMobData;
import ayamitsu.mobdictionary.util.EntityUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ayamitsu0321 on 2015/07/27.
 */
public class GuiMobDictionary extends GuiScreen {

    private static final ResourceLocation dictionaryResource = new ResourceLocation(MobDictionary.MODID, "textures/gui/dictionary.png");

    // size of screen pixels
    protected int xSize = 176;
    protected int ySize = 166;

    protected int stringColor = 0x303030;
    // FontRenderer#FONT_HEIGHT = 9
    protected int stringHeight;
    // 名前の欄の文字列同士の間隔
    protected int stringYMargin = 6;
    // どれだけスクロールしたか
    protected int scrollAmount = 0;
    // 名前のXのあたり判定
    protected int sizeNameAreaX = 60;
    // 選択してる番号
    protected int currentNo = 0;
    // 名前の欄の上と下の端の高さ
    protected int topEdge = 20;
    protected int bottomEdge = 11;
    // 左端からの名前の欄の中央までのXの距離
    protected int namesCenterOffsetX = 126;

    // {unlocalized name, localized name}
    protected NamePair[] nameList;

    protected Entity displayEntity;
    // controllable scale
    protected float entityScale = 1.0F;
    // rotation
    protected double yaw = 0.0D;
    protected double yaw2 = 0.0D;

    private static final List<String> tooltipStringList = Arrays.asList(
            "mobdictionary.common.output_piece",
            "mobdictionary.common.need_a_paper"
    );

    public GuiMobDictionary() {
        super();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.stringHeight = fontRendererObj.FONT_HEIGHT;
        String[] names = MobDatas.toArray();

        this.nameList = new NamePair[names.length];

        //あらかじめローカライズしておく
        for (int i = 0; i < names.length; i++) {
            if (EntityUtils.containsName(names[i])) {
                this.nameList[i] = new NamePair(names[i], StatCollector.translateToLocal("entity." + names[i] + ".name"));
            } else {
                this.nameList[i] = new NamePair(names[i], names[i]);
            }
        }

        Arrays.sort(this.nameList);

        int originX = this.width - this.xSize >> 1;
        int originY = this.height - this.ySize >> 1;

        GuiButton button = new GuiButtonMobDictionary(0, originX + 19, originY + 136, "");
        button.enabled = this.nameList.length > 0;

        this.buttonList.add(button);
    }

    // GUIを開いてるときに時間がとまるか
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.yaw2 = this.yaw;
        this.drawGuiBackgroundLayer(mouseX, mouseY, partialTicks);
        this.drawMobModel(mouseX, mouseY, partialTicks, this.nameList[this.currentNo].unlocalized);
        this.drawMobInfo(mouseX, mouseY, partialTicks);
        this.drawMobNames(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);// draw buttons
        this.drawTooltip(mouseX, mouseY, partialTicks);
        this.yaw = 2.0D + yaw2 + (yaw2 - yaw) * partialTicks;
    }

    public void drawGuiBackgroundLayer(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(dictionaryResource);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

    public void drawMobInfo(int mouseX, int mouseY, float partialTicks) {
        int originX = this.width - this.xSize >> 1;
        int originY = this.height - this.ySize >> 1;

        if (this.nameList.length > 0) {
            NamePair pair = this.nameList[this.currentNo];
            this.fontRendererObj.drawString("Name:", originX + 19, originY + 85, this.stringColor);
            this.fontRendererObj.drawString(pair.unlocalized, originX + 19, originY + 95, this.stringColor);

            if (this.displayEntity instanceof EntityLivingBase) {
                EntityLivingBase living = (EntityLivingBase)this.displayEntity;
                this.fontRendererObj.drawString("Health:", originX + 19, originY + 106, this.stringColor);
                this.fontRendererObj.drawString(String.format("%.1f", living.getMaxHealth()), originX + 19, originY + 116, this.stringColor);
            }
        }
    }

    public void drawMobNames(int mouseX, int mouseY, float partialTicks) {
        int originX = (this.width - this.xSize) / 2;
        int originY = (this.height - this.ySize) / 2;
        //int namesCenterOffsetX = 126;
        //int topEdge = 20;
        //int bottomEdge = 11;
        //int sizeNameAreaX = 60;
        //int sizeNameAreaY = this.ySize - (topEdge + bottomEdge);// = 135
        //int stringPos = 0;

        String str = new StringBuilder().append(MobDatas.getRegisteredValue()).append("/").append(MobDatas.getAllMobValue()).toString();
        this.fontRendererObj.drawString(str, originX + namesCenterOffsetX - this.fontRendererObj.getStringWidth(str) / 2, originY + 8, this.stringColor);

        int stringWidth;

        if (this.nameList.length > 0) {

            // scrollの実装
            // 取得しておかないとリセットされない
            /*int wheel = Mouse.getDWheel();

            if (
                    this.nameList.length > (this.ySize - this.bottomEdge) / (this.stringHeight + this.stringYMargin)
                    && this.isMouseInArea(
                            originX + this.namesCenterOffsetX - this.sizeNameAreaX / 2,
                            originX + this.namesCenterOffsetX + this.sizeNameAreaX / 2,
                            originY + this.topEdge,
                            originY + this.ySize - this.bottomEdge
                    )
                //&& scrollAmount < nameList.length - (this.ySize - bottomEdge) / (this.stringHeight + this.stringYMargin)
                    ) {// scrollできるかどうか

                if (wheel < 0) {// 下にscroll
                    this.scrollAmount += 1;

                    if (this.scrollAmount > this.nameList.length - (this.ySize - this.bottomEdge) / (this.stringHeight + this.stringYMargin)) {
                        this.scrollAmount = this.nameList.length - (this.ySize - this.bottomEdge) / (this.stringHeight + this.stringYMargin);
                    }
                } else if (wheel > 0) {// 上にscroll
                    this.scrollAmount -= 1;

                    if (this.scrollAmount < 0) {
                        this.scrollAmount = 0;
                    }
                }
            }*/

            int var1;// 名前が思いつかなかった

            for (int i = 0; i < this.nameList.length; i++) {
                if ((this.topEdge + ((i + 1) * (this.stringHeight + this.stringYMargin))) > (this.ySize - this.bottomEdge)) {
                    break;
                }

                var1 = i + this.scrollAmount;
                String translatedName = this.nameList[var1].localized;
                // 中央揃えのため
                stringWidth = this.fontRendererObj.getStringWidth(translatedName);
                // 選択してるかマウスオーバーしてるか
                int color = var1 == this.currentNo
                        || this.isMouseInArea(
                        originX + this.namesCenterOffsetX - this.sizeNameAreaX / 2,
                        originX + this.namesCenterOffsetX + this.sizeNameAreaX / 2,
                        originY + this.topEdge + (i * (this.stringHeight + this.stringYMargin)),
                        originY + this.topEdge + (i * (this.stringHeight + this.stringYMargin) + this.stringHeight)
                ) ? 0xffffff : this.stringColor;
                // 名前の描画
                this.fontRendererObj.drawString(
                        translatedName,
                        originX + namesCenterOffsetX - stringWidth / 2,
                        originY + this.topEdge + (i * (this.stringHeight + this.stringYMargin)),
                        color
                );
            }
        }
    }

    protected void drawMobModel(int mouseX, int mouseY, float partialTicks, String unlocalizedName) {
        int originX = (this.width - this.xSize) / 2;
        int originY = (this.height - this.ySize) / 2;

        if (unlocalizedName == null) {
            this.displayEntity = null;
        } else if (this.displayEntity == null || !EntityList.getEntityString(this.displayEntity).equals(unlocalizedName)) {
            this.displayEntity = EntityList.createEntityByName(unlocalizedName, this.mc.theWorld);
        }

        if (this.displayEntity == null) {
            String str = "null";
            this.fontRendererObj.drawString(str, originX + 58 - this.fontRendererObj.getStringWidth(str), originY + 44, this.stringColor);
        } else {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glPushMatrix();
            // offset
            GL11.glTranslatef(originX + 49, originY + 70, 50F);
            //float defaultScale = 32F;
            //GL11.glScalef(-defaultScale, defaultScale, defaultScale);
            //float size = MathHelper.sqrt_float(this.displayEntity.height * this.displayEntity.height + this.displayEntity.width * this.displayEntity.width);
            float scale = 25F;//defaultScale / size;
            GL11.glScalef(-scale, scale, scale);
            // マウスホイールでサイズ調整するところ
            GL11.glScalef(this.entityScale, this.entityScale, this.entityScale);

            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);

            // rotation
            float xRot = (float)(this.yaw2 + (this.yaw - this.yaw2) * partialTicks * 10F);
            GL11.glRotatef(xRot, 0.0F, 1.0F, 0.0F);

            RenderManager manager = this.mc.getRenderManager();
            manager.playerViewY = 180F;
            manager.renderEntityWithPosYaw(this.displayEntity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
            GL11.glPopMatrix();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected boolean isMouseInArea(int x1, int x2, int y1, int y2) {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight;// - 1;
        boolean flag = x1 <= mouseX && mouseX < x2 && y1 <= mouseY && mouseY < y2;
        return flag;
    }

    @SuppressWarnings("unchecked")
    protected void drawTooltip(int mouseX, int mouseY, float partialTicks) {
        if (((GuiButton)this.buttonList.get(0)).isMouseOver()) {// Button
            List<String> list = new ArrayList<String>();

            for (Object obj : tooltipStringList) {
                list.add(StatCollector.translateToLocal((String)obj));
            }

            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    list.set(i, list.get(i));
                } else {
                    list.set(i, EnumChatFormatting.GRAY + list.get(i));
                }
            }

            this.drawHoveringText(list, mouseX, mouseY, this.fontRendererObj);
        }
    }

    @Override
    public void updateScreen() {
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {// output
            if (this.nameList.length > 0) {// 登録数が1以上
                EntityPlayer player = this.mc.thePlayer;
                InventoryPlayer inventory = player.inventory;

                if (inventory.hasItem(Items.paper)) {// 紙があるか
                    if (inventory.getFirstEmptyStack() > 0) {// インベントリに空きがあるか
                        ItemStack itemStack = new ItemStack(MobDictionary.mobData);
                        NBTTagCompound nbt = new NBTTagCompound();
                        ItemMobData.setEntityNameToNBT(this.nameList[this.currentNo].unlocalized, nbt);
                        itemStack.setTagCompound(nbt);

                        inventory.consumeInventoryItem(Items.paper);
                        inventory.addItemStackToInventory(itemStack);
                    }
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int originX = (this.width - this.xSize) / 2;
        int originY = (this.height - this.ySize) / 2;
        int var1;

        for (int i = 0; i < this.nameList.length; i++) {
            if ((this.topEdge + ((i + 1) * (this.stringHeight + this.stringYMargin))) > (this.ySize - this.bottomEdge)) {
                break;
            }

            var1 = i + this.scrollAmount;

            if (this.isMouseInArea(
                    originX + this.namesCenterOffsetX - this.sizeNameAreaX / 2,
                    originX + this.namesCenterOffsetX + this.sizeNameAreaX / 2,
                    originY + this.topEdge + (i * (this.stringHeight + this.stringYMargin)),
                    originY + this.topEdge + (i * (this.stringHeight + this.stringYMargin) + this.stringHeight)
            )) {
                if (this.currentNo != var1) {
                    this.entityScale = 1.0F;// reset
                }

                this.currentNo = var1;
                break;
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.mouseWheeled();
    }

    protected void mouseWheeled() {
        int originX = (this.width - this.xSize) / 2;
        int originY = (this.height - this.ySize) / 2;
        int wheel = Mouse.getDWheel();

        // names
        if (this.nameList.length > 0) {
            if (
                    this.nameList.length > (this.ySize - this.bottomEdge) / (this.stringHeight + this.stringYMargin)
                            && this.isMouseInArea(
                            originX + this.namesCenterOffsetX - this.sizeNameAreaX / 2,
                            originX + this.namesCenterOffsetX + this.sizeNameAreaX / 2,
                            originY + this.topEdge,
                            originY + this.ySize - this.bottomEdge
                    )
                //&& scrollAmount < nameList.length - (this.ySize - bottomEdge) / (this.stringHeight + this.stringYMargin)
                    ) {// scrollできるかどうか

                if (wheel < 0) {// 下にscroll
                    this.scrollAmount += 1;

                    if (this.scrollAmount > this.nameList.length - (this.ySize - this.bottomEdge) / (this.stringHeight + this.stringYMargin)) {
                        this.scrollAmount = this.nameList.length - (this.ySize - this.bottomEdge) / (this.stringHeight + this.stringYMargin);
                    }
                } else if (wheel > 0) {// 上にscroll
                    this.scrollAmount -= 1;

                    if (this.scrollAmount < 0) {
                        this.scrollAmount = 0;
                    }
                }
            }
        }

        // model 拡大縮小
        if (this.nameList.length > 0) {
            if (this.isMouseInArea(
                    originX,
                    originX + 49 * 2,
                    originY,
                    originY + 80
            )){
                if (wheel < 0) {
                    this.entityScale /= 1.2F;
                } else if (wheel > 0) {
                    this.entityScale *= 1.2F;
                }
            }
        }
    }

    static class NamePair implements Comparable {

        public String unlocalized;
        public String localized;

        public NamePair(String unlocalized, String localized) {
            this.unlocalized = unlocalized;
            this.localized = localized;
        }

        // localizeした文字列で比較
        @Override
        public int compareTo(Object o) {
            NamePair other = (NamePair)o;
            return this.localized.compareTo(other.localized);
        }
    }
}
