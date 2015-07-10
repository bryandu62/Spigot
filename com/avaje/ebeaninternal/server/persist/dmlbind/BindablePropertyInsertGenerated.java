package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.generatedproperty.GeneratedProperty;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import java.sql.SQLException;

public class BindablePropertyInsertGenerated
  extends BindableProperty
{
  private final GeneratedProperty gen;
  
  public BindablePropertyInsertGenerated(BeanProperty prop, GeneratedProperty gen)
  {
    super(prop);
    this.gen = gen;
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
  
  private void dmlBind(BindableRequest request, boolean checkIncludes, Object bean, boolean bindNull)
    throws SQLException
  {
    Object value = this.gen.getInsertValue(this.prop, bean);
    if (bean != null)
    {
      this.prop.setValueIntercept(bean, value);
      request.registerAdditionalProperty(this.prop.getName());
    }
    request.bind(value, this.prop, this.prop.getName(), bindNull);
  }
  
  public void dmlAppend(GenerateDmlRequest request, boolean checkIncludes)
  {
    request.appendColumn(this.prop.getDbColumn());
  }
}
