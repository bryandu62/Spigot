package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;

public class EmptyChunk
  extends Chunk
{
  public EmptyChunk(World ☃, int ☃, int ☃)
  {
    super(☃, ☃, ☃);
  }
  
  public boolean a(int ☃, int ☃)
  {
    return (☃ == this.locX) && (☃ == this.locZ);
  }
  
  public int b(int ☃, int ☃)
  {
    return 0;
  }
  
  public void initLighting() {}
  
  public Block getType(BlockPosition ☃)
  {
    return Blocks.AIR;
  }
  
  public int b(BlockPosition ☃)
  {
    return 255;
  }
  
  public int c(BlockPosition ☃)
  {
    return 0;
  }
  
  public int getBrightness(EnumSkyBlock ☃, BlockPosition ☃)
  {
    return ☃.c;
  }
  
  public void a(EnumSkyBlock ☃, BlockPosition ☃, int ☃) {}
  
  public int a(BlockPosition ☃, int ☃)
  {
    return 0;
  }
  
  public void a(Entity ☃) {}
  
  public void b(Entity ☃) {}
  
  public void a(Entity ☃, int ☃) {}
  
  public boolean d(BlockPosition ☃)
  {
    return false;
  }
  
  public TileEntity a(BlockPosition ☃, Chunk.EnumTileEntityState ☃)
  {
    return null;
  }
  
  public void a(TileEntity ☃) {}
  
  public void a(BlockPosition ☃, TileEntity ☃) {}
  
  public void e(BlockPosition ☃) {}
  
  public void addEntities() {}
  
  public void removeEntities() {}
  
  public void e() {}
  
  public void a(Entity ☃, AxisAlignedBB ☃, List<Entity> ☃, Predicate<? super Entity> ☃) {}
  
  public <T extends Entity> void a(Class<? extends T> ☃, AxisAlignedBB ☃, List<T> ☃, Predicate<? super T> ☃) {}
  
  public boolean a(boolean ☃)
  {
    return false;
  }
  
  public Random a(long ☃)
  {
    return new Random(getWorld().getSeed() + this.locX * this.locX * 4987142 + this.locX * 5947611 + this.locZ * this.locZ * 4392871L + this.locZ * 389711 ^ ☃);
  }
  
  public boolean isEmpty()
  {
    return true;
  }
  
  public boolean c(int ☃, int ☃)
  {
    return true;
  }
}
