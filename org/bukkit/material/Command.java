package org.bukkit.material;

import org.bukkit.Material;

public class Command
  extends MaterialData
  implements Redstone
{
  public Command()
  {
    super(Material.COMMAND);
  }
  
  @Deprecated
  public Command(int type)
  {
    super(type);
  }
  
  public Command(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Command(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Command(Material type, byte data)
  {
    super(type, data);
  }
  
  public boolean isPowered()
  {
    return (getData() & 0x1) != 0;
  }
  
  public void setPowered(boolean bool)
  {
    setData((byte)(bool ? getData() | 0x1 : getData() & 0xFFFFFFFE));
  }
  
  public String toString()
  {
    return super.toString() + " " + (isPowered() ? "" : "NOT ") + "POWERED";
  }
  
  public Command clone()
  {
    return (Command)super.clone();
  }
}
