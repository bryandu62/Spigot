package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AttributeModifiable
  implements AttributeInstance
{
  private final AttributeMapBase a;
  private final IAttribute b;
  private final Map<Integer, Set<AttributeModifier>> c = Maps.newHashMap();
  private final Map<String, Set<AttributeModifier>> d = Maps.newHashMap();
  private final Map<UUID, AttributeModifier> e = Maps.newHashMap();
  private double f;
  private boolean g = true;
  private double h;
  
  public AttributeModifiable(AttributeMapBase ☃, IAttribute ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.f = ☃.b();
    for (int ☃ = 0; ☃ < 3; ☃++) {
      this.c.put(Integer.valueOf(☃), Sets.newHashSet());
    }
  }
  
  public IAttribute getAttribute()
  {
    return this.b;
  }
  
  public double b()
  {
    return this.f;
  }
  
  public void setValue(double ☃)
  {
    if (☃ == b()) {
      return;
    }
    this.f = ☃;
    f();
  }
  
  public Collection<AttributeModifier> a(int ☃)
  {
    return (Collection)this.c.get(Integer.valueOf(☃));
  }
  
  public Collection<AttributeModifier> c()
  {
    Set<AttributeModifier> ☃ = Sets.newHashSet();
    for (int ☃ = 0; ☃ < 3; ☃++) {
      ☃.addAll(a(☃));
    }
    return ☃;
  }
  
  public AttributeModifier a(UUID ☃)
  {
    return (AttributeModifier)this.e.get(☃);
  }
  
  public boolean a(AttributeModifier ☃)
  {
    return this.e.get(☃.a()) != null;
  }
  
  public void b(AttributeModifier ☃)
  {
    if (a(☃.a()) != null) {
      throw new IllegalArgumentException("Modifier is already applied on this attribute!");
    }
    Set<AttributeModifier> ☃ = (Set)this.d.get(☃.b());
    if (☃ == null)
    {
      ☃ = Sets.newHashSet();
      this.d.put(☃.b(), ☃);
    }
    ((Set)this.c.get(Integer.valueOf(☃.c()))).add(☃);
    ☃.add(☃);
    this.e.put(☃.a(), ☃);
    
    f();
  }
  
  protected void f()
  {
    this.g = true;
    this.a.a(this);
  }
  
  public void c(AttributeModifier ☃)
  {
    for (int ☃ = 0; ☃ < 3; ☃++)
    {
      Set<AttributeModifier> ☃ = (Set)this.c.get(Integer.valueOf(☃));
      ☃.remove(☃);
    }
    Set<AttributeModifier> ☃ = (Set)this.d.get(☃.b());
    if (☃ != null)
    {
      ☃.remove(☃);
      if (☃.isEmpty()) {
        this.d.remove(☃.b());
      }
    }
    this.e.remove(☃.a());
    f();
  }
  
  public double getValue()
  {
    if (this.g)
    {
      this.h = g();
      this.g = false;
    }
    return this.h;
  }
  
  private double g()
  {
    double ☃ = b();
    for (AttributeModifier ☃ : b(0)) {
      ☃ += ☃.d();
    }
    double ☃ = ☃;
    for (AttributeModifier ☃ : b(1)) {
      ☃ += ☃ * ☃.d();
    }
    for (AttributeModifier ☃ : b(2)) {
      ☃ *= (1.0D + ☃.d());
    }
    return this.b.a(☃);
  }
  
  private Collection<AttributeModifier> b(int ☃)
  {
    Set<AttributeModifier> ☃ = Sets.newHashSet(a(☃));
    
    IAttribute ☃ = this.b.d();
    while (☃ != null)
    {
      AttributeInstance ☃ = this.a.a(☃);
      if (☃ != null) {
        ☃.addAll(☃.a(☃));
      }
      ☃ = ☃.d();
    }
    return ☃;
  }
}
