package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import java.sql.SQLException;
import java.util.List;

public class BindableProperty
  implements Bindable
{
  protected final BeanProperty prop;
  
  public BindableProperty(BeanProperty prop)
  {
    this.prop = prop;
  }
  
  public String toString()
  {
    return this.prop.toString();
  }
  
  public void addChanged(PersistRequestBean<?> request, List<Bindable> list)
  {
    if (request.hasChanged(this.prop)) {
      list.add(this);
    }
  }
  
  public void dmlInsert(GenerateDmlRequest request, boolean checkIncludes)
  {
    dmlAppend(request, checkIncludes);
  }
  
  public void dmlAppend(GenerateDmlRequest request, boolean checkIncludes)
  {
    if ((checkIncludes) && (!request.isIncluded(this.prop))) {
      return;
    }
    request.appendColumn(this.prop.getDbColumn());
  }
  
  public void dmlWhere(GenerateDmlRequest request, boolean checkIncludes, Object bean)
  {
    if ((checkIncludes) && (!request.isIncludedWhere(this.prop))) {
      return;
    }
    if ((bean == null) || (request.isDbNull(this.prop.getValue(bean)))) {
      request.appendColumnIsNull(this.prop.getDbColumn());
    } else {
      request.appendColumn(this.prop.getDbColumn());
    }
  }
  
  public void dmlBind(BindableRequest request, boolean checkIncludes, Object bean)
    throws SQLException
  {
    if ((checkIncludes) && (!request.isIncluded(this.prop))) {
      return;
    }
    dmlBind(request, bean, true);
  }
  
  public void dmlBindWhere(BindableRequest request, boolean checkIncludes, Object bean)
    throws SQLException
  {
    if ((checkIncludes) && (!request.isIncludedWhere(this.prop))) {
      return;
    }
    dmlBind(request, bean, false);
  }
  
  private void dmlBind(BindableRequest request, Object bean, boolean bindNull)
    throws SQLException
  {
    Object value = null;
    if (bean != null) {
      value = this.prop.getValue(bean);
    }
    request.bind(value, this.prop, this.prop.getName(), bindNull);
  }
}
