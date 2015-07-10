package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.config.CompoundTypeProperty;
import com.avaje.ebeaninternal.server.query.SplitName;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public final class CtCompoundTypeScalarList
{
  private final LinkedHashMap<String, ScalarType<?>> scalarProps = new LinkedHashMap();
  private final LinkedHashMap<String, CtCompoundProperty> compoundProperties = new LinkedHashMap();
  
  public List<CtCompoundProperty> getNonScalarProperties()
  {
    List<CtCompoundProperty> nonScalarProps = new ArrayList();
    for (String propKey : this.compoundProperties.keySet()) {
      if (!this.scalarProps.containsKey(propKey)) {
        nonScalarProps.add(this.compoundProperties.get(propKey));
      }
    }
    return nonScalarProps;
  }
  
  public void addCompoundProperty(String propName, CtCompoundType<?> t, CompoundTypeProperty<?, ?> prop)
  {
    CtCompoundProperty parent = null;
    String[] split = SplitName.split(propName);
    if (split[0] != null) {
      parent = (CtCompoundProperty)this.compoundProperties.get(split[0]);
    }
    CtCompoundProperty p = new CtCompoundProperty(propName, parent, t, prop);
    this.compoundProperties.put(propName, p);
  }
  
  public void addScalarType(String propName, ScalarType<?> scalar)
  {
    this.scalarProps.put(propName, scalar);
  }
  
  public CtCompoundProperty getCompoundType(String propName)
  {
    return (CtCompoundProperty)this.compoundProperties.get(propName);
  }
  
  public Set<Map.Entry<String, ScalarType<?>>> entries()
  {
    return this.scalarProps.entrySet();
  }
}
