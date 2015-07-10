package com.avaje.ebeaninternal.server.deploy;

public class DNativeQuery
{
  final String query;
  
  public DNativeQuery(String query)
  {
    this.query = query;
  }
  
  public String getQuery()
  {
    return this.query;
  }
}
