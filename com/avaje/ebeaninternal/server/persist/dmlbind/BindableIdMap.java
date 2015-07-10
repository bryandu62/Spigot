package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import javax.persistence.PersistenceException;

public final class BindableIdMap
  implements BindableId
{
  private final BeanProperty[] uids;
  private final MatchedImportedProperty[] matches;
  
  public BindableIdMap(BeanProperty[] uids, BeanDescriptor<?> desc)
  {
    this.uids = uids;
    this.matches = MatchedImportedProperty.build(uids, desc);
  }
  
  public boolean isEmpty()
  {
    return false;
  }
  
  public boolean isConcatenated()
  {
    return true;
  }
  
  public String getIdentityColumn()
  {
    return null;
  }
  
  public String toString()
  {
    return Arrays.toString(this.uids);
  }
  
  public void addChanged(PersistRequestBean<?> request, List<Bindable> list) {}
  
  public void dmlWhere(GenerateDmlRequest request, boolean checkIncludes, Object bean)
  {
    dmlAppend(request, false);
  }
  
  public void dmlInsert(GenerateDmlRequest request, boolean checkIncludes)
  {
    dmlAppend(request, checkIncludes);
  }
  
  public void dmlAppend(GenerateDmlRequest request, boolean checkIncludes)
  {
    for (int i = 0; i < this.uids.length; i++) {
      request.appendColumn(this.uids[i].getDbColumn());
    }
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
    LinkedHashMap<String, Object> mapId = new LinkedHashMap();
    for (int i = 0; i < this.uids.length; i++)
    {
      Object value = this.uids[i].getValue(bean);
      
      bindRequest.bind(value, this.uids[i], this.uids[i].getName(), bindNull);
      
      mapId.put(this.uids[i].getName(), value);
    }
    bindRequest.setIdValue(mapId);
  }
  
  public boolean deriveConcatenatedId(PersistRequestBean<?> persist)
  {
    if (this.matches == null)
    {
      String m = "Matches for the concatinated key columns where not found? I expect that the concatinated key was null, and this bean does not have ManyToOne assoc beans matching the primary key columns?";
      
      throw new PersistenceException(m);
    }
    Object bean = persist.getBean();
    for (int i = 0; i < this.matches.length; i++) {
      this.matches[i].populate(bean, bean);
    }
    return true;
  }
}
