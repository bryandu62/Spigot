package com.mysql.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Properties;

public class ReflectiveStatementInterceptorAdapter
  implements StatementInterceptorV2
{
  private final StatementInterceptor toProxy;
  final Method v2PostProcessMethod;
  
  public ReflectiveStatementInterceptorAdapter(StatementInterceptor toProxy)
  {
    this.toProxy = toProxy;
    this.v2PostProcessMethod = getV2PostProcessMethod(toProxy.getClass());
  }
  
  public void destroy()
  {
    this.toProxy.destroy();
  }
  
  public boolean executeTopLevelOnly()
  {
    return this.toProxy.executeTopLevelOnly();
  }
  
  public void init(Connection conn, Properties props)
    throws SQLException
  {
    this.toProxy.init(conn, props);
  }
  
  public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, Connection connection, int warningCount, boolean noIndexUsed, boolean noGoodIndexUsed, SQLException statementException)
    throws SQLException
  {
    try
    {
      return (ResultSetInternalMethods)this.v2PostProcessMethod.invoke(this.toProxy, new Object[] { sql, interceptedStatement, originalResultSet, connection, new Integer(warningCount), noIndexUsed ? Boolean.TRUE : Boolean.FALSE, noGoodIndexUsed ? Boolean.TRUE : Boolean.FALSE, statementException });
    }
    catch (IllegalArgumentException e)
    {
      SQLException sqlEx = new SQLException("Unable to reflectively invoke interceptor");
      sqlEx.initCause(e);
      
      throw sqlEx;
    }
    catch (IllegalAccessException e)
    {
      SQLException sqlEx = new SQLException("Unable to reflectively invoke interceptor");
      sqlEx.initCause(e);
      
      throw sqlEx;
    }
    catch (InvocationTargetException e)
    {
      SQLException sqlEx = new SQLException("Unable to reflectively invoke interceptor");
      sqlEx.initCause(e);
      
      throw sqlEx;
    }
  }
  
  public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection)
    throws SQLException
  {
    return this.toProxy.preProcess(sql, interceptedStatement, connection);
  }
  
  public static final Method getV2PostProcessMethod(Class toProxyClass)
  {
    try
    {
      return toProxyClass.getMethod("postProcess", new Class[] { String.class, Statement.class, ResultSetInternalMethods.class, Connection.class, Integer.TYPE, Boolean.TYPE, Boolean.TYPE, SQLException.class });
    }
    catch (SecurityException e)
    {
      return null;
    }
    catch (NoSuchMethodException e) {}
    return null;
  }
}
