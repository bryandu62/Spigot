package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.PersistenceException;

public final class BindableIdScalar
  implements BindableId
{
  private final BeanProperty uidProp;
  
  public BindableIdScalar(BeanProperty uidProp)
  {
    this.uidProp = uidProp;
  }
  
  public boolean isEmpty()
  {
    return false;
  }
  
  public boolean isConcatenated()
  {
    return false;
  }
  
  public String getIdentityColumn()
  {
    return this.uidProp.getDbColumn();
  }
  
  public String toString()
  {
    return this.uidProp.toString();
  }
  
  public void addChanged(PersistRequestBean<?> request, List<Bindable> list) {}
  
  public boolean deriveConcatenatedId(PersistRequestBean<?> persist)
  {
    throw new PersistenceException("Should not be called? only for concatinated keys");
  }
  
  public void dmlWhere(GenerateDmlRequest request, boolean checkIncludes, Object bean)
  {
    request.appendColumn(this.uidProp.getDbColumn());
  }
  
  public void dmlInsert(GenerateDmlRequest request, boolean checkIncludes)
  {
    dmlAppend(request, checkIncludes);
  }
  
  public void dmlAppend(GenerateDmlRequest request, boolean checkIncludes)
  {
    request.appendColumn(this.uidProp.getDbColumn());
  }
  
  public void dmlBind(BindableRequest request, boolean checkIncludes, Object bean)
    throws SQLException
  {
    dmlBind(request, checkIncludes, bean, true);
  }
  
  public void dmlBindWhere(BindableRequest request, boolean checkIncludes, Object bean)
    throws SQLException
  {
    dmlBind(request, checkIncludes, bean, false);
  }
  
  private void dmlBind(BindableRequest bindRequest, boolean checkIncludes, Object bean, boolean bindNull)
    throws SQLException
  {
    Object value = this.uidProp.getValue(bean);
    
    bindRequest.bind(value, this.uidProp, this.uidProp.getName(), bindNull);
    
    bindRequest.setIdValue(value);
  }
}
