package com.avaje.ebeaninternal.server.deploy.id;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssoc;
import com.avaje.ebeaninternal.server.deploy.DbSqlContext;
import com.avaje.ebeaninternal.server.deploy.IntersectionRow;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import com.avaje.ebeaninternal.server.persist.dmlbind.BindableRequest;
import com.avaje.ebeaninternal.util.ValueUtil;
import java.sql.SQLException;

public class ImportedIdMultiple
  implements ImportedId
{
  final BeanPropertyAssoc<?> owner;
  final ImportedIdSimple[] imported;
  
  public ImportedIdMultiple(BeanPropertyAssoc<?> owner, ImportedIdSimple[] imported)
  {
    this.owner = owner;
    this.imported = imported;
  }
  
  public void addFkeys(String name) {}
  
  public String getLogicalName()
  {
    return null;
  }
  
  public boolean isScalar()
  {
    return false;
  }
  
  public String getDbColumn()
  {
    return null;
  }
  
  public void sqlAppend(DbSqlContext ctx)
  {
    for (int i = 0; i < this.imported.length; i++) {
      ctx.appendColumn(this.imported[i].localDbColumn);
    }
  }
  
  public void dmlAppend(GenerateDmlRequest request)
  {
    for (int i = 0; i < this.imported.length; i++) {
      request.appendColumn(this.imported[i].localDbColumn);
    }
  }
  
  public void dmlWhere(GenerateDmlRequest request, Object bean)
  {
    if (bean == null) {
      for (int i = 0; i < this.imported.length; i++) {
        request.appendColumnIsNull(this.imported[i].localDbColumn);
      }
    } else {
      for (int i = 0; i < this.imported.length; i++)
      {
        Object value = this.imported[i].foreignProperty.getValue(bean);
        if (value == null) {
          request.appendColumnIsNull(this.imported[i].localDbColumn);
        } else {
          request.appendColumn(this.imported[i].localDbColumn);
        }
      }
    }
  }
  
  public boolean hasChanged(Object bean, Object oldValues)
  {
    for (int i = 0; i < this.imported.length; i++)
    {
      Object id = this.imported[i].foreignProperty.getValue(bean);
      Object oldId = this.imported[i].foreignProperty.getValue(oldValues);
      if (!ValueUtil.areEqual(id, oldId)) {
        return true;
      }
    }
    return false;
  }
  
  public Object bind(BindableRequest request, Object bean, boolean bindNull)
    throws SQLException
  {
    for (int i = 0; i < this.imported.length; i++) {
      if (this.imported[i].owner.isUpdateable())
      {
        Object scalarValue = this.imported[i].foreignProperty.getValue(bean);
        request.bind(scalarValue, this.imported[i].foreignProperty, this.imported[i].localDbColumn, true);
      }
    }
    return null;
  }
  
  public void buildImport(IntersectionRow row, Object other)
  {
    for (int i = 0; i < this.imported.length; i++)
    {
      Object scalarValue = this.imported[i].foreignProperty.getValue(other);
      row.put(this.imported[i].localDbColumn, scalarValue);
    }
  }
  
  public BeanProperty findMatchImport(String matchDbColumn)
  {
    BeanProperty p = null;
    for (int i = 0; i < this.imported.length; i++)
    {
      p = this.imported[i].findMatchImport(matchDbColumn);
      if (p != null) {
        return p;
      }
    }
    return p;
  }
}
