package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import java.sql.SQLException;
import java.util.List;

public class BindableIdEmpty
  implements BindableId
{
  public boolean isEmpty()
  {
    return true;
  }
  
  public void addChanged(PersistRequestBean<?> request, List<Bindable> list) {}
  
  public void dmlInsert(GenerateDmlRequest request, boolean checkIncludes) {}
  
  public void dmlAppend(GenerateDmlRequest request, boolean checkIncludes) {}
  
  public void dmlWhere(GenerateDmlRequest request, boolean checkIncludes, Object bean) {}
  
  public void dmlBind(BindableRequest request, boolean checkIncludes, Object bean)
    throws SQLException
  {}
  
  public void dmlBindWhere(BindableRequest request, boolean checkIncludes, Object bean)
    throws SQLException
  {}
  
  public boolean isConcatenated()
  {
    return false;
  }
  
  public String getIdentityColumn()
  {
    return null;
  }
  
  public boolean deriveConcatenatedId(PersistRequestBean<?> persist)
  {
    return false;
  }
}
