package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebeaninternal.server.deploy.id.ImportedId;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeployUpdateMapFactory
{
  private static final Logger logger = Logger.getLogger(DeployUpdateMapFactory.class.getName());
  
  public static Map<String, String> build(BeanDescriptor<?> descriptor)
  {
    Map<String, String> deployMap = new HashMap();
    
    String shortName = descriptor.getName();
    String beanName = shortName.toLowerCase();
    deployMap.put(beanName, descriptor.getBaseTable());
    
    BeanProperty[] baseScalar = descriptor.propertiesBaseScalar();
    for (BeanProperty baseProp : baseScalar) {
      if ((baseProp.isDbInsertable()) || (baseProp.isDbUpdatable())) {
        deployMap.put(baseProp.getName().toLowerCase(), baseProp.getDbColumn());
      }
    }
    BeanPropertyAssocOne<?>[] oneImported = descriptor.propertiesOneImported();
    for (BeanPropertyAssocOne<?> assocOne : oneImported)
    {
      ImportedId importedId = assocOne.getImportedId();
      if (importedId == null)
      {
        String m = descriptor.getFullName() + " importedId is null for associated: " + assocOne.getFullBeanName();
        logger.log(Level.SEVERE, m);
      }
      else if (importedId.isScalar())
      {
        deployMap.put(importedId.getLogicalName(), importedId.getDbColumn());
      }
    }
    return deployMap;
  }
}
