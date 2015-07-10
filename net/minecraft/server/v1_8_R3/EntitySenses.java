package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;

public class EntitySenses
{
  EntityInsentient a;
  List<Entity> b = Lists.newArrayList();
  List<Entity> c = Lists.newArrayList();
  
  public EntitySenses(EntityInsentient ☃)
  {
    this.a = ☃;
  }
  
  public void a()
  {
    this.b.clear();
    this.c.clear();
  }
  
  public boolean a(Entity ☃)
  {
    if (this.b.contains(☃)) {
      return true;
    }
    if (this.c.contains(☃)) {
      return false;
    }
    this.a.world.methodProfiler.a("canSee");
    boolean ☃ = this.a.hasLineOfSight(☃);
    this.a.world.methodProfiler.b();
    if (☃) {
      this.b.add(☃);
    } else {
      this.c.add(☃);
    }
    return ☃;
  }
}
