package ayamitsu.dictionary.gui;

import java.util.Arrays;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;

import ayamitsu.dictionary.MobDatas;

public class GuiMobDictionary extends GuiScreen
{
	protected int xSize;
	protected int ySize;
	public String[] names;
	public int currentNo = 0;
	private int listHeight = 0;
	private int scrollY = 0;
	private int scrollHeight = 0;
	private final int stringColor = 0x303030;
	private Entity entity = null;
	boolean controlModel = false;
	boolean controlRot = false;
	private float rotH = 0.0F;//水平方向の回転
	private float rotV = 0.0F;//垂直方向の回転
	private float traH = 0.0F;//水平方向の位置
	private float traV = 0.0F;//水平方向の位置
	private int rotTick = 0;
	private static String[] controlMode = new String[] {
		"Rotation", "Translation"
	};
	private boolean showMode = true;
	protected int registerdValue;
	protected int allMobValue = MobDatas.getAllMobValue();
	protected double yaw = 0.0D;
	protected double yaw2 = 0.0D;

	public GuiMobDictionary()
	{
		super();
		this.xSize = 176;
		this.ySize = 166;
		this.names = MobDatas.toArray();
		this.registerdValue = this.names.length;

		Arrays.sort(this.names);

		if (this.names.length > 0)
		{
			this.listHeight = 15 * (this.names.length + 1) - 135;
			this.scrollHeight = (int)(139.0D / (this.listHeight + 139.0D) * 139.0D);

			if (this.scrollHeight <= 0 || this.scrollHeight > 139.0D)
			{
				this.scrollHeight = 139;
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame()
    {
        return false;
    }

	@Override
	public void drawScreen(int x, int y, float f)
	{
		this.yaw2 = this.yaw;
		this.drawDefaultBackground();
		this.drawGuiContainerBackgroundLayer(f, x, y);
		this.drawNames(f, x, y);
		super.drawScreen(x, y, f);
		this.yaw = 2.0D + yaw2 + (yaw2 - yaw) * f;
	}

	//backdround
	public void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/ayamitsu/dictionary/dic_book.png");
		int j = (this.width - this.xSize) / 2;
		int k = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
		//modeの表示
		if (this.controlModel && this.showMode)
		{
			String mode = this.controlRot ? GuiMobDictionary.controlMode[0] : GuiMobDictionary.controlMode[1];
			this.fontRenderer.drawString("Mode:" + mode, j + 3, k - 7, 16777215);
		}
	}

	//draw completed value, name, id, health
	private void drawNames(float f, int x, int y)
	{
		int var4 = this.width - this.xSize >> 1;
		int var5 = this.height - this.ySize >> 1;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		//getDicRegisteredValue() / getEntityValueOfTypes()
		String var1 = String.valueOf(this.registerdValue) + " / " + String.valueOf(this.allMobValue);
		this.fontRenderer.drawString(var1, var4 + 126 - this.fontRenderer.getStringWidth(var1) / 2, var5 + 8, this.stringColor);

		//detail
		if (this.names.length > 0)
		{
			this.drawMobModel(f, x, y, this.names[this.currentNo]);
			this.fontRenderer.drawString("Name :", var4 + 19, var5 + 85, this.stringColor);
			this.fontRenderer.drawString(StatCollector.translateToLocal("entity." + this.names[this.currentNo] + ".name"), var4 + 19, var5 + 95, this.stringColor);
			//this.fontRenderer.drawString("ID :", var4 + 19, var5 + 106, this.stringColor);
			//this.fontRenderer.drawString(String.valueOf(MobDictionary.getIDFromName(names[currentNo])), var4 + 19, var5 + 116, this.stringColor);

			if (this.entity != null && this.entity instanceof EntityLiving)
			{
				this.fontRenderer.drawString("Health : ", var4 + 19, var5 + 106, this.stringColor);
				this.fontRenderer.drawString(String.valueOf(((EntityLiving)this.entity).getMaxHealth()), var4 + 19, var5 + 116, this.stringColor);
			}
		}

		int var6;

		//draw entity contents
		for (var6 = 0; var6 < this.names.length; ++var6)
		{
			String var8 = this.names[var6];
			var8 = StatCollector.translateToLocal("entity." + var8 + ".name");
			int var7 = 15 * var6 + 20 - this.scrollY;

			if (var7 >= 20 && var7 + 9 < 135)
			{
				int var9 = Mouse.getEventX() * this.width / this.mc.displayWidth;
				int var10 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

				if (this.mouseInRadioButton(var9 - var4, var10 - var5, var6))
				{
					this.fontRenderer.drawString(var8, var4 + 126 - this.fontRenderer.getStringWidth(var8) / 2, var7 + var5, 16777215);
				}
				else
				{
					this.fontRenderer.drawString(var8, var4 + 126 - this.fontRenderer.getStringWidth(var8) / 2, var7 + var5, this.stringColor);
				}
			}
		}

		if (this.scrollHeight != 139)
		{
			var6 = Mouse.getDWheel();

			if (var6 < 0)
			{
				this.scrollY += 15;

				if (this.scrollY > this.listHeight)
				{
					this.scrollY = this.listHeight;
				}
			}
			else if (var6 > 0)
			{
				this.scrollY -= 15;

				if (this.scrollY < 0)
				{
					this.scrollY = 0;
				}
			}
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public boolean mouseInRadioButton(int var1, int var2, int var3)
	{
		short var4 = 90;
		int var5 = 15 * var3 + 16 - this.scrollY;
		return var1 >= var4 - 1 && var1 < var4 + 70 && var2 >= var5 - 1 && var2 < var5 + 14;
	}

	@Override
	protected void mouseClicked(int var1, int var2, int var3)
	{
		super.mouseClicked(var1, var2, var3);

		if (var3 == 1)
		{
			this.mc.displayGuiScreen((GuiScreen)null);
		}

		int var4 = this.width - this.xSize >> 1;
		int var5 = this.height - this.ySize >> 1;
		var1 -= var4;
		var2 -= var5;

		if (var3 == 0 && var1 >= 90 && var1 < 160 && var2 >= 16 && var2 < 135)
		{
			for (int var6 = 0; var6 < this.names.length; ++var6)
			{
				if (this.mouseInRadioButton(var1, var2, var6))
				{
					//set entity
					this.currentNo = var6;
					break;
				}
			}
		}
	}

	@Override
	public void updateScreen()
	{
		if (Keyboard.getEventKeyState())
		{
			this.rotTick++;

			switch (Keyboard.getEventKey())
			{
				//↑, ←, ↓, →
				case 200: this.doControlVertical(true); break;
				case 203: this.doControlHorizon(false); break;
				case 208: this.doControlVertical(false); break;
				case 205: this.doControlHorizon(true); break;
			}
		}
		else
		{
			this.rotTick = 0;
		}
	}

	@Override
	protected void keyTyped(char par1, int par2)
	{
		//'E' or inventory trigger key
		if (par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.keyCode)
		{
			//close GUI
			this.mc.thePlayer.closeScreen();
		}
		else if (par2 == 44)
		{
			this.controlModel = !this.controlModel;
		}
		else if (par2 == 45)
		{
			this.controlRot = !this.controlRot;
		}
		else if (par2 == 46)
		{
			this.showMode = !this.showMode;
		}
	}

	private void doControlHorizon(boolean flag)
	{
		float f = flag ? 0.5F : -0.5F;
		float f1 = flag ? (this.rotTick & 0xff) * 0.05F : -(float)(this.rotTick & 0xff) * 0.05F;

		if (this.controlRot)
		{
			this.rotH = this.rotH + f + f1;
		}
		else
		{
			this.traH = this.traH + (f + f1) * 0.1F;
		}
	}

	private void doControlVertical(boolean flag)
	{
		float f = flag ? -0.5F : 0.5F;
		float f1 = flag ? -(float)(this.rotTick & 0xff) * 0.05F : (this.rotTick & 0xff) * 0.05F;

		if (this.controlRot)
		{
			this.rotV = this.rotV + f + f1;
		}
		else
		{
			this.traV = this.traV + (f + f1) * 0.1F;
		}
	}

	//draw entity
	protected void drawMobModel(float var1, int var2, int var3, String var4)//f, x, y, entityname
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int j = (this.width - this.xSize) / 2;
		int k = (this.height - this.ySize) / 2;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		if (var4 != null && (this.entity == null || this.entity != null && !EntityList.getEntityString(this.entity).equals(var4)))
		{
			this.entity = EntityList.createEntityByName(var4, this.mc.theWorld);
		}

		if (this.entity != null && var4 == null)
		{
			this.entity = null;
		}

		if (this.entity == null)
		{
			String str = "null";
			this.fontRenderer.drawString(str, j + 58 - this.fontRenderer.getStringWidth(str), k + 44, this.stringColor);
		}

		//render
		if (this.entity != null)
		{
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glPushMatrix();
			GL11.glTranslatef(j + 49, k + 70, 50F);
			float size = MathHelper.sqrt_float(this.entity.height * this.entity.height + this.entity.width * this.entity.width);
			float defScale = 80F;//60F;
			float scale = defScale * 0.4F / size;
			if (this.entity instanceof EntityDragon)
			{
				scale *= 1.8F;
			}
			else if (this.entity instanceof EntityIronGolem)
			{
				scale *= 1.4F;
			}
			GL11.glScalef(-scale, scale, scale);
			GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
			//RenderHelper.enableStandardItemLighting();
			float advance = this.entity instanceof EntitySquid ? 1.125F : this.entity instanceof EntityGhast || this.entity instanceof EntityDragon ? 2.8F : 0.0F;
			//GL11.glTranslatef(0.0F, entity.yOffset + advance, 0.0F);
			GL11.glTranslatef(0.0F, 0.0F + advance, 0.0F);

			if (!this.controlModel)
			{
				float xRot = (float)(this.yaw2 + (this.yaw - this.yaw2) * var1 * 10F);
				GL11.glRotatef(xRot, 0.0F, 1.0F, 0.0F);
			}
			else
			{
				GL11.glRotatef(this.rotH, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(this.rotV, 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(this.traH, 0.0F, this.traH);
				GL11.glTranslatef(0.0F, this.traV, 0.0F);
			}

			RenderManager.instance.playerViewY = 180F;
			RenderManager.instance.renderEntityWithPosYaw(this.entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
			GL11.glPopMatrix();
			//RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		}

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

}
