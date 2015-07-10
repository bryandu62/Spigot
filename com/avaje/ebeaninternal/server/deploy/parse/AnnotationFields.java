package com.avaje.ebeaninternal.server.deploy.parse;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.EmbeddedColumns;
import com.avaje.ebean.annotation.Encrypted;
import com.avaje.ebean.annotation.Formula;
import com.avaje.ebean.annotation.LdapAttribute;
import com.avaje.ebean.annotation.LdapId;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.avaje.ebean.config.EncryptDeploy;
import com.avaje.ebean.config.EncryptDeploy.Mode;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebean.config.NamingConvention;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.config.dbplatform.DbEncrypt;
import com.avaje.ebean.config.dbplatform.DbEncryptFunction;
import com.avaje.ebean.config.dbplatform.IdType;
import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotNull;
import com.avaje.ebean.validation.Pattern;
import com.avaje.ebean.validation.Patterns;
import com.avaje.ebean.validation.ValidatorMeta;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor.EntityType;
import com.avaje.ebeaninternal.server.deploy.generatedproperty.GeneratedPropertyFactory;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanEmbedded;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyAssoc;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyCompound;
import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import com.avaje.ebeaninternal.server.type.CtCompoundType;
import com.avaje.ebeaninternal.server.type.DataEncryptSupport;
import com.avaje.ebeaninternal.server.type.ScalarType;
import com.avaje.ebeaninternal.server.type.ScalarTypeBytesBase;
import com.avaje.ebeaninternal.server.type.ScalarTypeBytesEncrypted;
import com.avaje.ebeaninternal.server.type.ScalarTypeEncryptedWrapper;
import com.avaje.ebeaninternal.server.type.ScalarTypeLdapBoolean;
import com.avaje.ebeaninternal.server.type.ScalarTypeLdapDate;
import com.avaje.ebeaninternal.server.type.ScalarTypeLdapTimestamp;
import com.avaje.ebeaninternal.server.type.TypeManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PersistenceException;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

public class AnnotationFields
  extends AnnotationParser
{
  private FetchType defaultLobFetchType = FetchType.LAZY;
  private GeneratedPropertyFactory generatedPropFactory = new GeneratedPropertyFactory();
  
  public AnnotationFields(DeployBeanInfo<?> info)
  {
    super(info);
    if (GlobalProperties.getBoolean("ebean.lobEagerFetch", false)) {
      this.defaultLobFetchType = FetchType.EAGER;
    }
  }
  
  public void parse()
  {
    Iterator<DeployBeanProperty> it = this.descriptor.propertiesAll();
    while (it.hasNext())
    {
      DeployBeanProperty prop = (DeployBeanProperty)it.next();
      if ((prop instanceof DeployBeanPropertyAssoc)) {
        readAssocOne(prop);
      } else {
        readField(prop);
      }
      readValidations(prop);
    }
  }
  
  private void readAssocOne(DeployBeanProperty prop)
  {
    Id id = (Id)get(prop, Id.class);
    if (id != null)
    {
      prop.setId(true);
      prop.setNullable(false);
    }
    EmbeddedId embeddedId = (EmbeddedId)get(prop, EmbeddedId.class);
    if (embeddedId != null)
    {
      prop.setId(true);
      prop.setNullable(false);
      prop.setEmbedded(true);
    }
  }
  
  private void readField(DeployBeanProperty prop)
  {
    boolean isEnum = prop.getPropertyType().isEnum();
    Enumerated enumerated = (Enumerated)get(prop, Enumerated.class);
    if ((isEnum) || (enumerated != null)) {
      this.util.setEnumScalarType(enumerated, prop);
    }
    prop.setDbRead(true);
    prop.setDbInsertable(true);
    prop.setDbUpdateable(true);
    
    Column column = (Column)get(prop, Column.class);
    if (column != null) {
      readColumn(column, prop);
    }
    LdapAttribute ldapAttribute = (LdapAttribute)get(prop, LdapAttribute.class);
    if (ldapAttribute != null) {
      readLdapAttribute(ldapAttribute, prop);
    }
    if (prop.getDbColumn() == null) {
      if (BeanDescriptor.EntityType.LDAP.equals(this.descriptor.getEntityType()))
      {
        prop.setDbColumn(prop.getName());
      }
      else
      {
        String dbColumn = this.namingConvention.getColumnFromProperty(this.beanType, prop.getName());
        prop.setDbColumn(dbColumn);
      }
    }
    GeneratedValue gen = (GeneratedValue)get(prop, GeneratedValue.class);
    if (gen != null) {
      readGenValue(gen, prop);
    }
    Id id = (Id)get(prop, Id.class);
    if (id != null) {
      readId(id, prop);
    }
    LdapId ldapId = (LdapId)get(prop, LdapId.class);
    if (ldapId != null)
    {
      prop.setId(true);
      prop.setNullable(false);
    }
    Lob lob = (Lob)get(prop, Lob.class);
    Temporal temporal = (Temporal)get(prop, Temporal.class);
    if (temporal != null) {
      readTemporal(temporal, prop);
    } else if (lob != null) {
      this.util.setLobType(prop);
    }
    Formula formula = (Formula)get(prop, Formula.class);
    if (formula != null) {
      prop.setSqlFormula(formula.select(), formula.join());
    }
    Version version = (Version)get(prop, Version.class);
    if (version != null)
    {
      prop.setVersionColumn(true);
      this.generatedPropFactory.setVersion(prop);
    }
    Basic basic = (Basic)get(prop, Basic.class);
    if (basic != null)
    {
      prop.setFetchType(basic.fetch());
      if (!basic.optional()) {
        prop.setNullable(false);
      }
    }
    else if (prop.isLob())
    {
      prop.setFetchType(this.defaultLobFetchType);
    }
    CreatedTimestamp ct = (CreatedTimestamp)get(prop, CreatedTimestamp.class);
    if (ct != null) {
      this.generatedPropFactory.setInsertTimestamp(prop);
    }
    UpdatedTimestamp ut = (UpdatedTimestamp)get(prop, UpdatedTimestamp.class);
    if (ut != null) {
      this.generatedPropFactory.setUpdateTimestamp(prop);
    }
    NotNull notNull = (NotNull)get(prop, NotNull.class);
    if (notNull != null) {
      prop.setNullable(false);
    }
    Length length = (Length)get(prop, Length.class);
    if ((length != null) && 
      (length.max() < Integer.MAX_VALUE)) {
      prop.setDbLength(length.max());
    }
    EmbeddedColumns columns = (EmbeddedColumns)get(prop, EmbeddedColumns.class);
    if (columns != null) {
      if ((prop instanceof DeployBeanPropertyCompound))
      {
        DeployBeanPropertyCompound p = (DeployBeanPropertyCompound)prop;
        
        String propColumns = columns.columns();
        Map<String, String> propMap = StringHelper.delimitedToMap(propColumns, ",", "=");
        
        p.getDeployEmbedded().putAll(propMap);
        
        CtCompoundType<?> compoundType = p.getCompoundType();
        if (compoundType == null) {
          throw new RuntimeException("No registered CtCompoundType for " + p.getPropertyType());
        }
      }
      else
      {
        throw new RuntimeException("Can't use EmbeddedColumns on ScalarType " + prop.getFullBeanName());
      }
    }
    Transient t = (Transient)get(prop, Transient.class);
    if (t != null)
    {
      prop.setDbRead(false);
      prop.setDbInsertable(false);
      prop.setDbUpdateable(false);
      prop.setTransient(true);
    }
    if (!prop.isTransient())
    {
      EncryptDeploy encryptDeploy = this.util.getEncryptDeploy(this.info.getDescriptor().getBaseTableFull(), prop.getDbColumn());
      if ((encryptDeploy == null) || (encryptDeploy.getMode().equals(EncryptDeploy.Mode.MODE_ANNOTATION)))
      {
        Encrypted encrypted = (Encrypted)get(prop, Encrypted.class);
        if (encrypted != null) {
          setEncryption(prop, encrypted.dbEncryption(), encrypted.dbLength());
        }
      }
      else if (EncryptDeploy.Mode.MODE_ENCRYPT.equals(encryptDeploy.getMode()))
      {
        setEncryption(prop, encryptDeploy.isDbEncrypt(), encryptDeploy.getDbLength());
      }
    }
    if (BeanDescriptor.EntityType.LDAP.equals(this.descriptor.getEntityType())) {
      adjustTypesForLdap(prop);
    }
  }
  
  private static final ScalarTypeLdapBoolean LDAP_BOOLEAN_SCALARTYPE = new ScalarTypeLdapBoolean();
  
  private void adjustTypesForLdap(DeployBeanProperty prop)
  {
    Class<?> pt = prop.getPropertyType();
    if ((Boolean.TYPE.equals(pt)) || (Boolean.class.equals(pt)))
    {
      prop.setScalarType(LDAP_BOOLEAN_SCALARTYPE);
    }
    else
    {
      ScalarType<?> sqlScalarType = prop.getScalarType();
      int sqlType = sqlScalarType.getJdbcType();
      if (sqlType == 93) {
        prop.setScalarType(new ScalarTypeLdapTimestamp(sqlScalarType));
      } else if (sqlType == 91) {
        prop.setScalarType(new ScalarTypeLdapDate(sqlScalarType));
      }
    }
  }
  
  private void setEncryption(DeployBeanProperty prop, boolean dbEncString, int dbLen)
  {
    this.util.checkEncryptKeyManagerDefined(prop.getFullBeanName());
    
    ScalarType<?> st = prop.getScalarType();
    if (byte[].class.equals(st.getType()))
    {
      ScalarTypeBytesBase baseType = (ScalarTypeBytesBase)st;
      DataEncryptSupport support = createDataEncryptSupport(prop);
      ScalarTypeBytesEncrypted encryptedScalarType = new ScalarTypeBytesEncrypted(baseType, support);
      prop.setScalarType(encryptedScalarType);
      prop.setLocalEncrypted(true);
      return;
    }
    if (dbEncString)
    {
      DbEncrypt dbEncrypt = this.util.getDbPlatform().getDbEncrypt();
      if (dbEncrypt != null)
      {
        int jdbcType = prop.getScalarType().getJdbcType();
        DbEncryptFunction dbEncryptFunction = dbEncrypt.getDbEncryptFunction(jdbcType);
        if (dbEncryptFunction != null)
        {
          prop.setDbEncryptFunction(dbEncryptFunction, dbEncrypt, dbLen);
          return;
        }
      }
    }
    prop.setScalarType(createScalarType(prop, st));
    prop.setLocalEncrypted(true);
    if (dbLen > 0) {
      prop.setDbLength(dbLen);
    }
  }
  
  private ScalarTypeEncryptedWrapper<?> createScalarType(DeployBeanProperty prop, ScalarType<?> st)
  {
    DataEncryptSupport support = createDataEncryptSupport(prop);
    ScalarTypeBytesBase byteType = getDbEncryptType(prop);
    
    return new ScalarTypeEncryptedWrapper(st, byteType, support);
  }
  
  private ScalarTypeBytesBase getDbEncryptType(DeployBeanProperty prop)
  {
    int dbType = prop.isLob() ? 2004 : -3;
    return (ScalarTypeBytesBase)this.util.getTypeManager().getScalarType(dbType);
  }
  
  private DataEncryptSupport createDataEncryptSupport(DeployBeanProperty prop)
  {
    String table = this.info.getDescriptor().getBaseTable();
    String column = prop.getDbColumn();
    
    return this.util.createDataEncryptSupport(table, column);
  }
  
  private void readId(Id id, DeployBeanProperty prop)
  {
    prop.setId(true);
    prop.setNullable(false);
    if (prop.getPropertyType().equals(UUID.class)) {
      if (this.descriptor.getIdGeneratorName() == null)
      {
        this.descriptor.setIdGeneratorName("auto.uuid");
        this.descriptor.setIdType(IdType.GENERATOR);
      }
    }
  }
  
  private void readGenValue(GeneratedValue gen, DeployBeanProperty prop)
  {
    String genName = gen.generator();
    
    SequenceGenerator sequenceGenerator = (SequenceGenerator)find(prop, SequenceGenerator.class);
    if ((sequenceGenerator != null) && 
      (sequenceGenerator.name().equals(genName))) {
      genName = sequenceGenerator.sequenceName();
    }
    GenerationType strategy = gen.strategy();
    if (strategy == GenerationType.IDENTITY)
    {
      this.descriptor.setIdType(IdType.IDENTITY);
    }
    else if (strategy == GenerationType.SEQUENCE)
    {
      this.descriptor.setIdType(IdType.SEQUENCE);
      if ((genName != null) && (genName.length() > 0)) {
        this.descriptor.setIdGeneratorName(genName);
      }
    }
    else if ((strategy == GenerationType.AUTO) && 
      (prop.getPropertyType().equals(UUID.class)))
    {
      this.descriptor.setIdGeneratorName("auto.uuid");
      this.descriptor.setIdType(IdType.GENERATOR);
    }
  }
  
  private void readTemporal(Temporal temporal, DeployBeanProperty prop)
  {
    TemporalType type = temporal.value();
    if (type.equals(TemporalType.DATE)) {
      prop.setDbType(91);
    } else if (type.equals(TemporalType.TIMESTAMP)) {
      prop.setDbType(93);
    } else if (type.equals(TemporalType.TIME)) {
      prop.setDbType(92);
    } else {
      throw new PersistenceException("Unhandled type " + type);
    }
  }
  
  private void readColumn(Column columnAnn, DeployBeanProperty prop)
  {
    if (!isEmpty(columnAnn.name()))
    {
      String dbColumn = this.databasePlatform.convertQuotedIdentifiers(columnAnn.name());
      prop.setDbColumn(dbColumn);
    }
    prop.setDbInsertable(columnAnn.insertable());
    prop.setDbUpdateable(columnAnn.updatable());
    prop.setNullable(columnAnn.nullable());
    prop.setUnique(columnAnn.unique());
    if (columnAnn.precision() > 0) {
      prop.setDbLength(columnAnn.precision());
    } else if (columnAnn.length() != 255) {
      prop.setDbLength(columnAnn.length());
    }
    prop.setDbScale(columnAnn.scale());
    prop.setDbColumnDefn(columnAnn.columnDefinition());
    
    String baseTable = this.descriptor.getBaseTable();
    String tableName = columnAnn.table();
    if ((!tableName.equals("")) && (!tableName.equalsIgnoreCase(baseTable))) {
      prop.setSecondaryTable(tableName);
    }
  }
  
  private void readValidations(DeployBeanProperty prop)
  {
    Field field = prop.getField();
    if (field != null)
    {
      Annotation[] fieldAnnotations = field.getAnnotations();
      for (int i = 0; i < fieldAnnotations.length; i++) {
        readValidations(prop, fieldAnnotations[i]);
      }
    }
    Method readMethod = prop.getReadMethod();
    if (readMethod != null)
    {
      Annotation[] methAnnotations = readMethod.getAnnotations();
      for (int i = 0; i < methAnnotations.length; i++) {
        readValidations(prop, methAnnotations[i]);
      }
    }
  }
  
  private void readValidations(DeployBeanProperty prop, Annotation ann)
  {
    Class<?> type = ann.annotationType();
    if (type.equals(Patterns.class))
    {
      Patterns patterns = (Patterns)ann;
      Pattern[] patternsArray = patterns.patterns();
      for (int i = 0; i < patternsArray.length; i++) {
        this.util.createValidator(prop, patternsArray[i]);
      }
    }
    else
    {
      ValidatorMeta meta = (ValidatorMeta)type.getAnnotation(ValidatorMeta.class);
      if (meta != null) {
        this.util.createValidator(prop, ann);
      }
    }
  }
}
