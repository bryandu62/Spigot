package net.minecraft.server.v1_8_R3;

public class PlayerConnectionUtils
{
  public static <T extends PacketListener> void ensureMainThread(Packet<T> ☃, final T ☃, IAsyncTaskHandler ☃)
    throws CancelledPacketHandleException
  {
    if (!☃.isMainThread())
    {
      ☃.postToMainThread(new Runnable()
      {
        public void run()
        {
          this.a.a(☃);
        }
      });
      throw CancelledPacketHandleException.INSTANCE;
    }
  }
}
