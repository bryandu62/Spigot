package com.avaje.ebeaninternal.server.deploy.id;

import com.avaje.ebeaninternal.server.core.InternString;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanFkeyProperty;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssoc;
import com.avaje.ebeaninternal.server.deploy.DbSqlContext;
import com.avaje.ebeaninternal.server.deploy.IntersectionRow;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import com.avaje.ebeaninternal.server.persist.dmlbind.BindableRequest;
import com.avaje.ebeaninternal.util.ValueUtil;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.persistence.PersistenceException;

public final class ImportedIdSimple
  implements ImportedId, Comparable<ImportedIdSimple>
{
  private static final class EntryComparator
    implements Comparator<ImportedIdSimple>
  {
    public int compare(ImportedIdSimple o1, ImportedIdSimple o2)
    {
      return o1.compareTo(o2);
    }
  }
  
  private static final EntryComparator COMPARATOR = new EntryComparator(null);
  protected final BeanPropertyAssoc<?> owner;
  protected final String localDbColumn;
  protected final String logicalName;
  protected final BeanProperty foreignProperty;
  protected final int position;
  
  public ImportedIdSimple(BeanPropertyAssoc<?> owner, String localDbColumn, BeanProperty foreignProperty, int position)
  {
    this.owner = owner;
    this.localDbColumn = InternString.intern(localDbColumn);
    this.foreignProperty = foreignProperty;
    this.position = position;
    this.logicalName = InternString.intern(owner.getName() + "." + foreignProperty.getName());
  }
  
  public static ImportedIdSimple[] sort(List<ImportedIdSimple> list)
  {
    ImportedIdSimple[] importedIds = (ImportedIdSimple[])list.toArray(new ImportedIdSimple[list.size()]);
    
    Arrays.sort(importedIds, COMPARATOR);
    return importedIds;
  }
  
  public boolean equals(Object obj)
  {
    return obj == this;
  }
  
  public int compareTo(ImportedIdSimple other)
  {
    return this.position == other.position ? 0 : this.position < other.position ? -1 : 1;
  }
  
  public void addFkeys(String name)
  {
    BeanFkeyProperty fkey = new BeanFkeyProperty(null, name + "." + this.foreignProperty.getName(), this.localDbColumn, this.owner.getDeployOrder());
    this.owner.getBeanDescriptor().add(fkey);
  }
  
  public boolean isScalar()
  {
    return true;
  }
  
  public String getLogicalName()
  {
    return this.logicalName;
  }
  
  public String getDbColumn()
  {
    return this.localDbColumn;
  }
  
  private Object getIdValue(Object bean)
  {
    return this.foreignProperty.getValueWithInheritance(bean);
  }
  
  public void buildImport(IntersectionRow row, Object other)
  {
    Object value = getIdValue(other);
    if (value == null)
    {
      String msg = "Foreign Key value null?";
      throw new PersistenceException(msg);
    }
    row.put(this.localDbColumn, value);
  }
  
  public void sqlAppend(DbSqlContext ctx)
  {
    ctx.appendColumn(this.localDbColumn);
  }
  
  public void dmlAppend(GenerateDmlRequest request)
  {
    request.appendColumn(this.localDbColumn);
  }
  
  public void dmlWhere(GenerateDmlRequest request, Object bean)
  {
    if (this.owner.isDbUpdatable())
    {
      Object value = null;
      if (bean != null) {
        value = getIdValue(bean);
      }
      if (value == null) {
        request.appendColumnIsNull(this.localDbColumn);
      } else {
        request.appendColumn(this.localDbColumn);
      }
    }
  }
  
  public boolean hasChanged(Object bean, Object oldValues)
  {
    Object id = getIdValue(bean);
    if (oldValues != null)
    {
      Object oldId = getIdValue(oldValues);
      return !ValueUtil.areEqual(id, oldId);
    }
    return true;
  }
  
  public Object bind(BindableRequest request, Object bean, boolean bindNull)
    throws SQLException
  {
    Object value = null;
    if (bean != null) {
      value = getIdValue(bean);
    }
    request.bind(value, this.foreignProperty, this.localDbColumn, bindNull);
    return value;
  }
  
  public BeanProperty findMatchImport(String matchDbColumn)
  {
    if (matchDbColumn.equals(this.localDbColumn)) {
      return this.foreignProperty;
    }
    return null;
  }
}
