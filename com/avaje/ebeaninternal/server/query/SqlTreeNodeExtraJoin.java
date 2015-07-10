package com.avaje.ebeaninternal.server.query;

import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssoc;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.DbReadContext;
import com.avaje.ebeaninternal.server.deploy.DbSqlContext;
import com.avaje.ebeaninternal.server.deploy.TableJoin;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlTreeNodeExtraJoin
  implements SqlTreeNode
{
  final BeanPropertyAssoc<?> assocBeanProperty;
  final String prefix;
  final boolean manyJoin;
  List<SqlTreeNodeExtraJoin> children;
  
  public SqlTreeNodeExtraJoin(String prefix, BeanPropertyAssoc<?> assocBeanProperty)
  {
    this.prefix = prefix;
    this.assocBeanProperty = assocBeanProperty;
    this.manyJoin = (assocBeanProperty instanceof BeanPropertyAssocMany);
  }
  
  public void buildSelectExpressionChain(List<String> selectChain) {}
  
  public boolean isManyJoin()
  {
    return this.manyJoin;
  }
  
  public String getName()
  {
    return this.prefix;
  }
  
  public void addChild(SqlTreeNodeExtraJoin child)
  {
    if (this.children == null) {
      this.children = new ArrayList();
    }
    this.children.add(child);
  }
  
  public void appendFrom(DbSqlContext ctx, boolean forceOuterJoin)
  {
    boolean manyToMany = false;
    if ((this.assocBeanProperty instanceof BeanPropertyAssocMany))
    {
      BeanPropertyAssocMany<?> manyProp = (BeanPropertyAssocMany)this.assocBeanProperty;
      if (manyProp.isManyToMany())
      {
        manyToMany = true;
        
        String alias = ctx.getTableAlias(this.prefix);
        String[] split = SplitName.split(this.prefix);
        String parentAlias = ctx.getTableAlias(split[0]);
        String alias2 = alias + "z_";
        
        TableJoin manyToManyJoin = manyProp.getIntersectionTableJoin();
        manyToManyJoin.addJoin(forceOuterJoin, parentAlias, alias2, ctx);
        
        this.assocBeanProperty.addJoin(forceOuterJoin, alias2, alias, ctx);
      }
    }
    if (!manyToMany) {
      this.assocBeanProperty.addJoin(forceOuterJoin, this.prefix, ctx);
    }
    if (this.children != null)
    {
      if (this.manyJoin) {
        forceOuterJoin = true;
      }
      for (int i = 0; i < this.children.size(); i++)
      {
        SqlTreeNodeExtraJoin child = (SqlTreeNodeExtraJoin)this.children.get(i);
        child.appendFrom(ctx, forceOuterJoin);
      }
    }
  }
  
  public void appendSelect(DbSqlContext ctx, boolean subQuery) {}
  
  public void appendWhere(DbSqlContext ctx) {}
  
  public void load(DbReadContext ctx, Object parentBean)
    throws SQLException
  {}
}
