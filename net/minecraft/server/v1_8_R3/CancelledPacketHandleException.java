package net.minecraft.server.v1_8_R3;

public final class CancelledPacketHandleException
  extends RuntimeException
{
  public static final CancelledPacketHandleException INSTANCE = new CancelledPacketHandleException();
  
  private CancelledPacketHandleException()
  {
    setStackTrace(new StackTraceElement[0]);
  }
  
  public synchronized Throwable fillInStackTrace()
  {
    setStackTrace(new StackTraceElement[0]);
    return this;
  }
}
