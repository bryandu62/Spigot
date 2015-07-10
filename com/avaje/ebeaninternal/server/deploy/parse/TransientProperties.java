package com.avaje.ebeaninternal.server.deploy.parse;

import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyAssocOne;
import java.util.List;

public class TransientProperties
{
  public void process(DeployBeanDescriptor<?> desc)
  {
    List<DeployBeanProperty> props = desc.propertiesBase();
    for (int i = 0; i < props.size(); i++)
    {
      DeployBeanProperty prop = (DeployBeanProperty)props.get(i);
      if ((!prop.isDbRead()) && (!prop.isDbInsertable()) && (!prop.isDbUpdateable())) {
        prop.setTransient(true);
      }
    }
    List<DeployBeanPropertyAssocOne<?>> ones = desc.propertiesAssocOne();
    for (int i = 0; i < ones.size(); i++)
    {
      DeployBeanPropertyAssocOne<?> prop = (DeployBeanPropertyAssocOne)ones.get(i);
      if ((prop.getBeanTable() == null) && 
        (!prop.isEmbedded())) {
        prop.setTransient(true);
      }
    }
    List<DeployBeanPropertyAssocMany<?>> manys = desc.propertiesAssocMany();
    for (int i = 0; i < manys.size(); i++)
    {
      DeployBeanPropertyAssocMany<?> prop = (DeployBeanPropertyAssocMany)manys.get(i);
      if (prop.getBeanTable() == null) {
        prop.setTransient(true);
      }
    }
  }
}
