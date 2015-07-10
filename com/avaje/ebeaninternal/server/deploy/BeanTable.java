package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebeaninternal.server.core.InternString;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanTable;
import com.avaje.ebeaninternal.server.deploy.meta.DeployTableJoin;
import com.avaje.ebeaninternal.server.deploy.meta.DeployTableJoinColumn;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BeanTable
{
  private static final Logger logger = Logger.getLogger(BeanTable.class.getName());
  private final Class<?> beanType;
  private final String baseTable;
  private final BeanProperty[] idProperties;
  
  public BeanTable(DeployBeanTable mutable, BeanDescriptorMap owner)
  {
    this.beanType = mutable.getBeanType();
    this.baseTable = InternString.intern(mutable.getBaseTable());
    this.idProperties = mutable.createIdProperties(owner);
  }
  
  public String toString()
  {
    return this.baseTable;
  }
  
  public String getBaseTable()
  {
    return this.baseTable;
  }
  
  public String getUnqualifiedBaseTable()
  {
    String[] chunks = this.baseTable.split("\\.");
    return chunks.length == 2 ? chunks[1] : chunks[0];
  }
  
  public BeanProperty[] getIdProperties()
  {
    return this.idProperties;
  }
  
  public Class<?> getBeanType()
  {
    return this.beanType;
  }
  
  public void createJoinColumn(String foreignKeyPrefix, DeployTableJoin join, boolean reverse)
  {
    boolean complexKey = false;
    BeanProperty[] props = this.idProperties;
    if ((this.idProperties.length == 1) && 
      ((this.idProperties[0] instanceof BeanPropertyAssocOne)))
    {
      BeanPropertyAssocOne<?> assocOne = (BeanPropertyAssocOne)this.idProperties[0];
      props = assocOne.getProperties();
      complexKey = true;
    }
    for (int i = 0; i < props.length; i++)
    {
      String lc = props[i].getDbColumn();
      String fk = lc;
      if (foreignKeyPrefix != null) {
        fk = foreignKeyPrefix + "_" + fk;
      }
      if (complexKey)
      {
        boolean usePrefixOnComplex = GlobalProperties.getBoolean("ebean.prefixComplexKeys", false);
        if (!usePrefixOnComplex)
        {
          String msg = "On table[" + this.baseTable + "] foreign key column [" + lc + "]";
          logger.log(Level.FINE, msg);
          fk = lc;
        }
      }
      DeployTableJoinColumn joinCol = new DeployTableJoinColumn(lc, fk);
      if (reverse) {
        joinCol = joinCol.reverse();
      }
      join.addJoinColumn(joinCol);
    }
  }
}
