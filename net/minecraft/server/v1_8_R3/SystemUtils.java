package net.minecraft.server.v1_8_R3;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.apache.logging.log4j.Logger;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Processor;

public class SystemUtils
{
  private static String a;
  
  public static String a()
  {
    return a == null ? "<unknown>" : a;
  }
  
  public static <V> V a(FutureTask<V> futuretask, Logger logger)
  {
    try
    {
      futuretask.run();
      return (V)futuretask.get();
    }
    catch (ExecutionException executionexception)
    {
      logger.fatal("Error executing task", executionexception);
    }
    catch (InterruptedException interruptedexception)
    {
      logger.fatal("Error executing task", interruptedexception);
    }
    return null;
  }
  
  static
  {
    try
    {
      Processor[] aprocessor = new SystemInfo().getHardware().getProcessors();
      
      a = String.format("%dx %s", new Object[] { Integer.valueOf(aprocessor.length), aprocessor[0] }).replaceAll("\\s+", " ");
    }
    catch (Throwable localThrowable) {}
  }
}
