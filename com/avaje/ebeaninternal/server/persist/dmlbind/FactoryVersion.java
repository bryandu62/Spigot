package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import java.util.ArrayList;
import java.util.List;

public class FactoryVersion
{
  public Bindable create(BeanDescriptor<?> desc)
  {
    List<Bindable> verList = new ArrayList();
    
    BeanProperty[] vers = desc.propertiesVersion();
    for (int i = 0; i < vers.length; i++) {
      verList.add(new BindableProperty(vers[i]));
    }
    BeanPropertyAssocOne<?>[] embedded = desc.propertiesEmbedded();
    for (int j = 0; j < embedded.length; j++) {
      if (embedded[j].isEmbeddedVersion())
      {
        List<Bindable> bindList = new ArrayList();
        
        BeanProperty[] embProps = embedded[j].getProperties();
        for (int i = 0; i < embProps.length; i++) {
          if (embProps[i].isVersion()) {
            bindList.add(new BindableProperty(embProps[i]));
          }
        }
        verList.add(new BindableEmbedded(embedded[j], bindList));
      }
    }
    if (verList.size() == 0) {
      return null;
    }
    if (verList.size() == 1) {
      return (Bindable)verList.get(0);
    }
    return new BindableList(verList);
  }
}
