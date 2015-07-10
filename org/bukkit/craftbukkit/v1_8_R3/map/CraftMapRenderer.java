package org.bukkit.craftbukkit.v1_8_R3.map;

import java.util.Map;
import java.util.UUID;
import net.minecraft.server.v1_8_R3.MapIcon;
import net.minecraft.server.v1_8_R3.WorldMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class CraftMapRenderer
  extends MapRenderer
{
  private final WorldMap worldMap;
  
  public CraftMapRenderer(CraftMapView mapView, WorldMap worldMap)
  {
    super(false);
    this.worldMap = worldMap;
  }
  
  public void render(MapView map, MapCanvas canvas, Player player)
  {
    for (int x = 0; x < 128; x++) {
      for (int y = 0; y < 128; y++) {
        canvas.setPixel(x, y, this.worldMap.colors[(y * 128 + x)]);
      }
    }
    MapCursorCollection cursors = canvas.getCursors();
    while (cursors.size() > 0) {
      cursors.removeCursor(cursors.getCursor(0));
    }
    for (UUID key : this.worldMap.decorations.keySet())
    {
      Player other = Bukkit.getPlayer(key);
      if ((other == null) || (player.canSee(other)))
      {
        MapIcon decoration = (MapIcon)this.worldMap.decorations.get(key);
        cursors.addCursor(decoration.getX(), decoration.getY(), (byte)(decoration.getRotation() & 0xF), decoration.getType());
      }
    }
  }
}
