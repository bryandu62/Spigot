package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandSetWorldSpawn
  extends CommandAbstract
{
  public String getCommand()
  {
    return "setworldspawn";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.setworldspawn.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    BlockPosition ☃;
    if (☃.length == 0)
    {
      ☃ = b(☃).getChunkCoordinates();
    }
    else
    {
      BlockPosition ☃;
      if ((☃.length == 3) && (☃.getWorld() != null)) {
        ☃ = a(☃, ☃, 0, true);
      } else {
        throw new ExceptionUsage("commands.setworldspawn.usage", new Object[0]);
      }
    }
    BlockPosition ☃;
    ☃.getWorld().B(☃);
    MinecraftServer.getServer().getPlayerList().sendAll(new PacketPlayOutSpawnPosition(☃));
    a(☃, this, "commands.setworldspawn.success", new Object[] { Integer.valueOf(☃.getX()), Integer.valueOf(☃.getY()), Integer.valueOf(☃.getZ()) });
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if ((☃.length > 0) && (☃.length <= 3)) {
      return a(☃, 0, ☃);
    }
    return null;
  }
}
