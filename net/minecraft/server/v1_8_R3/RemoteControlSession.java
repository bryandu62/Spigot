package net.minecraft.server.v1_8_R3;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RemoteControlSession
  extends RemoteConnectionThread
{
  private static final Logger h = ;
  private boolean i;
  private Socket j;
  private byte[] k = new byte['ִ'];
  private String l;
  
  RemoteControlSession(IMinecraftServer ☃, Socket ☃)
  {
    super(☃, "RCON Client");
    this.j = ☃;
    try
    {
      this.j.setSoTimeout(0);
    }
    catch (Exception ☃)
    {
      this.a = false;
    }
    this.l = ☃.a("rcon.password", "");
    b("Rcon connection from: " + ☃.getInetAddress());
  }
  
  public void run()
  {
    try
    {
      while (this.a)
      {
        BufferedInputStream ☃ = new BufferedInputStream(this.j.getInputStream());
        int ☃ = ☃.read(this.k, 0, 1460);
        if (10 > ☃) {
          return;
        }
        int ☃ = 0;
        int ☃ = StatusChallengeUtils.b(this.k, 0, ☃);
        if (☃ != ☃ - 4) {
          return;
        }
        ☃ += 4;
        int ☃ = StatusChallengeUtils.b(this.k, ☃, ☃);
        ☃ += 4;
        
        int ☃ = StatusChallengeUtils.b(this.k, ☃);
        ☃ += 4;
        switch (☃)
        {
        case 3: 
          String ☃ = StatusChallengeUtils.a(this.k, ☃, ☃);
          ☃ += ☃.length();
          if ((0 != ☃.length()) && (☃.equals(this.l)))
          {
            this.i = true;
            a(☃, 2, "");
          }
          else
          {
            this.i = false;
            f();
          }
          break;
        case 2: 
          if (this.i)
          {
            String ☃ = StatusChallengeUtils.a(this.k, ☃, ☃);
            try
            {
              a(☃, this.b.executeRemoteCommand(☃));
            }
            catch (Exception ☃)
            {
              a(☃, "Error executing: " + ☃ + " (" + ☃.getMessage() + ")");
            }
          }
          else
          {
            f();
          }
          break;
        default: 
          a(☃, String.format("Unknown request %s", new Object[] { Integer.toHexString(☃) }));
        }
      }
    }
    catch (SocketTimeoutException localSocketTimeoutException) {}catch (IOException localIOException) {}catch (Exception ☃)
    {
      h.error("Exception whilst parsing RCON input", ☃);
    }
    finally
    {
      g();
    }
  }
  
  private void a(int ☃, int ☃, String ☃)
    throws IOException
  {
    ByteArrayOutputStream ☃ = new ByteArrayOutputStream(1248);
    DataOutputStream ☃ = new DataOutputStream(☃);
    byte[] ☃ = ☃.getBytes("UTF-8");
    ☃.writeInt(Integer.reverseBytes(☃.length + 10));
    ☃.writeInt(Integer.reverseBytes(☃));
    ☃.writeInt(Integer.reverseBytes(☃));
    ☃.write(☃);
    ☃.write(0);
    ☃.write(0);
    this.j.getOutputStream().write(☃.toByteArray());
  }
  
  private void f()
    throws IOException
  {
    a(-1, 2, "");
  }
  
  private void a(int ☃, String ☃)
    throws IOException
  {
    int ☃ = ☃.length();
    for (;;)
    {
      int ☃ = 4096 <= ☃ ? 4096 : ☃;
      a(☃, 0, ☃.substring(0, ☃));
      ☃ = ☃.substring(☃);
      ☃ = ☃.length();
      if (0 == ☃) {
        break;
      }
    }
  }
  
  private void g()
  {
    if (null == this.j) {
      return;
    }
    try
    {
      this.j.close();
    }
    catch (IOException ☃)
    {
      c("IO: " + ☃.getMessage());
    }
    this.j = null;
  }
}
