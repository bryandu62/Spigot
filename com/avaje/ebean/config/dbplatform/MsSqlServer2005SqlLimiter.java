package com.avaje.ebean.config.dbplatform;

public class MsSqlServer2005SqlLimiter
  implements SqlLimiter
{
  final String rowNumberWindowAlias;
  
  public MsSqlServer2005SqlLimiter(String rowNumberWindowAlias)
  {
    this.rowNumberWindowAlias = rowNumberWindowAlias;
  }
  
  public MsSqlServer2005SqlLimiter()
  {
    this("as limitresult");
  }
  
  public SqlLimitResponse limit(SqlLimitRequest request)
  {
    StringBuilder sb = new StringBuilder(500);
    
    int firstRow = request.getFirstRow();
    
    int lastRow = request.getMaxRows();
    if (lastRow > 0) {
      lastRow = lastRow + firstRow + 1;
    }
    if (firstRow < 1)
    {
      sb.append(" select top ").append(lastRow).append(" ");
      if (request.isDistinct()) {
        sb.append("distinct ");
      }
      sb.append(request.getDbSql());
      return new SqlLimitResponse(sb.toString(), false);
    }
    sb.append("select * ").append('\n').append("from ( ");
    
    sb.append("select ");
    if (request.isDistinct()) {
      sb.append("distinct ");
    }
    sb.append("top ").append(lastRow);
    sb.append(" row_number() over (order by ");
    sb.append(request.getDbOrderBy());
    sb.append(") as rn, ");
    sb.append(request.getDbSql());
    
    sb.append('\n').append(") ");
    sb.append(this.rowNumberWindowAlias);
    sb.append(" where ");
    if (firstRow > 0)
    {
      sb.append(" rn > ").append(firstRow);
      if (lastRow > 0) {
        sb.append(" and ");
      }
    }
    if (lastRow > 0) {
      sb.append(" rn <= ").append(lastRow);
    }
    String sql = request.getDbPlatform().completeSql(sb.toString(), request.getOrmQuery());
    
    return new SqlLimitResponse(sql, true);
  }
}
