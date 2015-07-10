package org.bukkit.craftbukkit.v1_8_R3.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.minecraft.server.v1_8_R3.WorldMap;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;

public final class CraftMapView
  implements MapView
{
  private final Map<CraftPlayer, RenderData> renderCache = new HashMap();
  private final List<MapRenderer> renderers = new ArrayList();
  private final Map<MapRenderer, Map<CraftPlayer, CraftMapCanvas>> canvases = new HashMap();
  protected final WorldMap worldMap;
  
  public CraftMapView(WorldMap worldMap)
  {
    this.worldMap = worldMap;
    addRenderer(new CraftMapRenderer(this, worldMap));
  }
  
  public short getId()
  {
    String text = this.worldMap.id;
    if (text.startsWith("map_")) {
      try
      {
        return Short.parseShort(text.substring("map_".length()));
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw new IllegalStateException("Map has non-numeric ID");
      }
    }
    throw new IllegalStateException("Map has invalid ID");
  }
  
  public boolean isVirtual()
  {
    return (this.renderers.size() > 0) && (!(this.renderers.get(0) instanceof CraftMapRenderer));
  }
  
  public MapView.Scale getScale()
  {
    return MapView.Scale.valueOf(this.worldMap.scale);
  }
  
  public void setScale(MapView.Scale scale)
  {
    this.worldMap.scale = scale.getValue();
  }
  
  public World getWorld()
  {
    byte dimension = this.worldMap.map;
    for (World world : Bukkit.getServer().getWorlds()) {
      if (((CraftWorld)world).getHandle().dimension == dimension) {
        return world;
      }
    }
    return null;
  }
  
  public void setWorld(World world)
  {
    this.worldMap.map = ((byte)((CraftWorld)world).getHandle().dimension);
  }
  
  public int getCenterX()
  {
    return this.worldMap.centerX;
  }
  
  public int getCenterZ()
  {
    return this.worldMap.centerZ;
  }
  
  public void setCenterX(int x)
  {
    this.worldMap.centerX = x;
  }
  
  public void setCenterZ(int z)
  {
    this.worldMap.centerZ = z;
  }
  
  public List<MapRenderer> getRenderers()
  {
    return new ArrayList(this.renderers);
  }
  
  public void addRenderer(MapRenderer renderer)
  {
    if (!this.renderers.contains(renderer))
    {
      this.renderers.add(renderer);
      this.canvases.put(renderer, new HashMap());
      renderer.initialize(this);
    }
  }
  
  public boolean removeRenderer(MapRenderer renderer)
  {
    if (this.renderers.contains(renderer))
    {
      this.renderers.remove(renderer);
      int x;
      for (Iterator localIterator = ((Map)this.canvases.get(renderer)).entrySet().iterator(); localIterator.hasNext(); x < 128)
      {
        Map.Entry<CraftPlayer, CraftMapCanvas> entry = (Map.Entry)localIterator.next();
        x = 0; continue;
        for (int y = 0; y < 128; y++) {
          ((CraftMapCanvas)entry.getValue()).setPixel(x, y, (byte)-1);
        }
        x++;
      }
      this.canvases.remove(renderer);
      return true;
    }
    return false;
  }
  
  private boolean isContextual()
  {
    for (MapRenderer renderer : this.renderers) {
      if (renderer.isContextual()) {
        return true;
      }
    }
    return false;
  }
  
  public RenderData render(CraftPlayer player)
  {
    boolean context = isContextual();
    RenderData render = (RenderData)this.renderCache.get(context ? player : null);
    if (render == null)
    {
      render = new RenderData();
      this.renderCache.put(context ? player : null, render);
    }
    if ((context) && (this.renderCache.containsKey(null))) {
      this.renderCache.remove(null);
    }
    Arrays.fill(render.buffer, (byte)0);
    render.cursors.clear();
    CraftMapCanvas canvas;
    int i;
    for (Iterator localIterator = this.renderers.iterator(); localIterator.hasNext(); i < canvas.getCursors().size())
    {
      MapRenderer renderer = (MapRenderer)localIterator.next();
      canvas = (CraftMapCanvas)((Map)this.canvases.get(renderer)).get(renderer.isContextual() ? player : null);
      if (canvas == null)
      {
        canvas = new CraftMapCanvas(this);
        ((Map)this.canvases.get(renderer)).put(renderer.isContextual() ? player : null, canvas);
      }
      canvas.setBase(render.buffer);
      renderer.render(this, canvas, player);
      
      byte[] buf = canvas.getBuffer();
      for (int i = 0; i < buf.length; i++)
      {
        byte color = buf[i];
        if ((color >= 0) || (color <= -113)) {
          render.buffer[i] = color;
        }
      }
      i = 0; continue;
      render.cursors.add(canvas.getCursors().getCursor(i));i++;
    }
    return render;
  }
}
