package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.api.DerivedRelationshipData;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import java.sql.SQLException;

public abstract interface BindableRequest
{
  public abstract void setIdValue(Object paramObject);
  
  public abstract Object bind(Object paramObject, BeanProperty paramBeanProperty, String paramString, boolean paramBoolean)
    throws SQLException;
  
  public abstract Object bind(String paramString, Object paramObject, int paramInt)
    throws SQLException;
  
  public abstract Object bindNoLog(Object paramObject, int paramInt, String paramString)
    throws SQLException;
  
  public abstract Object bindNoLog(Object paramObject, BeanProperty paramBeanProperty, String paramString, boolean paramBoolean)
    throws SQLException;
  
  public abstract boolean isIncluded(BeanProperty paramBeanProperty);
  
  public abstract boolean isIncludedWhere(BeanProperty paramBeanProperty);
  
  public abstract void registerUpdateGenValue(BeanProperty paramBeanProperty, Object paramObject1, Object paramObject2);
  
  public abstract void registerAdditionalProperty(String paramString);
  
  public abstract PersistRequestBean<?> getPersistRequest();
  
  public abstract void registerDerivedRelationship(DerivedRelationshipData paramDerivedRelationshipData);
}
