package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyCompound;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class BindableCompound
  implements Bindable
{
  private final Bindable[] items;
  private final BeanPropertyCompound compound;
  
  public BindableCompound(BeanPropertyCompound embProp, List<Bindable> list)
  {
    this.compound = embProp;
    this.items = ((Bindable[])list.toArray(new Bindable[list.size()]));
  }
  
  public String toString()
  {
    return "BindableCompound " + this.compound + " items:" + Arrays.toString(this.items);
  }
  
  public void dmlInsert(GenerateDmlRequest request, boolean checkIncludes)
  {
    dmlAppend(request, checkIncludes);
  }
  
  public void dmlAppend(GenerateDmlRequest request, boolean checkIncludes)
  {
    if ((checkIncludes) && (!request.isIncluded(this.compound))) {
      return;
    }
    for (int i = 0; i < this.items.length; i++) {
      this.items[i].dmlAppend(request, false);
    }
  }
  
  public void dmlWhere(GenerateDmlRequest request, boolean checkIncludes, Object origBean)
  {
    if ((checkIncludes) && (!request.isIncludedWhere(this.compound))) {
      return;
    }
    Object valueObject = this.compound.getValue(origBean);
    for (int i = 0; i < this.items.length; i++) {
      this.items[i].dmlWhere(request, false, valueObject);
    }
  }
  
  public void addChanged(PersistRequestBean<?> request, List<Bindable> list)
  {
    if (request.hasChanged(this.compound)) {
      list.add(this);
    }
  }
  
  public void dmlBind(BindableRequest bindRequest, boolean checkIncludes, Object bean)
    throws SQLException
  {
    if ((checkIncludes) && (!bindRequest.isIncluded(this.compound))) {
      return;
    }
    Object valueObject = this.compound.getValue(bean);
    for (int i = 0; i < this.items.length; i++) {
      this.items[i].dmlBind(bindRequest, false, valueObject);
    }
  }
  
  public void dmlBindWhere(BindableRequest bindRequest, boolean checkIncludes, Object bean)
    throws SQLException
  {
    if ((checkIncludes) && (!bindRequest.isIncludedWhere(this.compound))) {
      return;
    }
    Object valueObject = this.compound.getValue(bean);
    for (int i = 0; i < this.items.length; i++) {
      this.items[i].dmlBindWhere(bindRequest, false, valueObject);
    }
  }
}
