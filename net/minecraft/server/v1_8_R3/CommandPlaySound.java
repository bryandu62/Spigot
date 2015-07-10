package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandPlaySound
  extends CommandAbstract
{
  public String getCommand()
  {
    return "playsound";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.playsound.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 2) {
      throw new ExceptionUsage(getUsage(☃), new Object[0]);
    }
    int ☃ = 0;
    String ☃ = ☃[(☃++)];
    EntityPlayer ☃ = a(☃, ☃[(☃++)]);
    
    Vec3D ☃ = ☃.d();
    
    double ☃ = ☃.a;
    if (☃.length > ☃) {
      ☃ = b(☃, ☃[(☃++)], true);
    }
    double ☃ = ☃.b;
    if (☃.length > ☃) {
      ☃ = b(☃, ☃[(☃++)], 0, 0, false);
    }
    double ☃ = ☃.c;
    if (☃.length > ☃) {
      ☃ = b(☃, ☃[(☃++)], true);
    }
    double ☃ = 1.0D;
    if (☃.length > ☃) {
      ☃ = a(☃[(☃++)], 0.0D, 3.4028234663852886E38D);
    }
    double ☃ = 1.0D;
    if (☃.length > ☃) {
      ☃ = a(☃[(☃++)], 0.0D, 2.0D);
    }
    double ☃ = 0.0D;
    if (☃.length > ☃) {
      ☃ = a(☃[☃], 0.0D, 1.0D);
    }
    double ☃ = ☃ > 1.0D ? ☃ * 16.0D : 16.0D;
    double ☃ = ☃.f(☃, ☃, ☃);
    if (☃ > ☃)
    {
      if (☃ <= 0.0D) {
        throw new CommandException("commands.playsound.playerTooFar", new Object[] { ☃.getName() });
      }
      double ☃ = ☃ - ☃.locX;
      double ☃ = ☃ - ☃.locY;
      double ☃ = ☃ - ☃.locZ;
      double ☃ = Math.sqrt(☃ * ☃ + ☃ * ☃ + ☃ * ☃);
      if (☃ > 0.0D)
      {
        ☃ = ☃.locX + ☃ / ☃ * 2.0D;
        ☃ = ☃.locY + ☃ / ☃ * 2.0D;
        ☃ = ☃.locZ + ☃ / ☃ * 2.0D;
      }
      ☃ = ☃;
    }
    ☃.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect(☃, ☃, ☃, ☃, (float)☃, (float)☃));
    a(☃, this, "commands.playsound.success", new Object[] { ☃, ☃.getName() });
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 2) {
      return a(☃, MinecraftServer.getServer().getPlayers());
    }
    if ((☃.length > 2) && (☃.length <= 5)) {
      return a(☃, 2, ☃);
    }
    return null;
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    return ☃ == 1;
  }
}
