package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

public class LoadBalancedAutoCommitInterceptor
  implements StatementInterceptorV2
{
  private int matchingAfterStatementCount = 0;
  private int matchingAfterStatementThreshold = 0;
  private String matchingAfterStatementRegex;
  private ConnectionImpl conn;
  private LoadBalancingConnectionProxy proxy = null;
  
  public void destroy() {}
  
  public boolean executeTopLevelOnly()
  {
    return false;
  }
  
  public void init(Connection conn, Properties props)
    throws SQLException
  {
    this.conn = ((ConnectionImpl)conn);
    
    String autoCommitSwapThresholdAsString = props.getProperty("loadBalanceAutoCommitStatementThreshold", "0");
    try
    {
      this.matchingAfterStatementThreshold = Integer.parseInt(autoCommitSwapThresholdAsString);
    }
    catch (NumberFormatException nfe) {}
    String autoCommitSwapRegex = props.getProperty("loadBalanceAutoCommitStatementRegex", "");
    if ("".equals(autoCommitSwapRegex)) {
      return;
    }
    this.matchingAfterStatementRegex = autoCommitSwapRegex;
  }
  
  public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, Connection connection, int warningCount, boolean noIndexUsed, boolean noGoodIndexUsed, SQLException statementException)
    throws SQLException
  {
    if (!this.conn.getAutoCommit())
    {
      this.matchingAfterStatementCount = 0;
    }
    else
    {
      if ((this.proxy == null) && (this.conn.isProxySet()))
      {
        MySQLConnection lcl_proxy = this.conn.getLoadBalanceSafeProxy();
        while ((lcl_proxy != null) && (!(lcl_proxy instanceof LoadBalancedMySQLConnection))) {
          lcl_proxy = lcl_proxy.getLoadBalanceSafeProxy();
        }
        if (lcl_proxy != null) {
          this.proxy = ((LoadBalancedMySQLConnection)lcl_proxy).getProxy();
        }
      }
      if (this.proxy != null) {
        if ((this.matchingAfterStatementRegex == null) || (sql.matches(this.matchingAfterStatementRegex))) {
          this.matchingAfterStatementCount += 1;
        }
      }
      if (this.matchingAfterStatementCount >= this.matchingAfterStatementThreshold)
      {
        this.matchingAfterStatementCount = 0;
        try
        {
          if (this.proxy != null) {
            this.proxy.pickNewConnection();
          }
        }
        catch (SQLException e) {}
      }
    }
    return originalResultSet;
  }
  
  public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection)
    throws SQLException
  {
    return null;
  }
}
