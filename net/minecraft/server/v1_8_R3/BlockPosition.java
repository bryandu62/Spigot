package net.minecraft.server.v1_8_R3;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;

public class BlockPosition
  extends BaseBlockPosition
{
  public static final BlockPosition ZERO = new BlockPosition(0, 0, 0);
  private static final int c = 1 + MathHelper.c(MathHelper.b(30000000));
  private static final int d = c;
  private static final int e = 64 - c - d;
  private static final int f = 0 + d;
  private static final int g = f + e;
  private static final long h = (1L << c) - 1L;
  private static final long i = (1L << e) - 1L;
  private static final long j = (1L << d) - 1L;
  
  public BlockPosition(int ☃, int ☃, int ☃)
  {
    super(☃, ☃, ☃);
  }
  
  public BlockPosition(double ☃, double ☃, double ☃)
  {
    super(☃, ☃, ☃);
  }
  
  public BlockPosition(Entity ☃)
  {
    this(☃.locX, ☃.locY, ☃.locZ);
  }
  
  public BlockPosition(Vec3D ☃)
  {
    this(☃.a, ☃.b, ☃.c);
  }
  
  public BlockPosition(BaseBlockPosition ☃)
  {
    this(☃.getX(), ☃.getY(), ☃.getZ());
  }
  
  public BlockPosition a(double ☃, double ☃, double ☃)
  {
    if ((☃ == 0.0D) && (☃ == 0.0D) && (☃ == 0.0D)) {
      return this;
    }
    return new BlockPosition(getX() + ☃, getY() + ☃, getZ() + ☃);
  }
  
  public BlockPosition a(int ☃, int ☃, int ☃)
  {
    if ((☃ == 0) && (☃ == 0) && (☃ == 0)) {
      return this;
    }
    return new BlockPosition(getX() + ☃, getY() + ☃, getZ() + ☃);
  }
  
  public BlockPosition a(BaseBlockPosition ☃)
  {
    if ((☃.getX() == 0) && (☃.getY() == 0) && (☃.getZ() == 0)) {
      return this;
    }
    return new BlockPosition(getX() + ☃.getX(), getY() + ☃.getY(), getZ() + ☃.getZ());
  }
  
  public BlockPosition b(BaseBlockPosition ☃)
  {
    if ((☃.getX() == 0) && (☃.getY() == 0) && (☃.getZ() == 0)) {
      return this;
    }
    return new BlockPosition(getX() - ☃.getX(), getY() - ☃.getY(), getZ() - ☃.getZ());
  }
  
  public BlockPosition up()
  {
    return up(1);
  }
  
  public BlockPosition up(int ☃)
  {
    return shift(EnumDirection.UP, ☃);
  }
  
  public BlockPosition down()
  {
    return down(1);
  }
  
  public BlockPosition down(int ☃)
  {
    return shift(EnumDirection.DOWN, ☃);
  }
  
  public BlockPosition north()
  {
    return north(1);
  }
  
  public BlockPosition north(int ☃)
  {
    return shift(EnumDirection.NORTH, ☃);
  }
  
  public BlockPosition south()
  {
    return south(1);
  }
  
  public BlockPosition south(int ☃)
  {
    return shift(EnumDirection.SOUTH, ☃);
  }
  
  public BlockPosition west()
  {
    return west(1);
  }
  
  public BlockPosition west(int ☃)
  {
    return shift(EnumDirection.WEST, ☃);
  }
  
  public BlockPosition east()
  {
    return east(1);
  }
  
  public BlockPosition east(int ☃)
  {
    return shift(EnumDirection.EAST, ☃);
  }
  
  public BlockPosition shift(EnumDirection ☃)
  {
    return shift(☃, 1);
  }
  
  public BlockPosition shift(EnumDirection ☃, int ☃)
  {
    if (☃ == 0) {
      return this;
    }
    return new BlockPosition(getX() + ☃.getAdjacentX() * ☃, getY() + ☃.getAdjacentY() * ☃, getZ() + ☃.getAdjacentZ() * ☃);
  }
  
  public BlockPosition c(BaseBlockPosition ☃)
  {
    return new BlockPosition(getY() * ☃.getZ() - getZ() * ☃.getY(), getZ() * ☃.getX() - getX() * ☃.getZ(), getX() * ☃.getY() - getY() * ☃.getX());
  }
  
  public long asLong()
  {
    return (getX() & h) << g | (getY() & i) << f | (getZ() & j) << 0;
  }
  
  public static BlockPosition fromLong(long ☃)
  {
    int ☃ = (int)(☃ << 64 - g - c >> 64 - c);
    int ☃ = (int)(☃ << 64 - f - e >> 64 - e);
    int ☃ = (int)(☃ << 64 - d >> 64 - d);
    
    return new BlockPosition(☃, ☃, ☃);
  }
  
  public static Iterable<BlockPosition> a(BlockPosition ☃, BlockPosition ☃)
  {
    BlockPosition ☃ = new BlockPosition(Math.min(☃.getX(), ☃.getX()), Math.min(☃.getY(), ☃.getY()), Math.min(☃.getZ(), ☃.getZ()));
    final BlockPosition ☃ = new BlockPosition(Math.max(☃.getX(), ☃.getX()), Math.max(☃.getY(), ☃.getY()), Math.max(☃.getZ(), ☃.getZ()));
    
    new Iterable()
    {
      public Iterator<BlockPosition> iterator()
      {
        new AbstractIterator()
        {
          private BlockPosition b = null;
          
          protected BlockPosition a()
          {
            if (this.b == null)
            {
              this.b = BlockPosition.1.this.a;
              return this.b;
            }
            if (this.b.equals(BlockPosition.1.this.b)) {
              return (BlockPosition)endOfData();
            }
            int ☃ = this.b.getX();
            int ☃ = this.b.getY();
            int ☃ = this.b.getZ();
            if (☃ < BlockPosition.1.this.b.getX())
            {
              ☃++;
            }
            else if (☃ < BlockPosition.1.this.b.getY())
            {
              ☃ = BlockPosition.1.this.a.getX();
              ☃++;
            }
            else if (☃ < BlockPosition.1.this.b.getZ())
            {
              ☃ = BlockPosition.1.this.a.getX();
              ☃ = BlockPosition.1.this.a.getY();
              ☃++;
            }
            this.b = new BlockPosition(☃, ☃, ☃);
            return this.b;
          }
        };
      }
    };
  }
  
  public static final class MutableBlockPosition
    extends BlockPosition
  {
    private int c;
    private int d;
    private int e;
    
    public MutableBlockPosition()
    {
      this(0, 0, 0);
    }
    
    public MutableBlockPosition(int ☃, int ☃, int ☃)
    {
      super(0, 0);
      this.c = ☃;
      this.d = ☃;
      this.e = ☃;
    }
    
    public int getX()
    {
      return this.c;
    }
    
    public int getY()
    {
      return this.d;
    }
    
    public int getZ()
    {
      return this.e;
    }
    
    public MutableBlockPosition c(int ☃, int ☃, int ☃)
    {
      this.c = ☃;
      this.d = ☃;
      this.e = ☃;
      return this;
    }
  }
  
  public static Iterable<MutableBlockPosition> b(BlockPosition ☃, BlockPosition ☃)
  {
    BlockPosition ☃ = new BlockPosition(Math.min(☃.getX(), ☃.getX()), Math.min(☃.getY(), ☃.getY()), Math.min(☃.getZ(), ☃.getZ()));
    final BlockPosition ☃ = new BlockPosition(Math.max(☃.getX(), ☃.getX()), Math.max(☃.getY(), ☃.getY()), Math.max(☃.getZ(), ☃.getZ()));
    
    new Iterable()
    {
      public Iterator<BlockPosition.MutableBlockPosition> iterator()
      {
        new AbstractIterator()
        {
          private BlockPosition.MutableBlockPosition b = null;
          
          protected BlockPosition.MutableBlockPosition a()
          {
            if (this.b == null)
            {
              this.b = new BlockPosition.MutableBlockPosition(BlockPosition.2.this.a.getX(), BlockPosition.2.this.a.getY(), BlockPosition.2.this.a.getZ());
              return this.b;
            }
            if (this.b.equals(BlockPosition.2.this.b)) {
              return (BlockPosition.MutableBlockPosition)endOfData();
            }
            int ☃ = this.b.getX();
            int ☃ = this.b.getY();
            int ☃ = this.b.getZ();
            if (☃ < BlockPosition.2.this.b.getX())
            {
              ☃++;
            }
            else if (☃ < BlockPosition.2.this.b.getY())
            {
              ☃ = BlockPosition.2.this.a.getX();
              ☃++;
            }
            else if (☃ < BlockPosition.2.this.b.getZ())
            {
              ☃ = BlockPosition.2.this.a.getX();
              ☃ = BlockPosition.2.this.a.getY();
              ☃++;
            }
            BlockPosition.MutableBlockPosition.a(this.b, ☃);
            BlockPosition.MutableBlockPosition.b(this.b, ☃);
            BlockPosition.MutableBlockPosition.c(this.b, ☃);
            return this.b;
          }
        };
      }
    };
  }
}
