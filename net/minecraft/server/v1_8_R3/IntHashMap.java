package net.minecraft.server.v1_8_R3;

public class IntHashMap<V>
{
  private transient IntHashMapEntry<V>[] a;
  private transient int b;
  private int c;
  private final float d;
  
  public IntHashMap()
  {
    this.d = 0.75F;
    this.c = 12;
    this.a = new IntHashMapEntry[16];
  }
  
  private static int g(int ☃)
  {
    ☃ ^= ☃ >>> 20 ^ ☃ >>> 12;
    return ☃ ^ ☃ >>> 7 ^ ☃ >>> 4;
  }
  
  private static int a(int ☃, int ☃)
  {
    return ☃ & ☃ - 1;
  }
  
  public V get(int ☃)
  {
    int ☃ = g(☃);
    for (IntHashMapEntry<V> ☃ = this.a[a(☃, this.a.length)]; ☃ != null; ☃ = ☃.c) {
      if (☃.a == ☃) {
        return (V)☃.b;
      }
    }
    return null;
  }
  
  public boolean b(int ☃)
  {
    return c(☃) != null;
  }
  
  final IntHashMapEntry<V> c(int ☃)
  {
    int ☃ = g(☃);
    for (IntHashMapEntry<V> ☃ = this.a[a(☃, this.a.length)]; ☃ != null; ☃ = ☃.c) {
      if (☃.a == ☃) {
        return ☃;
      }
    }
    return null;
  }
  
  public void a(int ☃, V ☃)
  {
    int ☃ = g(☃);
    int ☃ = a(☃, this.a.length);
    for (IntHashMapEntry<V> ☃ = this.a[☃]; ☃ != null; ☃ = ☃.c) {
      if (☃.a == ☃)
      {
        ☃.b = ☃;
        return;
      }
    }
    a(☃, ☃, ☃, ☃);
  }
  
  private void h(int ☃)
  {
    IntHashMapEntry<V>[] ☃ = this.a;
    int ☃ = ☃.length;
    if (☃ == 1073741824)
    {
      this.c = Integer.MAX_VALUE;
      return;
    }
    IntHashMapEntry<V>[] ☃ = new IntHashMapEntry[☃];
    a(☃);
    this.a = ☃;
    this.c = ((int)(☃ * this.d));
  }
  
  private void a(IntHashMapEntry<V>[] ☃)
  {
    IntHashMapEntry<V>[] ☃ = this.a;
    int ☃ = ☃.length;
    for (int ☃ = 0; ☃ < ☃.length; ☃++)
    {
      IntHashMapEntry<V> ☃ = ☃[☃];
      if (☃ != null)
      {
        ☃[☃] = null;
        do
        {
          IntHashMapEntry<V> ☃ = ☃.c;
          int ☃ = a(☃.d, ☃);
          ☃.c = ☃[☃];
          ☃[☃] = ☃;
          ☃ = ☃;
        } while (☃ != null);
      }
    }
  }
  
  public V d(int ☃)
  {
    IntHashMapEntry<V> ☃ = e(☃);
    return ☃ == null ? null : ☃.b;
  }
  
  final IntHashMapEntry<V> e(int ☃)
  {
    int ☃ = g(☃);
    int ☃ = a(☃, this.a.length);
    IntHashMapEntry<V> ☃ = this.a[☃];
    IntHashMapEntry<V> ☃ = ☃;
    while (☃ != null)
    {
      IntHashMapEntry<V> ☃ = ☃.c;
      if (☃.a == ☃)
      {
        this.b -= 1;
        if (☃ == ☃) {
          this.a[☃] = ☃;
        } else {
          ☃.c = ☃;
        }
        return ☃;
      }
      ☃ = ☃;
      ☃ = ☃;
    }
    return ☃;
  }
  
  public void c()
  {
    IntHashMapEntry<V>[] ☃ = this.a;
    for (int ☃ = 0; ☃ < ☃.length; ☃++) {
      ☃[☃] = null;
    }
    this.b = 0;
  }
  
  static class IntHashMapEntry<V>
  {
    final int a;
    V b;
    IntHashMapEntry<V> c;
    final int d;
    
    IntHashMapEntry(int ☃, int ☃, V ☃, IntHashMapEntry<V> ☃)
    {
      this.b = ☃;
      this.c = ☃;
      this.a = ☃;
      this.d = ☃;
    }
    
    public final int a()
    {
      return this.a;
    }
    
    public final V b()
    {
      return (V)this.b;
    }
    
    public final boolean equals(Object ☃)
    {
      if (!(☃ instanceof IntHashMapEntry)) {
        return false;
      }
      IntHashMapEntry<V> ☃ = (IntHashMapEntry)☃;
      Object ☃ = Integer.valueOf(a());
      Object ☃ = Integer.valueOf(☃.a());
      if ((☃ == ☃) || ((☃ != null) && (☃.equals(☃))))
      {
        Object ☃ = b();
        Object ☃ = ☃.b();
        if ((☃ == ☃) || ((☃ != null) && (☃.equals(☃)))) {
          return true;
        }
      }
      return false;
    }
    
    public final int hashCode()
    {
      return IntHashMap.f(this.a);
    }
    
    public final String toString()
    {
      return a() + "=" + b();
    }
  }
  
  private void a(int ☃, int ☃, V ☃, int ☃)
  {
    IntHashMapEntry<V> ☃ = this.a[☃];
    this.a[☃] = new IntHashMapEntry(☃, ☃, ☃, ☃);
    if (this.b++ >= this.c) {
      h(2 * this.a.length);
    }
  }
}
