package com.jakenelson1999.lemonutil.threads;

import com.jakenelson1999.lemonutil.LemonUtil;
import java.io.PrintStream;
import scala.Console;

public class ArmourGetValuesThread extends Thread
{
  public void run()
  {
    System.out.println("Armour values has started");
    for (;;) {
      LemonUtil.setArmourMessage(LemonUtil.getArmourStatus());
      try {
        Thread.sleep(3000L); } catch (InterruptedException e) {} Console.out().println("ArmourGetValues> Not In-Game.");
    }
  }
}


/* Location:              C:\Users\Jake\Downloads\LemonUtil 1.11.jar!\com\jakenelson1999\lemonutil\threads\ArmourGetValuesThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */