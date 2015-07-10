package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutMultiBlockChange
  implements Packet<PacketListenerPlayOut>
{
  private ChunkCoordIntPair a;
  private MultiBlockChangeInfo[] b;
  
  public PacketPlayOutMultiBlockChange() {}
  
  public PacketPlayOutMultiBlockChange(int ☃, short[] ☃, Chunk ☃)
  {
    this.a = new ChunkCoordIntPair(☃.locX, ☃.locZ);
    
    this.b = new MultiBlockChangeInfo[☃];
    for (int ☃ = 0; ☃ < this.b.length; ☃++) {
      this.b[☃] = new MultiBlockChangeInfo(☃[☃], ☃);
    }
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = new ChunkCoordIntPair(☃.readInt(), ☃.readInt());
    this.b = new MultiBlockChangeInfo[☃.e()];
    for (int ☃ = 0; ☃ < this.b.length; ☃++) {
      this.b[☃] = new MultiBlockChangeInfo(☃.readShort(), (IBlockData)Block.d.a(☃.e()));
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeInt(this.a.x);
    ☃.writeInt(this.a.z);
    ☃.b(this.b.length);
    for (MultiBlockChangeInfo ☃ : this.b)
    {
      ☃.writeShort(☃.b());
      ☃.b(Block.d.b(☃.c()));
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public class MultiBlockChangeInfo
  {
    private final short b;
    private final IBlockData c;
    
    public MultiBlockChangeInfo(short ☃, IBlockData ☃)
    {
      this.b = ☃;
      this.c = ☃;
    }
    
    public MultiBlockChangeInfo(short ☃, Chunk ☃)
    {
      this.b = ☃;
      this.c = ☃.getBlockData(a());
    }
    
    public BlockPosition a()
    {
      return new BlockPosition(PacketPlayOutMultiBlockChange.a(PacketPlayOutMultiBlockChange.this).a(this.b >> 12 & 0xF, this.b & 0xFF, this.b >> 8 & 0xF));
    }
    
    public short b()
    {
      return this.b;
    }
    
    public IBlockData c()
    {
      return this.c;
    }
  }
}
