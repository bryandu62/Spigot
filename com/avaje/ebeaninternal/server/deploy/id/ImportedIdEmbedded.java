package com.avaje.ebeaninternal.server.deploy.id;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanFkeyProperty;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssoc;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.DbSqlContext;
import com.avaje.ebeaninternal.server.deploy.IntersectionRow;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import com.avaje.ebeaninternal.server.persist.dmlbind.BindableRequest;
import com.avaje.ebeaninternal.util.ValueUtil;
import java.sql.SQLException;
import javax.persistence.PersistenceException;

public class ImportedIdEmbedded
  implements ImportedId
{
  final BeanPropertyAssoc<?> owner;
  final BeanPropertyAssocOne<?> foreignAssocOne;
  final ImportedIdSimple[] imported;
  
  public ImportedIdEmbedded(BeanPropertyAssoc<?> owner, BeanPropertyAssocOne<?> foreignAssocOne, ImportedIdSimple[] imported)
  {
    this.owner = owner;
    this.foreignAssocOne = foreignAssocOne;
    this.imported = imported;
  }
  
  public void addFkeys(String name)
  {
    BeanProperty[] embeddedProps = this.foreignAssocOne.getProperties();
    for (int i = 0; i < this.imported.length; i++)
    {
      String n = name + "." + this.foreignAssocOne.getName() + "." + embeddedProps[i].getName();
      BeanFkeyProperty fkey = new BeanFkeyProperty(null, n, this.imported[i].localDbColumn, this.foreignAssocOne.getDeployOrder());
      this.owner.getBeanDescriptor().add(fkey);
    }
  }
  
  public boolean isScalar()
  {
    return false;
  }
  
  public String getLogicalName()
  {
    return this.owner.getName() + "." + this.foreignAssocOne.getName();
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
    Object embeddedId = null;
    if (bean != null) {
      embeddedId = this.foreignAssocOne.getValue(bean);
    }
    if (embeddedId == null) {
      for (int i = 0; i < this.imported.length; i++) {
        if (this.imported[i].owner.isDbUpdatable()) {
          request.appendColumnIsNull(this.imported[i].localDbColumn);
        }
      }
    } else {
      for (int i = 0; i < this.imported.length; i++) {
        if (this.imported[i].owner.isDbUpdatable())
        {
          Object value = this.imported[i].foreignProperty.getValue(embeddedId);
          if (value == null) {
            request.appendColumnIsNull(this.imported[i].localDbColumn);
          } else {
            request.appendColumn(this.imported[i].localDbColumn);
          }
        }
      }
    }
  }
  
  public boolean hasChanged(Object bean, Object oldValues)
  {
    Object id = this.foreignAssocOne.getValue(bean);
    Object oldId = this.foreignAssocOne.getValue(oldValues);
    
    return !ValueUtil.areEqual(id, oldId);
  }
  
  public Object bind(BindableRequest request, Object bean, boolean bindNull)
    throws SQLException
  {
    Object embeddedId = null;
    if (bean != null) {
      embeddedId = this.foreignAssocOne.getValue(bean);
    }
    if (embeddedId == null) {
      for (int i = 0; i < this.imported.length; i++) {
        if (this.imported[i].owner.isUpdateable()) {
          request.bind(null, this.imported[i].foreignProperty, this.imported[i].localDbColumn, true);
        }
      }
    } else {
      for (int i = 0; i < this.imported.length; i++) {
        if (this.imported[i].owner.isUpdateable())
        {
          Object scalarValue = this.imported[i].foreignProperty.getValue(embeddedId);
          request.bind(scalarValue, this.imported[i].foreignProperty, this.imported[i].localDbColumn, true);
        }
      }
    }
    return null;
  }
  
  public void buildImport(IntersectionRow row, Object other)
  {
    Object embeddedId = this.foreignAssocOne.getValue(other);
    if (embeddedId == null)
    {
      String msg = "Foreign Key value null?";
      throw new PersistenceException(msg);
    }
    for (int i = 0; i < this.imported.length; i++)
    {
      Object scalarValue = this.imported[i].foreignProperty.getValue(embeddedId);
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
