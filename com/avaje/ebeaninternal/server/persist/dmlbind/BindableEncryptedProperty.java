package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebean.config.EncryptKey;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import java.sql.SQLException;
import java.util.List;

public class BindableEncryptedProperty
  implements Bindable
{
  private final BeanProperty prop;
  private final boolean bindEncryptDataFirst;
  
  public BindableEncryptedProperty(BeanProperty prop, boolean bindEncryptDataFirst)
  {
    this.prop = prop;
    this.bindEncryptDataFirst = bindEncryptDataFirst;
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
    if ((checkIncludes) && (!request.isIncluded(this.prop))) {
      return;
    }
    request.appendColumn(this.prop.getDbColumn(), this.prop.getDbBind());
  }
  
  public void dmlAppend(GenerateDmlRequest request, boolean checkIncludes)
  {
    if ((checkIncludes) && (!request.isIncluded(this.prop))) {
      return;
    }
    request.appendColumn(this.prop.getDbColumn(), "=", this.prop.getDbBind());
  }
  
  public void dmlWhere(GenerateDmlRequest request, boolean checkIncludes, Object bean)
  {
    if ((checkIncludes) && (!request.isIncluded(this.prop))) {
      return;
    }
    if ((bean == null) || (request.isDbNull(this.prop.getValue(bean)))) {
      request.appendColumnIsNull(this.prop.getDbColumn());
    } else {
      request.appendColumn("? = ", this.prop.getDecryptSql());
    }
  }
  
  public void dmlBind(BindableRequest request, boolean checkIncludes, Object bean)
    throws SQLException
  {
    if ((checkIncludes) && (!request.isIncluded(this.prop))) {
      return;
    }
    Object value = null;
    if (bean != null) {
      value = this.prop.getValue(bean);
    }
    String encryptKeyValue = this.prop.getEncryptKey().getStringValue();
    if (!this.bindEncryptDataFirst) {
      request.bindNoLog(encryptKeyValue, 12, this.prop.getName() + "=****");
    }
    request.bindNoLog(value, this.prop, this.prop.getName(), true);
    if (this.bindEncryptDataFirst) {
      request.bindNoLog(encryptKeyValue, 12, this.prop.getName() + "=****");
    }
  }
  
  public void dmlBindWhere(BindableRequest request, boolean checkIncludes, Object bean)
    throws SQLException
  {
    if ((checkIncludes) && (!request.isIncluded(this.prop))) {
      return;
    }
    Object value = null;
    if (bean != null) {
      value = this.prop.getValue(bean);
    }
    String encryptKeyValue = this.prop.getEncryptKey().getStringValue();
    
    request.bind(value, this.prop, this.prop.getName(), false);
    request.bindNoLog(encryptKeyValue, 12, this.prop.getName() + "=****");
  }
}
