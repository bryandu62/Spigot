package net.minecraft.server.v1_8_R3;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AttributeMapBase
{
  protected final Map<IAttribute, AttributeInstance> a = Maps.newHashMap();
  protected final Map<String, AttributeInstance> b = new InsensitiveStringMap();
  protected final Multimap<IAttribute, IAttribute> c = HashMultimap.create();
  
  public AttributeInstance a(IAttribute ☃)
  {
    return (AttributeInstance)this.a.get(☃);
  }
  
  public AttributeInstance a(String ☃)
  {
    return (AttributeInstance)this.b.get(☃);
  }
  
  public AttributeInstance b(IAttribute ☃)
  {
    if (this.b.containsKey(☃.getName())) {
      throw new IllegalArgumentException("Attribute is already registered!");
    }
    AttributeInstance ☃ = c(☃);
    this.b.put(☃.getName(), ☃);
    this.a.put(☃, ☃);
    
    IAttribute ☃ = ☃.d();
    while (☃ != null)
    {
      this.c.put(☃, ☃);
      ☃ = ☃.d();
    }
    return ☃;
  }
  
  protected abstract AttributeInstance c(IAttribute paramIAttribute);
  
  public Collection<AttributeInstance> a()
  {
    return this.b.values();
  }
  
  public void a(AttributeInstance ☃) {}
  
  public void a(Multimap<String, AttributeModifier> ☃)
  {
    for (Map.Entry<String, AttributeModifier> ☃ : ☃.entries())
    {
      AttributeInstance ☃ = a((String)☃.getKey());
      if (☃ != null) {
        ☃.c((AttributeModifier)☃.getValue());
      }
    }
  }
  
  public void b(Multimap<String, AttributeModifier> ☃)
  {
    for (Map.Entry<String, AttributeModifier> ☃ : ☃.entries())
    {
      AttributeInstance ☃ = a((String)☃.getKey());
      if (☃ != null)
      {
        ☃.c((AttributeModifier)☃.getValue());
        ☃.b((AttributeModifier)☃.getValue());
      }
    }
  }
}
