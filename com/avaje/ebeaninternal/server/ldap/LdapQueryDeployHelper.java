package com.avaje.ebeaninternal.server.ldap;

import com.avaje.ebeaninternal.api.SpiExpressionList;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.DeployPropertyParser;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryDetail;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryProperties;
import com.avaje.ebeaninternal.util.DefaultExpressionRequest;
import java.util.ArrayList;
import java.util.Iterator;

public class LdapQueryDeployHelper
{
  private final LdapOrmQueryRequest<?> request;
  private final SpiQuery<?> query;
  private final BeanDescriptor<?> desc;
  private String filterExpr;
  private Object[] filterValues;
  
  public LdapQueryDeployHelper(LdapOrmQueryRequest<?> request)
  {
    this.request = request;
    this.query = request.getQuery();
    this.desc = request.getBeanDescriptor();
    
    parse();
  }
  
  public String[] getSelectedProperties()
  {
    OrmQueryProperties chunk = this.query.getDetail().getChunk(null, false);
    if (chunk.allProperties()) {
      return null;
    }
    ArrayList<String> ldapSelectProps = new ArrayList();
    
    Iterator<String> selectProperties = chunk.getSelectProperties();
    while (selectProperties.hasNext())
    {
      String propName = (String)selectProperties.next();
      BeanProperty p = this.desc.getBeanProperty(propName);
      if (p != null) {
        propName = p.getDbColumn();
      }
      ldapSelectProps.add(propName);
    }
    return (String[])ldapSelectProps.toArray(new String[ldapSelectProps.size()]);
  }
  
  private void parse()
  {
    DeployPropertyParser deployParser = this.desc.createDeployPropertyParser();
    
    String baseWhere = this.query.getAdditionalWhere();
    if (baseWhere != null) {
      baseWhere = deployParser.parse(baseWhere);
    }
    SpiExpressionList<?> whereExp = this.query.getWhereExpressions();
    if (whereExp != null)
    {
      DefaultExpressionRequest expReq = new DefaultExpressionRequest(this.request, deployParser);
      
      ArrayList<?> bindValues = whereExp.buildBindValues(expReq);
      this.filterValues = bindValues.toArray(new Object[bindValues.size()]);
      String exprWhere = whereExp.buildSql(expReq);
      if (baseWhere != null) {
        this.filterExpr = ("(&" + baseWhere + exprWhere + ")");
      } else {
        this.filterExpr = exprWhere;
      }
    }
    else
    {
      this.filterExpr = baseWhere;
    }
  }
  
  public String getFilterExpr()
  {
    return this.filterExpr;
  }
  
  public Object[] getFilterValues()
  {
    return this.filterValues;
  }
}
