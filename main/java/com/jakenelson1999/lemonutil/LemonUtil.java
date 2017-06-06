package com.jakenelson1999.lemonutil;

import com.google.common.collect.Lists;
import com.jakenelson1999.lemonutil.commands.UtilCommand;
import com.jakenelson1999.lemonutil.events.KeyInputEvent;
import com.jakenelson1999.lemonutil.events.RecieveChatEvent;
import com.jakenelson1999.lemonutil.threads.ArmourGetValuesThread;
import com.jakenelson1999.lemonutil.threads.TPARequestThread;
import com.jakenelson1999.lemonutil.threads.WordScrambleThread;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

@Mod(modid = "lemonutil", version = "1.2", clientSideOnly = true, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.11]")
public class LemonUtil {
	
	/*
	 *  Set all triggers. Checked for when messages are recieved.
	 * 
	 */
	
	private static String enderDragonTrigger = "The dragon has awoken! It can be seen dropping supply crates around the End dimension!";
	private static String tpaRequestTrigger = "would like to teleport to you!";
	private static String executeTrigger = "Who Is at Fl rightnow";
	private static String tpaRequestAcceptTrigger = "accepted your teleport request";
	private static String leavePvPTrigger = "You have left combat. You may now safely logout";

	private static String unscrambleTrigger = "a9d99sifiasifiasfi";

	/*
	 * Set the Mod Prefix and name.
	 * 
	 */
	
	public static String prefix = TextFormatting.DARK_AQUA + "[" + TextFormatting.AQUA + "LC" + TextFormatting.DARK_AQUA
			+ "] ";
	private static String ArmourMessage = TextFormatting.GOLD + "Welcome to " + TextFormatting.YELLOW + "Lemon"
			+ TextFormatting.GOLD + "Util";
	public static String modname = "lemonutil";

	private static long lastepoch = System.currentTimeMillis() / 1000L;
	private static long lastepochscramble = 0L;

	// Set the comment delay
	
	public static int commanddelay = 1200;

	// Set the amount of requests that have been accepted.
	
	private static int acceptRequests = 0;
	static double currentversion = 1.2D;

	/*
	 *  Settings to allow the user to automatically disconnect on leaving PvP and another to decide whether to auto send requests to other users.
	 * 
	 */
	
	public static boolean disconnectOnLeavePvP = false;
	public static boolean sendrequests = false;

	public static ArrayList<String> repeatcommands = new ArrayList() {};

	public static ArrayList<String> cmdalias = new ArrayList() {};

	public static ArrayList<String> tparequestqueue = new ArrayList();

	static String welcomemessage = getArmourMessage();

	
	/**
	 * Gets the latest version number from pastebin.
	 *
	 * @return the latest version number
	 */
	
	public static Double getLatestVersion() {
		try {
			URL url = new URL("http://pastebin.com/raw/FFDMbd6t");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			if ((line = in.readLine()) != null) {
				System.out.println(line);
				return Double.valueOf(line);
			}
			in.close();
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("I/O Error: " + e.getMessage());
		}
		return Double.valueOf(currentversion);
	}

	
	
	/**
	 * Gets the latest download link from pastebin.
	 *
	 * @return the latest download link
	 */
	
	
	public static String getLatestDownloadLink() {
		try {
			URL url = new URL("http://pastebin.com/raw/wvVd8hu7");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			if ((line = in.readLine()) != null) {
				System.out.println(line);
				return line;
			}
			in.close();
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("I/O Error: " + e.getMessage());
		}
		return "[Error: Link Not Found!]";
	}

	static double latestversion = getLatestVersion().doubleValue();
	static String latestlink = getLatestDownloadLink();
	public static String playername = "";

	/**
	 * Prints a string to the chat.
	 *
	 * @param s the string to print to chat
	 */
	
	public static void printToChat(String s) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString(TextFormatting.AQUA + s));
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		
		// Start the threads for armour, TPA requests and the WordScramble answerer.
		
		new ArmourGetValuesThread().start();
		new TPARequestThread().start();
		new WordScrambleThread().start();

		FMLCommonHandler.instance().bus().register(this);

		MinecraftForge.EVENT_BUS.register(new RecieveChatEvent());
		MinecraftForge.EVENT_BUS.register(new KeyInputEvent());
		try {
			ClientCommandHandler.instance.registerCommand(new UtilCommand());
		} catch (Exception e) {
			System.out.println("error");
		}
		try {
			File file = new File("friendslist.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	@SubscribeEvent
	public void onDisconnectFromServerEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
		
		// Disable all settings on leaving a server.
		
		sendrequests = false;
		disconnectOnLeavePvP = false;
		lastepochscramble = 0L;
		tparequestqueue.removeAll(tparequestqueue);
	}

	@SubscribeEvent
	public void onConnectedToServerEvent(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new Runnable() {
			public void run() {
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				String unformattedname = StringUtils
						.stripControlCodes(Minecraft.getMinecraft().thePlayer.getDisplayNameString());

				/*
				 *  Show a message telling the user if theres an update and mod's command on joining a server.
				 * 
				 */
				
				if (unformattedname.contains(" ")) {
					LemonUtil.playername = unformattedname.split(" ")[1];
				} else {
					LemonUtil.playername = unformattedname;
				}

				if (LemonUtil.latestversion > LemonUtil.currentversion) {
					LemonUtil.printToChat(LemonUtil.prefix + TextFormatting.YELLOW + "There is a new LemonUtil "
							+ TextFormatting.AQUA + "Mod avaliable @ " + TextFormatting.GRAY + LemonUtil.latestlink);
				}
				LemonUtil.printToChat(LemonUtil.prefix + TextFormatting.GRAY + "Type /lemonutil to access the "
						+ TextFormatting.YELLOW + "LemonUtil" + TextFormatting.GRAY + " mod features.");
			}

		});
		executorService.shutdown();
	}

	

	/**
	 * Gets armo(u)r status.
	 *
	 * @return a formatted string for armo(u)r status.
	 */
	
	public static String getArmourStatus() {
		try {
			// Get the armour inventory.
			Iterable<ItemStack> armour = Minecraft.getMinecraft().thePlayer.getArmorInventoryList();
			List<ItemStack> myList = Lists.newArrayList(armour);
			String sb = TextFormatting.AQUA + "";
			boolean itemAdded = false;
			for (ItemStack a : myList) {
				if (a.getMaxDamage() != 0) {
					// Get the percentage damage.
					float damaged = a.getMetadata() / a.getMaxDamage();
					// If the damage is above 40%
					if (damaged > 0.4D) {
						// Add the armour to the armour status string.
						sb = sb + TextFormatting.YELLOW + a.getDisplayName().split(" ")[1] + " " + TextFormatting.GOLD
								+ String.valueOf(Math.ceil(100.0F - damaged * 100.0F)) + "% ";

						itemAdded = true;
					}
				}
			}

			if (itemAdded) {
				return sb;
			}

			return "";
		} catch (NullPointerException localNullPointerException) {
		}

		return "";
	}
	
	/**
	 * An multi-OS notification system.
	 *
	 * @param  title The title of the message.
	 * @param  subtitle The subtitle for the message.
	 * @param  message The message content.
	 */
	public static void notify(String title, String subtitle, String message) {
		if (getOs() == "mac") {
			try {
				// Run osascript to notify the user.
				Runtime.getRuntime().exec(new String[] { "osascript", "-e", "display notification \"" + message
						+ "\" with title \"" + title + "\" subtitle \"" + subtitle + "\" sound name \"Funk\"" });
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (getOs() == "windows") {
			try {
				try {
					// Create a VBS script to notify the user.
					PrintWriter writer = new PrintWriter("messageservice.vbs", "Cp1252");
					writer.println("x=msgbox(\"" + subtitle + " | " + message + "\", 0, \"" + title + "\")");
					writer.close();
				} catch (IOException localIOException1) {
				}

				Runtime.getRuntime().exec(new String[] { "wscript.exe", "messageservice.vbs" });
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A simple function to check if a message is sent by a player.
	 *
	 */
	
	public static boolean messageSentByPlayer(String msg) {
		return msg.contains("[");
	}

	/**
	 * A function to get the current OS.
	 *
	 */
	public static String getOs() {
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			return "mac";
		}
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			return "windows";
		}
		return "";
	}

	/**
	 * A function to print a help menu to the chat.
	 *
	 */
	public static void showHelp() {
		printToChat(" ");
		printToChat(TextFormatting.AQUA + "- - - - " + TextFormatting.GOLD + "Lemon" + TextFormatting.YELLOW + "Util "
				+ currentversion + TextFormatting.AQUA + "- - - -");
		printToChat(
				TextFormatting.GOLD + "/lemonutil armor - " + TextFormatting.YELLOW + "Shows test armour durability");
		printToChat(" ");
		printToChat(TextFormatting.GOLD + "/lemonutil autotpaccept all - " + TextFormatting.YELLOW
				+ "Accepts TPs from everyone.");
		printToChat(" ");
		printToChat(TextFormatting.GOLD + "/lemonutil autotpaccept friends - " + TextFormatting.YELLOW
				+ "Accepts TPs from friends.");
		printToChat(TextFormatting.GRAY + "Friends list stored in %appdata%/.minecraft/friends.txt");
		printToChat(" ");
		printToChat(TextFormatting.GOLD + "/lemonutil autotpaccept off - " + TextFormatting.YELLOW
				+ "Denies all AutoTp requests.");
	}

	public static String getExecuteTrigger() {
		return executeTrigger;
	}

	public static void setExecuteTrigger(String t) {
		executeTrigger = t;
	}

	public static long getLastepoch() {
		return lastepoch;
	}

	public static void setLastepoch(long le) {
		lastepoch = le;
	}

	public static String getArmourMessage() {
		return ArmourMessage;
	}

	public static void setArmourMessage(String armourMessage) {
		ArmourMessage = armourMessage;
	}

	public static int getAcceptRequests() {
		return acceptRequests;
	}

	public static void setAcceptRequests(int ar) {
		acceptRequests = ar;
	}

	public static String getTpaRequestTrigger() {
		return tpaRequestTrigger;
	}

	public void setTpaRequestTrigger(String tt) {
		tpaRequestTrigger = tt;
	}

	public static String getEnderDragonTrigger() {
		return enderDragonTrigger;
	}

	public void setEnderDragonTrigger(String et) {
		enderDragonTrigger = et;
	}

	public static String getTpaRequestAcceptTrigger() {
		return tpaRequestAcceptTrigger;
	}

	public static void setTpaRequestAcceptTrigger(String tat) {
		tpaRequestAcceptTrigger = tat;
	}

	public static String getLeavePvPTrigger() {
		return leavePvPTrigger;
	}

	public static void setLeavePvPTrigger(String lpt) {
		leavePvPTrigger = lpt;
	}

	public static long getLastepochscramble() {
		return lastepochscramble;
	}

	public static void setLastepochscramble(long les) {
		lastepochscramble = les;
	}

	public static String getUnscrambleTrigger() {
		return unscrambleTrigger;
	}


}
