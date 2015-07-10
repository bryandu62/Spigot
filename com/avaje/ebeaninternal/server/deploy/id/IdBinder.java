package com.avaje.ebeaninternal.server.deploy.id;

import com.avaje.ebeaninternal.api.SpiExpressionRequest;
import com.avaje.ebeaninternal.server.core.DefaultSqlUpdate;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.DbReadContext;
import com.avaje.ebeaninternal.server.deploy.DbSqlContext;
import com.avaje.ebeaninternal.server.type.DataBind;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;

public abstract interface IdBinder
{
  public abstract void initialise();
  
  public abstract String writeTerm(Object paramObject);
  
  public abstract Object readTerm(String paramString);
  
  public abstract void writeData(DataOutput paramDataOutput, Object paramObject)
    throws IOException;
  
  public abstract Object readData(DataInput paramDataInput)
    throws IOException;
  
  public abstract void createLdapNameById(LdapName paramLdapName, Object paramObject)
    throws InvalidNameException;
  
  public abstract void createLdapNameByBean(LdapName paramLdapName, Object paramObject)
    throws InvalidNameException;
  
  public abstract String getIdProperty();
  
  public abstract BeanProperty findBeanProperty(String paramString);
  
  public abstract int getPropertyCount();
  
  public abstract boolean isComplexId();
  
  public abstract String getDefaultOrderBy();
  
  public abstract String getOrderBy(String paramString, boolean paramBoolean);
  
  public abstract Object[] getBindValues(Object paramObject);
  
  public abstract Object[] getIdValues(Object paramObject);
  
  public abstract String getAssocOneIdExpr(String paramString1, String paramString2);
  
  public abstract String getAssocIdInExpr(String paramString);
  
  public abstract void bindId(DataBind paramDataBind, Object paramObject)
    throws SQLException;
  
  public abstract void bindId(DefaultSqlUpdate paramDefaultSqlUpdate, Object paramObject);
  
  public abstract void addIdInBindValue(SpiExpressionRequest paramSpiExpressionRequest, Object paramObject);
  
  public abstract String getBindIdInSql(String paramString);
  
  public abstract String getIdInValueExpr(int paramInt);
  
  public abstract String getIdInValueExprDelete(int paramInt);
  
  public abstract void buildSelectExpressionChain(String paramString, List<String> paramList);
  
  public abstract Object readSet(DbReadContext paramDbReadContext, Object paramObject)
    throws SQLException;
  
  public abstract void loadIgnore(DbReadContext paramDbReadContext);
  
  public abstract Object read(DbReadContext paramDbReadContext)
    throws SQLException;
  
  public abstract void appendSelect(DbSqlContext paramDbSqlContext, boolean paramBoolean);
  
  public abstract String getBindIdSql(String paramString);
  
  public abstract BeanProperty[] getProperties();
  
  public abstract Object convertSetId(Object paramObject1, Object paramObject2);
}
