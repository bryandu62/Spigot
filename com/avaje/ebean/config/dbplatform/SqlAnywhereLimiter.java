package com.avaje.ebean.config.dbplatform;

public class SqlAnywhereLimiter
  implements SqlLimiter
{
  public SqlLimitResponse limit(SqlLimitRequest request)
  {
    StringBuilder sb = new StringBuilder(500);
    
    int firstRow = request.getFirstRow();
    int maxRows = request.getMaxRows();
    if (maxRows > 0) {
      maxRows += 1;
    }
    sb.append("select ");
    if (request.isDistinct()) {
      sb.append("distinct ");
    }
    if (maxRows > 0) {
      sb.append("top ").append(maxRows).append(" ");
    }
    if (firstRow > 0) {
      sb.append("start at ").append(firstRow + 1).append(" ");
    }
    sb.append(request.getDbSql());
    
    String sql = request.getDbPlatform().completeSql(sb.toString(), request.getOrmQuery());
    
    return new SqlLimitResponse(sql, false);
  }
}
