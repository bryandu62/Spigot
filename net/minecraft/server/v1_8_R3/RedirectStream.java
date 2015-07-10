package net.minecraft.server.v1_8_R3;

import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RedirectStream
  extends PrintStream
{
  private static final Logger a = ;
  private final String b;
  
  public RedirectStream(String ☃, OutputStream ☃)
  {
    super(☃);
    this.b = ☃;
  }
  
  public void println(String ☃)
  {
    a(☃);
  }
  
  public void println(Object ☃)
  {
    a(String.valueOf(☃));
  }
  
  private void a(String ☃)
  {
    StackTraceElement[] ☃ = Thread.currentThread().getStackTrace();
    StackTraceElement ☃ = ☃[Math.min(3, ☃.length)];
    a.info("[{}]@.({}:{}): {}", new Object[] { this.b, ☃.getFileName(), Integer.valueOf(☃.getLineNumber()), ☃ });
  }
}
