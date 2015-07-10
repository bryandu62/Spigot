package net.minecraft.server.v1_8_R3;

public class NibbleArray
{
  private final byte[] a;
  
  public NibbleArray()
  {
    this.a = new byte['ࠀ'];
  }
  
  public NibbleArray(byte[] abyte)
  {
    this.a = abyte;
    if (abyte.length != 2048) {
      throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + abyte.length);
    }
  }
  
  public int a(int i, int j, int k)
  {
    return a(b(i, j, k));
  }
  
  public void a(int i, int j, int k, int l)
  {
    a(b(i, j, k), l);
  }
  
  private int b(int i, int j, int k)
  {
    return j << 8 | k << 4 | i;
  }
  
  public int a(int i)
  {
    int j = c(i);
    
    return b(i) ? this.a[j] & 0xF : this.a[j] >> 4 & 0xF;
  }
  
  public void a(int i, int j)
  {
    int k = c(i);
    if (b(i)) {
      this.a[k] = ((byte)(this.a[k] & 0xF0 | j & 0xF));
    } else {
      this.a[k] = ((byte)(this.a[k] & 0xF | (j & 0xF) << 4));
    }
  }
  
  private boolean b(int i)
  {
    return (i & 0x1) == 0;
  }
  
  private int c(int i)
  {
    return i >> 1;
  }
  
  public byte[] a()
  {
    return this.a;
  }
}
