package net.minecraft.server.v1_8_R3;

public class CommandException
  extends Exception
{
  private final Object[] a;
  
  public CommandException(String ☃, Object... ☃)
  {
    super(☃);
    
    this.a = ☃;
  }
  
  public Object[] getArgs()
  {
    return this.a;
  }
}
