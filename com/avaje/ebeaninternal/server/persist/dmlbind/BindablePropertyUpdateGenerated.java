package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.generatedproperty.GeneratedProperty;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import java.sql.SQLException;
import java.util.List;

public class BindablePropertyUpdateGenerated
  extends BindableProperty
{
  private final GeneratedProperty gen;
  
  public BindablePropertyUpdateGenerated(BeanProperty prop, GeneratedProperty gen)
  {
    super(prop);
    this.gen = gen;
  }
  
  public void addChanged(PersistRequestBean<?> request, List<Bindable> list)
  {
    list.add(this);
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
    Object value = this.gen.getUpdateValue(this.prop, bean);
    
    request.bind(value, this.prop, this.prop.getName(), bindNull);
    if (request.isIncluded(this.prop)) {
      request.registerUpdateGenValue(this.prop, bean, value);
    }
  }
  
  public void dmlAppend(GenerateDmlRequest request, boolean checkIncludes)
  {
    if ((checkIncludes) && (!request.isIncluded(this.prop))) {
      return;
    }
    request.appendColumn(this.prop.getDbColumn());
  }
}
