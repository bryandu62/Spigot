package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandParticle
  extends CommandAbstract
{
  public String getCommand()
  {
    return "particle";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.particle.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 8) {
      throw new ExceptionUsage("commands.particle.usage", new Object[0]);
    }
    boolean ☃ = false;
    EnumParticle ☃ = null;
    for (EnumParticle ☃ : EnumParticle.values()) {
      if (☃.f())
      {
        if (☃[0].startsWith(☃.b()))
        {
          ☃ = true;
          ☃ = ☃;
          break;
        }
      }
      else if (☃[0].equals(☃.b()))
      {
        ☃ = true;
        ☃ = ☃;
        break;
      }
    }
    if (!☃) {
      throw new CommandException("commands.particle.notFound", new Object[] { ☃[0] });
    }
    String ☃ = ☃[0];
    Vec3D ☃ = ☃.d();
    double ☃ = (float)b(☃.a, ☃[1], true);
    double ☃ = (float)b(☃.b, ☃[2], true);
    double ☃ = (float)b(☃.c, ☃[3], true);
    double ☃ = (float)c(☃[4]);
    double ☃ = (float)c(☃[5]);
    double ☃ = (float)c(☃[6]);
    double ☃ = (float)c(☃[7]);
    
    int ☃ = 0;
    if (☃.length > 8) {
      ☃ = a(☃[8], 0);
    }
    boolean ☃ = false;
    if ((☃.length > 9) && ("force".equals(☃[9]))) {
      ☃ = true;
    }
    World ☃ = ☃.getWorld();
    if ((☃ instanceof WorldServer))
    {
      WorldServer ☃ = (WorldServer)☃;
      int[] ☃ = new int[☃.d()];
      if (☃.f())
      {
        String[] ☃ = ☃[0].split("_", 3);
        for (int ☃ = 1; ☃ < ☃.length; ☃++) {
          try
          {
            ☃[(☃ - 1)] = Integer.parseInt(☃[☃]);
          }
          catch (NumberFormatException ☃)
          {
            throw new CommandException("commands.particle.notFound", new Object[] { ☃[0] });
          }
        }
      }
      ☃.a(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      a(☃, this, "commands.particle.success", new Object[] { ☃, Integer.valueOf(Math.max(☃, 1)) });
    }
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, EnumParticle.a());
    }
    if ((☃.length > 1) && (☃.length <= 4)) {
      return a(☃, 1, ☃);
    }
    if (☃.length == 10) {
      return a(☃, new String[] { "normal", "force" });
    }
    return null;
  }
}
