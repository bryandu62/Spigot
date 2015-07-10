package com.avaje.ebeaninternal.server.query;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.DbSqlContext;
import com.avaje.ebeaninternal.server.deploy.TableJoinColumn;
import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import com.avaje.ebeaninternal.server.util.ArrayStack;
import java.util.ArrayList;
import java.util.HashSet;

public class DefaultDbSqlContext
  implements DbSqlContext
{
  private static final String NEW_LINE = "\n";
  private static final String COMMA = ", ";
  private static final String PERIOD = ".";
  private final String tableAliasPlaceHolder;
  private final String columnAliasPrefix;
  private final ArrayStack<String> tableAliasStack = new ArrayStack();
  private final ArrayStack<String> joinStack = new ArrayStack();
  private final ArrayStack<String> prefixStack = new ArrayStack();
  private final boolean useColumnAlias;
  private int columnIndex;
  private StringBuilder sb = new StringBuilder(140);
  private HashSet<String> formulaJoins;
  private HashSet<String> tableJoins;
  private SqlTreeAlias alias;
  private String currentPrefix;
  private ArrayList<BeanProperty> encryptedProps;
  
  public DefaultDbSqlContext(SqlTreeAlias alias, String tableAliasPlaceHolder)
  {
    this.tableAliasPlaceHolder = tableAliasPlaceHolder;
    this.columnAliasPrefix = null;
    this.useColumnAlias = false;
    this.alias = alias;
  }
  
  public DefaultDbSqlContext(SqlTreeAlias alias, String tableAliasPlaceHolder, String columnAliasPrefix, boolean alwaysUseColumnAlias)
  {
    this.alias = alias;
    this.tableAliasPlaceHolder = tableAliasPlaceHolder;
    this.columnAliasPrefix = columnAliasPrefix;
    this.useColumnAlias = alwaysUseColumnAlias;
  }
  
  public void addEncryptedProp(BeanProperty p)
  {
    if (this.encryptedProps == null) {
      this.encryptedProps = new ArrayList();
    }
    this.encryptedProps.add(p);
  }
  
  public BeanProperty[] getEncryptedProps()
  {
    if (this.encryptedProps == null) {
      return null;
    }
    return (BeanProperty[])this.encryptedProps.toArray(new BeanProperty[this.encryptedProps.size()]);
  }
  
  public String peekJoin()
  {
    return (String)this.joinStack.peek();
  }
  
  public void popJoin()
  {
    this.joinStack.pop();
  }
  
  public void pushJoin(String node)
  {
    this.joinStack.push(node);
  }
  
  public void addJoin(String type, String table, TableJoinColumn[] cols, String a1, String a2)
  {
    if (this.tableJoins == null) {
      this.tableJoins = new HashSet();
    }
    String joinKey = table + "-" + a1 + "-" + a2;
    if (this.tableJoins.contains(joinKey)) {
      return;
    }
    this.tableJoins.add(joinKey);
    
    this.sb.append("\n");
    this.sb.append(type);
    
    this.sb.append(" ").append(table).append(" ");
    this.sb.append(a2);
    
    this.sb.append(" on ");
    for (int i = 0; i < cols.length; i++)
    {
      TableJoinColumn pair = cols[i];
      if (i > 0) {
        this.sb.append(" and ");
      }
      this.sb.append(a2);
      this.sb.append(".").append(pair.getForeignDbColumn());
      this.sb.append(" = ");
      this.sb.append(a1);
      this.sb.append(".").append(pair.getLocalDbColumn());
    }
    this.sb.append(" ");
  }
  
  public String getTableAlias(String prefix)
  {
    return this.alias.getTableAlias(prefix);
  }
  
  public String getTableAliasManyWhere(String prefix)
  {
    return this.alias.getTableAliasManyWhere(prefix);
  }
  
  public void pushSecondaryTableAlias(String alias)
  {
    this.tableAliasStack.push(alias);
  }
  
  public String getRelativePrefix(String propName)
  {
    return this.currentPrefix + "." + propName;
  }
  
  public void pushTableAlias(String prefix)
  {
    this.prefixStack.push(this.currentPrefix);
    this.currentPrefix = prefix;
    this.tableAliasStack.push(getTableAlias(prefix));
  }
  
  public void popTableAlias()
  {
    this.tableAliasStack.pop();
    
    this.currentPrefix = ((String)this.prefixStack.pop());
  }
  
  public StringBuilder getBuffer()
  {
    return this.sb;
  }
  
  public DefaultDbSqlContext append(String s)
  {
    this.sb.append(s);
    return this;
  }
  
  public DefaultDbSqlContext append(char s)
  {
    this.sb.append(s);
    return this;
  }
  
  public void appendFormulaJoin(String sqlFormulaJoin, boolean forceOuterJoin)
  {
    String tableAlias = (String)this.tableAliasStack.peek();
    String converted = StringHelper.replaceString(sqlFormulaJoin, this.tableAliasPlaceHolder, tableAlias);
    if (this.formulaJoins == null) {
      this.formulaJoins = new HashSet();
    } else if (this.formulaJoins.contains(converted)) {
      return;
    }
    this.formulaJoins.add(converted);
    
    this.sb.append("\n");
    if ((forceOuterJoin) && 
      ("join".equals(sqlFormulaJoin.substring(0, 4).toLowerCase()))) {
      append(" left outer ");
    }
    this.sb.append(converted);
    this.sb.append(" ");
  }
  
  public void appendFormulaSelect(String sqlFormulaSelect)
  {
    String tableAlias = (String)this.tableAliasStack.peek();
    String converted = StringHelper.replaceString(sqlFormulaSelect, this.tableAliasPlaceHolder, tableAlias);
    
    this.sb.append(", ");
    this.sb.append(converted);
  }
  
  public void appendColumn(String column)
  {
    appendColumn((String)this.tableAliasStack.peek(), column);
  }
  
  public void appendColumn(String tableAlias, String column)
  {
    this.sb.append(", ");
    if (column.indexOf("${}") > -1)
    {
      String x = StringHelper.replaceString(column, "${}", tableAlias);
      this.sb.append(x);
    }
    else
    {
      this.sb.append(tableAlias);
      this.sb.append(".");
      this.sb.append(column);
    }
    if (this.useColumnAlias)
    {
      this.sb.append(" ");
      this.sb.append(this.columnAliasPrefix);
      this.sb.append(this.columnIndex);
    }
    this.columnIndex += 1;
  }
  
  public String peekTableAlias()
  {
    return (String)this.tableAliasStack.peek();
  }
  
  public void appendRawColumn(String rawcolumnWithTableAlias)
  {
    this.sb.append(", ");
    this.sb.append(rawcolumnWithTableAlias);
    if (this.useColumnAlias)
    {
      this.sb.append(" ");
      this.sb.append(this.columnAliasPrefix);
      this.sb.append(this.columnIndex);
    }
    this.columnIndex += 1;
  }
  
  public int length()
  {
    return this.sb.length();
  }
  
  public String getContent()
  {
    String s = this.sb.toString();
    this.sb = new StringBuilder();
    return s;
  }
  
  public String toString()
  {
    return "DefaultDbSqlContext: " + this.sb.toString();
  }
}
