package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import javax.persistence.PersistenceException;

public class BeanSqlSelect
{
  public static final String HAVING_PREDICATES = "${HAVING_PREDICATES}";
  public static final String WHERE_PREDICATES = "${WHERE_PREDICATES}";
  public static final String AND_PREDICATES = "${AND_PREDICATES}";
  public static final String ORDER_BY = "${ORDER_BY}";
  final String sql;
  final PredicatesType predicatesType;
  final boolean hasOrderBy;
  
  public static enum PredicatesType
  {
    HAVING,  WHERE,  AND,  NONE;
    
    private PredicatesType() {}
  }
  
  public BeanSqlSelect(String sql, PredicatesType predicatesType, boolean hasOrderBy)
  {
    this.sql = sql;
    this.predicatesType = predicatesType;
    this.hasOrderBy = hasOrderBy;
  }
  
  public String getSql()
  {
    return this.sql;
  }
  
  public PredicatesType getPredicatesType()
  {
    return this.predicatesType;
  }
  
  public boolean hasOrderBy()
  {
    return this.hasOrderBy;
  }
  
  public String addPredicates(String query, String predicates)
  {
    if (predicates == null)
    {
      switch (this.predicatesType)
      {
      case HAVING: 
        return StringHelper.replaceString(query, "${HAVING_PREDICATES}", "");
      case WHERE: 
        return StringHelper.replaceString(query, "${WHERE_PREDICATES}", "");
      case AND: 
        return StringHelper.replaceString(query, "${AND_PREDICATES}", "");
      case NONE: 
        return query;
      }
      throw new PersistenceException("predicatesType " + this.predicatesType + " not handled");
    }
    switch (this.predicatesType)
    {
    case HAVING: 
      return StringHelper.replaceString(query, "${HAVING_PREDICATES}", " HAVING " + predicates);
    case WHERE: 
      return StringHelper.replaceString(query, "${WHERE_PREDICATES}", " WHERE " + predicates);
    case AND: 
      return StringHelper.replaceString(query, "${AND_PREDICATES}", " AND " + predicates);
    case NONE: 
      return query;
    }
    throw new PersistenceException("predicatesType " + this.predicatesType + " not handled");
  }
  
  public String addOrderBy(String query, String orderBy)
  {
    if (!this.hasOrderBy) {
      return query;
    }
    if (orderBy == null) {
      return StringHelper.replaceString(query, "${ORDER_BY}", "");
    }
    return StringHelper.replaceString(query, "${ORDER_BY}", " ORDER BY " + orderBy);
  }
}
