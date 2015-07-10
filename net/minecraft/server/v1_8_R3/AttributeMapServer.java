package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class AttributeMapServer
  extends AttributeMapBase
{
  private final Set<AttributeInstance> e = Sets.newHashSet();
  protected final Map<String, AttributeInstance> d = new InsensitiveStringMap();
  
  public AttributeModifiable e(IAttribute ☃)
  {
    return (AttributeModifiable)super.a(☃);
  }
  
  public AttributeModifiable b(String ☃)
  {
    AttributeInstance ☃ = super.a(☃);
    if (☃ == null) {
      ☃ = (AttributeInstance)this.d.get(☃);
    }
    return (AttributeModifiable)☃;
  }
  
  public AttributeInstance b(IAttribute ☃)
  {
    AttributeInstance ☃ = super.b(☃);
    if (((☃ instanceof AttributeRanged)) && (((AttributeRanged)☃).g() != null)) {
      this.d.put(((AttributeRanged)☃).g(), ☃);
    }
    return ☃;
  }
  
  protected AttributeInstance c(IAttribute ☃)
  {
    return new AttributeModifiable(this, ☃);
  }
  
  public void a(AttributeInstance ☃)
  {
    if (☃.getAttribute().c()) {
      this.e.add(☃);
    }
    for (IAttribute ☃ : this.c.get(☃.getAttribute()))
    {
      AttributeModifiable ☃ = e(☃);
      if (☃ != null) {
        ☃.f();
      }
    }
  }
  
  public Set<AttributeInstance> getAttributes()
  {
    return this.e;
  }
  
  public Collection<AttributeInstance> c()
  {
    Set<AttributeInstance> ☃ = Sets.newHashSet();
    for (AttributeInstance ☃ : a()) {
      if (☃.getAttribute().c()) {
        ☃.add(☃);
      }
    }
    return ☃;
  }
}
