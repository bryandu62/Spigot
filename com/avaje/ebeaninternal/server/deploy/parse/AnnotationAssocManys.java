package com.avaje.ebeaninternal.server.deploy.parse;

import com.avaje.ebean.annotation.LdapAttribute;
import com.avaje.ebean.annotation.PrivateOwned;
import com.avaje.ebean.annotation.Where;
import com.avaje.ebean.bean.BeanCollection.ModifyListenMode;
import com.avaje.ebean.config.NamingConvention;
import com.avaje.ebean.config.TableName;
import com.avaje.ebeaninternal.server.deploy.BeanCascadeInfo;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptorManager;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanTable;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.meta.DeployTableJoin;
import com.avaje.ebeaninternal.server.deploy.meta.DeployTableJoinColumn;
import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import java.util.Iterator;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

public class AnnotationAssocManys
  extends AnnotationParser
{
  private final BeanDescriptorManager factory;
  
  public AnnotationAssocManys(DeployBeanInfo<?> info, BeanDescriptorManager factory)
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
      if ((prop instanceof DeployBeanPropertyAssocMany)) {
        read((DeployBeanPropertyAssocMany)prop);
      }
    }
  }
  
  private void read(DeployBeanPropertyAssocMany<?> prop)
  {
    OneToMany oneToMany = (OneToMany)get(prop, OneToMany.class);
    if (oneToMany != null)
    {
      readToOne(oneToMany, prop);
      PrivateOwned privateOwned = (PrivateOwned)get(prop, PrivateOwned.class);
      if (privateOwned != null)
      {
        prop.setModifyListenMode(BeanCollection.ModifyListenMode.REMOVALS);
        prop.getCascadeInfo().setDelete(privateOwned.cascadeRemove());
      }
    }
    ManyToMany manyToMany = (ManyToMany)get(prop, ManyToMany.class);
    if (manyToMany != null) {
      readToMany(manyToMany, prop);
    }
    OrderBy orderBy = (OrderBy)get(prop, OrderBy.class);
    if (orderBy != null) {
      prop.setFetchOrderBy(orderBy.value());
    }
    MapKey mapKey = (MapKey)get(prop, MapKey.class);
    if (mapKey != null) {
      prop.setMapKey(mapKey.name());
    }
    Where where = (Where)get(prop, Where.class);
    if (where != null) {
      prop.setExtraWhere(where.clause());
    }
    BeanTable beanTable = prop.getBeanTable();
    JoinColumn joinColumn = (JoinColumn)get(prop, JoinColumn.class);
    if (joinColumn != null) {
      prop.getTableJoin().addJoinColumn(true, joinColumn, beanTable);
    }
    JoinColumns joinColumns = (JoinColumns)get(prop, JoinColumns.class);
    if (joinColumns != null) {
      prop.getTableJoin().addJoinColumn(true, joinColumns.value(), beanTable);
    }
    JoinTable joinTable = (JoinTable)get(prop, JoinTable.class);
    if (joinTable != null) {
      if (prop.isManyToMany()) {
        readJoinTable(joinTable, prop);
      } else {
        prop.getTableJoin().addJoinColumn(true, joinTable.joinColumns(), beanTable);
      }
    }
    LdapAttribute ldapAttribute = (LdapAttribute)get(prop, LdapAttribute.class);
    if (ldapAttribute != null) {
      readLdapAttribute(ldapAttribute, prop);
    }
    if (prop.getMappedBy() != null) {
      return;
    }
    if (prop.isManyToMany())
    {
      manyToManyDefaultJoins(prop);
      return;
    }
    if ((!prop.getTableJoin().hasJoinColumns()) && (beanTable != null))
    {
      NamingConvention nc = this.factory.getNamingConvention();
      
      String fkeyPrefix = null;
      if (nc.isUseForeignKeyPrefix()) {
        fkeyPrefix = nc.getColumnFromProperty(this.descriptor.getBeanType(), this.descriptor.getName());
      }
      BeanTable owningBeanTable = this.factory.getBeanTable(this.descriptor.getBeanType());
      owningBeanTable.createJoinColumn(fkeyPrefix, prop.getTableJoin(), false);
    }
  }
  
  private void readJoinTable(JoinTable joinTable, DeployBeanPropertyAssocMany<?> prop)
  {
    String intTableName = getFullTableName(joinTable);
    
    DeployTableJoin intJoin = new DeployTableJoin();
    intJoin.setTable(intTableName);
    
    intJoin.addJoinColumn(true, joinTable.joinColumns(), prop.getBeanTable());
    
    DeployTableJoin destJoin = prop.getTableJoin();
    destJoin.addJoinColumn(false, joinTable.inverseJoinColumns(), prop.getBeanTable());
    
    intJoin.setType("left outer join");
    
    DeployTableJoin inverseDest = destJoin.createInverse(intTableName);
    prop.setIntersectionJoin(intJoin);
    prop.setInverseJoin(inverseDest);
  }
  
  private String getFullTableName(JoinTable joinTable)
  {
    StringBuilder sb = new StringBuilder();
    if (!StringHelper.isNull(joinTable.catalog())) {
      sb.append(joinTable.catalog()).append(".");
    }
    if (!StringHelper.isNull(joinTable.schema())) {
      sb.append(joinTable.schema()).append(".");
    }
    sb.append(joinTable.name());
    return sb.toString();
  }
  
  private void manyToManyDefaultJoins(DeployBeanPropertyAssocMany<?> prop)
  {
    String intTableName = null;
    
    DeployTableJoin intJoin = prop.getIntersectionJoin();
    if (intJoin == null)
    {
      intJoin = new DeployTableJoin();
      prop.setIntersectionJoin(intJoin);
    }
    else
    {
      intTableName = intJoin.getTable();
    }
    BeanTable localTable = this.factory.getBeanTable(this.descriptor.getBeanType());
    BeanTable otherTable = this.factory.getBeanTable(prop.getTargetType());
    
    String localTableName = localTable.getUnqualifiedBaseTable();
    String otherTableName = otherTable.getUnqualifiedBaseTable();
    if (intTableName == null)
    {
      intTableName = getM2MJoinTableName(localTable, otherTable);
      
      intJoin.setTable(intTableName);
      intJoin.setType("left outer join");
    }
    DeployTableJoin destJoin = prop.getTableJoin();
    if ((intJoin.hasJoinColumns()) && (destJoin.hasJoinColumns())) {
      return;
    }
    if (!intJoin.hasJoinColumns())
    {
      BeanProperty[] localIds = localTable.getIdProperties();
      for (int i = 0; i < localIds.length; i++)
      {
        String fkCol = localTableName + "_" + localIds[i].getDbColumn();
        intJoin.addJoinColumn(new DeployTableJoinColumn(localIds[i].getDbColumn(), fkCol));
      }
    }
    if (!destJoin.hasJoinColumns())
    {
      BeanProperty[] otherIds = otherTable.getIdProperties();
      for (int i = 0; i < otherIds.length; i++)
      {
        String fkCol = otherTableName + "_" + otherIds[i].getDbColumn();
        destJoin.addJoinColumn(new DeployTableJoinColumn(fkCol, otherIds[i].getDbColumn()));
      }
    }
    DeployTableJoin inverseDest = destJoin.createInverse(intTableName);
    prop.setInverseJoin(inverseDest);
  }
  
  private String errorMsgMissingBeanTable(Class<?> type, String from)
  {
    return "Error with association to [" + type + "] from [" + from + "]. Is " + type + " registered?";
  }
  
  private void readToMany(ManyToMany propAnn, DeployBeanPropertyAssocMany<?> manyProp)
  {
    manyProp.setMappedBy(propAnn.mappedBy());
    manyProp.setFetchType(propAnn.fetch());
    
    setCascadeTypes(propAnn.cascade(), manyProp.getCascadeInfo());
    
    Class<?> targetType = propAnn.targetEntity();
    if (targetType.equals(Void.TYPE)) {
      targetType = manyProp.getTargetType();
    } else {
      manyProp.setTargetType(targetType);
    }
    BeanTable assoc = this.factory.getBeanTable(targetType);
    if (assoc == null)
    {
      String msg = errorMsgMissingBeanTable(targetType, manyProp.getFullBeanName());
      throw new RuntimeException(msg);
    }
    manyProp.setManyToMany(true);
    manyProp.setModifyListenMode(BeanCollection.ModifyListenMode.ALL);
    manyProp.setBeanTable(assoc);
    manyProp.getTableJoin().setType("left outer join");
  }
  
  private void readToOne(OneToMany propAnn, DeployBeanPropertyAssocMany<?> manyProp)
  {
    manyProp.setMappedBy(propAnn.mappedBy());
    manyProp.setFetchType(propAnn.fetch());
    
    setCascadeTypes(propAnn.cascade(), manyProp.getCascadeInfo());
    
    Class<?> targetType = propAnn.targetEntity();
    if (targetType.equals(Void.TYPE)) {
      targetType = manyProp.getTargetType();
    } else {
      manyProp.setTargetType(targetType);
    }
    BeanTable assoc = this.factory.getBeanTable(targetType);
    if (assoc == null)
    {
      String msg = errorMsgMissingBeanTable(targetType, manyProp.getFullBeanName());
      throw new RuntimeException(msg);
    }
    manyProp.setBeanTable(assoc);
    manyProp.getTableJoin().setType("left outer join");
  }
  
  private String getM2MJoinTableName(BeanTable lhsTable, BeanTable rhsTable)
  {
    TableName lhs = new TableName(lhsTable.getBaseTable());
    TableName rhs = new TableName(rhsTable.getBaseTable());
    
    TableName joinTable = this.namingConvention.getM2MJoinTableName(lhs, rhs);
    
    return joinTable.getQualifiedName();
  }
}
