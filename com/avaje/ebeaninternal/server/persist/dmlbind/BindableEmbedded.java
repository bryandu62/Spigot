package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class BindableEmbedded
  implements Bindable
{
  private final Bindable[] items;
  private final BeanPropertyAssocOne<?> embProp;
  
  public BindableEmbedded(BeanPropertyAssocOne<?> embProp, List<Bindable> list)
  {
    this.embProp = embProp;
    this.items = ((Bindable[])list.toArray(new Bindable[list.size()]));
  }
  
  public String toString()
  {
    return "BindableEmbedded " + this.embProp + " items:" + Arrays.toString(this.items);
  }
  
  public void dmlInsert(GenerateDmlRequest request, boolean checkIncludes)
  {
    dmlAppend(request, checkIncludes);
  }
  
  public void dmlAppend(GenerateDmlRequest request, boolean checkIncludes)
  {
    if ((checkIncludes) && (!request.isIncluded(this.embProp))) {
      return;
    }
    for (int i = 0; i < this.items.length; i++) {
      this.items[i].dmlAppend(request, false);
    }
  }
  
  public void dmlWhere(GenerateDmlRequest request, boolean checkIncludes, Object origBean)
  {
    if ((checkIncludes) && (!request.isIncludedWhere(this.embProp))) {
      return;
    }
    Object embBean = this.embProp.getValue(origBean);
    Object oldValues = getOldValue(embBean);
    for (int i = 0; i < this.items.length; i++) {
      this.items[i].dmlWhere(request, false, oldValues);
    }
  }
  
  public void addChanged(PersistRequestBean<?> request, List<Bindable> list)
  {
    if (request.hasChanged(this.embProp)) {
      list.add(this);
    }
  }
  
  public void dmlBind(BindableRequest bindRequest, boolean checkIncludes, Object bean)
    throws SQLException
  {
    if ((checkIncludes) && (!bindRequest.isIncluded(this.embProp))) {
      return;
    }
    Object embBean = this.embProp.getValue(bean);
    for (int i = 0; i < this.items.length; i++) {
      this.items[i].dmlBind(bindRequest, false, embBean);
    }
  }
  
  public void dmlBindWhere(BindableRequest bindRequest, boolean checkIncludes, Object bean)
    throws SQLException
  {
    if ((checkIncludes) && (!bindRequest.isIncludedWhere(this.embProp))) {
      return;
    }
    Object embBean = this.embProp.getValue(bean);
    Object oldEmbBean = getOldValue(embBean);
    for (int i = 0; i < this.items.length; i++) {
      this.items[i].dmlBindWhere(bindRequest, false, oldEmbBean);
    }
  }
  
  private Object getOldValue(Object embBean)
  {
    Object oldValues = null;
    if ((embBean instanceof EntityBean)) {
      oldValues = ((EntityBean)embBean)._ebean_getIntercept().getOldValues();
    }
    if (oldValues == null) {
      oldValues = embBean;
    }
    return oldValues;
  }
}
