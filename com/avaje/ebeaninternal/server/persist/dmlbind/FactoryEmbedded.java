package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.persist.dml.DmlMode;
import java.util.ArrayList;
import java.util.List;

public class FactoryEmbedded
{
  private final FactoryProperty factoryProperty;
  
  public FactoryEmbedded(boolean bindEncryptDataFirst)
  {
    this.factoryProperty = new FactoryProperty(bindEncryptDataFirst);
  }
  
  public void create(List<Bindable> list, BeanDescriptor<?> desc, DmlMode mode, boolean withLobs)
  {
    BeanPropertyAssocOne<?>[] embedded = desc.propertiesEmbedded();
    for (int j = 0; j < embedded.length; j++)
    {
      List<Bindable> bindList = new ArrayList();
      
      BeanProperty[] props = embedded[j].getProperties();
      for (int i = 0; i < props.length; i++)
      {
        Bindable item = this.factoryProperty.create(props[i], mode, withLobs);
        if (item != null) {
          bindList.add(item);
        }
      }
      list.add(new BindableEmbedded(embedded[j], bindList));
    }
  }
}
