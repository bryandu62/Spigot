package com.avaje.ebeaninternal.server.lib.sql;

import java.sql.Connection;

public abstract interface DataSourcePoolListener
{
  public abstract void onAfterBorrowConnection(Connection paramConnection);
  
  public abstract void onBeforeReturnConnection(Connection paramConnection);
}
