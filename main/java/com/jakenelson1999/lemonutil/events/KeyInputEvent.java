package com.jakenelson1999.lemonutil.events;

import com.jakenelson1999.lemonutil.LemonUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyInputEvent
{
  @SubscribeEvent
  public void onKeyPress(InputEvent.KeyInputEvent event)
  {
	// A key to type /pv 1 (in order to access player vault 1), when pressed.
    if (Keyboard.isKeyDown(33)) {
      Minecraft.getMinecraft().thePlayer.sendChatMessage("/pv 1");
    }
    
    // A Key to toggle disconnecting after the left PVP Trigger is activated.
    if (Keyboard.isKeyDown(19)) {
      if (LemonUtil.disconnectOnLeavePvP) {
        LemonUtil.printToChat(LemonUtil.prefix + "Autodisconnect when out of PvP " + TextFormatting.RED + "Disabled");
        LemonUtil.disconnectOnLeavePvP = false;
      } else {
        LemonUtil.printToChat(LemonUtil.prefix + "Autodisconnect when out of PvP " + TextFormatting.GREEN + "Enabled");
        LemonUtil.disconnectOnLeavePvP = true;
      }
    }
  }
}

