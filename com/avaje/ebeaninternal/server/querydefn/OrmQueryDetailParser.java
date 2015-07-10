package com.avaje.ebeaninternal.server.querydefn;

import javax.persistence.PersistenceException;

public class OrmQueryDetailParser
{
  private final OrmQueryDetail detail = new OrmQueryDetail();
  private int maxRows;
  private int firstRow;
  private String rawWhereClause;
  private String rawOrderBy;
  private final SimpleTextParser parser;
  
  public OrmQueryDetailParser(String oql)
  {
    this.parser = new SimpleTextParser(oql);
  }
  
  public void parse()
    throws PersistenceException
  {
    this.parser.nextWord();
    processInitial();
  }
  
  protected void assign(DefaultOrmQuery<?> query)
  {
    query.setOrmQueryDetail(this.detail);
    query.setFirstRow(this.firstRow);
    query.setMaxRows(this.maxRows);
    query.setRawWhereClause(this.rawWhereClause);
    query.order(this.rawOrderBy);
  }
  
  private void processInitial()
  {
    if (this.parser.isMatch("find"))
    {
      OrmQueryProperties props = readFindFetch();
      this.detail.setBase(props);
    }
    else
    {
      process();
    }
    while (!this.parser.isFinished()) {
      process();
    }
  }
  
  private boolean isFetch()
  {
    return (this.parser.isMatch("fetch")) || (this.parser.isMatch("join"));
  }
  
  private void process()
  {
    if (isFetch())
    {
      OrmQueryProperties props = readFindFetch();
      this.detail.putFetchPath(props);
    }
    else if (this.parser.isMatch("where"))
    {
      readWhere();
    }
    else if (this.parser.isMatch("order", "by"))
    {
      readOrderBy();
    }
    else if (this.parser.isMatch("limit"))
    {
      readLimit();
    }
    else
    {
      throw new PersistenceException("Query expected 'fetch', 'where','order by' or 'limit' keyword but got [" + this.parser.getWord() + "] \r " + this.parser.getOql());
    }
  }
  
  private void readLimit()
  {
    try
    {
      String maxLimit = this.parser.nextWord();
      this.maxRows = Integer.parseInt(maxLimit);
      
      String offsetKeyword = this.parser.nextWord();
      if (offsetKeyword != null)
      {
        if (!this.parser.isMatch("offset")) {
          throw new PersistenceException("expected offset keyword but got " + this.parser.getWord());
        }
        String firstRowLimit = this.parser.nextWord();
        this.firstRow = Integer.parseInt(firstRowLimit);
        this.parser.nextWord();
      }
    }
    catch (NumberFormatException e)
    {
      String msg = "Expected an integer for maxRows or firstRows in limit offset clause";
      throw new PersistenceException(msg, e);
    }
  }
  
  private void readOrderBy()
  {
    this.parser.nextWord();
    
    StringBuilder sb = new StringBuilder();
    while ((this.parser.nextWord() != null) && 
      (!this.parser.isMatch("limit")))
    {
      String w = this.parser.getWord();
      if (!w.startsWith("(")) {
        sb.append(" ");
      }
      sb.append(w);
    }
    this.rawOrderBy = sb.toString().trim();
    if (!this.parser.isFinished()) {
      readLimit();
    }
  }
  
  private void readWhere()
  {
    int nextMode = 0;
    StringBuilder sb = new StringBuilder();
    while (this.parser.nextWord() != null)
    {
      if (this.parser.isMatch("order", "by"))
      {
        nextMode = 1;
        break;
      }
      if (this.parser.isMatch("limit"))
      {
        nextMode = 2;
        break;
      }
      sb.append(" ").append(this.parser.getWord());
    }
    String whereClause = sb.toString().trim();
    if (whereClause.length() > 0) {
      this.rawWhereClause = whereClause;
    }
    if (nextMode == 1) {
      readOrderBy();
    } else if (nextMode == 2) {
      readLimit();
    }
  }
  
  private OrmQueryProperties readFindFetch()
  {
    boolean readAlias = false;
    
    String props = null;
    String path = this.parser.nextWord();
    String token = null;
    while ((token = this.parser.nextWord()) != null) {
      if ((!readAlias) && (this.parser.isMatch("as")))
      {
        this.parser.nextWord();
        readAlias = true;
      }
      else if ('(' == token.charAt(0))
      {
        props = token;
        this.parser.nextWord();
      }
      else if (!isFindFetchEnd())
      {
        if (!readAlias) {
          readAlias = true;
        } else {
          throw new PersistenceException("Expected (props) or new 'fetch' 'where' but got " + token);
        }
      }
    }
    if (props != null) {
      props = props.substring(1, props.length() - 1);
    }
    return new OrmQueryProperties(path, props);
  }
  
  private boolean isFindFetchEnd()
  {
    if (isFetch()) {
      return true;
    }
    if (this.parser.isMatch("where")) {
      return true;
    }
    if (this.parser.isMatch("order", "by")) {
      return true;
    }
    return false;
  }
}
