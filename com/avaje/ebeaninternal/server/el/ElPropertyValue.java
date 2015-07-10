package com.avaje.ebeaninternal.server.el;

import com.avaje.ebean.text.StringFormatter;
import com.avaje.ebean.text.StringParser;

public abstract interface ElPropertyValue
  extends ElPropertyDeploy
{
  public abstract Object[] getAssocOneIdValues(Object paramObject);
  
  public abstract String getAssocOneIdExpr(String paramString1, String paramString2);
  
  public abstract String getAssocIdInValueExpr(int paramInt);
  
  public abstract String getAssocIdInExpr(String paramString);
  
  public abstract boolean isAssocId();
  
  public abstract boolean isAssocProperty();
  
  public abstract boolean isLocalEncrypted();
  
  public abstract boolean isDbEncrypted();
  
  public abstract int getDeployOrder();
  
  public abstract StringParser getStringParser();
  
  public abstract StringFormatter getStringFormatter();
  
  public abstract boolean isDateTimeCapable();
  
  public abstract int getJdbcType();
  
  public abstract Object parseDateTime(long paramLong);
  
  public abstract Object elGetValue(Object paramObject);
  
  public abstract Object elGetReference(Object paramObject);
  
  public abstract void elSetValue(Object paramObject1, Object paramObject2, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void elSetReference(Object paramObject);
  
  public abstract Object elConvertType(Object paramObject);
}
