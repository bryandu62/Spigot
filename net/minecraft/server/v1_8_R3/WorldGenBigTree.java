package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;

public class WorldGenBigTree
  extends WorldGenTreeAbstract
{
  private Random k;
  private World l;
  
  static class Position
    extends BlockPosition
  {
    private final int c;
    
    public Position(BlockPosition ☃, int ☃)
    {
      super(☃.getY(), ☃.getZ());
      this.c = ☃;
    }
    
    public int q()
    {
      return this.c;
    }
  }
  
  private BlockPosition m = BlockPosition.ZERO;
  int a;
  int b;
  double c = 0.618D;
  double d = 0.381D;
  double e = 1.0D;
  double f = 1.0D;
  int g = 1;
  int h = 12;
  int i = 4;
  List<Position> j;
  
  public WorldGenBigTree(boolean ☃)
  {
    super(☃);
  }
  
  void a()
  {
    this.b = ((int)(this.a * this.c));
    if (this.b >= this.a) {
      this.b = (this.a - 1);
    }
    int ☃ = (int)(1.382D + Math.pow(this.f * this.a / 13.0D, 2.0D));
    if (☃ < 1) {
      ☃ = 1;
    }
    int ☃ = this.m.getY() + this.b;
    int ☃ = this.a - this.i;
    
    this.j = Lists.newArrayList();
    this.j.add(new Position(this.m.up(☃), ☃));
    for (; ☃ >= 0; ☃--)
    {
      float ☃ = a(☃);
      if (☃ >= 0.0F) {
        for (int ☃ = 0; ☃ < ☃; ☃++)
        {
          double ☃ = this.e * ☃ * (this.k.nextFloat() + 0.328D);
          double ☃ = this.k.nextFloat() * 2.0F * 3.141592653589793D;
          
          double ☃ = ☃ * Math.sin(☃) + 0.5D;
          double ☃ = ☃ * Math.cos(☃) + 0.5D;
          
          BlockPosition ☃ = this.m.a(☃, ☃ - 1, ☃);
          BlockPosition ☃ = ☃.up(this.i);
          if (a(☃, ☃) == -1)
          {
            int ☃ = this.m.getX() - ☃.getX();
            int ☃ = this.m.getZ() - ☃.getZ();
            
            double ☃ = ☃.getY() - Math.sqrt(☃ * ☃ + ☃ * ☃) * this.d;
            int ☃ = ☃ > ☃ ? ☃ : (int)☃;
            BlockPosition ☃ = new BlockPosition(this.m.getX(), ☃, this.m.getZ());
            if (a(☃, ☃) == -1) {
              this.j.add(new Position(☃, ☃.getY()));
            }
          }
        }
      }
    }
  }
  
  void a(BlockPosition ☃, float ☃, IBlockData ☃)
  {
    int ☃ = (int)(☃ + 0.618D);
    for (int ☃ = -☃; ☃ <= ☃; ☃++) {
      for (int ☃ = -☃; ☃ <= ☃; ☃++) {
        if (Math.pow(Math.abs(☃) + 0.5D, 2.0D) + Math.pow(Math.abs(☃) + 0.5D, 2.0D) <= ☃ * ☃)
        {
          BlockPosition ☃ = ☃.a(☃, 0, ☃);
          
          Material ☃ = this.l.getType(☃).getBlock().getMaterial();
          if ((☃ == Material.AIR) || (☃ == Material.LEAVES)) {
            a(this.l, ☃, ☃);
          }
        }
      }
    }
  }
  
  float a(int ☃)
  {
    if (☃ < this.a * 0.3F) {
      return -1.0F;
    }
    float ☃ = this.a / 2.0F;
    float ☃ = ☃ - ☃;
    
    float ☃ = MathHelper.c(☃ * ☃ - ☃ * ☃);
    if (☃ == 0.0F) {
      ☃ = ☃;
    } else if (Math.abs(☃) >= ☃) {
      return 0.0F;
    }
    return ☃ * 0.5F;
  }
  
  float b(int ☃)
  {
    if ((☃ < 0) || (☃ >= this.i)) {
      return -1.0F;
    }
    if ((☃ == 0) || (☃ == this.i - 1)) {
      return 2.0F;
    }
    return 3.0F;
  }
  
  void a(BlockPosition ☃)
  {
    for (int ☃ = 0; ☃ < this.i; ☃++) {
      a(☃.up(☃), b(☃), Blocks.LEAVES.getBlockData().set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false)));
    }
  }
  
  void a(BlockPosition ☃, BlockPosition ☃, Block ☃)
  {
    BlockPosition ☃ = ☃.a(-☃.getX(), -☃.getY(), -☃.getZ());
    
    int ☃ = b(☃);
    
    float ☃ = ☃.getX() / ☃;
    float ☃ = ☃.getY() / ☃;
    float ☃ = ☃.getZ() / ☃;
    for (int ☃ = 0; ☃ <= ☃; ☃++)
    {
      BlockPosition ☃ = ☃.a(0.5F + ☃ * ☃, 0.5F + ☃ * ☃, 0.5F + ☃ * ☃);
      BlockLogAbstract.EnumLogRotation ☃ = b(☃, ☃);
      
      a(this.l, ☃, ☃.getBlockData().set(BlockLogAbstract.AXIS, ☃));
    }
  }
  
  private int b(BlockPosition ☃)
  {
    int ☃ = MathHelper.a(☃.getX());
    int ☃ = MathHelper.a(☃.getY());
    int ☃ = MathHelper.a(☃.getZ());
    if ((☃ > ☃) && (☃ > ☃)) {
      return ☃;
    }
    if (☃ > ☃) {
      return ☃;
    }
    return ☃;
  }
  
  private BlockLogAbstract.EnumLogRotation b(BlockPosition ☃, BlockPosition ☃)
  {
    BlockLogAbstract.EnumLogRotation ☃ = BlockLogAbstract.EnumLogRotation.Y;
    int ☃ = Math.abs(☃.getX() - ☃.getX());
    int ☃ = Math.abs(☃.getZ() - ☃.getZ());
    int ☃ = Math.max(☃, ☃);
    if (☃ > 0) {
      if (☃ == ☃) {
        ☃ = BlockLogAbstract.EnumLogRotation.X;
      } else if (☃ == ☃) {
        ☃ = BlockLogAbstract.EnumLogRotation.Z;
      }
    }
    return ☃;
  }
  
  void b()
  {
    for (Position ☃ : this.j) {
      a(☃);
    }
  }
  
  boolean c(int ☃)
  {
    return ☃ >= this.a * 0.2D;
  }
  
  void c()
  {
    BlockPosition ☃ = this.m;
    BlockPosition ☃ = this.m.up(this.b);
    Block ☃ = Blocks.LOG;
    
    a(☃, ☃, ☃);
    if (this.g == 2)
    {
      a(☃.east(), ☃.east(), ☃);
      a(☃.east().south(), ☃.east().south(), ☃);
      a(☃.south(), ☃.south(), ☃);
    }
  }
  
  void d()
  {
    for (Position ☃ : this.j)
    {
      int ☃ = ☃.q();
      BlockPosition ☃ = new BlockPosition(this.m.getX(), ☃, this.m.getZ());
      if ((!☃.equals(☃)) && 
        (c(☃ - this.m.getY()))) {
        a(☃, ☃, Blocks.LOG);
      }
    }
  }
  
  int a(BlockPosition ☃, BlockPosition ☃)
  {
    BlockPosition ☃ = ☃.a(-☃.getX(), -☃.getY(), -☃.getZ());
    
    int ☃ = b(☃);
    
    float ☃ = ☃.getX() / ☃;
    float ☃ = ☃.getY() / ☃;
    float ☃ = ☃.getZ() / ☃;
    if (☃ == 0) {
      return -1;
    }
    for (int ☃ = 0; ☃ <= ☃; ☃++)
    {
      BlockPosition ☃ = ☃.a(0.5F + ☃ * ☃, 0.5F + ☃ * ☃, 0.5F + ☃ * ☃);
      if (!a(this.l.getType(☃).getBlock())) {
        return ☃;
      }
    }
    return -1;
  }
  
  public void e()
  {
    this.i = 5;
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    this.l = ☃;
    this.m = ☃;
    
    this.k = new Random(☃.nextLong());
    if (this.a == 0) {
      this.a = (5 + this.k.nextInt(this.h));
    }
    if (!f()) {
      return false;
    }
    a();
    b();
    c();
    d();
    
    return true;
  }
  
  private boolean f()
  {
    Block ☃ = this.l.getType(this.m.down()).getBlock();
    if ((☃ != Blocks.DIRT) && (☃ != Blocks.GRASS) && (☃ != Blocks.FARMLAND)) {
      return false;
    }
    int ☃ = a(this.m, this.m.up(this.a - 1));
    if (☃ == -1) {
      return true;
    }
    if (☃ < 6) {
      return false;
    }
    this.a = ☃;
    return true;
  }
}
