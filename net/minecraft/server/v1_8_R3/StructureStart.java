package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public abstract class StructureStart
{
  protected LinkedList<StructurePiece> a = new LinkedList();
  protected StructureBoundingBox b;
  private int c;
  private int d;
  
  public StructureStart() {}
  
  public StructureStart(int ☃, int ☃)
  {
    this.c = ☃;
    this.d = ☃;
  }
  
  public StructureBoundingBox a()
  {
    return this.b;
  }
  
  public LinkedList<StructurePiece> b()
  {
    return this.a;
  }
  
  public void a(World ☃, Random ☃, StructureBoundingBox ☃)
  {
    Iterator<StructurePiece> ☃ = this.a.iterator();
    while (☃.hasNext())
    {
      StructurePiece ☃ = (StructurePiece)☃.next();
      if ((☃.c().a(☃)) && 
        (!☃.a(☃, ☃, ☃))) {
        ☃.remove();
      }
    }
  }
  
  protected void c()
  {
    this.b = StructureBoundingBox.a();
    for (StructurePiece ☃ : this.a) {
      this.b.b(☃.c());
    }
  }
  
  public NBTTagCompound a(int ☃, int ☃)
  {
    NBTTagCompound ☃ = new NBTTagCompound();
    
    ☃.setString("id", WorldGenFactory.a(this));
    ☃.setInt("ChunkX", ☃);
    ☃.setInt("ChunkZ", ☃);
    ☃.set("BB", this.b.g());
    
    NBTTagList ☃ = new NBTTagList();
    for (StructurePiece ☃ : this.a) {
      ☃.add(☃.b());
    }
    ☃.set("Children", ☃);
    
    a(☃);
    
    return ☃;
  }
  
  public void a(NBTTagCompound ☃) {}
  
  public void a(World ☃, NBTTagCompound ☃)
  {
    this.c = ☃.getInt("ChunkX");
    this.d = ☃.getInt("ChunkZ");
    if (☃.hasKey("BB")) {
      this.b = new StructureBoundingBox(☃.getIntArray("BB"));
    }
    NBTTagList ☃ = ☃.getList("Children", 10);
    for (int ☃ = 0; ☃ < ☃.size(); ☃++) {
      this.a.add(WorldGenFactory.b(☃.get(☃), ☃));
    }
    b(☃);
  }
  
  public void b(NBTTagCompound ☃) {}
  
  protected void a(World ☃, Random ☃, int ☃)
  {
    int ☃ = ☃.F() - ☃;
    
    int ☃ = this.b.d() + 1;
    if (☃ < ☃) {
      ☃ += ☃.nextInt(☃ - ☃);
    }
    int ☃ = ☃ - this.b.e;
    this.b.a(0, ☃, 0);
    for (StructurePiece ☃ : this.a) {
      ☃.a(0, ☃, 0);
    }
  }
  
  protected void a(World ☃, Random ☃, int ☃, int ☃)
  {
    int ☃ = ☃ - ☃ + 1 - this.b.d();
    int ☃ = 1;
    if (☃ > 1) {
      ☃ = ☃ + ☃.nextInt(☃);
    } else {
      ☃ = ☃;
    }
    int ☃ = ☃ - this.b.b;
    this.b.a(0, ☃, 0);
    for (StructurePiece ☃ : this.a) {
      ☃.a(0, ☃, 0);
    }
  }
  
  public boolean d()
  {
    return true;
  }
  
  public boolean a(ChunkCoordIntPair ☃)
  {
    return true;
  }
  
  public void b(ChunkCoordIntPair ☃) {}
  
  public int e()
  {
    return this.c;
  }
  
  public int f()
  {
    return this.d;
  }
}
