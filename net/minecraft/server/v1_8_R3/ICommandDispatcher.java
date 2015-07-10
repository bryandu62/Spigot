package net.minecraft.server.v1_8_R3;

public abstract interface ICommandDispatcher
{
  public abstract void a(ICommandListener paramICommandListener, ICommand paramICommand, int paramInt, String paramString, Object... paramVarArgs);
}
