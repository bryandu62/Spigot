package net.minecraft.server.v1_8_R3;

public class LongHashMap<V>
{
  private transient LongHashMapEntry<V>[] entries;
  private transient int count;
  private int c;
  private int d;
  private final float e;
  private volatile transient int f;
  
  public LongHashMap()
  {
    this.e = 0.75F;
    this.d = 3072;
    this.entries = new LongHashMapEntry['က'];
    this.c = (this.entries.length - 1);
  }
  
  private static int g(long ☃)
  {
    return a((int)(☃ ^ ☃ >>> 32));
  }
  
  private static int a(int ☃)
  {
    ☃ ^= ☃ >>> 20 ^ ☃ >>> 12;
    return ☃ ^ ☃ >>> 7 ^ ☃ >>> 4;
  }
  
  private static int a(int ☃, int ☃)
  {
    return ☃ & ☃;
  }
  
  public int count()
  {
    return this.count;
  }
  
  public V getEntry(long ☃)
  {
    int ☃ = g(☃);
    for (LongHashMapEntry<V> ☃ = this.entries[a(☃, this.c)]; ☃ != null; ☃ = ☃.c) {
      if (☃.a == ☃) {
        return (V)☃.b;
      }
    }
    return null;
  }
  
  public boolean contains(long ☃)
  {
    return c(☃) != null;
  }
  
  final LongHashMapEntry<V> c(long ☃)
  {
    int ☃ = g(☃);
    for (LongHashMapEntry<V> ☃ = this.entries[a(☃, this.c)]; ☃ != null; ☃ = ☃.c) {
      if (☃.a == ☃) {
        return ☃;
      }
    }
    return null;
  }
  
  public void put(long ☃, V ☃)
  {
    int ☃ = g(☃);
    int ☃ = a(☃, this.c);
    for (LongHashMapEntry<V> ☃ = this.entries[☃]; ☃ != null; ☃ = ☃.c) {
      if (☃.a == ☃)
      {
        ☃.b = ☃;
        return;
      }
    }
    this.f += 1;
    a(☃, ☃, ☃, ☃);
  }
  
  private void b(int ☃)
  {
    LongHashMapEntry<V>[] ☃ = this.entries;
    int ☃ = ☃.length;
    if (☃ == 1073741824)
    {
      this.d = Integer.MAX_VALUE;
      return;
    }
    LongHashMapEntry<V>[] ☃ = new LongHashMapEntry[☃];
    a(☃);
    this.entries = ☃;
    this.c = (this.entries.length - 1);
    this.d = ((int)(☃ * this.e));
  }
  
  private void a(LongHashMapEntry<V>[] ☃)
  {
    LongHashMapEntry<V>[] ☃ = this.entries;
    int ☃ = ☃.length;
    for (int ☃ = 0; ☃ < ☃.length; ☃++)
    {
      LongHashMapEntry<V> ☃ = ☃[☃];
      if (☃ != null)
      {
        ☃[☃] = null;
        do
        {
          LongHashMapEntry<V> ☃ = ☃.c;
          int ☃ = a(☃.d, ☃ - 1);
          ☃.c = ☃[☃];
          ☃[☃] = ☃;
          ☃ = ☃;
        } while (☃ != null);
      }
    }
  }
  
  public V remove(long ☃)
  {
    LongHashMapEntry<V> ☃ = e(☃);
    return ☃ == null ? null : ☃.b;
  }
  
  final LongHashMapEntry<V> e(long ☃)
  {
    int ☃ = g(☃);
    int ☃ = a(☃, this.c);
    LongHashMapEntry<V> ☃ = this.entries[☃];
    LongHashMapEntry<V> ☃ = ☃;
    while (☃ != null)
    {
      LongHashMapEntry<V> ☃ = ☃.c;
      if (☃.a == ☃)
      {
        this.f += 1;
        this.count -= 1;
        if (☃ == ☃) {
          this.entries[☃] = ☃;
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
  
  static class LongHashMapEntry<V>
  {
    final long a;
    V b;
    LongHashMapEntry<V> c;
    final int d;
    
    LongHashMapEntry(int ☃, long ☃, V ☃, LongHashMapEntry<V> ☃)
    {
      this.b = ☃;
      this.c = ☃;
      this.a = ☃;
      this.d = ☃;
    }
    
    public final long a()
    {
      return this.a;
    }
    
    public final V b()
    {
      return (V)this.b;
    }
    
    public final boolean equals(Object ☃)
    {
      if (!(☃ instanceof LongHashMapEntry)) {
        return false;
      }
      LongHashMapEntry<V> ☃ = (LongHashMapEntry)☃;
      Object ☃ = Long.valueOf(a());
      Object ☃ = Long.valueOf(☃.a());
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
      return LongHashMap.f(this.a);
    }
    
    public final String toString()
    {
      return a() + "=" + b();
    }
  }
  
  private void a(int ☃, long ☃, V ☃, int ☃)
  {
    LongHashMapEntry<V> ☃ = this.entries[☃];
    this.entries[☃] = new LongHashMapEntry(☃, ☃, ☃, ☃);
    if (this.count++ >= this.d) {
      b(2 * this.entries.length);
    }
  }
}
