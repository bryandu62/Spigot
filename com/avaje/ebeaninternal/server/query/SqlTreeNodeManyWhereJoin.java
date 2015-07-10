package com.avaje.ebeaninternal.server.query;

import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssoc;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.DbReadContext;
import com.avaje.ebeaninternal.server.deploy.DbSqlContext;
import com.avaje.ebeaninternal.server.deploy.TableJoin;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlTreeNodeManyWhereJoin
  implements SqlTreeNode
{
  private final String parentPrefix;
  private final String prefix;
  private final BeanPropertyAssoc<?> nodeBeanProp;
  private final SqlTreeNode[] children;
  
  public SqlTreeNodeManyWhereJoin(String prefix, BeanPropertyAssoc<?> prop)
  {
    this.nodeBeanProp = prop;
    this.prefix = prefix;
    
    String[] split = SplitName.split(prefix);
    this.parentPrefix = split[0];
    
    List<SqlTreeNode> childrenList = new ArrayList(0);
    this.children = ((SqlTreeNode[])childrenList.toArray(new SqlTreeNode[childrenList.size()]));
  }
  
  public void appendFrom(DbSqlContext ctx, boolean forceOuterJoin)
  {
    appendFromBaseTable(ctx, forceOuterJoin);
    for (int i = 0; i < this.children.length; i++) {
      this.children[i].appendFrom(ctx, forceOuterJoin);
    }
  }
  
  public void appendFromBaseTable(DbSqlContext ctx, boolean forceOuterJoin)
  {
    String alias = ctx.getTableAliasManyWhere(this.prefix);
    String parentAlias = ctx.getTableAliasManyWhere(this.parentPrefix);
    if ((this.nodeBeanProp instanceof BeanPropertyAssocOne))
    {
      this.nodeBeanProp.addInnerJoin(parentAlias, alias, ctx);
    }
    else
    {
      BeanPropertyAssocMany<?> manyProp = (BeanPropertyAssocMany)this.nodeBeanProp;
      if (!manyProp.isManyToMany())
      {
        manyProp.addInnerJoin(parentAlias, alias, ctx);
      }
      else
      {
        String alias2 = alias + "z_";
        
        TableJoin manyToManyJoin = manyProp.getIntersectionTableJoin();
        manyToManyJoin.addInnerJoin(parentAlias, alias2, ctx);
        manyProp.addInnerJoin(alias2, alias, ctx);
      }
    }
  }
  
  public void buildSelectExpressionChain(List<String> selectChain) {}
  
  public void appendSelect(DbSqlContext ctx, boolean subQuery) {}
  
  public void appendWhere(DbSqlContext ctx) {}
  
  public void load(DbReadContext ctx, Object parentBean)
    throws SQLException
  {}
}
