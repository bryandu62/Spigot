package net.minecraft.server.v1_8_R3;

public abstract interface ICommandListener
{
  public abstract String getName();
  
  public abstract IChatBaseComponent getScoreboardDisplayName();
  
  public abstract void sendMessage(IChatBaseComponent paramIChatBaseComponent);
  
  public abstract boolean a(int paramInt, String paramString);
  
  public abstract BlockPosition getChunkCoordinates();
  
  public abstract Vec3D d();
  
  public abstract World getWorld();
  
  public abstract Entity f();
  
  public abstract boolean getSendCommandFeedback();
  
  public abstract void a(CommandObjectiveExecutor.EnumCommandResult paramEnumCommandResult, int paramInt);
}
