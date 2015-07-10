package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public class PersistentVillage
  extends PersistentBase
{
  private World world;
  private final List<BlockPosition> c = Lists.newArrayList();
  private final List<VillageDoor> d = Lists.newArrayList();
  private final List<Village> villages = Lists.newArrayList();
  private int time;
  
  public PersistentVillage(String ☃)
  {
    super(☃);
  }
  
  public PersistentVillage(World ☃)
  {
    super(a(☃.worldProvider));
    this.world = ☃;
    c();
  }
  
  public void a(World ☃)
  {
    this.world = ☃;
    for (Village ☃ : this.villages) {
      ☃.a(☃);
    }
  }
  
  public void a(BlockPosition ☃)
  {
    if (this.c.size() > 64) {
      return;
    }
    if (!e(☃)) {
      this.c.add(☃);
    }
  }
  
  public void tick()
  {
    this.time += 1;
    for (Village ☃ : this.villages) {
      ☃.a(this.time);
    }
    e();
    f();
    g();
    if (this.time % 400 == 0) {
      c();
    }
  }
  
  private void e()
  {
    for (Iterator<Village> ☃ = this.villages.iterator(); ☃.hasNext();)
    {
      Village ☃ = (Village)☃.next();
      if (☃.g())
      {
        ☃.remove();
        c();
      }
    }
  }
  
  public List<Village> getVillages()
  {
    return this.villages;
  }
  
  public Village getClosestVillage(BlockPosition ☃, int ☃)
  {
    Village ☃ = null;
    double ☃ = 3.4028234663852886E38D;
    for (Village ☃ : this.villages)
    {
      double ☃ = ☃.a().i(☃);
      if (☃ < ☃)
      {
        float ☃ = ☃ + ☃.b();
        if (☃ <= ☃ * ☃)
        {
          ☃ = ☃;
          ☃ = ☃;
        }
      }
    }
    return ☃;
  }
  
  private void f()
  {
    if (this.c.isEmpty()) {
      return;
    }
    b((BlockPosition)this.c.remove(0));
  }
  
  private void g()
  {
    for (int ☃ = 0; ☃ < this.d.size(); ☃++)
    {
      VillageDoor ☃ = (VillageDoor)this.d.get(☃);
      Village ☃ = getClosestVillage(☃.d(), 32);
      if (☃ == null)
      {
        ☃ = new Village(this.world);
        this.villages.add(☃);
        c();
      }
      ☃.a(☃);
    }
    this.d.clear();
  }
  
  private void b(BlockPosition ☃)
  {
    int ☃ = 16;int ☃ = 4;int ☃ = 16;
    for (int ☃ = -☃; ☃ < ☃; ☃++) {
      for (int ☃ = -☃; ☃ < ☃; ☃++) {
        for (int ☃ = -☃; ☃ < ☃; ☃++)
        {
          BlockPosition ☃ = ☃.a(☃, ☃, ☃);
          if (f(☃))
          {
            VillageDoor ☃ = c(☃);
            if (☃ == null) {
              d(☃);
            } else {
              ☃.a(this.time);
            }
          }
        }
      }
    }
  }
  
  private VillageDoor c(BlockPosition ☃)
  {
    for (VillageDoor ☃ : this.d) {
      if ((☃.d().getX() == ☃.getX()) && (☃.d().getZ() == ☃.getZ()) && (Math.abs(☃.d().getY() - ☃.getY()) <= 1)) {
        return ☃;
      }
    }
    for (Village ☃ : this.villages)
    {
      VillageDoor ☃ = ☃.e(☃);
      if (☃ != null) {
        return ☃;
      }
    }
    return null;
  }
  
  private void d(BlockPosition ☃)
  {
    EnumDirection ☃ = BlockDoor.h(this.world, ☃);
    EnumDirection ☃ = ☃.opposite();
    
    int ☃ = a(☃, ☃, 5);
    int ☃ = a(☃, ☃, ☃ + 1);
    if (☃ != ☃) {
      this.d.add(new VillageDoor(☃, ☃ < ☃ ? ☃ : ☃, this.time));
    }
  }
  
  private int a(BlockPosition ☃, EnumDirection ☃, int ☃)
  {
    int ☃ = 0;
    for (int ☃ = 1; ☃ <= 5; ☃++) {
      if (this.world.i(☃.shift(☃, ☃)))
      {
        ☃++;
        if (☃ >= ☃) {
          return ☃;
        }
      }
    }
    return ☃;
  }
  
  private boolean e(BlockPosition ☃)
  {
    for (BlockPosition ☃ : this.c) {
      if (☃.equals(☃)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean f(BlockPosition ☃)
  {
    Block ☃ = this.world.getType(☃).getBlock();
    if ((☃ instanceof BlockDoor)) {
      return ☃.getMaterial() == Material.WOOD;
    }
    return false;
  }
  
  public void a(NBTTagCompound ☃)
  {
    this.time = ☃.getInt("Tick");
    NBTTagList ☃ = ☃.getList("Villages", 10);
    for (int ☃ = 0; ☃ < ☃.size(); ☃++)
    {
      NBTTagCompound ☃ = ☃.get(☃);
      Village ☃ = new Village();
      ☃.a(☃);
      this.villages.add(☃);
    }
  }
  
  public void b(NBTTagCompound ☃)
  {
    ☃.setInt("Tick", this.time);
    NBTTagList ☃ = new NBTTagList();
    for (Village ☃ : this.villages)
    {
      NBTTagCompound ☃ = new NBTTagCompound();
      ☃.b(☃);
      ☃.add(☃);
    }
    ☃.set("Villages", ☃);
  }
  
  public static String a(WorldProvider ☃)
  {
    return "villages" + ☃.getSuffix();
  }
}
