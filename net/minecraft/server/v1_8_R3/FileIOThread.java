package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

public class FileIOThread
  implements Runnable
{
  private static final FileIOThread a = new FileIOThread();
  private List<IAsyncChunkSaver> b = Collections.synchronizedList(Lists.newArrayList());
  private volatile long c;
  private volatile long d;
  private volatile boolean e;
  
  private FileIOThread()
  {
    Thread ☃ = new Thread(this, "File IO Thread");
    ☃.setPriority(1);
    ☃.start();
  }
  
  public static FileIOThread a()
  {
    return a;
  }
  
  public void run()
  {
    for (;;)
    {
      c();
    }
  }
  
  private void c()
  {
    for (int ☃ = 0; ☃ < this.b.size(); ☃++)
    {
      IAsyncChunkSaver ☃ = (IAsyncChunkSaver)this.b.get(☃);
      boolean ☃ = ☃.c();
      if (!☃)
      {
        this.b.remove(☃--);
        this.d += 1L;
      }
      try
      {
        Thread.sleep(this.e ? 0L : 10L);
      }
      catch (InterruptedException ☃)
      {
        ☃.printStackTrace();
      }
    }
    if (this.b.isEmpty()) {
      try
      {
        Thread.sleep(25L);
      }
      catch (InterruptedException ☃)
      {
        ☃.printStackTrace();
      }
    }
  }
  
  public void a(IAsyncChunkSaver ☃)
  {
    if (this.b.contains(☃)) {
      return;
    }
    this.c += 1L;
    this.b.add(☃);
  }
  
  public void b()
    throws InterruptedException
  {
    this.e = true;
    while (this.c != this.d) {
      Thread.sleep(10L);
    }
    this.e = false;
  }
}
