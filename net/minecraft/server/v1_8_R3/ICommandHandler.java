package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Map;

public abstract interface ICommandHandler
{
  public abstract int a(ICommandListener paramICommandListener, String paramString);
  
  public abstract List<String> a(ICommandListener paramICommandListener, String paramString, BlockPosition paramBlockPosition);
  
  public abstract List<ICommand> a(ICommandListener paramICommandListener);
  
  public abstract Map<String, ICommand> getCommands();
}
