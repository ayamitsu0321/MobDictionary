package ayamitsu.mobdictionary.item.crafting;

import ayamitsu.mobdictionary.MobDictionary;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by ayamitsu0321 on 2015/08/06.
 */
public class RecipeMobData implements IRecipe {

    private final ItemStack recipeOutput;

    public RecipeMobData() {
        recipeOutput = new ItemStack(MobDictionary.mobData);
    }

    @Override
    public boolean matches(InventoryCrafting p_77569_1_, World worldIn) {
        int dataCount = 0;
        int paperCount = 0;
        ItemStack data = null;

        for (int i = 0; i < p_77569_1_.getSizeInventory(); i++) {
            ItemStack item = p_77569_1_.getStackInSlot(i);

            if (item != null) {
                if (item.getItem() == MobDictionary.mobData) {
                    dataCount += 1;
                    data = item;
                } else if (item.getItem() == Items.paper) {
                    paperCount += 1;
                }
            }
        }

        if (dataCount == 1 && paperCount == 1 && data != null) {
            this.recipeOutput.setTagCompound((NBTTagCompound)data.getTagCompound().copy());
            return true;
        }

        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
        return this.recipeOutput.copy();
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting p_179532_1_) {
        ItemStack[] aitemstack = new ItemStack[p_179532_1_.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack itemstack = p_179532_1_.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return aitemstack;
    }
}
