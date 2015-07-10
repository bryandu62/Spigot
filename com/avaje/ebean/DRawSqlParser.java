package com.avaje.ebean;

import com.avaje.ebeaninternal.server.querydefn.SimpleTextParser;

class DRawSqlParser
{
  public static final String $_AND_HAVING = "${andHaving}";
  public static final String $_HAVING = "${having}";
  public static final String $_AND_WHERE = "${andWhere}";
  public static final String $_WHERE = "${where}";
  private static final String ORDER_BY = "order by";
  private final SimpleTextParser textParser;
  private String sql;
  private int placeHolderWhere;
  private int placeHolderAndWhere;
  private int placeHolderHaving;
  private int placeHolderAndHaving;
  private boolean hasPlaceHolders;
  private int selectPos = -1;
  private int distinctPos = -1;
  private int fromPos = -1;
  private int wherePos = -1;
  private int groupByPos = -1;
  private int havingPos = -1;
  private int orderByPos = -1;
  private boolean whereExprAnd;
  private int whereExprPos = -1;
  private boolean havingExprAnd;
  private int havingExprPos = -1;
  
  public static RawSql.Sql parse(String sql)
  {
    return new DRawSqlParser(sql).parse();
  }
  
  private DRawSqlParser(String sqlString)
  {
    sqlString = sqlString.trim();
    this.sql = sqlString;
    this.hasPlaceHolders = findAndRemovePlaceHolders();
    this.textParser = new SimpleTextParser(sqlString);
  }
  
  private RawSql.Sql parse()
  {
    if (!hasPlaceHolders()) {
      parseSqlFindKeywords(true);
    }
    this.whereExprPos = findWhereExprPosition();
    this.havingExprPos = findHavingExprPosition();
    
    String preFrom = removeWhitespace(findPreFromSql());
    String preWhere = removeWhitespace(findPreWhereSql());
    String preHaving = removeWhitespace(findPreHavingSql());
    String orderBySql = findOrderBySql();
    
    preFrom = trimSelectKeyword(preFrom);
    
    return new RawSql.Sql(this.sql.hashCode(), preFrom, preWhere, this.whereExprAnd, preHaving, this.havingExprAnd, orderBySql, this.distinctPos > -1);
  }
  
  private boolean findAndRemovePlaceHolders()
  {
    this.placeHolderWhere = removePlaceHolder("${where}");
    this.placeHolderAndWhere = removePlaceHolder("${andWhere}");
    this.placeHolderHaving = removePlaceHolder("${having}");
    this.placeHolderAndHaving = removePlaceHolder("${andHaving}");
    return hasPlaceHolders();
  }
  
  private int removePlaceHolder(String placeHolder)
  {
    int pos = this.sql.indexOf(placeHolder);
    if (pos > -1)
    {
      int after = pos + placeHolder.length() + 1;
      if (after > this.sql.length()) {
        this.sql = this.sql.substring(0, pos);
      } else {
        this.sql = (this.sql.substring(0, pos) + this.sql.substring(after));
      }
    }
    return pos;
  }
  
  private boolean hasPlaceHolders()
  {
    if (this.placeHolderWhere > -1) {
      return true;
    }
    if (this.placeHolderAndWhere > -1) {
      return true;
    }
    if (this.placeHolderHaving > -1) {
      return true;
    }
    if (this.placeHolderAndHaving > -1) {
      return true;
    }
    return false;
  }
  
  private String trimSelectKeyword(String preWhereExprSql)
  {
    if (this.selectPos < 0) {
      throw new IllegalStateException("select keyword not found?");
    }
    preWhereExprSql = preWhereExprSql.trim();
    String select = preWhereExprSql.substring(0, 7);
    if (!select.equalsIgnoreCase("select ")) {
      throw new RuntimeException("Expecting [" + preWhereExprSql + "] to start with \"select\"");
    }
    preWhereExprSql = preWhereExprSql.substring(7).trim();
    if (this.distinctPos > -1)
    {
      String distinct = preWhereExprSql.substring(0, 9);
      if (!distinct.equalsIgnoreCase("distinct ")) {
        throw new RuntimeException("Expecting [" + preWhereExprSql + "] to start with \"select distinct\"");
      }
      preWhereExprSql = preWhereExprSql.substring(9);
    }
    return preWhereExprSql;
  }
  
  private String findOrderBySql()
  {
    if (this.orderByPos > -1)
    {
      int pos = this.orderByPos + "order by".length();
      return this.sql.substring(pos).trim();
    }
    return null;
  }
  
  private String findPreHavingSql()
  {
    if (this.havingExprPos > this.whereExprPos) {
      return this.sql.substring(this.whereExprPos, this.havingExprPos - 1);
    }
    if (this.whereExprPos > -1)
    {
      if (this.orderByPos == -1) {
        return this.sql.substring(this.whereExprPos);
      }
      if (this.whereExprPos == this.orderByPos) {
        return "";
      }
      return this.sql.substring(this.whereExprPos, this.orderByPos - 1);
    }
    return null;
  }
  
  private String findPreFromSql()
  {
    return this.sql.substring(0, this.fromPos - 1);
  }
  
  private String findPreWhereSql()
  {
    if (this.whereExprPos > -1) {
      return this.sql.substring(this.fromPos, this.whereExprPos - 1);
    }
    return this.sql.substring(this.fromPos);
  }
  
  private void parseSqlFindKeywords(boolean allKeywords)
  {
    this.selectPos = this.textParser.findWordLower("select");
    if (this.selectPos == -1)
    {
      String msg = "Error parsing sql, can not find SELECT keyword in:";
      throw new RuntimeException(msg + this.sql);
    }
    String possibleDistinct = this.textParser.nextWord();
    if ("distinct".equals(possibleDistinct)) {
      this.distinctPos = (this.textParser.getPos() - 8);
    }
    this.fromPos = this.textParser.findWordLower("from");
    if (this.fromPos == -1)
    {
      String msg = "Error parsing sql, can not find FROM keyword in:";
      throw new RuntimeException(msg + this.sql);
    }
    if (!allKeywords) {
      return;
    }
    this.wherePos = this.textParser.findWordLower("where");
    if (this.wherePos == -1) {
      this.groupByPos = this.textParser.findWordLower("group", this.fromPos + 5);
    } else {
      this.groupByPos = this.textParser.findWordLower("group");
    }
    if (this.groupByPos > -1) {
      this.havingPos = this.textParser.findWordLower("having");
    }
    int startOrderBy = this.havingPos;
    if (startOrderBy == -1) {
      startOrderBy = this.groupByPos;
    }
    if (startOrderBy == -1) {
      startOrderBy = this.wherePos;
    }
    if (startOrderBy == -1) {
      startOrderBy = this.fromPos;
    }
    this.orderByPos = this.textParser.findWordLower("order", startOrderBy);
  }
  
  private int findWhereExprPosition()
  {
    if (this.hasPlaceHolders)
    {
      if (this.placeHolderWhere > -1) {
        return this.placeHolderWhere;
      }
      this.whereExprAnd = true;
      return this.placeHolderAndWhere;
    }
    this.whereExprAnd = (this.wherePos > 0);
    if (this.groupByPos > 0) {
      return this.groupByPos;
    }
    if (this.havingPos > 0) {
      return this.havingPos;
    }
    if (this.orderByPos > 0) {
      return this.orderByPos;
    }
    return -1;
  }
  
  private int findHavingExprPosition()
  {
    if (this.hasPlaceHolders)
    {
      if (this.placeHolderHaving > -1) {
        return this.placeHolderHaving;
      }
      this.havingExprAnd = true;
      return this.placeHolderAndHaving;
    }
    this.havingExprAnd = (this.havingPos > 0);
    if (this.orderByPos > 0) {
      return this.orderByPos;
    }
    return -1;
  }
  
  private String removeWhitespace(String sql)
  {
    if (sql == null) {
      return "";
    }
    boolean removeWhitespace = false;
    
    int length = sql.length();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++)
    {
      char c = sql.charAt(i);
      if (removeWhitespace)
      {
        if (!Character.isWhitespace(c))
        {
          sb.append(c);
          removeWhitespace = false;
        }
      }
      else if ((c == '\r') || (c == '\n'))
      {
        sb.append('\n');
        removeWhitespace = true;
      }
      else
      {
        sb.append(c);
      }
    }
    String s = sb.toString();
    return s.trim();
  }
}
