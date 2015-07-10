package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract interface BalanceStrategy
  extends Extension
{
  public abstract ConnectionImpl pickConnection(LoadBalancingConnectionProxy paramLoadBalancingConnectionProxy, List<String> paramList, Map<String, ConnectionImpl> paramMap, long[] paramArrayOfLong, int paramInt)
    throws SQLException;
}
