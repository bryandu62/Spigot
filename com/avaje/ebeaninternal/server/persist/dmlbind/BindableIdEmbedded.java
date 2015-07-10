package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.persistence.PersistenceException;

public final class BindableIdEmbedded
  implements BindableId
{
  private final BeanPropertyAssocOne<?> embId;
  private final BeanProperty[] props;
  private final MatchedImportedProperty[] matches;
  
  public BindableIdEmbedded(BeanPropertyAssocOne<?> embId, BeanDescriptor<?> desc)
  {
    this.embId = embId;
    this.props = embId.getTargetDescriptor().propertiesBaseScalar();
    this.matches = MatchedImportedProperty.build(this.props, desc);
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
    return this.embId + " props:" + Arrays.toString(this.props);
  }
  
  public void addChanged(PersistRequestBean<?> request, List<Bindable> list) {}
  
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
    if ((checkIncludes) && (!bindRequest.isIncluded(this.embId))) {
      return;
    }
    Object idValue = this.embId.getValue(bean);
    for (int i = 0; i < this.props.length; i++)
    {
      Object value = this.props[i].getValue(idValue);
      bindRequest.bind(value, this.props[i], this.props[i].getDbColumn(), bindNull);
    }
    bindRequest.setIdValue(idValue);
  }
  
  public void dmlWhere(GenerateDmlRequest request, boolean checkIncludes, Object bean)
  {
    if ((checkIncludes) && (!request.isIncluded(this.embId))) {
      return;
    }
    dmlAppend(request, false);
  }
  
  public void dmlInsert(GenerateDmlRequest request, boolean checkIncludes)
  {
    dmlAppend(request, checkIncludes);
  }
  
  public void dmlAppend(GenerateDmlRequest request, boolean checkIncludes)
  {
    if ((checkIncludes) && (!request.isIncluded(this.embId))) {
      return;
    }
    for (int i = 0; i < this.props.length; i++) {
      request.appendColumn(this.props[i].getDbColumn());
    }
  }
  
  public boolean deriveConcatenatedId(PersistRequestBean<?> persist)
  {
    if (this.matches == null)
    {
      String m = "Matches for the concatinated key columns where not found? I expect that the concatinated key was null, and this bean does not have ManyToOne assoc beans matching the primary key columns?";
      
      throw new PersistenceException(m);
    }
    Object bean = persist.getBean();
    
    Object newId = this.embId.createEmbeddedId();
    for (int i = 0; i < this.matches.length; i++) {
      this.matches[i].populate(bean, newId);
    }
    this.embId.setValueIntercept(bean, newId);
    return true;
  }
}
