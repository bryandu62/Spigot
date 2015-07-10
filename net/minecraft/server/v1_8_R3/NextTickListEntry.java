package net.minecraft.server.v1_8_R3;

public class NextTickListEntry
  implements Comparable<NextTickListEntry>
{
  private static long d;
  private final Block e;
  public final BlockPosition a;
  public long b;
  public int c;
  private long f;
  
  public NextTickListEntry(BlockPosition blockposition, Block block)
  {
    this.f = (d++);
    this.a = blockposition;
    this.e = block;
  }
  
  public boolean equals(Object object)
  {
    if (!(object instanceof NextTickListEntry)) {
      return false;
    }
    NextTickListEntry nextticklistentry = (NextTickListEntry)object;
    
    return (this.a.equals(nextticklistentry.a)) && (Block.a(this.e, nextticklistentry.e));
  }
  
  public int hashCode()
  {
    return this.a.hashCode();
  }
  
  public NextTickListEntry a(long i)
  {
    this.b = i;
    return this;
  }
  
  public void a(int i)
  {
    this.c = i;
  }
  
  public int a(NextTickListEntry nextticklistentry)
  {
    return this.f > nextticklistentry.f ? 1 : this.f < nextticklistentry.f ? -1 : this.c != nextticklistentry.c ? this.c - nextticklistentry.c : this.b > nextticklistentry.b ? 1 : this.b < nextticklistentry.b ? -1 : 0;
  }
  
  public String toString()
  {
    return Block.getId(this.e) + ": " + this.a + ", " + this.b + ", " + this.c + ", " + this.f;
  }
  
  public Block a()
  {
    return this.e;
  }
  
  public int compareTo(NextTickListEntry object)
  {
    return a(object);
  }
}
