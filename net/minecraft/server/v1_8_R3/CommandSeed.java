package net.minecraft.server.v1_8_R3;

public class CommandSeed
  extends CommandAbstract
{
  public boolean canUse(ICommandListener ☃)
  {
    return (MinecraftServer.getServer().T()) || (super.canUse(☃));
  }
  
  public String getCommand()
  {
    return "seed";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.seed.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    World ☃ = (☃ instanceof EntityHuman) ? ((EntityHuman)☃).world : MinecraftServer.getServer().getWorldServer(0);
    ☃.sendMessage(new ChatMessage("commands.seed.success", new Object[] { Long.valueOf(☃.getSeed()) }));
  }
}
