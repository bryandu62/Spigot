package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandSpawnpoint
  extends CommandAbstract
{
  public String getCommand()
  {
    return "spawnpoint";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.spawnpoint.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if ((☃.length > 1) && (☃.length < 4)) {
      throw new ExceptionUsage("commands.spawnpoint.usage", new Object[0]);
    }
    EntityPlayer ☃ = ☃.length > 0 ? a(☃, ☃[0]) : b(☃);
    BlockPosition ☃ = ☃.length > 3 ? a(☃, ☃, 1, true) : ☃.getChunkCoordinates();
    if (☃.world != null)
    {
      ☃.setRespawnPosition(☃, true);
      a(☃, this, "commands.spawnpoint.success", new Object[] { ☃.getName(), Integer.valueOf(☃.getX()), Integer.valueOf(☃.getY()), Integer.valueOf(☃.getZ()) });
    }
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, MinecraftServer.getServer().getPlayers());
    }
    if ((☃.length > 1) && (☃.length <= 4)) {
      return a(☃, 1, ☃);
    }
    return null;
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    return ☃ == 0;
  }
}
