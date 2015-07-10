package com.avaje.ebeaninternal.util;

import com.avaje.ebeaninternal.api.SpiExpressionRequest;
import com.avaje.ebeaninternal.server.core.SpiOrmQueryRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.DeployParser;
import java.util.ArrayList;

public class DefaultExpressionRequest
  implements SpiExpressionRequest
{
  private final SpiOrmQueryRequest<?> queryRequest;
  private final BeanDescriptor<?> beanDescriptor;
  private final StringBuilder sb = new StringBuilder();
  private final ArrayList<Object> bindValues = new ArrayList();
  private final DeployParser deployParser;
  private int paramIndex;
  
  public DefaultExpressionRequest(SpiOrmQueryRequest<?> queryRequest, DeployParser deployParser)
  {
    this.queryRequest = queryRequest;
    this.beanDescriptor = queryRequest.getBeanDescriptor();
    this.deployParser = deployParser;
  }
  
  public DefaultExpressionRequest(BeanDescriptor<?> beanDescriptor)
  {
    this.beanDescriptor = beanDescriptor;
    this.queryRequest = null;
    this.deployParser = null;
  }
  
  public String parseDeploy(String logicalProp)
  {
    String s = this.deployParser.getDeployWord(logicalProp);
    return s == null ? logicalProp : s;
  }
  
  public int nextParameter()
  {
    return ++this.paramIndex;
  }
  
  public BeanDescriptor<?> getBeanDescriptor()
  {
    return this.beanDescriptor;
  }
  
  public SpiOrmQueryRequest<?> getQueryRequest()
  {
    return this.queryRequest;
  }
  
  public SpiExpressionRequest append(String sql)
  {
    this.sb.append(sql);
    return this;
  }
  
  public void addBindValue(Object bindValue)
  {
    this.bindValues.add(bindValue);
  }
  
  public boolean includeProperty(String propertyName)
  {
    return true;
  }
  
  public String getSql()
  {
    return this.sb.toString();
  }
  
  public ArrayList<Object> getBindValues()
  {
    return this.bindValues;
  }
}
