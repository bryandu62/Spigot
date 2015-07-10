package com.avaje.ebeaninternal.server.deploy.parse;

import com.avaje.ebean.Query.UseIndex;
import com.avaje.ebean.annotation.CacheStrategy;
import com.avaje.ebean.annotation.LdapDomain;
import com.avaje.ebean.annotation.NamedUpdate;
import com.avaje.ebean.annotation.NamedUpdates;
import com.avaje.ebean.annotation.UpdateMode;
import com.avaje.ebean.config.NamingConvention;
import com.avaje.ebean.config.TableName;
import com.avaje.ebeaninternal.server.core.CacheOptions;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor.EntityType;
import com.avaje.ebeaninternal.server.deploy.CompoundUniqueContraint;
import com.avaje.ebeaninternal.server.deploy.DeployNamedQuery;
import com.avaje.ebeaninternal.server.deploy.DeployNamedUpdate;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

public class AnnotationClass
  extends AnnotationParser
{
  public AnnotationClass(DeployBeanInfo<?> info)
  {
    super(info);
  }
  
  public void parse()
  {
    read(this.descriptor.getBeanType());
    setTableName();
  }
  
  private void setTableName()
  {
    if (this.descriptor.isBaseTableType())
    {
      TableName tableName = this.namingConvention.getTableName(this.descriptor.getBeanType());
      
      this.descriptor.setBaseTable(tableName);
    }
  }
  
  private String[] parseLdapObjectclasses(String objectclasses)
  {
    if ((objectclasses == null) || (objectclasses.length() == 0)) {
      return null;
    }
    return objectclasses.split(",");
  }
  
  private boolean isXmlElement(Class<?> cls)
  {
    XmlRootElement rootElement = (XmlRootElement)cls.getAnnotation(XmlRootElement.class);
    if (rootElement != null) {
      return true;
    }
    XmlType xmlType = (XmlType)cls.getAnnotation(XmlType.class);
    if (xmlType != null) {
      return true;
    }
    return false;
  }
  
  private void read(Class<?> cls)
  {
    LdapDomain ldapDomain = (LdapDomain)cls.getAnnotation(LdapDomain.class);
    if (ldapDomain != null)
    {
      this.descriptor.setName(cls.getSimpleName());
      this.descriptor.setEntityType(BeanDescriptor.EntityType.LDAP);
      this.descriptor.setLdapBaseDn(ldapDomain.baseDn());
      this.descriptor.setLdapObjectclasses(parseLdapObjectclasses(ldapDomain.objectclass()));
    }
    Entity entity = (Entity)cls.getAnnotation(Entity.class);
    if (entity != null)
    {
      if (entity.name().equals("")) {
        this.descriptor.setName(cls.getSimpleName());
      } else {
        this.descriptor.setName(entity.name());
      }
    }
    else if (isXmlElement(cls))
    {
      this.descriptor.setName(cls.getSimpleName());
      this.descriptor.setEntityType(BeanDescriptor.EntityType.XMLELEMENT);
    }
    Embeddable embeddable = (Embeddable)cls.getAnnotation(Embeddable.class);
    if (embeddable != null)
    {
      this.descriptor.setEntityType(BeanDescriptor.EntityType.EMBEDDED);
      this.descriptor.setName("Embeddable:" + cls.getSimpleName());
    }
    UniqueConstraint uc = (UniqueConstraint)cls.getAnnotation(UniqueConstraint.class);
    if (uc != null) {
      this.descriptor.addCompoundUniqueConstraint(new CompoundUniqueContraint(uc.columnNames()));
    }
    Table table = (Table)cls.getAnnotation(Table.class);
    if (table != null)
    {
      UniqueConstraint[] uniqueConstraints = table.uniqueConstraints();
      if (uniqueConstraints != null) {
        for (UniqueConstraint c : uniqueConstraints) {
          this.descriptor.addCompoundUniqueConstraint(new CompoundUniqueContraint(c.columnNames()));
        }
      }
    }
    UpdateMode updateMode = (UpdateMode)cls.getAnnotation(UpdateMode.class);
    if (updateMode != null) {
      this.descriptor.setUpdateChangesOnly(updateMode.updateChangesOnly());
    }
    NamedQueries namedQueries = (NamedQueries)cls.getAnnotation(NamedQueries.class);
    if (namedQueries != null) {
      readNamedQueries(namedQueries);
    }
    NamedQuery namedQuery = (NamedQuery)cls.getAnnotation(NamedQuery.class);
    if (namedQuery != null) {
      readNamedQuery(namedQuery);
    }
    NamedUpdates namedUpdates = (NamedUpdates)cls.getAnnotation(NamedUpdates.class);
    if (namedUpdates != null) {
      readNamedUpdates(namedUpdates);
    }
    NamedUpdate namedUpdate = (NamedUpdate)cls.getAnnotation(NamedUpdate.class);
    if (namedUpdate != null) {
      readNamedUpdate(namedUpdate);
    }
    CacheStrategy cacheStrategy = (CacheStrategy)cls.getAnnotation(CacheStrategy.class);
    if (cacheStrategy != null) {
      readCacheStrategy(cacheStrategy);
    }
  }
  
  private void readCacheStrategy(CacheStrategy cacheStrategy)
  {
    CacheOptions cacheOptions = this.descriptor.getCacheOptions();
    cacheOptions.setUseCache(cacheStrategy.useBeanCache());
    cacheOptions.setReadOnly(cacheStrategy.readOnly());
    cacheOptions.setWarmingQuery(cacheStrategy.warmingQuery());
    if (cacheStrategy.naturalKey().length() > 0)
    {
      String propName = cacheStrategy.naturalKey().trim();
      DeployBeanProperty beanProperty = this.descriptor.getBeanProperty(propName);
      if (beanProperty != null)
      {
        beanProperty.setNaturalKey(true);
        cacheOptions.setNaturalKey(propName);
      }
    }
    if (!Query.UseIndex.DEFAULT.equals(cacheStrategy.useIndex())) {
      this.descriptor.setUseIndex(cacheStrategy.useIndex());
    }
  }
  
  private void readNamedQueries(NamedQueries namedQueries)
  {
    NamedQuery[] queries = namedQueries.value();
    for (int i = 0; i < queries.length; i++) {
      readNamedQuery(queries[i]);
    }
  }
  
  private void readNamedQuery(NamedQuery namedQuery)
  {
    DeployNamedQuery q = new DeployNamedQuery(namedQuery);
    this.descriptor.add(q);
  }
  
  private void readNamedUpdates(NamedUpdates updates)
  {
    NamedUpdate[] updateArray = updates.value();
    for (int i = 0; i < updateArray.length; i++) {
      readNamedUpdate(updateArray[i]);
    }
  }
  
  private void readNamedUpdate(NamedUpdate update)
  {
    DeployNamedUpdate upd = new DeployNamedUpdate(update);
    this.descriptor.add(upd);
  }
}
