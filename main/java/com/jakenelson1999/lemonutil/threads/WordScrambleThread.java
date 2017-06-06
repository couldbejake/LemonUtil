package com.jakenelson1999.lemonutil.threads;

import com.jakenelson1999.lemonutil.LemonUtil;
import net.minecraft.util.text.TextFormatting;





public class WordScrambleThread
  extends Thread
{
  public void run()
  {
    double lastprint = 0.0D;
    
    for (;;)
    {
      if (LemonUtil.getLastepochscramble() != 0L)
      {
        long nextScramble = LemonUtil.getLastepochscramble() + 300L;
        long currentTime = System.currentTimeMillis() / 1000L;
        
        int difference = Integer.valueOf((int)(nextScramble - currentTime - 2L)).intValue();
        
        double minutes = Math.floor(difference / 60);
        
        if ((difference < 3) && (difference >= 0)) {
          LemonUtil.printToChat(LemonUtil.prefix + TextFormatting.GRAY + "Next Unscramble in " + difference + " seconds!");
        }
        else if (minutes != lastprint) {
          if (minutes == 1.0D) {
            LemonUtil.printToChat(LemonUtil.prefix + "Next Unscramble in " + minutes + " minute!");
          } else {
            LemonUtil.printToChat(LemonUtil.prefix + "Next Unscramble in " + minutes + " minutes!");
          }
          lastprint = minutes;
        }
      }
      

      try
      {
        Thread.sleep(1000L);
      }
      catch (InterruptedException localInterruptedException) {}
    }
  }
}


/* Location:              C:\Users\Jake\Downloads\LemonUtil 1.11.jar!\com\jakenelson1999\lemonutil\threads\WordScrambleThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */