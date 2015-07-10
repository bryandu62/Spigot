package com.avaje.ebeaninternal.server.persist.dml;

import com.avaje.ebeaninternal.server.core.ConcurrencyMode;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.persist.dmlbind.Bindable;
import com.avaje.ebeaninternal.server.persist.dmlbind.BindableId;
import java.sql.SQLException;
import java.util.Set;

public final class DeleteMeta
{
  private final String sqlVersion;
  private final String sqlNone;
  private final BindableId id;
  private final Bindable version;
  private final Bindable all;
  private final String tableName;
  private final boolean emptyStringAsNull;
  
  public DeleteMeta(boolean emptyStringAsNull, BeanDescriptor<?> desc, BindableId id, Bindable version, Bindable all)
  {
    this.emptyStringAsNull = emptyStringAsNull;
    this.tableName = desc.getBaseTable();
    this.id = id;
    this.version = version;
    this.all = all;
    
    this.sqlNone = genSql(ConcurrencyMode.NONE);
    this.sqlVersion = genSql(ConcurrencyMode.VERSION);
  }
  
  public boolean isEmptyStringAsNull()
  {
    return this.emptyStringAsNull;
  }
  
  public String getTableName()
  {
    return this.tableName;
  }
  
  public void bind(PersistRequestBean<?> persist, DmlHandler bind)
    throws SQLException
  {
    Object bean = persist.getBean();
    
    this.id.dmlBind(bind, false, bean);
    switch (persist.getConcurrencyMode())
    {
    case VERSION: 
      this.version.dmlBind(bind, false, bean);
      break;
    case ALL: 
      Object oldBean = persist.getOldValues();
      this.all.dmlBindWhere(bind, true, oldBean);
      break;
    }
  }
  
  public String getSql(PersistRequestBean<?> request)
    throws SQLException
  {
    if (this.id.isEmpty()) {
      throw new IllegalStateException("Can not deleteById on " + request.getFullName() + " as no @Id property");
    }
    switch (request.determineConcurrencyMode())
    {
    case NONE: 
      return this.sqlNone;
    case VERSION: 
      return this.sqlVersion;
    case ALL: 
      return genDynamicWhere(request.getLoadedProperties(), request.getOldValues());
    }
    throw new RuntimeException("Invalid mode " + request.determineConcurrencyMode());
  }
  
  private String genSql(ConcurrencyMode conMode)
  {
    GenerateDmlRequest request = new GenerateDmlRequest(this.emptyStringAsNull);
    
    request.append("delete from ").append(this.tableName);
    request.append(" where ");
    
    request.setWhereIdMode();
    this.id.dmlAppend(request, false);
    if (ConcurrencyMode.VERSION.equals(conMode))
    {
      if (this.version == null) {
        return null;
      }
      this.version.dmlAppend(request, false);
    }
    else if (ConcurrencyMode.ALL.equals(conMode))
    {
      throw new RuntimeException("Never called for ConcurrencyMode.ALL");
    }
    return request.toString();
  }
  
  private String genDynamicWhere(Set<String> includedProps, Object oldBean)
    throws SQLException
  {
    GenerateDmlRequest request = new GenerateDmlRequest(this.emptyStringAsNull, includedProps, oldBean);
    
    request.append(this.sqlNone);
    
    request.setWhereMode();
    this.all.dmlWhere(request, true, oldBean);
    
    return request.toString();
  }
}
