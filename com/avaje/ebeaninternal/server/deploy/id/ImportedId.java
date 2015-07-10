package com.avaje.ebeaninternal.server.deploy.id;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.DbSqlContext;
import com.avaje.ebeaninternal.server.deploy.IntersectionRow;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import com.avaje.ebeaninternal.server.persist.dmlbind.BindableRequest;
import java.sql.SQLException;

public abstract interface ImportedId
{
  public abstract void addFkeys(String paramString);
  
  public abstract boolean isScalar();
  
  public abstract String getLogicalName();
  
  public abstract String getDbColumn();
  
  public abstract void sqlAppend(DbSqlContext paramDbSqlContext);
  
  public abstract void dmlAppend(GenerateDmlRequest paramGenerateDmlRequest);
  
  public abstract void dmlWhere(GenerateDmlRequest paramGenerateDmlRequest, Object paramObject);
  
  public abstract boolean hasChanged(Object paramObject1, Object paramObject2);
  
  public abstract Object bind(BindableRequest paramBindableRequest, Object paramObject, boolean paramBoolean)
    throws SQLException;
  
  public abstract void buildImport(IntersectionRow paramIntersectionRow, Object paramObject);
  
  public abstract BeanProperty findMatchImport(String paramString);
}
