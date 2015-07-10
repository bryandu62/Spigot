package com.avaje.ebeaninternal.server.deploy.parse;

import com.avaje.ebean.annotation.EmbeddedColumns;
import com.avaje.ebean.annotation.Where;
import com.avaje.ebean.config.NamingConvention;
import com.avaje.ebean.validation.NotNull;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptorManager;
import com.avaje.ebeaninternal.server.deploy.BeanTable;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanEmbedded;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.meta.DeployTableJoin;
import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

public class AnnotationAssocOnes
  extends AnnotationParser
{
  private final BeanDescriptorManager factory;
  
  public AnnotationAssocOnes(DeployBeanInfo<?> info, BeanDescriptorManager factory)
  {
    super(info);
    this.factory = factory;
  }
  
  public void parse()
  {
    Iterator<DeployBeanProperty> it = this.descriptor.propertiesAll();
    while (it.hasNext())
    {
      DeployBeanProperty prop = (DeployBeanProperty)it.next();
      if ((prop instanceof DeployBeanPropertyAssocOne)) {
        readAssocOne((DeployBeanPropertyAssocOne)prop);
      }
    }
  }
  
  private void readAssocOne(DeployBeanPropertyAssocOne<?> prop)
  {
    ManyToOne manyToOne = (ManyToOne)get(prop, ManyToOne.class);
    if (manyToOne != null) {
      readManyToOne(manyToOne, prop);
    }
    OneToOne oneToOne = (OneToOne)get(prop, OneToOne.class);
    if (oneToOne != null) {
      readOneToOne(oneToOne, prop);
    }
    Embedded embedded = (Embedded)get(prop, Embedded.class);
    if (embedded != null) {
      readEmbedded(embedded, prop);
    }
    EmbeddedId emId = (EmbeddedId)get(prop, EmbeddedId.class);
    if (emId != null)
    {
      prop.setEmbedded(true);
      prop.setId(true);
      prop.setNullable(false);
    }
    Column column = (Column)get(prop, Column.class);
    if ((column != null) && (!isEmpty(column.name()))) {
      prop.setDbColumn(column.name());
    }
    Id id = (Id)get(prop, Id.class);
    if (id != null)
    {
      prop.setEmbedded(true);
      prop.setId(true);
      prop.setNullable(false);
    }
    Where where = (Where)get(prop, Where.class);
    if (where != null) {
      prop.setExtraWhere(where.clause());
    }
    NotNull notNull = (NotNull)get(prop, NotNull.class);
    if (notNull != null)
    {
      prop.setNullable(false);
      
      prop.getTableJoin().setType("join");
    }
    BeanTable beanTable = prop.getBeanTable();
    JoinColumn joinColumn = (JoinColumn)get(prop, JoinColumn.class);
    if (joinColumn != null)
    {
      prop.getTableJoin().addJoinColumn(false, joinColumn, beanTable);
      if (!joinColumn.updatable()) {
        prop.setDbUpdateable(false);
      }
    }
    JoinColumns joinColumns = (JoinColumns)get(prop, JoinColumns.class);
    if (joinColumns != null) {
      prop.getTableJoin().addJoinColumn(false, joinColumns.value(), beanTable);
    }
    JoinTable joinTable = (JoinTable)get(prop, JoinTable.class);
    if (joinTable != null) {
      prop.getTableJoin().addJoinColumn(false, joinTable.joinColumns(), beanTable);
    }
    this.info.setBeanJoinType(prop, prop.isNullable());
    if ((!prop.getTableJoin().hasJoinColumns()) && (beanTable != null)) {
      if (prop.getMappedBy() == null)
      {
        NamingConvention nc = this.factory.getNamingConvention();
        
        String fkeyPrefix = null;
        if (nc.isUseForeignKeyPrefix()) {
          fkeyPrefix = nc.getColumnFromProperty(this.beanType, prop.getName());
        }
        beanTable.createJoinColumn(fkeyPrefix, prop.getTableJoin(), true);
      }
    }
  }
  
  private String errorMsgMissingBeanTable(Class<?> type, String from)
  {
    return "Error with association to [" + type + "] from [" + from + "]. Is " + type + " registered?";
  }
  
  private void readManyToOne(ManyToOne propAnn, DeployBeanProperty prop)
  {
    DeployBeanPropertyAssocOne<?> beanProp = (DeployBeanPropertyAssocOne)prop;
    
    setCascadeTypes(propAnn.cascade(), beanProp.getCascadeInfo());
    
    BeanTable assoc = this.factory.getBeanTable(beanProp.getPropertyType());
    if (assoc == null)
    {
      String msg = errorMsgMissingBeanTable(beanProp.getPropertyType(), prop.getFullBeanName());
      throw new RuntimeException(msg);
    }
    beanProp.setBeanTable(assoc);
    beanProp.setDbInsertable(true);
    beanProp.setDbUpdateable(true);
    beanProp.setNullable(propAnn.optional());
    beanProp.setFetchType(propAnn.fetch());
  }
  
  private void readOneToOne(OneToOne propAnn, DeployBeanPropertyAssocOne<?> prop)
  {
    prop.setOneToOne(true);
    prop.setDbInsertable(true);
    prop.setDbUpdateable(true);
    prop.setNullable(propAnn.optional());
    prop.setFetchType(propAnn.fetch());
    prop.setMappedBy(propAnn.mappedBy());
    if (!"".equals(propAnn.mappedBy())) {
      prop.setOneToOneExported(true);
    }
    setCascadeTypes(propAnn.cascade(), prop.getCascadeInfo());
    
    BeanTable assoc = this.factory.getBeanTable(prop.getPropertyType());
    if (assoc == null)
    {
      String msg = errorMsgMissingBeanTable(prop.getPropertyType(), prop.getFullBeanName());
      throw new RuntimeException(msg);
    }
    prop.setBeanTable(assoc);
  }
  
  private void readEmbedded(Embedded propAnn, DeployBeanPropertyAssocOne<?> prop)
  {
    prop.setEmbedded(true);
    prop.setDbInsertable(true);
    prop.setDbUpdateable(true);
    
    EmbeddedColumns columns = (EmbeddedColumns)get(prop, EmbeddedColumns.class);
    if (columns != null)
    {
      String propColumns = columns.columns();
      Map<String, String> propMap = StringHelper.delimitedToMap(propColumns, ",", "=");
      
      prop.getDeployEmbedded().putAll(propMap);
    }
    AttributeOverrides attrOverrides = (AttributeOverrides)get(prop, AttributeOverrides.class);
    if (attrOverrides != null)
    {
      HashMap<String, String> propMap = new HashMap();
      AttributeOverride[] aoArray = attrOverrides.value();
      for (int i = 0; i < aoArray.length; i++)
      {
        String propName = aoArray[i].name();
        String columnName = aoArray[i].column().name();
        
        propMap.put(propName, columnName);
      }
      prop.getDeployEmbedded().putAll(propMap);
    }
  }
}
