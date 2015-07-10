package com.avaje.ebeaninternal.server.persist.dml;

import com.avaje.ebeaninternal.api.SpiUpdatePlan;
import com.avaje.ebeaninternal.server.core.ConcurrencyMode;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.persist.dmlbind.Bindable;
import com.avaje.ebeaninternal.server.persist.dmlbind.BindableId;
import com.avaje.ebeaninternal.server.persist.dmlbind.BindableList;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.PersistenceException;

public final class UpdateMeta
{
  private final String sqlVersion;
  private final String sqlNone;
  private final Bindable set;
  private final BindableId id;
  private final Bindable version;
  private final Bindable all;
  private final String tableName;
  private final UpdatePlan modeNoneUpdatePlan;
  private final UpdatePlan modeVersionUpdatePlan;
  private final boolean emptyStringAsNull;
  
  public UpdateMeta(boolean emptyStringAsNull, BeanDescriptor<?> desc, Bindable set, BindableId id, Bindable version, Bindable all)
  {
    this.emptyStringAsNull = emptyStringAsNull;
    this.tableName = desc.getBaseTable();
    this.set = set;
    this.id = id;
    this.version = version;
    this.all = all;
    
    this.sqlNone = genSql(ConcurrencyMode.NONE, null, null);
    this.sqlVersion = genSql(ConcurrencyMode.VERSION, null, null);
    
    this.modeNoneUpdatePlan = new UpdatePlan(ConcurrencyMode.NONE, this.sqlNone, set);
    this.modeVersionUpdatePlan = new UpdatePlan(ConcurrencyMode.VERSION, this.sqlVersion, set);
  }
  
  public boolean isEmptyStringAsNull()
  {
    return this.emptyStringAsNull;
  }
  
  public String getTableName()
  {
    return this.tableName;
  }
  
  public void bind(PersistRequestBean<?> persist, DmlHandler bind, SpiUpdatePlan updatePlan)
    throws SQLException
  {
    Object bean = persist.getBean();
    
    bind.bindLogAppend(" set[");
    
    updatePlan.bindSet(bind, bean);
    
    bind.bindLogAppend("] where[");
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
  
  public SpiUpdatePlan getUpdatePlan(PersistRequestBean<?> request)
  {
    ConcurrencyMode mode = request.determineConcurrencyMode();
    if (request.isDynamicUpdateSql()) {
      return getDynamicUpdatePlan(mode, request);
    }
    switch (mode)
    {
    case NONE: 
      return this.modeNoneUpdatePlan;
    case VERSION: 
      return this.modeVersionUpdatePlan;
    case ALL: 
      Object oldValues = request.getOldValues();
      if (oldValues == null) {
        throw new PersistenceException("OldValues are null?");
      }
      String sql = genDynamicWhere(request.getUpdatedProperties(), request.getLoadedProperties(), oldValues);
      return new UpdatePlan(ConcurrencyMode.ALL, sql, this.set);
    }
    throw new RuntimeException("Invalid mode " + mode);
  }
  
  private SpiUpdatePlan getDynamicUpdatePlan(ConcurrencyMode mode, PersistRequestBean<?> persistRequest)
  {
    Set<String> updatedProps = persistRequest.getUpdatedProperties();
    if (ConcurrencyMode.ALL.equals(mode))
    {
      String sql = genSql(mode, persistRequest, null);
      if (sql == null) {
        return UpdatePlan.EMPTY_SET_CLAUSE;
      }
      return new UpdatePlan(null, mode, sql, this.set, updatedProps);
    }
    int hash = mode.hashCode();
    hash = hash * 31 + (updatedProps == null ? 0 : updatedProps.hashCode());
    Integer key = Integer.valueOf(hash);
    
    BeanDescriptor<?> beanDescriptor = persistRequest.getBeanDescriptor();
    SpiUpdatePlan updatePlan = beanDescriptor.getUpdatePlan(key);
    if (updatePlan != null) {
      return updatePlan;
    }
    List<Bindable> list = new ArrayList();
    this.set.addChanged(persistRequest, list);
    BindableList bindableList = new BindableList(list);
    
    String sql = genSql(mode, persistRequest, bindableList);
    
    updatePlan = new UpdatePlan(key, mode, sql, bindableList, null);
    
    beanDescriptor.putUpdatePlan(key, updatePlan);
    
    return updatePlan;
  }
  
  private String genSql(ConcurrencyMode conMode, PersistRequestBean<?> persistRequest, BindableList bindableList)
  {
    GenerateDmlRequest request;
    GenerateDmlRequest request;
    if (persistRequest == null) {
      request = new GenerateDmlRequest(this.emptyStringAsNull);
    } else {
      request = persistRequest.createGenerateDmlRequest(this.emptyStringAsNull);
    }
    request.append("update ").append(this.tableName).append(" set ");
    
    request.setUpdateSetMode();
    if (bindableList != null) {
      bindableList.dmlAppend(request, false);
    } else {
      this.set.dmlAppend(request, true);
    }
    if (request.getBindColumnCount() == 0) {
      return null;
    }
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
      this.all.dmlWhere(request, true, request.getOldValues());
    }
    return request.toString();
  }
  
  private String genDynamicWhere(Set<String> loadedProps, Set<String> whereProps, Object oldBean)
  {
    GenerateDmlRequest request = new GenerateDmlRequest(this.emptyStringAsNull, loadedProps, whereProps, oldBean);
    
    request.append(this.sqlNone);
    
    request.setWhereMode();
    this.all.dmlWhere(request, true, oldBean);
    
    return request.toString();
  }
}
