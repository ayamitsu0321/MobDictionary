package net.minecraft.src.dictionary;

import net.minecraft.src.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiMobDictionary extends GuiScreen
{
	protected int xSize;
    protected int ySize;
	public static String[] names;
	public static int currentNo = 0;
	private int listHeight = 0;
    private int scrollY = 0;
    private int scrollHeight = 0;
    private final int stringColor = 0x303030;
	private Entity entity = null;
	boolean controlModel = false;
	boolean controlRot = false;
	private float rotH = 0.0F;//…•½•ûŒü‚Ì‰ñ“]
	private float rotV = 0.0F;//‚’¼•ûŒü‚Ì‰ñ“]
	private float traH = 0.0F;//…•½•ûŒü‚ÌˆÊ’u
	private float traV = 0.0F;//…•½•ûŒü‚ÌˆÊ’u
	private int rotTick = 0;
	private static String[] controlMode = new String[] {
		"Rotation", "Translation"
	};
	private boolean showMode = true;
	
	public GuiMobDictionary(InventoryPlayer inv)
	{
		super();
		xSize = 176;
        ySize = 166;
		names = MobDictionary.getNames_dic();//nameList
		
		if (names.length > 0)
		{
			this.listHeight = 15 * (names.length + 1) - 135;
			this.scrollHeight = (int)(139.0D / ((double)this.listHeight + 139.0D) * 139.0D);
			
			if (this.scrollHeight <= 0 || (double)this.scrollHeight > 139.0D)
            {
                this.scrollHeight = 139;
            }
		}
	}
	
	@Override
    public void drawScreen(int x, int y, float f)
    {
    	drawDefaultBackground();
    	drawGuiContainerBackgroundLayer(f, x, y);
    	drawNames(f, x, y);
        super.drawScreen(x, y, f);
    }
	
	//backdround
	public void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		String path = "/dictionary/dic_book.png";
		int i = mc.renderEngine.getTexture(path);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i);
		int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
		//mode‚Ì•\Ž¦
		if (controlModel && showMode)
		{
			String mode = controlRot ? controlMode[0] : controlMode[1];
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
		String var1 = String.valueOf(MobDictionary.getDicRegisteredValue()) + " / " + String.valueOf(MobDictionary.getEntityValueOfTypes());
		this.fontRenderer.drawString(var1, var4 + 126 - this.fontRenderer.getStringWidth(var1) / 2, var5 + 8, this.stringColor);
		
		//detail
		if (names.length > 0)
		{
			drawMobModel(f, x, y, names[currentNo]);
			this.fontRenderer.drawString("Name :", var4 + 19, var5 + 85, this.stringColor);
			this.fontRenderer.drawString(names[currentNo], var4 + 19, var5 + 95, this.stringColor);
			this.fontRenderer.drawString("ID :", var4 + 19, var5 + 106, this.stringColor);
			this.fontRenderer.drawString(String.valueOf(MobDictionary.getIDFromName(names[currentNo])), var4 + 19, var5 + 116, this.stringColor);
			
			if (entity != null && entity instanceof EntityLiving)
			{
				this.fontRenderer.drawString("Health : ", var4 + 19, var5 + 127, this.stringColor);
				this.fontRenderer.drawString(String.valueOf(((EntityLiving)entity).getMaxHealth()), var4 + 19, var5 + 137, this.stringColor);
			}
		}
		
		int var6;

		//draw entity contents
        for (var6 = 0; var6 < names.length; ++var6)
        {
        	String var8 = names[var6];
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
            for (int var6 = 0; var6 < names.length; ++var6)
            {
                if (this.mouseInRadioButton(var1, var2, var6))
                {
                	//set entity
                    currentNo = var6;
                    break;
                }
            }
        }
    }
	
	public void updateScreen()
	{
		if (Keyboard.getEventKeyState())
		{
			rotTick++;
			
			switch (Keyboard.getEventKey())
			{
				//ª, ©, «, ¨
    			case 200: doControlVertical(true); break;
    			case 203: doControlHorizon(false); break;
    			case 208: doControlVertical(false); break;
    			case 205: doControlHorizon(true); break;
			}
		}
		else
		{
			rotTick = 0;
		}
	}
	
	protected void keyTyped(char par1, int par2)
    {
    	//'E' or inventory trigger key
        if (par2 == 1 || par2 == mc.gameSettings.keyBindInventory.keyCode)
        {
        	//close GUI
            mc.thePlayer.closeScreen();
        }
    	else if (par2 == 44)
    	{
    		controlModel = !controlModel;
    	}
    	else if (par2 == 45)
    	{
    		controlRot = !controlRot;
    	}
    	else if (par2 == 46)
    	{
    		showMode = !showMode;
    	}
    }
	
	private void doControlHorizon(boolean flag)
	{
		float f = flag ? 0.5F : -0.5F;
		float f1 = flag ? (float)(rotTick & 0xff) * 0.05F : -(float)(rotTick & 0xff) * 0.05F;
		if (controlRot)
		{
			rotH = rotH + f + f1;
		}
		else
		{
			traH = traH + (f + f1) * 0.1F;
		}
	}
	
	private void doControlVertical(boolean flag)
	{
		float f = flag ? -0.5F : 0.5F;
		float f1 = flag ? -(float)(rotTick & 0xff) * 0.05F : (float)(rotTick & 0xff) * 0.05F;
		if (controlRot)
		{
			rotV = rotV + f + f1;
		}
		else
		{
			traV = traV + (f + f1) * 0.1F;
		}
	}
	
	//draw entity
	protected void drawMobModel(float var1, int var2, int var3, String var4)//f, x, y, entityname
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		if (var4 != null && (entity == null || entity != null && !EntityList.getEntityString(entity).equals(var4)))
		{
			entity = EntityList.createEntityByName(var4, mc.theWorld);
		}
		
		if (entity != null && var4 == null)
		{
			entity = null;
		}
		
		if (entity == null)
		{
			String str = "null";
			this.fontRenderer.drawString(str, j + 58 - this.fontRenderer.getStringWidth(str), k + 44, this.stringColor);
		}
		
		//render
		if (entity != null)
		{
			
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        	GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glPushMatrix();
			GL11.glTranslatef(j + 49, k + 70, 50F);
			float size = MathHelper.sqrt_float(entity.height * entity.height + entity.width * entity.width);
			float defScale = 60F;
			float scale = defScale * 0.4F / size;
			if (entity instanceof EntityDragon)
			{
				scale *= 1.8F;
			}
			else if (entity instanceof EntityIronGolem)
			{
				scale *= 1.4F;
			}
			GL11.glScalef(-scale, scale, scale);
			GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
			//RenderHelper.enableStandardItemLighting();
			float advance = entity instanceof EntitySquid ? 1.125F : entity instanceof EntityGhast || entity instanceof EntityDragon ? 2.8F : 0.0F;
			//GL11.glTranslatef(0.0F, entity.yOffset + advance, 0.0F);
			GL11.glTranslatef(0.0F, 0.0F + advance, 0.0F);
			if (!controlModel)
			{
				float xRot = (float)(mod_MobDictionary.yaw2 + (mod_MobDictionary.yaw - mod_MobDictionary.yaw2) * var1 * 10F);
				GL11.glRotatef(xRot, 0.0F, 1.0F, 0.0F);
			}
			else
			{
				GL11.glRotatef(rotH, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(rotV, 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(traH, 0.0F, traH);
				GL11.glTranslatef(0.0F, traV, 0.0F);
			}
			RenderManager.instance.playerViewY = 180F;
			RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
			GL11.glPopMatrix();
			//RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			
		}
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
}