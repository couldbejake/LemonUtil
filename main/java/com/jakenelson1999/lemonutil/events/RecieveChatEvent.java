package com.jakenelson1999.lemonutil.events;

import com.jakenelson1999.lemonutil.LemonUtil;
import com.jakenelson1999.lemonutil.util.UnscrambleUtils;
import io.netty.util.internal.ThreadLocalRandom;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RecieveChatEvent
{
  @SubscribeEvent
  public void onChatMessage(ClientChatReceivedEvent event)
  {
	// Get the unformatted chat message.
    String message = StringUtils.stripControlCodes(event.getMessage().getFormattedText());
    
    if (message.contains(LemonUtil.getUnscrambleTrigger()))
    {
      LemonUtil.setLastepochscramble(System.currentTimeMillis() / 1000L);
      
      // Get the 8th word of the sentence (Specifically for the server LemonCloud).
      final String scrambleword = UnscrambleUtils.getWord(message.split(" ")[8]);
      
      if (scrambleword != "") {
        LemonUtil.printToChat(LemonUtil.prefix + "Answering Scramble Question!");
        
        int wordlength = scrambleword.length();
        
        // Randomize time until message is sent (in order to hide the fact it's a robot answering).
        long lowerbound = Math.round(0.14D * wordlength - 0.62D);
        long upperbound = Math.round(0.29D * wordlength - 0.62D);
        
        final int time = MathHelper.floor_double(1000L * ThreadLocalRandom.current().nextLong(lowerbound, upperbound + 1L));
        
        ExecutorService executorService = java.util.concurrent.Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable()
        {
          public void run() {
            try {
              Thread.sleep(time);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            
            Minecraft.getMinecraft().thePlayer.sendChatMessage(scrambleword);
          }
        });
        executorService.shutdown();
      }
    }
    

    if (!LemonUtil.messageSentByPlayer(message)) {
      if (message.contains(LemonUtil.getEnderDragonTrigger())) {
    	// Notify the user that a dragon drop has started.
        LemonUtil.notify("DragonD", "Dragon Drop!", "A Dragon drop has just started @ the end.");
      }
      // Check if a TPA request has been recieved.
      if (message.contains(LemonUtil.getTpaRequestTrigger())) {
        String user = message.split(" ")[0];
        if (LemonUtil.getAcceptRequests() == 2)
        {
          Minecraft.getMinecraft().thePlayer.sendChatMessage("/tpaccept");
        }
        
        if (LemonUtil.getAcceptRequests() == 1) {
          FileInputStream fstream = null;
          // Check whether the user who has attempted to TPA is on the friendslist file.
          try {
            fstream = new FileInputStream("friendslist.txt");
          } catch (FileNotFoundException e1) { e1.printStackTrace();
            try {
              File file = new File("friendslist.txt");
              file.createNewFile();
            } catch (IOException e2) {
              e2.printStackTrace();
            }
          }
          
          BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
          try
          {
            String strLine;
            while ((strLine = br.readLine()) != null) {
              System.out.println(strLine);
              if (strLine.equalsIgnoreCase(user)) {
            	// Alert the user that an auto TP request has been accepted.
                LemonUtil.printToChat(TextFormatting.GOLD + "[Lemon" + TextFormatting.YELLOW + "Util" + TextFormatting.GOLD + "] Auto-accepting TP Request from " + user);
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/tpaccept");
              }
            }
            br.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
          

          Minecraft.getMinecraft().thePlayer.sendChatMessage("/tpaccept");
        }
      }
      
      if (message.contains(LemonUtil.getTpaRequestAcceptTrigger())) {
    	  // Stop when someone accepts the TPA request.
        LemonUtil.printToChat(TextFormatting.GOLD + "[Lemon" + TextFormatting.YELLOW + "Util" + TextFormatting.GOLD + "] Request accepted pausing TPA requests.");
        LemonUtil.notify("TPA requests", "[Request Accepted]", message);
        LemonUtil.sendrequests = false;
      }
      

      if ((message.contains(LemonUtil.getLeavePvPTrigger())) && (LemonUtil.disconnectOnLeavePvP)) {
    	  // If disconnect on leave pvp is enabled, the client will leave the server and connect to a null world.
        LemonUtil.disconnectOnLeavePvP = false;
        Minecraft.getMinecraft().theWorld.sendQuittingDisconnectingPacket();
        Minecraft.getMinecraft().loadWorld((WorldClient)null);

      }
      

    }
    else if ((message.contains(":")) && 
      (message.toLowerCase().split(":")[1].contains(LemonUtil.playername.toLowerCase())) && 
      (!Minecraft.getMinecraft().inGameHasFocus))
    {
      String mentionedby = null;
      
      if (message.contains("[Lvl.")) {
        String tempmention = message.split(":")[0];
        if (tempmention.contains(" ")) {
          mentionedby = tempmention.split(" ")[(tempmention.split(" ").length - 1)];
        } else {
          mentionedby = tempmention;
        }
      }
      else {
        mentionedby = message.split("]")[0].replace("[", "");
      }
      

      // If username mentioned notify the user.

      LemonUtil.notify(LemonUtil.playername, "You were mentioned by " + mentionedby, message.split(":")[1]);
      LemonUtil.printToChat(TextFormatting.GRAY + "[LC] You were mentioned by " + TextFormatting.AQUA + mentionedby);
    }
    
}
}

