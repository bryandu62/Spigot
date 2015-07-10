package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;

public class PistonExtendsChecker
{
  private final World a;
  private final BlockPosition b;
  private final BlockPosition c;
  private final EnumDirection d;
  private final List<BlockPosition> e = Lists.newArrayList();
  private final List<BlockPosition> f = Lists.newArrayList();
  
  public PistonExtendsChecker(World ☃, BlockPosition ☃, EnumDirection ☃, boolean ☃)
  {
    this.a = ☃;
    this.b = ☃;
    if (☃)
    {
      this.d = ☃;
      this.c = ☃.shift(☃);
    }
    else
    {
      this.d = ☃.opposite();
      this.c = ☃.shift(☃, 2);
    }
  }
  
  public boolean a()
  {
    this.e.clear();
    this.f.clear();
    
    Block ☃ = this.a.getType(this.c).getBlock();
    if (!BlockPiston.a(☃, this.a, this.c, this.d, false))
    {
      if (☃.k() != 1) {
        return false;
      }
      this.f.add(this.c);
      return true;
    }
    if (!a(this.c)) {
      return false;
    }
    for (int ☃ = 0; ☃ < this.e.size(); ☃++)
    {
      BlockPosition ☃ = (BlockPosition)this.e.get(☃);
      if ((this.a.getType(☃).getBlock() == Blocks.SLIME) && 
        (!b(☃))) {
        return false;
      }
    }
    return true;
  }
  
  private boolean a(BlockPosition ☃)
  {
    Block ☃ = this.a.getType(☃).getBlock();
    if (☃.getMaterial() == Material.AIR) {
      return true;
    }
    if (!BlockPiston.a(☃, this.a, ☃, this.d, false)) {
      return true;
    }
    if (☃.equals(this.b)) {
      return true;
    }
    if (this.e.contains(☃)) {
      return true;
    }
    int ☃ = 1;
    if (☃ + this.e.size() > 12) {
      return false;
    }
    while (☃ == Blocks.SLIME)
    {
      BlockPosition ☃ = ☃.shift(this.d.opposite(), ☃);
      ☃ = this.a.getType(☃).getBlock();
      if ((☃.getMaterial() == Material.AIR) || (!BlockPiston.a(☃, this.a, ☃, this.d, false)) || (☃.equals(this.b))) {
        break;
      }
      ☃++;
      if (☃ + this.e.size() > 12) {
        return false;
      }
    }
    int ☃ = 0;
    for (int ☃ = ☃ - 1; ☃ >= 0; ☃--)
    {
      this.e.add(☃.shift(this.d.opposite(), ☃));
      ☃++;
    }
    for (int ☃ = 1;; ☃++)
    {
      BlockPosition ☃ = ☃.shift(this.d, ☃);
      
      int ☃ = this.e.indexOf(☃);
      if (☃ > -1)
      {
        a(☃, ☃);
        for (int ☃ = 0; ☃ <= ☃ + ☃; ☃++)
        {
          BlockPosition ☃ = (BlockPosition)this.e.get(☃);
          if ((this.a.getType(☃).getBlock() == Blocks.SLIME) && 
            (!b(☃))) {
            return false;
          }
        }
        return true;
      }
      ☃ = this.a.getType(☃).getBlock();
      if (☃.getMaterial() == Material.AIR) {
        return true;
      }
      if ((!BlockPiston.a(☃, this.a, ☃, this.d, true)) || (☃.equals(this.b))) {
        return false;
      }
      if (☃.k() == 1)
      {
        this.f.add(☃);
        return true;
      }
      if (this.e.size() >= 12) {
        return false;
      }
      this.e.add(☃);
      ☃++;
    }
  }
  
  private void a(int ☃, int ☃)
  {
    List<BlockPosition> ☃ = Lists.newArrayList();
    List<BlockPosition> ☃ = Lists.newArrayList();
    List<BlockPosition> ☃ = Lists.newArrayList();
    
    ☃.addAll(this.e.subList(0, ☃));
    ☃.addAll(this.e.subList(this.e.size() - ☃, this.e.size()));
    ☃.addAll(this.e.subList(☃, this.e.size() - ☃));
    
    this.e.clear();
    this.e.addAll(☃);
    this.e.addAll(☃);
    this.e.addAll(☃);
  }
  
  private boolean b(BlockPosition ☃)
  {
    for (EnumDirection ☃ : ) {
      if ((☃.k() != this.d.k()) && 
        (!a(☃.shift(☃)))) {
        return false;
      }
    }
    return true;
  }
  
  public List<BlockPosition> getMovedBlocks()
  {
    return this.e;
  }
  
  public List<BlockPosition> getBrokenBlocks()
  {
    return this.f;
  }
}
