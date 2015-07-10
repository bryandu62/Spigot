package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.NetherWartsState;

public class NetherWarts
  extends MaterialData
{
  public NetherWarts()
  {
    super(Material.NETHER_WARTS);
  }
  
  public NetherWarts(NetherWartsState state)
  {
    this();
    setState(state);
  }
  
  @Deprecated
  public NetherWarts(int type)
  {
    super(type);
  }
  
  public NetherWarts(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public NetherWarts(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public NetherWarts(Material type, byte data)
  {
    super(type, data);
  }
  
  public NetherWartsState getState()
  {
    switch (getData())
    {
    case 0: 
      return NetherWartsState.SEEDED;
    case 1: 
      return NetherWartsState.STAGE_ONE;
    case 2: 
      return NetherWartsState.STAGE_TWO;
    }
    return NetherWartsState.RIPE;
  }
  
  public void setState(NetherWartsState state)
  {
    switch (state)
    {
    case RIPE: 
      setData((byte)0);
      return;
    case SEEDED: 
      setData((byte)1);
      return;
    case STAGE_ONE: 
      setData((byte)2);
      return;
    case STAGE_TWO: 
      setData((byte)3);
      return;
    }
  }
  
  public String toString()
  {
    return getState() + " " + super.toString();
  }
  
  public NetherWarts clone()
  {
    return (NetherWarts)super.clone();
  }
}
