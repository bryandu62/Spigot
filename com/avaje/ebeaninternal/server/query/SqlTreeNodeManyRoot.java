package com.avaje.ebeaninternal.server.query;

import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.DbReadContext;
import com.avaje.ebeaninternal.server.deploy.DbSqlContext;
import java.sql.SQLException;
import java.util.List;

public final class SqlTreeNodeManyRoot
  extends SqlTreeNodeBean
{
  public SqlTreeNodeManyRoot(String prefix, BeanPropertyAssocMany<?> prop, SqlTreeProperties props, List<SqlTreeNode> myList)
  {
    super(prefix, prop, prop.getTargetDescriptor(), props, myList, true);
  }
  
  protected void postLoad(DbReadContext cquery, Object loadedBean, Object id)
  {
    cquery.setLoadedManyBean(loadedBean);
  }
  
  public void load(DbReadContext cquery, Object parentBean)
    throws SQLException
  {
    super.load(cquery, null);
  }
  
  public void appendFrom(DbSqlContext ctx, boolean forceOuterJoin)
  {
    super.appendFrom(ctx, true);
  }
}
