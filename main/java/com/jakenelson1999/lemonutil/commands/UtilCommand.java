package com.jakenelson1999.lemonutil.commands;

import com.jakenelson1999.lemonutil.LemonUtil;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class UtilCommand implements ICommand {
	
	public int compareTo(ICommand o) {
		return 0;
	}

	public boolean checkPermission(MinecraftServer arg0, ICommandSender arg1) {
		return true;
	}

	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if ((args.length == 1) || (args.length == 2)) {
			if ((args[0].toLowerCase().contains("armour")) || (args[0].toLowerCase().contains("armor"))) {
				String status = LemonUtil.getArmourStatus();
				if (status == "")
					LemonUtil.printToChat("No damaged armour to display!");
				else {
					LemonUtil.printToChat(LemonUtil.getArmourStatus());
				}
			}
			if ((args[0].toLowerCase().contains("autotpaccept")) && (args[1].toLowerCase().contains("all"))) {
				LemonUtil.printToChat(
						"> Accepting TP Requests from " + TextFormatting.WHITE + "" + TextFormatting.BOLD + "All");
				LemonUtil.setAcceptRequests(2);
			}
			if ((args[0].toLowerCase().contains("autotpaccept")) && (args[1].toLowerCase().contains("friends"))) {
				LemonUtil.printToChat(
						"> Accepting TP Requests from " + TextFormatting.WHITE + "" + TextFormatting.BOLD + "Friends");
				LemonUtil.setAcceptRequests(1);
			}

			if ((args[0].toLowerCase().contains("autotpaccept")) && (args[1].toLowerCase().contains("off"))) {
				LemonUtil.printToChat(
						"> Accepting TP Requests from " + TextFormatting.WHITE + "" + TextFormatting.BOLD + "No-one");
				LemonUtil.setAcceptRequests(1);
			}
			if (args[0].toLowerCase().contains("whoisonline")) {
				Minecraft.getMinecraft().thePlayer.sendChatMessage("whoisonline");
			}

			if (args[0].toLowerCase().contains("tpall")) {

				if (LemonUtil.tparequestqueue.size() == 0) {
					NetHandlerPlayClient nethandlerpc = Minecraft.getMinecraft().thePlayer.connection;
					Collection<NetworkPlayerInfo> netplayerinfo = nethandlerpc.getPlayerInfoMap();

					for (NetworkPlayerInfo networkplayer : netplayerinfo) {
						String playername = networkplayer.getGameProfile().getName();
						LemonUtil.tparequestqueue.add(playername);
					}
				}

				LemonUtil.sendrequests = true;
			}

		} else {
			LemonUtil.showHelp();
		}
	}

	public List<String> getCommandAliases() {
		return LemonUtil.cmdalias;
	}

	public String getCommandName() {
		return "lemonutil";
	}

	public String getCommandUsage(ICommandSender arg0) {
		return null;
	}

	public List<String> getTabCompletionOptions(MinecraftServer arg0, ICommandSender arg1, String[] arg2,
			BlockPos arg3) {
		return null;
	}

	public boolean isUsernameIndex(String[] arg0, int arg1) {
		return false;
	}

}
