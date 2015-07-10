package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyCompound;
import com.avaje.ebeaninternal.server.persist.dml.DmlMode;
import java.util.ArrayList;
import java.util.List;

public class FactoryBaseProperties
{
  private final FactoryProperty factoryProperty;
  
  public FactoryBaseProperties(boolean bindEncryptDataFirst)
  {
    this.factoryProperty = new FactoryProperty(bindEncryptDataFirst);
  }
  
  public void create(List<Bindable> list, BeanDescriptor<?> desc, DmlMode mode, boolean withLobs)
  {
    add(desc.propertiesBaseScalar(), list, desc, mode, withLobs);
    
    BeanPropertyCompound[] compoundProps = desc.propertiesBaseCompound();
    for (int i = 0; i < compoundProps.length; i++)
    {
      BeanProperty[] props = compoundProps[i].getScalarProperties();
      
      ArrayList<Bindable> newList = new ArrayList(props.length);
      add(props, newList, desc, mode, withLobs);
      
      BindableCompound compoundBindable = new BindableCompound(compoundProps[i], newList);
      
      list.add(compoundBindable);
    }
  }
  
  private void add(BeanProperty[] props, List<Bindable> list, BeanDescriptor<?> desc, DmlMode mode, boolean withLobs)
  {
    for (int i = 0; i < props.length; i++)
    {
      Bindable item = this.factoryProperty.create(props[i], mode, withLobs);
      if (item != null) {
        list.add(item);
      }
    }
  }
}
