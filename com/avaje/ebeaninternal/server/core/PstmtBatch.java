package com.avaje.ebeaninternal.server.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract interface PstmtBatch
{
  public abstract void setBatchSize(PreparedStatement paramPreparedStatement, int paramInt);
  
  public abstract void addBatch(PreparedStatement paramPreparedStatement)
    throws SQLException;
  
  public abstract int executeBatch(PreparedStatement paramPreparedStatement, int paramInt, String paramString, boolean paramBoolean)
    throws SQLException;
}
