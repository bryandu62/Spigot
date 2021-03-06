package net.minecraft.server.v1_8_R3;

public class RemoteControlCommandListener
  implements ICommandListener
{
  private static final RemoteControlCommandListener instance = new RemoteControlCommandListener();
  private StringBuffer b = new StringBuffer();
  
  public static RemoteControlCommandListener getInstance()
  {
    return instance;
  }
  
  public void i()
  {
    this.b.setLength(0);
  }
  
  public String j()
  {
    return this.b.toString();
  }
  
  public String getName()
  {
    return "Rcon";
  }
  
  public IChatBaseComponent getScoreboardDisplayName()
  {
    return new ChatComponentText(getName());
  }
  
  public void sendMessage(String message)
  {
    this.b.append(message);
  }
  
  public void sendMessage(IChatBaseComponent ichatbasecomponent)
  {
    this.b.append(ichatbasecomponent.c());
  }
  
  public boolean a(int i, String s)
  {
    return true;
  }
  
  public BlockPosition getChunkCoordinates()
  {
    return new BlockPosition(0, 0, 0);
  }
  
  public Vec3D d()
  {
    return new Vec3D(0.0D, 0.0D, 0.0D);
  }
  
  public World getWorld()
  {
    return MinecraftServer.getServer().getWorld();
  }
  
  public Entity f()
  {
    return null;
  }
  
  public boolean getSendCommandFeedback()
  {
    return true;
  }
  
  public void a(CommandObjectiveExecutor.EnumCommandResult commandobjectiveexecutor_enumcommandresult, int i) {}
}
