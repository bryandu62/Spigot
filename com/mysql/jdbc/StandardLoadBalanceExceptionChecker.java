package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class StandardLoadBalanceExceptionChecker
  implements LoadBalanceExceptionChecker
{
  private List sqlStateList;
  private List sqlExClassList;
  
  public boolean shouldExceptionTriggerFailover(SQLException ex)
  {
    String sqlState = ex.getSQLState();
    Iterator i;
    if (sqlState != null)
    {
      if (sqlState.startsWith("08")) {
        return true;
      }
      if (this.sqlStateList != null) {
        for (i = this.sqlStateList.iterator(); i.hasNext();) {
          if (sqlState.startsWith(i.next().toString())) {
            return true;
          }
        }
      }
    }
    if ((ex instanceof CommunicationsException)) {
      return true;
    }
    Iterator i;
    if (this.sqlExClassList != null) {
      for (i = this.sqlExClassList.iterator(); i.hasNext();) {
        if (((Class)i.next()).isInstance(ex)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public void destroy() {}
  
  public void init(Connection conn, Properties props)
    throws SQLException
  {
    configureSQLStateList(props.getProperty("loadBalanceSQLStateFailover", null));
    configureSQLExceptionSubclassList(props.getProperty("loadBalanceSQLExceptionSubclassFailover", null));
  }
  
  private void configureSQLStateList(String sqlStates)
  {
    if ((sqlStates == null) || ("".equals(sqlStates))) {
      return;
    }
    List states = StringUtils.split(sqlStates, ",", true);
    List newStates = new ArrayList();
    Iterator i = states.iterator();
    while (i.hasNext())
    {
      String state = i.next().toString();
      if (state.length() > 0) {
        newStates.add(state);
      }
    }
    if (newStates.size() > 0) {
      this.sqlStateList = newStates;
    }
  }
  
  private void configureSQLExceptionSubclassList(String sqlExClasses)
  {
    if ((sqlExClasses == null) || ("".equals(sqlExClasses))) {
      return;
    }
    List classes = StringUtils.split(sqlExClasses, ",", true);
    List newClasses = new ArrayList();
    Iterator i = classes.iterator();
    while (i.hasNext())
    {
      String exClass = i.next().toString();
      try
      {
        Class c = Class.forName(exClass);
        newClasses.add(c);
      }
      catch (Exception e) {}
    }
    if (newClasses.size() > 0) {
      this.sqlExClassList = newClasses;
    }
  }
}
