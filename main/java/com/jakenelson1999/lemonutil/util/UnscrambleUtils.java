package com.jakenelson1999.lemonutil.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;

/*
 * A series of functions to help find the unscramble text faster than anyone else.
 * 
 */


public class UnscrambleUtils
{
  public static ArrayList<String> getLines(String filename)
  {
    ArrayList lines = new ArrayList();
    File f = new File(filename);
    if (f.exists()) {
      BufferedReader br = null;FileReader fr = null;
      try {
        fr = new FileReader(filename);br = new BufferedReader(fr);
        br = new BufferedReader(new FileReader(filename));
        String sCurrentLine; while ((sCurrentLine = br.readLine()) != null) {
          lines.add(sCurrentLine);
        }
        



        return lines;
      }
      catch (IOException localIOException1) {}finally
      {
        try
        {
          if (br != null) br.close(); if (fr != null) fr.close();
        }
        catch (IOException localIOException3) {}
      }
    }
	return lines;
  }
  
  public static int countOccourances(String string, String letter) {
    return string.split(letter, -1).length - 1;
  }
  
  public static boolean compareLetterOccourances(String word1, String word2) {
    char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    for (char c : alphabet) {
      if (countOccourances(word1, c + "") != countOccourances(word2, c + "")) {
        return false;
      }
    }
    
    return true;
  }
  
  public static void saveFileFromUrlWithCommonsIO(String fileName, String fileUrl)
    throws MalformedURLException, IOException
  {
    FileUtils.copyURLToFile(new URL(fileUrl), new File(fileName));
  }
  
  public static String getWord(String scrambledword)
  {
    String words = "air,stone,granite,polished,diorite,andesite,grass,dirt,coarse,irongolem,podzol,cobblestone,wood,plank,oak,spruce,birch,jungle,acacia,dark,sapling,bedrock,water,flowing,still,lava,sand,red,gravel,ore,gold,iron,coal,leaves,sponge,wet,glass,lazuli,lapis,block,dispenser,sandstone,chiseled,smooth,note,bed,rail,powered,detector,piston,sticky,cobweb,shrub,dead,fern,head,wool,white,orange,magenta,blue,light,yellow,lime,pink,gray,cyan,purple,brown,green,black,dandelion,poppy,orchid,allium,bluet,azure,tulip,daisy,oxeye,mushroom,slab,double,wooden,brick,nether,quartz,bricks,tnt,bookshelf,moss,obsidian,torch,fire,spawner,monster,stairs,chest,wire,redstone,diamond,table,crafting,crops,wheat,farmland,furnace,burning,sign,standing,door,ladder,wall-mounted,lever,pressure,plate,glowing,(off),(on),button,snow,ice,cactus,clay,canes,sugar,jukebox,fence,pumpkin,netherrack,soul,glowstone,portal,o'lantern,jack,cake,repeater,stained,trapdoor,egg,mossy,cracked,cap,bars,pane,melon,stem,vines,gate,mycelium,pad,lily,wart,enchantment,stand,brewing,cauldron,end,frame,dragon,lamp,cocoa,emerald,ender,hook,tripwire,command,beacon,wall,pot,flower,carrots,potatoes,mob,anvil,trapped,(light),weighted,(heavy),comparator,sensor,daylight,hopper,pillar,activator,dropper,slime,barrier,prismarine,lantern,sea,bale,hay,carpet,hardened,of,packed,sunflower,lilac,tallgrass,large,bush,rose,peony,banner,free-standing,inverted,rod,plant,chorus,purpur,seeds,beetroot,path,gateway,repeating,chain,frosted,magma,bone,void,structure,shovel,pickaxe,axe,and,steel,flint,apple,bow,arrow,charcoal,ingot,sword,stick,bowl,stew,golden,string,feather,gunpowder,hoe,bread,helmet,leather,tunic,pants,boots,chainmail,chestplate,leggings,porkchop,raw,cooked,painting,enchanted,bucket,minecart,saddle,snowball,boat,milk,paper,book,slimeball,with,compass,fishing,clock,dust,fish,salmon,clownfish,pufferfish,sack,ink,beans,coco,dye,meal,cookie,map,shears,beef,steak,chicken,flesh,rotten,pearl,blaze,tear,ghast,nugget,potion,bottle,eye,spider,fermented,powder,cream,glistering,creeper,spawn,skeleton,zombie,pigman,enderman,cave,silverfish,cube,bat,witch,endermite,guardian,shulker,pig,sheep,cow,squid,wolf,mooshroom,apple,slime,slimeball,snowball,chest,steak,glass,grass,cow,pig,bed,door,carpet,planks,sapling,leaves,shears,bedrock,wool,sheep,bow,web,creeper,enchant,potion,craftingtable,rottenflesh,netherrack,netherbrick,endstone,redstone,stone,stonebrick,sandstone,sand,iron,ironbars,gold,diamond,emerald,book,sword,axe,cobblestone,brick,ice,fire,obsidian,granite,andesite,diorite,painting,sign,itemframe,slab,jukebox,noteblock,quartz,anvil,vines,button,potato,carrot,flower,beacon,cauldron,lilypad,mushroom,cake,sugarcane,prismarine,sealantern,lever,dropper,hopper,dirt,podzol,lapis,zombie,ocelot,horse,rabbit,polar,bear,villager,o',enchanting,charge,quill,written,item,carrot,potato,baked,poisonous,empty,(skeleton),(wither,skeleton),(zombie),(human),(creeper),(dragon),on,a,star,pie,rocket,firework,shard,crystals,foot,rabbit's,hide,armor,lead,tag,name,mutton,fruit,popped,soup,breath,dragon's,splash,spectral,tipped,lingering,shield,elytra,disc,13,cat,blocks,chest,chirp,far,mall,mellohi,stal,strad,ward,11,blazerod,log";
    
    List<String> ar = new ArrayList(Arrays.asList(words.split(",")));
    
    for (String string : ar) {
      if (compareLetterOccourances(string, scrambledword.toLowerCase())) {
        return string;
      }
    }
    return "";
  }
}

