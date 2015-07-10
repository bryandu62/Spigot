package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public enum EnumDirection
  implements INamable
{
  private final int g;
  private final int h;
  private final int i;
  private final String j;
  private final EnumAxis k;
  private final EnumAxisDirection l;
  private final BaseBlockPosition m;
  private static final EnumDirection[] n;
  private static final EnumDirection[] o;
  private static final Map<String, EnumDirection> p;
  
  static
  {
    n = new EnumDirection[6];
    o = new EnumDirection[4];
    p = Maps.newHashMap();
    for (EnumDirection ☃ : values())
    {
      n[☃.g] = ☃;
      if (☃.k().c()) {
        o[☃.i] = ☃;
      }
      p.put(☃.j().toLowerCase(), ☃);
    }
  }
  
  private EnumDirection(int ☃, int ☃, int ☃, String ☃, EnumAxisDirection ☃, EnumAxis ☃, BaseBlockPosition ☃)
  {
    this.g = ☃;
    this.i = ☃;
    this.h = ☃;
    this.j = ☃;
    this.k = ☃;
    this.l = ☃;
    this.m = ☃;
  }
  
  public int a()
  {
    return this.g;
  }
  
  public int b()
  {
    return this.i;
  }
  
  public EnumAxisDirection c()
  {
    return this.l;
  }
  
  public EnumDirection opposite()
  {
    return fromType1(this.h);
  }
  
  public EnumDirection e()
  {
    switch (1.b[ordinal()])
    {
    case 1: 
      return EAST;
    case 2: 
      return SOUTH;
    case 3: 
      return WEST;
    case 4: 
      return NORTH;
    }
    throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
  }
  
  public EnumDirection f()
  {
    switch (1.b[ordinal()])
    {
    case 1: 
      return WEST;
    case 2: 
      return NORTH;
    case 3: 
      return EAST;
    case 4: 
      return SOUTH;
    }
    throw new IllegalStateException("Unable to get CCW facing of " + this);
  }
  
  public int getAdjacentX()
  {
    if (this.k == EnumAxis.X) {
      return this.l.a();
    }
    return 0;
  }
  
  public int getAdjacentY()
  {
    if (this.k == EnumAxis.Y) {
      return this.l.a();
    }
    return 0;
  }
  
  public int getAdjacentZ()
  {
    if (this.k == EnumAxis.Z) {
      return this.l.a();
    }
    return 0;
  }
  
  public String j()
  {
    return this.j;
  }
  
  public EnumAxis k()
  {
    return this.k;
  }
  
  public static EnumDirection fromType1(int ☃)
  {
    return n[MathHelper.a(☃ % n.length)];
  }
  
  public static EnumDirection fromType2(int ☃)
  {
    return o[MathHelper.a(☃ % o.length)];
  }
  
  public static EnumDirection fromAngle(double ☃)
  {
    return fromType2(MathHelper.floor(☃ / 90.0D + 0.5D) & 0x3);
  }
  
  public static EnumDirection a(Random ☃)
  {
    return values()[☃.nextInt(values().length)];
  }
  
  public String toString()
  {
    return this.j;
  }
  
  public String getName()
  {
    return this.j;
  }
  
  public static EnumDirection a(EnumAxisDirection ☃, EnumAxis ☃)
  {
    for (EnumDirection ☃ : ) {
      if ((☃.c() == ☃) && (☃.k() == ☃)) {
        return ☃;
      }
    }
    throw new IllegalArgumentException("No such direction: " + ☃ + " " + ☃);
  }
  
  public static enum EnumAxis
    implements Predicate<EnumDirection>, INamable
  {
    private static final Map<String, EnumAxis> d;
    private final String e;
    private final EnumDirection.EnumDirectionLimit f;
    
    static
    {
      d = Maps.newHashMap();
      for (EnumAxis ☃ : values()) {
        d.put(☃.a().toLowerCase(), ☃);
      }
    }
    
    private EnumAxis(String ☃, EnumDirection.EnumDirectionLimit ☃)
    {
      this.e = ☃;
      this.f = ☃;
    }
    
    public String a()
    {
      return this.e;
    }
    
    public boolean b()
    {
      return this.f == EnumDirection.EnumDirectionLimit.VERTICAL;
    }
    
    public boolean c()
    {
      return this.f == EnumDirection.EnumDirectionLimit.HORIZONTAL;
    }
    
    public String toString()
    {
      return this.e;
    }
    
    public boolean a(EnumDirection ☃)
    {
      return (☃ != null) && (☃.k() == this);
    }
    
    public EnumDirection.EnumDirectionLimit d()
    {
      return this.f;
    }
    
    public String getName()
    {
      return this.e;
    }
  }
  
  public static enum EnumAxisDirection
  {
    private final int c;
    private final String d;
    
    private EnumAxisDirection(int ☃, String ☃)
    {
      this.c = ☃;
      this.d = ☃;
    }
    
    public int a()
    {
      return this.c;
    }
    
    public String toString()
    {
      return this.d;
    }
  }
  
  public static enum EnumDirectionLimit
    implements Predicate<EnumDirection>, Iterable<EnumDirection>
  {
    private EnumDirectionLimit() {}
    
    public EnumDirection[] a()
    {
      switch (EnumDirection.1.c[ordinal()])
      {
      case 1: 
        return new EnumDirection[] { EnumDirection.NORTH, EnumDirection.EAST, EnumDirection.SOUTH, EnumDirection.WEST };
      case 2: 
        return new EnumDirection[] { EnumDirection.UP, EnumDirection.DOWN };
      }
      throw new Error("Someone's been tampering with the universe!");
    }
    
    public EnumDirection a(Random ☃)
    {
      EnumDirection[] ☃ = a();
      return ☃[☃.nextInt(☃.length)];
    }
    
    public boolean a(EnumDirection ☃)
    {
      return (☃ != null) && (☃.k().d() == this);
    }
    
    public Iterator<EnumDirection> iterator()
    {
      return Iterators.forArray(a());
    }
  }
}
