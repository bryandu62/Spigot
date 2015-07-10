package net.minecraft.server.v1_8_R3;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public class StructureBoundingBox
{
  public int a;
  public int b;
  public int c;
  public int d;
  public int e;
  public int f;
  
  public StructureBoundingBox() {}
  
  public StructureBoundingBox(int[] ☃)
  {
    if (☃.length == 6)
    {
      this.a = ☃[0];
      this.b = ☃[1];
      this.c = ☃[2];
      this.d = ☃[3];
      this.e = ☃[4];
      this.f = ☃[5];
    }
  }
  
  public static StructureBoundingBox a()
  {
    return new StructureBoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
  }
  
  public static StructureBoundingBox a(int ☃, int ☃, int ☃, int ☃, int ☃, int ☃, int ☃, int ☃, int ☃, EnumDirection ☃)
  {
    switch (1.a[☃.ordinal()])
    {
    default: 
      return new StructureBoundingBox(☃ + ☃, ☃ + ☃, ☃ + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃);
    case 1: 
      return new StructureBoundingBox(☃ + ☃, ☃ + ☃, ☃ - ☃ + 1 + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃);
    case 2: 
      return new StructureBoundingBox(☃ + ☃, ☃ + ☃, ☃ + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃);
    case 3: 
      return new StructureBoundingBox(☃ - ☃ + 1 + ☃, ☃ + ☃, ☃ + ☃, ☃ + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃);
    }
    return new StructureBoundingBox(☃ + ☃, ☃ + ☃, ☃ + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃, ☃ + ☃ - 1 + ☃);
  }
  
  public static StructureBoundingBox a(int ☃, int ☃, int ☃, int ☃, int ☃, int ☃)
  {
    return new StructureBoundingBox(Math.min(☃, ☃), Math.min(☃, ☃), Math.min(☃, ☃), Math.max(☃, ☃), Math.max(☃, ☃), Math.max(☃, ☃));
  }
  
  public StructureBoundingBox(StructureBoundingBox ☃)
  {
    this.a = ☃.a;
    this.b = ☃.b;
    this.c = ☃.c;
    this.d = ☃.d;
    this.e = ☃.e;
    this.f = ☃.f;
  }
  
  public StructureBoundingBox(int ☃, int ☃, int ☃, int ☃, int ☃, int ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
    this.d = ☃;
    this.e = ☃;
    this.f = ☃;
  }
  
  public StructureBoundingBox(BaseBlockPosition ☃, BaseBlockPosition ☃)
  {
    this.a = Math.min(☃.getX(), ☃.getX());
    this.b = Math.min(☃.getY(), ☃.getY());
    this.c = Math.min(☃.getZ(), ☃.getZ());
    this.d = Math.max(☃.getX(), ☃.getX());
    this.e = Math.max(☃.getY(), ☃.getY());
    this.f = Math.max(☃.getZ(), ☃.getZ());
  }
  
  public StructureBoundingBox(int ☃, int ☃, int ☃, int ☃)
  {
    this.a = ☃;
    this.c = ☃;
    this.d = ☃;
    this.f = ☃;
    
    this.b = 1;
    this.e = 512;
  }
  
  public boolean a(StructureBoundingBox ☃)
  {
    return (this.d >= ☃.a) && (this.a <= ☃.d) && (this.f >= ☃.c) && (this.c <= ☃.f) && (this.e >= ☃.b) && (this.b <= ☃.e);
  }
  
  public boolean a(int ☃, int ☃, int ☃, int ☃)
  {
    return (this.d >= ☃) && (this.a <= ☃) && (this.f >= ☃) && (this.c <= ☃);
  }
  
  public void b(StructureBoundingBox ☃)
  {
    this.a = Math.min(this.a, ☃.a);
    this.b = Math.min(this.b, ☃.b);
    this.c = Math.min(this.c, ☃.c);
    this.d = Math.max(this.d, ☃.d);
    this.e = Math.max(this.e, ☃.e);
    this.f = Math.max(this.f, ☃.f);
  }
  
  public void a(int ☃, int ☃, int ☃)
  {
    this.a += ☃;
    this.b += ☃;
    this.c += ☃;
    this.d += ☃;
    this.e += ☃;
    this.f += ☃;
  }
  
  public boolean b(BaseBlockPosition ☃)
  {
    return (☃.getX() >= this.a) && (☃.getX() <= this.d) && (☃.getZ() >= this.c) && (☃.getZ() <= this.f) && (☃.getY() >= this.b) && (☃.getY() <= this.e);
  }
  
  public BaseBlockPosition b()
  {
    return new BaseBlockPosition(this.d - this.a, this.e - this.b, this.f - this.c);
  }
  
  public int c()
  {
    return this.d - this.a + 1;
  }
  
  public int d()
  {
    return this.e - this.b + 1;
  }
  
  public int e()
  {
    return this.f - this.c + 1;
  }
  
  public BaseBlockPosition f()
  {
    return new BlockPosition(this.a + (this.d - this.a + 1) / 2, this.b + (this.e - this.b + 1) / 2, this.c + (this.f - this.c + 1) / 2);
  }
  
  public String toString()
  {
    return Objects.toStringHelper(this).add("x0", this.a).add("y0", this.b).add("z0", this.c).add("x1", this.d).add("y1", this.e).add("z1", this.f).toString();
  }
  
  public NBTTagIntArray g()
  {
    return new NBTTagIntArray(new int[] { this.a, this.b, this.c, this.d, this.e, this.f });
  }
}
