package thebetweenlands.items;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;
import thebetweenlands.blocks.BLBlockRegistry;
import thebetweenlands.creativetabs.ModCreativeTabs;
import thebetweenlands.entities.EntityBLItemFrame;
import thebetweenlands.items.loot.ItemExplorerHat;
import thebetweenlands.items.loot.ItemRingOfPower;
import thebetweenlands.items.loot.ItemVoodooDoll;
import thebetweenlands.recipes.BLMaterials;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class BLItemRegistry {
    public static final List<Item> ITEMS = new ArrayList<Item>();

    // BASIC MATERIALS
    public static final Item testItem = new TestItem().setUnlocalizedName("thebetweenlands.testItem").setTextureName("thebetweenlands:testItem");
    public static final Item swampTalisman = new SwampTalisman();
    public static final Item materialsBL = new ItemMaterialsBL();
    public static final Item materialCrushed = new ItemMaterialsCrushed().setUnlocalizedName("thebetweenlands.groundStuff"); // TODO fix needing unloc name here to stop null in registry
    public static final Item soundTest = new ItemSoundTest().setUnlocalizedName("thebetweenlands.testItemSound");
    public static final Item lifeCrystal = new ItemLifeCrystal().setUnlocalizedName("thebetweenlands.lifeCrystal").setTextureName("thebetweenlands:lifeCrystal");

    // WEAPONS TOOLS
    public static final Item weedwoodSword = new SwordBL(BLMaterials.toolWeedWood).setUnlocalizedName("thebetweenlands.weedwoodSword").setTextureName("thebetweenlands:weedWoodSword");
    public static final Item weedwoodPickaxe = new PickaxeBL(BLMaterials.toolWeedWood).setUnlocalizedName("thebetweenlands.weedwoodPickaxe").setTextureName("thebetweenlands:weedWoodPickaxe");
    public static final Item weedwoodAxe = new AxeBL(BLMaterials.toolWeedWood).setUnlocalizedName("thebetweenlands.weedwoodAxe").setTextureName("thebetweenlands:weedWoodAxe");
    public static final Item weedwoodShovel = new SpadeBL(BLMaterials.toolWeedWood).setUnlocalizedName("thebetweenlands.weedwoodShovel").setTextureName("thebetweenlands:weedWoodShovel");
    public static final Item weedwoodBow = new ItemWeedwoodBow().setUnlocalizedName("thebetweenlands.weedwoodBow").setTextureName("thebetweenlands:weedwoodBow");
    public static final Item anglerToothArrow = new ItemBLArrow("anglerToothArrow").setUnlocalizedName("thebetweenlands.anglerToothArrow").setTextureName("thebetweenlands:anglerToothArrowItem");
    public static final Item poisonedAnglerToothArrow = new ItemBLArrow("poisonedAnglerToothArrow").setUnlocalizedName("thebetweenlands.poisonedAnglerToothArrow");
    public static final Item octineArrow = new ItemBLArrow("octineArrow").setUnlocalizedName("thebetweenlands.octineArrow");
    public static final Item basiliskArrow = new ItemBLArrow("basiliskArrow").setUnlocalizedName("thebetweenlands.basiliskArrow");

    public static final Item betweenstoneSword = new SwordBL(BLMaterials.toolBetweenstone).setUnlocalizedName("thebetweenlands.betweenstoneSword").setTextureName("thebetweenlands:betweenstoneSword");
    public static final Item betweenstonePickaxe = new PickaxeBL(BLMaterials.toolBetweenstone).setUnlocalizedName("thebetweenlands.betweenstonePickaxe").setTextureName("thebetweenlands:betweenstonePickaxe");
    public static final Item betweenstoneAxe = new AxeBL(BLMaterials.toolBetweenstone).setUnlocalizedName("thebetweenlands.betweenstoneAxe").setTextureName("thebetweenlands:betweenstoneAxe");
    public static final Item betweenstoneShovel = new SpadeBL(BLMaterials.toolBetweenstone).setUnlocalizedName("thebetweenlands.betweenstoneShovel").setTextureName("thebetweenlands:betweenstoneShovel");

    public static final Item octineSword = new SwordBL(BLMaterials.toolOctine).setUnlocalizedName("thebetweenlands.octineSword").setTextureName("thebetweenlands:octineSword");
    public static final Item octinePickaxe = new PickaxeBL(BLMaterials.toolOctine).setUnlocalizedName("thebetweenlands.octinePickaxe").setTextureName("thebetweenlands:octinePickaxe");
    public static final Item octineAxe = new AxeBL(BLMaterials.toolOctine).setUnlocalizedName("thebetweenlands.octineAxe").setTextureName("thebetweenlands:octineAxe");
    public static final Item octineShovel = new SpadeBL(BLMaterials.toolOctine).setUnlocalizedName("thebetweenlands.octineShovel").setTextureName("thebetweenlands:octineShovel");

    public static final Item valoniteSword = new SwordBL(BLMaterials.toolValonite).setUnlocalizedName("thebetweenlands.valoniteSword").setTextureName("thebetweenlands:valoniteSword");
    public static final Item valonitePickaxe = new PickaxeBL(BLMaterials.toolValonite).setUnlocalizedName("thebetweenlands.valonitePickaxe").setTextureName("thebetweenlands:valonitePickaxe");
    public static final Item valoniteAxe = new AxeBL(BLMaterials.toolValonite).setUnlocalizedName("thebetweenlands.valoniteAxe").setTextureName("thebetweenlands:valoniteAxe");
    public static final Item valoniteShovel = new SpadeBL(BLMaterials.toolValonite).setUnlocalizedName("thebetweenlands.valoniteShovel").setTextureName("thebetweenlands:valoniteShovel");
    
    public static final Item weedwoodBucket = new ItemWeedwoodBucket();
    public static final Item weedwoodBucketTar = new ItemWeedwoodBucket(BLBlockRegistry.tarFluid).setUnlocalizedName("thebetweenlands.weedwoodBucketTar").setTextureName("thebetweenlands:weedwoodBucketTar");
    public static final Item weedwoodBucketWater = new ItemWeedwoodBucket(BLBlockRegistry.swampWater).setUnlocalizedName("thebetweenlands.weedwoodBucketWater").setTextureName("thebetweenlands:weedwoodBucketWater");
    
    // MISC ARMOUR
    public static final Item lurkerSkinHelmet = new LurkerSkinArmor(0).setUnlocalizedName("thebetweenlands.lurkerSkinHelmet").setTextureName("thebetweenlands:lurkerSkinHelmet");
    public static final Item lurkerSkinChestplate = new LurkerSkinArmor(1).setUnlocalizedName("thebetweenlands.lurkerSkinChestplate").setTextureName("thebetweenlands:lurkerSkinChestplate");
    public static final Item lurkerSkinLeggings = new LurkerSkinArmor(2).setUnlocalizedName("thebetweenlands.lurkerSkinLeggings").setTextureName("thebetweenlands:lurkerSkinLeggings");
    public static final Item lurkerSkinBoots = new LurkerSkinArmor(3).setUnlocalizedName("thebetweenlands.lurkerSkinBoots").setTextureName("thebetweenlands:lurkerSkinBoots");

    public static final Item boneHelmet = new BoneArmor(0).setUnlocalizedName("thebetweenlands.boneHelmet").setTextureName("thebetweenlands:boneHelmet");
    public static final Item boneChestplate = new BoneArmor(1).setUnlocalizedName("thebetweenlands.boneChestplate").setTextureName("thebetweenlands:boneChestplate");
    public static final Item boneLeggings = new BoneArmor(2).setUnlocalizedName("thebetweenlands.boneLeggings").setTextureName("thebetweenlands:boneLeggings");
    public static final Item boneBoots = new BoneArmor(3).setUnlocalizedName("thebetweenlands.boneBoots").setTextureName("thebetweenlands:boneBoots");
    
    public static final Item octineHelmet = new OctineArmor(0).setUnlocalizedName("thebetweenlands.octineHelmet").setTextureName("thebetweenlands:octineHelmet");
    public static final Item octineChestplate = new OctineArmor(1).setUnlocalizedName("thebetweenlands.octineChestplate").setTextureName("thebetweenlands:octineChestplate");
    public static final Item octineLeggings = new OctineArmor(2).setUnlocalizedName("thebetweenlands.octineLeggings").setTextureName("thebetweenlands:octineLeggings");
    public static final Item octineBoots = new OctineArmor(3).setUnlocalizedName("thebetweenlands.octineBoots").setTextureName("thebetweenlands:octineBoots");

    public static final Item valoniteHelmet = new ValoniteArmor(0).setUnlocalizedName("thebetweenlands.valoniteHelmet").setTextureName("thebetweenlands:valoniteHelmet");
    public static final Item valoniteChestplate = new ValoniteArmor(1).setUnlocalizedName("thebetweenlands.valoniteChestplate").setTextureName("thebetweenlands:valoniteChestplate");
    public static final Item valoniteLeggings = new ValoniteArmor(2).setUnlocalizedName("thebetweenlands.valoniteLeggings").setTextureName("thebetweenlands:valoniteLeggings");
    public static final Item valoniteBoots = new ValoniteArmor(3).setUnlocalizedName("thebetweenlands.valoniteBoots").setTextureName("thebetweenlands:valoniteBoots");

    public static final Item rubberBoots = new ItemRubberBoots().setUnlocalizedName("thebetweenlands.rubberBoots").setTextureName("thebetweenlands:rubberBoots");
    public static final Item rubberBootsImproved = new ItemImprovedRubberBoots().setUnlocalizedName("thebetweenlands.rubberBootsImproved").setTextureName("thebetweenlands:rubberBoots");
    
    // CREATIVE
    public static final Item spawnEggs = new SpawnEggs().setUnlocalizedName("thebetweenlands.monsterPlacer").setTextureName("spawn_egg");

    //FOOD
    public static final Item sapBall = new ItemSapBall().setUnlocalizedName("thebetweenlands.sapBall").setTextureName("thebetweenlands:sapBall");
    public static final Item rottenFood = new ItemRottenFood().setAlwaysEdible().setUnlocalizedName("thebetweenlands.rottenFood").setTextureName("thebetweenlands:rottenFood");
    public static final Item middleFruitSeeds = new ItemBLGenericSeed(0, 0F, BLBlockRegistry.middleFruitBush, BLBlockRegistry.farmedDirt).setUnlocalizedName("thebetweenlands.middleFruitSeeds").setTextureName("thebetweenlands:middleFruitSeeds");

    public static final Item anglerMeatRaw = new ItemFood(4, 1.8F, false).setUnlocalizedName("thebetweenlands.anglerMeatRaw").setTextureName("thebetweenlands:anglerFlesh");
    public static final Item anglerMeatCooked = new ItemFood(8, 18.0F, false).setUnlocalizedName("thebetweenlands.anglerMeatCooked").setTextureName("thebetweenlands:cookedAnglerFlesh");
    public static final Item frogLegsRaw = new ItemFood(2, 1.2F, false).setUnlocalizedName("thebetweenlands.frogLegsRaw").setTextureName("thebetweenlands:frogLegs");
    public static final Item frogLegsCooked = new ItemFood(2, 1.2F, false).setUnlocalizedName("thebetweenlands.frogLegsCooked").setTextureName("thebetweenlands:frogLegsCooked");
    public static final Item snailFleshRaw = new ItemFood(3, 1.6F, false).setUnlocalizedName("thebetweenlands.snailFleshRaw").setTextureName("thebetweenlands:snailMeat");
    public static final Item snailFleshCooked = new ItemFood(6, 13.2F, false).setUnlocalizedName("thebetweenlands.snailFleshCooked").setTextureName("thebetweenlands:cookedSnailMeat");
    public static final Item reedDonut = new ItemFood(6, 13.2F, false).setUnlocalizedName("thebetweenlands.reedDonut").setTextureName("thebetweenlands:donut");
    public static final Item jamDonut = new ItemFood(10, 20.0F, false).setUnlocalizedName("thebetweenlands.jamDonut").setTextureName("thebetweenlands:jamDonut");
    public static final Item gertsDonut = new ItemGertsDonut().setUnlocalizedName("thebetweenlands.gertsDonut").setTextureName("thebetweenlands:gertsDonut");
    public static final Item krakenTentacle = new ItemFood(8, 8.0F, false).setUnlocalizedName("thebetweenlands.krakenTentacle").setTextureName("thebetweenlands:krakenTentacle");
    public static final Item krakenCalamari = new ItemFood(14, 20.0F, false).setUnlocalizedName("thebetweenlands.krakenCalamari").setTextureName("thebetweenlands:krakenCalamari");
    public static final Item middleFruit = new ItemFood(6, 15.6F, false).setUnlocalizedName("thebetweenlands.middleFruit").setTextureName("thebetweenlands:middleFruit");
    public static final Item mincePie = new ItemFood(4, 14.8F, false).setUnlocalizedName("thebetweenlands.mincePie").setTextureName("thebetweenlands:mincePie");
    public static final Item weepingBluePetal = new ItemWeepingBluePetal().setUnlocalizedName("thebetweenlands.weepingBluePetal").setTextureName("thebetweenlands:weepingBluePetal");
    public static final Item wightsHeart = new ItemWightHeart().setUnlocalizedName("thebetweenlands.wightHeart").setTextureName("thebetweenlands:wightHeart");
    public static final Item yellowDottedFungus = new ItemFood(10, 20.0F, false).setUnlocalizedName("thebetweenlands.yellowDottedFungus").setTextureName("thebetweenlands:yellowDottedFungus");
    public static final Item siltCrabClaw = new ItemFood(2, 0.6F, false).setUnlocalizedName("thebetweenlands.siltCrabClaw").setTextureName("thebetweenlands:siltCrabClaw");
    public static final Item crabstick = new ItemFood(6, 5.3F, false).setUnlocalizedName("thebetweenlands.crabstick").setTextureName("thebetweenlands:crabStick");
    public static final Item nettleSoup = new ItemNettleSoup().setUnlocalizedName("thebetweenlands.nettleSoup").setTextureName("thebetweenlands:nettleSoup");
    public static final Item sludgeJello = new ItemFood(4, 15F, false).setUnlocalizedName("thebetweenlands.sludgeJello").setTextureName("thebetweenlands:sludgeJello");
    public static final Item middleFruitJello = new ItemFood(10, 20F, false).setUnlocalizedName("thebetweenlands.middleFruitJello").setTextureName("thebetweenlands:middleFruitJello");
    public static final Item sapJello = new ItemSapJello().setUnlocalizedName("thebetweenlands.sapJello").setTextureName("thebetweenlands:sapJello");
    public static final Item marshmallow = new ItemMarshmallow().setUnlocalizedName("thebetweenlands.marshmallow").setTextureName("thebetweenlands:greenMarshmallow");
    public static final Item marshmallowPink = new ItemMarshmallowPink().setUnlocalizedName("thebetweenlands.marshmallowPink").setTextureName("thebetweenlands:pinkMarshmallow");
    public static final Item flatheadMushroomItem = new ItemFlatheadMushroom().setUnlocalizedName("thebetweenlands.flatheadMushroomItem").setTextureName("thebetweenlands:flatheadMushroom");
    public static final Item blackHatMushroomItem = new ItemBlackHatMushroom().setUnlocalizedName("thebetweenlands.blackHatMushroomItem").setTextureName("thebetweenlands:blackHatMushroom");
    public static final Item bulbCappedMushroomItem = new ItemBulbCappedMushroom().setUnlocalizedName("thebetweenlands.bulbCappedMushroomItem").setTextureName("thebetweenlands:bulbCappedMushroom");
    public static final Item friedSwampKelp = new ItemFood(5, 8.0F, false).setUnlocalizedName("thebetweenlands.friedSwampKelp").setTextureName("thebetweenlands:friedSwampKelp");
    
    //DOORS
    public static final Item doorWeedwood = new ItemBLDoor(BLBlockRegistry.doorWeedwood);
    public static final Item doorOctine = new ItemBLDoor(BLBlockRegistry.doorOctine);
    
    //MISC
    public static final Item angryPebble = new ItemAngryPebble();
    public static final Item scroll = new Item().setUnlocalizedName("thebetweenlands.itemScroll").setTextureName("thebetweenlands:itemScroll").setMaxStackSize(1);
    public static final Item swampKelp = new Item().setUnlocalizedName("thebetweenlands.swampKelp").setTextureName("thebetweenlands:swampKelp");
    public static final Item weedwoodBucketRubber = new ItemWeedwoodBucketRubber();
    public static final Item itemFrame = new ItemBLHangingEntity(EntityBLItemFrame.class).setUnlocalizedName("thebetweenlands.weedwoodItemFrame").setTextureName("thebetweenlands:weedwoodItemFrame").setCreativeTab(ModCreativeTabs.blocks);
    public static final Item mudFlowerPot = new ItemReed(BLBlockRegistry.mudFlowerPot).setUnlocalizedName("thebetweenlands.mudFlowerPotItem").setCreativeTab(ModCreativeTabs.blocks).setTextureName("thebetweenlands:mudBrickFlowerPot");
    public static final Item compost = new ItemCompost().setUnlocalizedName("thebetweenlands.compost");
    public static final Item pestle = new ItemPestle().setUnlocalizedName("thebetweenlands.pestle");
    public static final Item weedwoodBucketInfusion = new ItemWeedwoodBucketInfusion();
    //LOOT
    public static final Item voodooDoll = new ItemVoodooDoll();
    public static final Item explorerHat = new ItemExplorerHat().setUnlocalizedName("thebetweenlands.explorerHat").setTextureName("thebetweenlands:explorerHat");
    public static final Item ringOfPower = new ItemRingOfPower();

    public static void init() {
        initCreativeTabs();
        registerItems();
        registerProperties();
    }

    private static void initCreativeTabs() {
        ModCreativeTabs.items.setTab(swampTalisman, materialsBL, sapBall, rottenFood, flatheadMushroomItem, blackHatMushroomItem, bulbCappedMushroomItem, anglerMeatRaw, anglerMeatCooked, frogLegsRaw, frogLegsCooked, snailFleshRaw,
                                     snailFleshCooked, swampKelp, friedSwampKelp, reedDonut, jamDonut, krakenTentacle, krakenCalamari, middleFruit, mincePie, weepingBluePetal,
                                     wightsHeart, yellowDottedFungus, siltCrabClaw, crabstick, nettleSoup, sludgeJello, middleFruitJello, sapJello, marshmallow, marshmallowPink, weedwoodBucket, weedwoodBucketWater, weedwoodBucketTar, weedwoodBucketInfusion, lifeCrystal, materialCrushed);
        ModCreativeTabs.specials.setTab(testItem, spawnEggs, angryPebble, scroll, soundTest, voodooDoll, ringOfPower);
        ModCreativeTabs.gears.setTab(weedwoodSword, weedwoodPickaxe, weedwoodAxe, weedwoodShovel, betweenstoneSword, betweenstonePickaxe, betweenstoneAxe, betweenstoneShovel, octineSword, octinePickaxe, octineAxe, octineShovel, valoniteSword, valonitePickaxe, valoniteAxe, valoniteShovel);
        ModCreativeTabs.gears.setTab(lurkerSkinHelmet, lurkerSkinChestplate, lurkerSkinLeggings, lurkerSkinBoots, boneHelmet, boneChestplate, boneLeggings, boneBoots, octineHelmet, octineChestplate, octineLeggings, octineBoots, valoniteHelmet, valoniteChestplate, valoniteLeggings, valoniteBoots, weedwoodBow, anglerToothArrow, poisonedAnglerToothArrow, octineArrow, basiliskArrow, explorerHat, pestle);
        ModCreativeTabs.plants.setTab(middleFruitSeeds);
    }

    private static void registerItems() {
        try {
            for (Field f : BLItemRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Item) registerItem((Item) obj);
                else if (obj instanceof Item[])
                    for (Item item : (Item[]) obj)
                        registerItem(item);
            }
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void registerItem(Item item) {
        ITEMS.add(item);
        String name = item.getUnlocalizedName();
        String[] strings = name.split("\\.");
        GameRegistry.registerItem(item, strings[strings.length - 1]);
    }

    private static void registerProperties() {
        GameRegistry.registerFuelHandler(new IFuelHandler() {
            @Override
            public int getBurnTime(ItemStack fuel) {
                return 0;
                //add fuels here
            }
        });
    }
}
