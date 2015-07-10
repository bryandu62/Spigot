package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import java.sql.SQLException;
import java.util.List;

public class BindableList
  implements Bindable
{
  private final Bindable[] items;
  
  public BindableList(List<Bindable> list)
  {
    this.items = ((Bindable[])list.toArray(new Bindable[list.size()]));
  }
  
  public void addChanged(PersistRequestBean<?> request, List<Bindable> list)
  {
    for (int i = 0; i < this.items.length; i++) {
      this.items[i].addChanged(request, list);
    }
  }
  
  public void dmlInsert(GenerateDmlRequest request, boolean checkIncludes)
  {
    for (int i = 0; i < this.items.length; i++) {
      this.items[i].dmlInsert(request, checkIncludes);
    }
  }
  
  public void dmlAppend(GenerateDmlRequest request, boolean checkIncludes)
  {
    for (int i = 0; i < this.items.length; i++) {
      this.items[i].dmlAppend(request, checkIncludes);
    }
  }
  
  public void dmlWhere(GenerateDmlRequest request, boolean checkIncludes, Object bean)
  {
    for (int i = 0; i < this.items.length; i++) {
      this.items[i].dmlWhere(request, checkIncludes, bean);
    }
  }
  
  public void dmlBind(BindableRequest bindRequest, boolean checkIncludes, Object bean)
    throws SQLException
  {
    for (int i = 0; i < this.items.length; i++) {
      this.items[i].dmlBind(bindRequest, checkIncludes, bean);
    }
  }
  
  public void dmlBindWhere(BindableRequest bindRequest, boolean checkIncludes, Object bean)
    throws SQLException
  {
    for (int i = 0; i < this.items.length; i++) {
      this.items[i].dmlBindWhere(bindRequest, checkIncludes, bean);
    }
  }
}
