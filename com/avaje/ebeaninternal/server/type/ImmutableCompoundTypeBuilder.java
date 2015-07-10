package com.avaje.ebeaninternal.server.type;

import java.util.HashMap;
import java.util.Map;

public final class ImmutableCompoundTypeBuilder
{
  private static ThreadLocal<ImmutableCompoundTypeBuilder> local = new ThreadLocal()
  {
    protected synchronized ImmutableCompoundTypeBuilder initialValue()
    {
      return new ImmutableCompoundTypeBuilder();
    }
  };
  private Map<Class<?>, Entry> entryMap;
  
  public ImmutableCompoundTypeBuilder()
  {
    this.entryMap = new HashMap();
  }
  
  public static void clear()
  {
    ((ImmutableCompoundTypeBuilder)local.get()).entryMap.clear();
  }
  
  public static Object set(CtCompoundType<?> ct, String propName, Object value)
  {
    return ((ImmutableCompoundTypeBuilder)local.get()).setValue(ct, propName, value);
  }
  
  private Object setValue(CtCompoundType<?> ct, String propName, Object value)
  {
    Entry e = getEntry(ct);
    Object compoundValue = e.set(propName, value);
    if (compoundValue != null) {
      removeEntry(ct);
    }
    return compoundValue;
  }
  
  private void removeEntry(CtCompoundType<?> ct)
  {
    this.entryMap.remove(ct.getCompoundTypeClass());
  }
  
  private Entry getEntry(CtCompoundType<?> ct)
  {
    Entry e = (Entry)this.entryMap.get(ct.getCompoundTypeClass());
    if (e == null)
    {
      e = new Entry(ct, null);
      this.entryMap.put(ct.getCompoundTypeClass(), e);
    }
    return e;
  }
  
  private static class Entry
  {
    private final CtCompoundType<?> ct;
    private final Map<String, Object> valueMap;
    
    private Entry(CtCompoundType<?> ct)
    {
      this.ct = ct;
      this.valueMap = new HashMap();
    }
    
    private Object set(String propName, Object value)
    {
      this.valueMap.put(propName, value);
      
      return this.ct.create(this.valueMap);
    }
  }
}
