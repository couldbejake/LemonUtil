package com.jakenelson1999.lemonutil.threads;

import com.jakenelson1999.lemonutil.LemonUtil;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class TPARequestThread
  extends Thread
{
  public void run()
  {
    try
    {
      for (;;)
      {
        if ((LemonUtil.sendrequests) && (LemonUtil.tparequestqueue.size() != 0)) {
          String player = (String)LemonUtil.tparequestqueue.get(0);
          
          for (String cmd : LemonUtil.repeatcommands)
          {
            Minecraft.getMinecraft().thePlayer.sendChatMessage(cmd.replace("%player%", player));
          }
          
          LemonUtil.tparequestqueue.remove(0);
        }
        try {
          Thread.sleep(LemonUtil.commanddelay);
        } catch (InterruptedException localInterruptedException) {}
      } } catch (NullPointerException npe) { LemonUtil.sendrequests = false;
    }
  }
}
