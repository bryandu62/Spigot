package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.config.ScalarTypeConverter;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyCompound;
import com.avaje.ebeaninternal.server.el.ElPropertyChainBuilder;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;
import com.avaje.ebeaninternal.server.query.SqlBeanLoad;
import com.avaje.ebeaninternal.server.text.json.ReadJsonContext;
import com.avaje.ebeaninternal.server.text.json.WriteJsonContext;
import com.avaje.ebeaninternal.server.type.CtCompoundProperty;
import com.avaje.ebeaninternal.server.type.CtCompoundPropertyElAdapter;
import com.avaje.ebeaninternal.server.type.CtCompoundType;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

public class BeanPropertyCompound
  extends BeanProperty
{
  private final CtCompoundType<?> compoundType;
  private final ScalarTypeConverter typeConverter;
  private final BeanProperty[] scalarProperties;
  private final LinkedHashMap<String, BeanProperty> propertyMap = new LinkedHashMap();
  private final LinkedHashMap<String, CtCompoundPropertyElAdapter> nonScalarMap = new LinkedHashMap();
  private final BeanPropertyCompoundRoot root;
  
  public BeanPropertyCompound(BeanDescriptorMap owner, BeanDescriptor<?> descriptor, DeployBeanPropertyCompound deploy)
  {
    super(owner, descriptor, deploy);
    
    this.compoundType = deploy.getCompoundType();
    this.typeConverter = deploy.getTypeConverter();
    
    this.root = deploy.getFlatProperties(owner, descriptor);
    
    this.scalarProperties = this.root.getScalarProperties();
    for (int i = 0; i < this.scalarProperties.length; i++) {
      this.propertyMap.put(this.scalarProperties[i].getName(), this.scalarProperties[i]);
    }
    List<CtCompoundProperty> nonScalarPropsList = this.root.getNonScalarProperties();
    for (int i = 0; i < nonScalarPropsList.size(); i++)
    {
      CtCompoundProperty ctProp = (CtCompoundProperty)nonScalarPropsList.get(i);
      CtCompoundPropertyElAdapter adapter = new CtCompoundPropertyElAdapter(ctProp);
      this.nonScalarMap.put(ctProp.getRelativeName(), adapter);
    }
  }
  
  public void initialise()
  {
    if ((!this.isTransient) && (this.compoundType == null))
    {
      String msg = "No cvoInternalType assigned to " + this.descriptor.getFullName() + "." + getName();
      throw new RuntimeException(msg);
    }
  }
  
  public void setDeployOrder(int deployOrder)
  {
    this.deployOrder = deployOrder;
    for (CtCompoundPropertyElAdapter adapter : this.nonScalarMap.values()) {
      adapter.setDeployOrder(deployOrder);
    }
  }
  
  public Object getValueUnderlying(Object bean)
  {
    Object value = getValue(bean);
    if (this.typeConverter != null) {
      value = this.typeConverter.unwrapValue(value);
    }
    return value;
  }
  
  public Object getValue(Object bean)
  {
    return super.getValue(bean);
  }
  
  public Object getValueIntercept(Object bean)
  {
    return super.getValueIntercept(bean);
  }
  
  public void setValue(Object bean, Object value)
  {
    super.setValue(bean, value);
  }
  
  public void setValueIntercept(Object bean, Object value)
  {
    super.setValueIntercept(bean, value);
  }
  
  public ElPropertyValue buildElPropertyValue(String propName, String remainder, ElPropertyChainBuilder chain, boolean propertyDeploy)
  {
    if (chain == null) {
      chain = new ElPropertyChainBuilder(true, propName);
    }
    chain.add(this);
    
    BeanProperty p = (BeanProperty)this.propertyMap.get(remainder);
    if (p != null) {
      return chain.add(p).build();
    }
    CtCompoundPropertyElAdapter elAdapter = (CtCompoundPropertyElAdapter)this.nonScalarMap.get(remainder);
    if (elAdapter == null) {
      throw new RuntimeException("property [" + remainder + "] not found in " + getFullBeanName());
    }
    return chain.add(elAdapter).build();
  }
  
  public void appendSelect(DbSqlContext ctx, boolean subQuery)
  {
    if (!this.isTransient) {
      for (int i = 0; i < this.scalarProperties.length; i++) {
        this.scalarProperties[i].appendSelect(ctx, subQuery);
      }
    }
  }
  
  public BeanProperty[] getScalarProperties()
  {
    return this.scalarProperties;
  }
  
  public Object readSet(DbReadContext ctx, Object bean, Class<?> type)
    throws SQLException
  {
    boolean assignable = (type == null) || (this.owningType.isAssignableFrom(type));
    
    Object v = this.compoundType.read(ctx.getDataReader());
    if (assignable) {
      setValue(bean, v);
    }
    return v;
  }
  
  public Object read(DbReadContext ctx)
    throws SQLException
  {
    Object v = this.compoundType.read(ctx.getDataReader());
    if (this.typeConverter != null) {
      v = this.typeConverter.wrapValue(v);
    }
    return v;
  }
  
  public void loadIgnore(DbReadContext ctx)
  {
    this.compoundType.loadIgnore(ctx.getDataReader());
  }
  
  public void load(SqlBeanLoad sqlBeanLoad)
    throws SQLException
  {
    sqlBeanLoad.load(this);
  }
  
  public Object elGetReference(Object bean)
  {
    return bean;
  }
  
  public void jsonWrite(WriteJsonContext ctx, Object bean)
  {
    Object valueObject = getValueIntercept(bean);
    this.compoundType.jsonWrite(ctx, valueObject, this.name);
  }
  
  public void jsonRead(ReadJsonContext ctx, Object bean)
  {
    Object objValue = this.compoundType.jsonRead(ctx);
    setValue(bean, objValue);
  }
}
