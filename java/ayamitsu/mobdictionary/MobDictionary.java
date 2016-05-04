package ayamitsu.mobdictionary;

import ayamitsu.mobdictionary.event.PlayerLoggedInHandler;
import ayamitsu.mobdictionary.item.ItemMobData;
import ayamitsu.mobdictionary.item.ItemMobDictionary;
import ayamitsu.mobdictionary.item.crafting.RecipeMobData;
import ayamitsu.mobdictionary.network.PacketHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;

/**
 * Created by ayamitsu0321 on 2015/04/12.
 */
@Mod(
        modid = MobDictionary.MODID,
        name = MobDictionary.NAME,
        version = MobDictionary.VERSION
)
public class MobDictionary {

    public static final String MODID = "mobdictionary";//"ayamitsu.mobdictionary";
    public static final String NAME = "MobDictionary";
    public static final String VERSION = "3.0.2";
    public static final String CHANNEL = "AYA|MD";

    public static final int GUI_DICTIONARY = 0;

    @Mod.Instance(MODID)
    public static MobDictionary instance;

    @SidedProxy(clientSide = "ayamitsu.mobdictionary.client.ClientProxy", serverSide = "ayamitsu.mobdictionary.server.ServerProxy")
    public static AbstractProxy proxy;

    public static CreativeTabs tabMobDictionary;

    public static Item mobDictionary;

    public static Item mobData;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        mobDictionary = new ItemMobDictionary().setUnlocalizedName("mobdictionary.dictionary").setRegistryName(new ResourceLocation(MODID, "dictionary"));
        mobData = new ItemMobData().setCreativeTab(tabMobDictionary).setUnlocalizedName("mobdictionary.data").setRegistryName(new ResourceLocation(MODID, "data"));

        tabMobDictionary = new CreativeTabs("mobdictionary") {
            @Override
            public Item getTabIconItem() {
                return MobDictionary.mobDictionary;
            }
        };

        mobDictionary.setCreativeTab(tabMobDictionary);
        mobData.setCreativeTab(tabMobDictionary);
        //GameRegistry.registerItem(this.mobDictionary, "dictionary");
        //GameRegistry.registerItem(this.mobData, "data");
        GameRegistry.register(mobDictionary);
        GameRegistry.register(mobData);

        GameRegistry.addShapelessRecipe(new ItemStack(mobDictionary, 1),
                new ItemStack(Items.BOOK, 1),
                new ItemStack(Blocks.SAPLING, 1, 0),
                new ItemStack(Blocks.SAPLING, 1, 1),
                new ItemStack(Blocks.SAPLING, 1, 2),
                new ItemStack(Blocks.SAPLING, 1, 3)
        );

        RecipeSorter.register("mobdictionary:data", RecipeMobData.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        GameRegistry.addRecipe(new RecipeMobData());
        /*GameRegistry.addShapelessRecipe(new ItemStack(this.mobData),
                new Object[] {
                        new ItemStack(this.mobData),
                        new ItemStack(Items.paper)
                }
        );*/


        MinecraftForge.EVENT_BUS.register(new PlayerLoggedInHandler());
        PacketHandler.INSTANCE.init();
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MobDatas.initAllMobValue();

        /*if (event.getSide().isClient()) {// client and integrated server
            try {
                MobDatas.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        proxy.postInit();
    }

}
