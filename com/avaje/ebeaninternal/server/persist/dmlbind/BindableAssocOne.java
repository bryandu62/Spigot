package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.api.DerivedRelationshipData;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.id.ImportedId;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import java.sql.SQLException;
import java.util.List;

public class BindableAssocOne
  implements Bindable
{
  private final BeanPropertyAssocOne<?> assocOne;
  private final ImportedId importedId;
  
  public BindableAssocOne(BeanPropertyAssocOne<?> assocOne)
  {
    this.assocOne = assocOne;
    this.importedId = assocOne.getImportedId();
  }
  
  public String toString()
  {
    return "BindableAssocOne " + this.assocOne;
  }
  
  public void addChanged(PersistRequestBean<?> request, List<Bindable> list)
  {
    if (request.hasChanged(this.assocOne)) {
      list.add(this);
    }
  }
  
  public void dmlInsert(GenerateDmlRequest request, boolean checkIncludes)
  {
    dmlAppend(request, checkIncludes);
  }
  
  public void dmlAppend(GenerateDmlRequest request, boolean checkIncludes)
  {
    if ((checkIncludes) && (!request.isIncluded(this.assocOne))) {
      return;
    }
    this.importedId.dmlAppend(request);
  }
  
  public void dmlWhere(GenerateDmlRequest request, boolean checkIncludes, Object bean)
  {
    if ((checkIncludes) && (!request.isIncludedWhere(this.assocOne))) {
      return;
    }
    Object assocBean = this.assocOne.getValue(bean);
    this.importedId.dmlWhere(request, assocBean);
  }
  
  public void dmlBind(BindableRequest request, boolean checkIncludes, Object bean)
    throws SQLException
  {
    if ((checkIncludes) && (!request.isIncluded(this.assocOne))) {
      return;
    }
    dmlBind(request, bean, true);
  }
  
  public void dmlBindWhere(BindableRequest request, boolean checkIncludes, Object bean)
    throws SQLException
  {
    if ((checkIncludes) && (!request.isIncludedWhere(this.assocOne))) {
      return;
    }
    dmlBind(request, bean, false);
  }
  
  private void dmlBind(BindableRequest request, Object bean, boolean bindNull)
    throws SQLException
  {
    Object assocBean = this.assocOne.getValue(bean);
    Object boundValue = this.importedId.bind(request, assocBean, bindNull);
    if ((bindNull) && (boundValue == null) && (assocBean != null))
    {
      DerivedRelationshipData d = new DerivedRelationshipData(assocBean, this.assocOne.getName(), bean);
      request.registerDerivedRelationship(d);
    }
  }
}
