package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSql.ColumnMapping;
import com.avaje.ebean.config.dbplatform.SqlLimitResponse;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import com.avaje.ebeaninternal.server.type.DataReader;
import com.avaje.ebeaninternal.server.type.RsetDataReaderIndexed;
import java.sql.ResultSet;
import java.util.List;

public class CQueryPlanRawSql
  extends CQueryPlan
{
  private final int[] rsetIndexPositions;
  
  public CQueryPlanRawSql(OrmQueryRequest<?> request, SqlLimitResponse sqlRes, SqlTree sqlTree, String logWhereSql)
  {
    super(request, sqlRes, sqlTree, true, logWhereSql, null);
    
    this.rsetIndexPositions = createIndexPositions(request, sqlTree);
  }
  
  public DataReader createDataReader(ResultSet rset)
  {
    return new RsetDataReaderIndexed(rset, this.rsetIndexPositions, isRowNumberIncluded());
  }
  
  private int[] createIndexPositions(OrmQueryRequest<?> request, SqlTree sqlTree)
  {
    List<String> chain = sqlTree.buildSelectExpressionChain();
    RawSql.ColumnMapping columnMapping = request.getQuery().getRawSql().getColumnMapping();
    
    int[] indexPositions = new int[chain.size()];
    for (int i = 0; i < chain.size(); i++)
    {
      String expr = (String)chain.get(i);
      int indexPos = 1 + columnMapping.getIndexPosition(expr);
      indexPositions[i] = indexPos;
    }
    return indexPositions;
  }
}
