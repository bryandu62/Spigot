package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.InheritInfo;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.PersistenceException;

public class BindableDiscriminator
  implements Bindable
{
  private final String columnName;
  private final Object discValue;
  private final int sqlType;
  
  public BindableDiscriminator(InheritInfo inheritInfo)
  {
    this.columnName = inheritInfo.getDiscriminatorColumn();
    this.discValue = inheritInfo.getDiscriminatorValue();
    this.sqlType = inheritInfo.getDiscriminatorType();
  }
  
  public String toString()
  {
    return this.columnName + " = " + this.discValue;
  }
  
  public void addChanged(PersistRequestBean<?> request, List<Bindable> list)
  {
    throw new PersistenceException("Never called (only for inserts)");
  }
  
  public void dmlInsert(GenerateDmlRequest request, boolean checkIncludes)
  {
    dmlAppend(request, checkIncludes);
  }
  
  public void dmlWhere(GenerateDmlRequest request, boolean checkIncludes, Object bean) {}
  
  public void dmlAppend(GenerateDmlRequest request, boolean checkIncludes)
  {
    request.appendColumn(this.columnName);
  }
  
  public void dmlBind(BindableRequest bindRequest, boolean checkIncludes, Object bean)
    throws SQLException
  {
    bindRequest.bind(this.columnName, this.discValue, this.sqlType);
  }
  
  public void dmlBindWhere(BindableRequest bindRequest, boolean checkIncludes, Object bean)
    throws SQLException
  {
    bindRequest.bind(this.columnName, this.discValue, this.sqlType);
  }
}
