package org.bukkit.craftbukkit.v1_8_R3.map;

import java.util.ArrayList;
import org.bukkit.map.MapCursor;

public class RenderData
{
  public final byte[] buffer;
  public final ArrayList<MapCursor> cursors;
  
  public RenderData()
  {
    this.buffer = new byte['䀀'];
    this.cursors = new ArrayList();
  }
}
