package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanEmbedded;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyAssocOne;
import java.util.Map;
import javax.persistence.PersistenceException;

public class BeanEmbeddedMetaFactory
{
  public static BeanEmbeddedMeta create(BeanDescriptorMap owner, DeployBeanPropertyAssocOne<?> prop, BeanDescriptor<?> descriptor)
  {
    BeanDescriptor<?> targetDesc = owner.getBeanDescriptor(prop.getTargetType());
    if (targetDesc == null)
    {
      String msg = "Could not find BeanDescriptor for " + prop.getTargetType() + ". Perhaps the EmbeddedId class is not registered?";
      
      throw new PersistenceException(msg);
    }
    Map<String, String> propColMap = prop.getDeployEmbedded().getPropertyColumnMap();
    
    BeanProperty[] sourceProperties = targetDesc.propertiesBaseScalar();
    
    BeanProperty[] embeddedProperties = new BeanProperty[sourceProperties.length];
    for (int i = 0; i < sourceProperties.length; i++)
    {
      String propertyName = sourceProperties[i].getName();
      String dbColumn = (String)propColMap.get(propertyName);
      if (dbColumn == null) {
        dbColumn = sourceProperties[i].getDbColumn();
      }
      BeanPropertyOverride overrides = new BeanPropertyOverride(dbColumn);
      embeddedProperties[i] = new BeanProperty(sourceProperties[i], overrides);
    }
    return new BeanEmbeddedMeta(embeddedProperties);
  }
}
