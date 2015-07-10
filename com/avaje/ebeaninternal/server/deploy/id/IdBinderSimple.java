package com.avaje.ebeaninternal.server.deploy.id;

import com.avaje.ebeaninternal.api.SpiExpressionRequest;
import com.avaje.ebeaninternal.server.core.DefaultSqlUpdate;
import com.avaje.ebeaninternal.server.core.InternString;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.DbReadContext;
import com.avaje.ebeaninternal.server.deploy.DbSqlContext;
import com.avaje.ebeaninternal.server.type.DataBind;
import com.avaje.ebeaninternal.server.type.ScalarType;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

public final class IdBinderSimple
  implements IdBinder
{
  private final BeanProperty idProperty;
  private final String bindIdSql;
  private final BeanProperty[] properties;
  private final Class<?> expectedType;
  private final ScalarType scalarType;
  
  public IdBinderSimple(BeanProperty idProperty)
  {
    this.idProperty = idProperty;
    this.scalarType = idProperty.getScalarType();
    this.expectedType = idProperty.getPropertyType();
    this.properties = new BeanProperty[1];
    this.properties[0] = idProperty;
    this.bindIdSql = InternString.intern(idProperty.getDbColumn() + " = ? ");
  }
  
  public void initialise() {}
  
  public Object readTerm(String idTermValue)
  {
    return this.scalarType.parse(idTermValue);
  }
  
  public String writeTerm(Object idValue)
  {
    return this.scalarType.format(idValue);
  }
  
  public String getOrderBy(String pathPrefix, boolean ascending)
  {
    StringBuilder sb = new StringBuilder();
    if (pathPrefix != null) {
      sb.append(pathPrefix).append(".");
    }
    sb.append(this.idProperty.getName());
    if (!ascending) {
      sb.append(" desc");
    }
    return sb.toString();
  }
  
  public void buildSelectExpressionChain(String prefix, List<String> selectChain)
  {
    this.idProperty.buildSelectExpressionChain(prefix, selectChain);
  }
  
  public void createLdapNameById(LdapName name, Object id)
    throws InvalidNameException
  {
    Rdn rdn = new Rdn(this.idProperty.getDbColumn(), id);
    name.add(rdn);
  }
  
  public void createLdapNameByBean(LdapName name, Object bean)
    throws InvalidNameException
  {
    Object id = this.idProperty.getValue(bean);
    createLdapNameById(name, id);
  }
  
  public int getPropertyCount()
  {
    return 1;
  }
  
  public String getIdProperty()
  {
    return this.idProperty.getName();
  }
  
  public BeanProperty findBeanProperty(String dbColumnName)
  {
    if (dbColumnName.equalsIgnoreCase(this.idProperty.getDbColumn())) {
      return this.idProperty;
    }
    return null;
  }
  
  public boolean isComplexId()
  {
    return false;
  }
  
  public String getDefaultOrderBy()
  {
    return this.idProperty.getName();
  }
  
  public BeanProperty[] getProperties()
  {
    return this.properties;
  }
  
  public String getBindIdInSql(String baseTableAlias)
  {
    if (baseTableAlias == null) {
      return this.idProperty.getDbColumn();
    }
    return baseTableAlias + "." + this.idProperty.getDbColumn();
  }
  
  public String getBindIdSql(String baseTableAlias)
  {
    if (baseTableAlias == null) {
      return this.bindIdSql;
    }
    return baseTableAlias + "." + this.bindIdSql;
  }
  
  public Object[] getIdValues(Object bean)
  {
    return new Object[] { this.idProperty.getValue(bean) };
  }
  
  public Object[] getBindValues(Object idValue)
  {
    return new Object[] { idValue };
  }
  
  public String getIdInValueExprDelete(int size)
  {
    return getIdInValueExpr(size);
  }
  
  public String getIdInValueExpr(int size)
  {
    StringBuilder sb = new StringBuilder(2 * size + 10);
    sb.append(" in");
    sb.append(" (?");
    for (int i = 1; i < size; i++) {
      sb.append(",?");
    }
    sb.append(") ");
    return sb.toString();
  }
  
  public void addIdInBindValue(SpiExpressionRequest request, Object value)
  {
    value = convertSetId(value, null);
    request.addBindValue(value);
  }
  
  public void bindId(DefaultSqlUpdate sqlUpdate, Object value)
  {
    sqlUpdate.addParameter(value);
  }
  
  public void bindId(DataBind dataBind, Object value)
    throws SQLException
  {
    value = this.idProperty.toBeanType(value);
    this.idProperty.bind(dataBind, value);
  }
  
  public void writeData(DataOutput os, Object value)
    throws IOException
  {
    this.idProperty.writeData(os, value);
  }
  
  public Object readData(DataInput is)
    throws IOException
  {
    return this.idProperty.readData(is);
  }
  
  public void loadIgnore(DbReadContext ctx)
  {
    this.idProperty.loadIgnore(ctx);
  }
  
  public Object readSet(DbReadContext ctx, Object bean)
    throws SQLException
  {
    Object id = this.idProperty.read(ctx);
    if (id != null) {
      this.idProperty.setValue(bean, id);
    }
    return id;
  }
  
  public Object read(DbReadContext ctx)
    throws SQLException
  {
    return this.idProperty.read(ctx);
  }
  
  public void appendSelect(DbSqlContext ctx, boolean subQuery)
  {
    this.idProperty.appendSelect(ctx, subQuery);
  }
  
  public String getAssocOneIdExpr(String prefix, String operator)
  {
    StringBuilder sb = new StringBuilder();
    if (prefix != null)
    {
      sb.append(prefix);
      sb.append(".");
    }
    sb.append(this.idProperty.getName());
    sb.append(operator);
    return sb.toString();
  }
  
  public String getAssocIdInExpr(String prefix)
  {
    StringBuilder sb = new StringBuilder();
    if (prefix != null)
    {
      sb.append(prefix);
      sb.append(".");
    }
    sb.append(this.idProperty.getName());
    return sb.toString();
  }
  
  public Object convertSetId(Object idValue, Object bean)
  {
    if (!idValue.getClass().equals(this.expectedType)) {
      idValue = this.scalarType.toBeanType(idValue);
    }
    if (bean != null) {
      this.idProperty.setValueIntercept(bean, idValue);
    }
    return idValue;
  }
}
