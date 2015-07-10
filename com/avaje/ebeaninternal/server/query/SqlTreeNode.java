package com.avaje.ebeaninternal.server.query;

import com.avaje.ebeaninternal.server.deploy.DbReadContext;
import com.avaje.ebeaninternal.server.deploy.DbSqlContext;
import java.sql.SQLException;
import java.util.List;

public abstract interface SqlTreeNode
{
  public static final char NEW_LINE = '\n';
  public static final String PERIOD = ".";
  public static final String COMMA = ", ";
  public static final int NORMAL = 0;
  public static final int SHARED = 1;
  public static final int READONLY = 2;
  
  public abstract void buildSelectExpressionChain(List<String> paramList);
  
  public abstract void appendSelect(DbSqlContext paramDbSqlContext, boolean paramBoolean);
  
  public abstract void appendFrom(DbSqlContext paramDbSqlContext, boolean paramBoolean);
  
  public abstract void appendWhere(DbSqlContext paramDbSqlContext);
  
  public abstract void load(DbReadContext paramDbReadContext, Object paramObject)
    throws SQLException;
}
