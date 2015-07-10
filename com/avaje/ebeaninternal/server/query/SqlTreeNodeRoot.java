package com.avaje.ebeaninternal.server.query;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.DbReadContext;
import com.avaje.ebeaninternal.server.deploy.DbSqlContext;
import com.avaje.ebeaninternal.server.deploy.TableJoin;
import java.util.List;

public final class SqlTreeNodeRoot
  extends SqlTreeNodeBean
{
  private final TableJoin includeJoin;
  
  public SqlTreeNodeRoot(BeanDescriptor<?> desc, SqlTreeProperties props, List<SqlTreeNode> myList, boolean withId, TableJoin includeJoin)
  {
    super(null, null, desc, props, myList, withId);
    this.includeJoin = includeJoin;
  }
  
  public SqlTreeNodeRoot(BeanDescriptor<?> desc, SqlTreeProperties props, List<SqlTreeNode> myList, boolean withId)
  {
    super(null, null, desc, props, myList, withId);
    this.includeJoin = null;
  }
  
  protected void postLoad(DbReadContext cquery, Object loadedBean, Object id)
  {
    cquery.setLoadedBean(loadedBean, id);
  }
  
  public boolean appendFromBaseTable(DbSqlContext ctx, boolean forceOuterJoin)
  {
    ctx.append(this.desc.getBaseTable());
    ctx.append(" ").append(ctx.getTableAlias(null));
    if (this.includeJoin != null)
    {
      String a1 = ctx.getTableAlias(null);
      String a2 = "int_";
      this.includeJoin.addJoin(forceOuterJoin, a1, a2, ctx);
    }
    return forceOuterJoin;
  }
}
