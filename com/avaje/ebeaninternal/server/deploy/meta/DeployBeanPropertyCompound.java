package com.avaje.ebeaninternal.server.deploy.meta;

import com.avaje.ebean.config.ScalarTypeConverter;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptorMap;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyCompoundRoot;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyCompoundScalar;
import com.avaje.ebeaninternal.server.type.CtCompoundProperty;
import com.avaje.ebeaninternal.server.type.CtCompoundType;
import com.avaje.ebeaninternal.server.type.CtCompoundTypeScalarList;
import com.avaje.ebeaninternal.server.type.ScalarType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DeployBeanPropertyCompound
  extends DeployBeanProperty
{
  final CtCompoundType<?> compoundType;
  final ScalarTypeConverter<?, ?> typeConverter;
  DeployBeanEmbedded deployEmbedded;
  
  public DeployBeanPropertyCompound(DeployBeanDescriptor<?> desc, Class<?> targetType, CtCompoundType<?> compoundType, ScalarTypeConverter<?, ?> typeConverter)
  {
    super(desc, targetType, null, null);
    this.compoundType = compoundType;
    this.typeConverter = typeConverter;
  }
  
  public BeanPropertyCompoundRoot getFlatProperties(BeanDescriptorMap owner, BeanDescriptor<?> descriptor)
  {
    BeanPropertyCompoundRoot rootProperty = new BeanPropertyCompoundRoot(this);
    
    CtCompoundTypeScalarList ctMeta = new CtCompoundTypeScalarList();
    
    this.compoundType.accumulateScalarTypes(null, ctMeta);
    
    List<BeanProperty> beanPropertyList = new ArrayList();
    for (Map.Entry<String, ScalarType<?>> entry : ctMeta.entries())
    {
      String relativePropertyName = (String)entry.getKey();
      ScalarType<?> scalarType = (ScalarType)entry.getValue();
      
      CtCompoundProperty ctProp = ctMeta.getCompoundType(relativePropertyName);
      
      String dbColumn = relativePropertyName.replace(".", "_");
      dbColumn = getDbColumn(relativePropertyName, dbColumn);
      
      DeployBeanProperty deploy = new DeployBeanProperty(null, scalarType.getType(), scalarType, null);
      deploy.setScalarType(scalarType);
      deploy.setDbColumn(dbColumn);
      deploy.setName(relativePropertyName);
      deploy.setDbInsertable(true);
      deploy.setDbUpdateable(true);
      deploy.setDbRead(true);
      
      BeanPropertyCompoundScalar bp = new BeanPropertyCompoundScalar(rootProperty, deploy, ctProp, this.typeConverter);
      beanPropertyList.add(bp);
      
      rootProperty.register(bp);
    }
    rootProperty.setNonScalarProperties(ctMeta.getNonScalarProperties());
    return rootProperty;
  }
  
  private String getDbColumn(String propName, String defaultDbColumn)
  {
    if (this.deployEmbedded == null) {
      return defaultDbColumn;
    }
    String dbColumn = (String)this.deployEmbedded.getPropertyColumnMap().get(propName);
    return dbColumn == null ? defaultDbColumn : dbColumn;
  }
  
  public DeployBeanEmbedded getDeployEmbedded()
  {
    if (this.deployEmbedded == null) {
      this.deployEmbedded = new DeployBeanEmbedded();
    }
    return this.deployEmbedded;
  }
  
  public ScalarTypeConverter<?, ?> getTypeConverter()
  {
    return this.typeConverter;
  }
  
  public CtCompoundType<?> getCompoundType()
  {
    return this.compoundType;
  }
}
