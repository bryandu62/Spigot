package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import java.sql.SQLException;
import java.util.List;

public abstract interface Bindable
{
  public abstract void addChanged(PersistRequestBean<?> paramPersistRequestBean, List<Bindable> paramList);
  
  public abstract void dmlInsert(GenerateDmlRequest paramGenerateDmlRequest, boolean paramBoolean);
  
  public abstract void dmlAppend(GenerateDmlRequest paramGenerateDmlRequest, boolean paramBoolean);
  
  public abstract void dmlWhere(GenerateDmlRequest paramGenerateDmlRequest, boolean paramBoolean, Object paramObject);
  
  public abstract void dmlBind(BindableRequest paramBindableRequest, boolean paramBoolean, Object paramObject)
    throws SQLException;
  
  public abstract void dmlBindWhere(BindableRequest paramBindableRequest, boolean paramBoolean, Object paramObject)
    throws SQLException;
}
