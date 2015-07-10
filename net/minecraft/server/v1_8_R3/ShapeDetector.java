package net.minecraft.server.v1_8_R3;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class ShapeDetector
{
  private final Predicate<ShapeDetectorBlock>[][][] a;
  private final int b;
  private final int c;
  private final int d;
  
  public ShapeDetector(Predicate<ShapeDetectorBlock>[][][] ☃)
  {
    this.a = ☃;
    
    this.b = ☃.length;
    if (this.b > 0)
    {
      this.c = ☃[0].length;
      if (this.c > 0) {
        this.d = ☃[0][0].length;
      } else {
        this.d = 0;
      }
    }
    else
    {
      this.c = 0;
      this.d = 0;
    }
  }
  
  public int b()
  {
    return this.c;
  }
  
  public int c()
  {
    return this.d;
  }
  
  private ShapeDetectorCollection a(BlockPosition ☃, EnumDirection ☃, EnumDirection ☃, LoadingCache<BlockPosition, ShapeDetectorBlock> ☃)
  {
    for (int ☃ = 0; ☃ < this.d; ☃++) {
      for (int ☃ = 0; ☃ < this.c; ☃++) {
        for (int ☃ = 0; ☃ < this.b; ☃++) {
          if (!this.a[☃][☃][☃].apply(☃.getUnchecked(a(☃, ☃, ☃, ☃, ☃, ☃)))) {
            return null;
          }
        }
      }
    }
    return new ShapeDetectorCollection(☃, ☃, ☃, ☃, this.d, this.c, this.b);
  }
  
  public ShapeDetectorCollection a(World ☃, BlockPosition ☃)
  {
    LoadingCache<BlockPosition, ShapeDetectorBlock> ☃ = a(☃, false);
    
    int ☃ = Math.max(Math.max(this.d, this.c), this.b);
    for (BlockPosition ☃ : BlockPosition.a(☃, ☃.a(☃ - 1, ☃ - 1, ☃ - 1))) {
      for (EnumDirection ☃ : EnumDirection.values()) {
        for (EnumDirection ☃ : EnumDirection.values()) {
          if ((☃ != ☃) && (☃ != ☃.opposite()))
          {
            ShapeDetectorCollection ☃ = a(☃, ☃, ☃, ☃);
            if (☃ != null) {
              return ☃;
            }
          }
        }
      }
    }
    return null;
  }
  
  public static LoadingCache<BlockPosition, ShapeDetectorBlock> a(World ☃, boolean ☃)
  {
    return CacheBuilder.newBuilder().build(new BlockLoader(☃, ☃));
  }
  
  protected static BlockPosition a(BlockPosition ☃, EnumDirection ☃, EnumDirection ☃, int ☃, int ☃, int ☃)
  {
    if ((☃ == ☃) || (☃ == ☃.opposite())) {
      throw new IllegalArgumentException("Invalid forwards & up combination");
    }
    BaseBlockPosition ☃ = new BaseBlockPosition(☃.getAdjacentX(), ☃.getAdjacentY(), ☃.getAdjacentZ());
    BaseBlockPosition ☃ = new BaseBlockPosition(☃.getAdjacentX(), ☃.getAdjacentY(), ☃.getAdjacentZ());
    BaseBlockPosition ☃ = ☃.d(☃);
    
    return ☃.a(☃.getX() * -☃ + ☃.getX() * ☃ + ☃.getX() * ☃, ☃.getY() * -☃ + ☃.getY() * ☃ + ☃.getY() * ☃, ☃.getZ() * -☃ + ☃.getZ() * ☃ + ☃.getZ() * ☃);
  }
  
  static class BlockLoader
    extends CacheLoader<BlockPosition, ShapeDetectorBlock>
  {
    private final World a;
    private final boolean b;
    
    public BlockLoader(World ☃, boolean ☃)
    {
      this.a = ☃;
      this.b = ☃;
    }
    
    public ShapeDetectorBlock a(BlockPosition ☃)
      throws Exception
    {
      return new ShapeDetectorBlock(this.a, ☃, this.b);
    }
  }
  
  public static class ShapeDetectorCollection
  {
    private final BlockPosition a;
    private final EnumDirection b;
    private final EnumDirection c;
    private final LoadingCache<BlockPosition, ShapeDetectorBlock> d;
    private final int e;
    private final int f;
    private final int g;
    
    public ShapeDetectorCollection(BlockPosition ☃, EnumDirection ☃, EnumDirection ☃, LoadingCache<BlockPosition, ShapeDetectorBlock> ☃, int ☃, int ☃, int ☃)
    {
      this.a = ☃;
      this.b = ☃;
      this.c = ☃;
      this.d = ☃;
      this.e = ☃;
      this.f = ☃;
      this.g = ☃;
    }
    
    public BlockPosition a()
    {
      return this.a;
    }
    
    public EnumDirection b()
    {
      return this.b;
    }
    
    public EnumDirection c()
    {
      return this.c;
    }
    
    public int d()
    {
      return this.e;
    }
    
    public int e()
    {
      return this.f;
    }
    
    public ShapeDetectorBlock a(int ☃, int ☃, int ☃)
    {
      return (ShapeDetectorBlock)this.d.getUnchecked(ShapeDetector.a(this.a, b(), c(), ☃, ☃, ☃));
    }
    
    public String toString()
    {
      return Objects.toStringHelper(this).add("up", this.c).add("forwards", this.b).add("frontTopLeft", this.a).toString();
    }
  }
}
