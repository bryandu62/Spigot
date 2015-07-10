package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.id.ImportedId;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.PersistenceException;

public class BindableUnidirectional
  implements Bindable
{
  private final BeanPropertyAssocOne<?> unidirectional;
  private final ImportedId importedId;
  private final BeanDescriptor<?> desc;
  
  public BindableUnidirectional(BeanDescriptor<?> desc, BeanPropertyAssocOne<?> unidirectional)
  {
    this.desc = desc;
    this.unidirectional = unidirectional;
    this.importedId = unidirectional.getImportedId();
  }
  
  public String toString()
  {
    return "BindableShadowFKey " + this.unidirectional;
  }
  
  public void addChanged(PersistRequestBean<?> request, List<Bindable> list)
  {
    throw new PersistenceException("Never called (for insert only)");
  }
  
  public void dmlInsert(GenerateDmlRequest request, boolean checkIncludes)
  {
    dmlAppend(request, checkIncludes);
  }
  
  public void dmlAppend(GenerateDmlRequest request, boolean checkIncludes)
  {
    this.importedId.dmlAppend(request);
  }
  
  public void dmlWhere(GenerateDmlRequest request, boolean checkIncludes, Object bean)
  {
    throw new RuntimeException("Never called");
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
    PersistRequestBean<?> persistRequest = request.getPersistRequest();
    Object parentBean = persistRequest.getParentBean();
    if (parentBean == null)
    {
      Class<?> localType = this.desc.getBeanType();
      Class<?> targetType = this.unidirectional.getTargetType();
      
      String msg = "Error inserting bean [" + localType + "] with unidirectional relationship. ";
      msg = msg + "For inserts you must use cascade save on the master bean [" + targetType + "].";
      throw new PersistenceException(msg);
    }
    this.importedId.bind(request, parentBean, bindNull);
  }
}
