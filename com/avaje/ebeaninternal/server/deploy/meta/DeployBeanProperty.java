package com.avaje.ebeaninternal.server.deploy.meta;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.avaje.ebean.config.ScalarTypeConverter;
import com.avaje.ebean.config.dbplatform.DbEncrypt;
import com.avaje.ebean.config.dbplatform.DbEncryptFunction;
import com.avaje.ebean.config.ldap.LdapAttributeAdapter;
import com.avaje.ebean.validation.factory.Validator;
import com.avaje.ebeaninternal.server.core.InternString;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor.EntityType;
import com.avaje.ebeaninternal.server.deploy.generatedproperty.GeneratedProperty;
import com.avaje.ebeaninternal.server.reflect.BeanReflectGetter;
import com.avaje.ebeaninternal.server.reflect.BeanReflectSetter;
import com.avaje.ebeaninternal.server.type.ScalarType;
import com.avaje.ebeaninternal.server.type.ScalarTypeEnum;
import com.avaje.ebeaninternal.server.type.ScalarTypeWrapper;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EmbeddedId;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Version;

public class DeployBeanProperty
{
  private static final int ID_ORDER = 1000000;
  private static final int UNIDIRECTIONAL_ORDER = 100000;
  private static final int AUDITCOLUMN_ORDER = -1000000;
  private static final int VERSIONCOLUMN_ORDER = -1000000;
  public static final String EXCLUDE_FROM_UPDATE_WHERE = "EXCLUDE_FROM_UPDATE_WHERE";
  public static final String EXCLUDE_FROM_DELETE_WHERE = "EXCLUDE_FROM_DELETE_WHERE";
  public static final String EXCLUDE_FROM_INSERT = "EXCLUDE_FROM_INSERT";
  public static final String EXCLUDE_FROM_UPDATE = "EXCLUDE_FROM_UPDATE";
  private boolean id;
  private boolean embedded;
  private boolean versionColumn;
  private boolean fetchEager = true;
  private boolean nullable = true;
  private boolean unique;
  private LdapAttributeAdapter ldapAttributeAdapter;
  private int dbLength;
  private int dbScale;
  private String dbColumnDefn;
  private boolean isTransient;
  private boolean localEncrypted;
  private boolean dbEncrypted;
  private DbEncryptFunction dbEncryptFunction;
  private int dbEncryptedType;
  private String dbBind = "?";
  private boolean dbRead;
  private boolean dbInsertable;
  private boolean dbUpdateable;
  private DeployTableJoin secondaryTableJoin;
  private String secondaryTableJoinPrefix;
  private String secondaryTable;
  private Class<?> owningType;
  private boolean lob;
  private boolean naturalKey;
  private String name;
  private Field field;
  private Class<?> propertyType;
  private ScalarType<?> scalarType;
  private String dbColumn;
  private String sqlFormulaSelect;
  private String sqlFormulaJoin;
  private int dbType;
  private Object defaultValue;
  private HashMap<String, String> extraAttributeMap = new HashMap();
  private Method readMethod;
  private Method writeMethod;
  private BeanReflectGetter getter;
  private BeanReflectSetter setter;
  private GeneratedProperty generatedProperty;
  private List<Validator> validators = new ArrayList();
  private final DeployBeanDescriptor<?> desc;
  private boolean undirectionalShadow;
  private int sortOrder;
  
  public DeployBeanProperty(DeployBeanDescriptor<?> desc, Class<?> propertyType, ScalarType<?> scalarType, ScalarTypeConverter<?, ?> typeConverter)
  {
    this.desc = desc;
    this.propertyType = propertyType;
    this.scalarType = wrapScalarType(propertyType, scalarType, typeConverter);
  }
  
  private ScalarType<?> wrapScalarType(Class<?> propertyType, ScalarType<?> scalarType, ScalarTypeConverter<?, ?> typeConverter)
  {
    if (typeConverter == null) {
      return scalarType;
    }
    return new ScalarTypeWrapper(propertyType, scalarType, typeConverter);
  }
  
  public int getSortOverride()
  {
    if (this.field == null) {
      return 0;
    }
    if (this.field.getAnnotation(Id.class) != null) {
      return 1000000;
    }
    if (this.field.getAnnotation(EmbeddedId.class) != null) {
      return 1000000;
    }
    if (this.undirectionalShadow) {
      return 100000;
    }
    if (this.field.getAnnotation(CreatedTimestamp.class) != null) {
      return -1000000;
    }
    if (this.field.getAnnotation(UpdatedTimestamp.class) != null) {
      return -1000000;
    }
    if (this.field.getAnnotation(Version.class) != null) {
      return -1000000;
    }
    return 0;
  }
  
  public boolean isScalar()
  {
    return true;
  }
  
  public String getFullBeanName()
  {
    return this.desc.getFullName() + "." + this.name;
  }
  
  public boolean isNullablePrimitive()
  {
    if ((this.nullable) && (this.propertyType.isPrimitive())) {
      return true;
    }
    return false;
  }
  
  public int getDbLength()
  {
    if ((this.dbLength == 0) && (this.scalarType != null)) {
      return this.scalarType.getLength();
    }
    return this.dbLength;
  }
  
  public int getSortOrder()
  {
    return this.sortOrder;
  }
  
  public void setSortOrder(int sortOrder)
  {
    this.sortOrder = sortOrder;
  }
  
  public boolean isUndirectionalShadow()
  {
    return this.undirectionalShadow;
  }
  
  public void setUndirectionalShadow(boolean undirectionalShadow)
  {
    this.undirectionalShadow = undirectionalShadow;
  }
  
  public boolean isLocalEncrypted()
  {
    return this.localEncrypted;
  }
  
  public void setLocalEncrypted(boolean localEncrypted)
  {
    this.localEncrypted = localEncrypted;
  }
  
  public void setDbLength(int dbLength)
  {
    this.dbLength = dbLength;
  }
  
  public int getDbScale()
  {
    return this.dbScale;
  }
  
  public void setDbScale(int dbScale)
  {
    this.dbScale = dbScale;
  }
  
  public String getDbColumnDefn()
  {
    return this.dbColumnDefn;
  }
  
  public void setDbColumnDefn(String dbColumnDefn)
  {
    if ((dbColumnDefn == null) || (dbColumnDefn.trim().length() == 0)) {
      this.dbColumnDefn = null;
    } else {
      this.dbColumnDefn = InternString.intern(dbColumnDefn);
    }
  }
  
  public String getDbConstraintExpression()
  {
    if ((this.scalarType instanceof ScalarTypeEnum))
    {
      ScalarTypeEnum etype = (ScalarTypeEnum)this.scalarType;
      
      return "check (" + this.dbColumn + " in " + etype.getContraintInValues() + ")";
    }
    return null;
  }
  
  public void addValidator(Validator validator)
  {
    this.validators.add(validator);
  }
  
  public boolean containsValidatorType(Class<?> type)
  {
    Iterator<Validator> it = this.validators.iterator();
    while (it.hasNext())
    {
      Validator validator = (Validator)it.next();
      if (validator.getClass().equals(type)) {
        return true;
      }
    }
    return false;
  }
  
  public Validator[] getValidators()
  {
    return (Validator[])this.validators.toArray(new Validator[this.validators.size()]);
  }
  
  public ScalarType<?> getScalarType()
  {
    return this.scalarType;
  }
  
  public void setScalarType(ScalarType<?> scalarType)
  {
    this.scalarType = scalarType;
  }
  
  public BeanReflectGetter getGetter()
  {
    return this.getter;
  }
  
  public BeanReflectSetter getSetter()
  {
    return this.setter;
  }
  
  public Method getReadMethod()
  {
    return this.readMethod;
  }
  
  public Method getWriteMethod()
  {
    return this.writeMethod;
  }
  
  public void setOwningType(Class<?> owningType)
  {
    this.owningType = owningType;
  }
  
  public Class<?> getOwningType()
  {
    return this.owningType;
  }
  
  public boolean isLocal()
  {
    return (this.owningType == null) || (this.owningType.equals(this.desc.getBeanType()));
  }
  
  public void setGetter(BeanReflectGetter getter)
  {
    this.getter = getter;
  }
  
  public void setSetter(BeanReflectSetter setter)
  {
    this.setter = setter;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = InternString.intern(name);
  }
  
  public Field getField()
  {
    return this.field;
  }
  
  public void setField(Field field)
  {
    this.field = field;
  }
  
  public boolean isNaturalKey()
  {
    return this.naturalKey;
  }
  
  public void setNaturalKey(boolean naturalKey)
  {
    this.naturalKey = naturalKey;
  }
  
  public boolean isGenerated()
  {
    return this.generatedProperty != null;
  }
  
  public GeneratedProperty getGeneratedProperty()
  {
    return this.generatedProperty;
  }
  
  public void setGeneratedProperty(GeneratedProperty generatedValue)
  {
    this.generatedProperty = generatedValue;
  }
  
  public boolean isNullable()
  {
    return this.nullable;
  }
  
  public void setNullable(boolean isNullable)
  {
    this.nullable = isNullable;
  }
  
  public boolean isUnique()
  {
    return this.unique;
  }
  
  public void setUnique(boolean unique)
  {
    this.unique = unique;
  }
  
  public LdapAttributeAdapter getLdapAttributeAdapter()
  {
    return this.ldapAttributeAdapter;
  }
  
  public void setLdapAttributeAdapter(LdapAttributeAdapter ldapAttributeAdapter)
  {
    this.ldapAttributeAdapter = ldapAttributeAdapter;
  }
  
  public boolean isVersionColumn()
  {
    return this.versionColumn;
  }
  
  public void setVersionColumn(boolean isVersionColumn)
  {
    this.versionColumn = isVersionColumn;
  }
  
  public boolean isFetchEager()
  {
    return this.fetchEager;
  }
  
  public void setFetchType(FetchType fetchType)
  {
    this.fetchEager = FetchType.EAGER.equals(fetchType);
  }
  
  public String getSqlFormulaSelect()
  {
    return this.sqlFormulaSelect;
  }
  
  public String getSqlFormulaJoin()
  {
    return this.sqlFormulaJoin;
  }
  
  public void setSqlFormula(String formulaSelect, String formulaJoin)
  {
    this.sqlFormulaSelect = formulaSelect;
    this.sqlFormulaJoin = (formulaJoin.equals("") ? null : formulaJoin);
    this.dbRead = true;
    this.dbInsertable = false;
    this.dbUpdateable = false;
  }
  
  public String getElPlaceHolder(BeanDescriptor.EntityType et)
  {
    if (this.sqlFormulaSelect != null) {
      return this.sqlFormulaSelect;
    }
    if (BeanDescriptor.EntityType.LDAP.equals(et)) {
      return getDbColumn();
    }
    if (this.secondaryTableJoinPrefix != null) {
      return "${" + this.secondaryTableJoinPrefix + "}" + getDbColumn();
    }
    return "${}" + getDbColumn();
  }
  
  public String getDbColumn()
  {
    if (this.sqlFormulaSelect != null) {
      return this.sqlFormulaSelect;
    }
    return this.dbColumn;
  }
  
  public void setDbColumn(String dbColumn)
  {
    this.dbColumn = InternString.intern(dbColumn);
  }
  
  public int getDbType()
  {
    return this.dbType;
  }
  
  public void setDbType(int dbType)
  {
    this.dbType = dbType;
    this.lob = isLobType(dbType);
  }
  
  public boolean isLob()
  {
    return this.lob;
  }
  
  private boolean isLobType(int type)
  {
    switch (type)
    {
    case 2005: 
      return true;
    case 2004: 
      return true;
    case -4: 
      return true;
    case -1: 
      return true;
    }
    return false;
  }
  
  public boolean isSecondaryTable()
  {
    return this.secondaryTable != null;
  }
  
  public String getSecondaryTable()
  {
    return this.secondaryTable;
  }
  
  public void setSecondaryTable(String secondaryTable)
  {
    this.secondaryTable = secondaryTable;
    this.dbInsertable = false;
    this.dbUpdateable = false;
  }
  
  public String getSecondaryTableJoinPrefix()
  {
    return this.secondaryTableJoinPrefix;
  }
  
  public DeployTableJoin getSecondaryTableJoin()
  {
    return this.secondaryTableJoin;
  }
  
  public void setSecondaryTableJoin(DeployTableJoin secondaryTableJoin, String prefix)
  {
    this.secondaryTableJoin = secondaryTableJoin;
    this.secondaryTableJoinPrefix = prefix;
  }
  
  public String getDbBind()
  {
    return this.dbBind;
  }
  
  public void setDbBind(String dbBind)
  {
    this.dbBind = dbBind;
  }
  
  public boolean isDbEncrypted()
  {
    return this.dbEncrypted;
  }
  
  public DbEncryptFunction getDbEncryptFunction()
  {
    return this.dbEncryptFunction;
  }
  
  public void setDbEncryptFunction(DbEncryptFunction dbEncryptFunction, DbEncrypt dbEncrypt, int dbLen)
  {
    this.dbEncryptFunction = dbEncryptFunction;
    this.dbEncrypted = true;
    this.dbBind = dbEncryptFunction.getEncryptBindSql();
    
    this.dbEncryptedType = (isLob() ? 2004 : dbEncrypt.getEncryptDbType());
    if (dbLen > 0) {
      setDbLength(dbLen);
    }
  }
  
  public int getDbEncryptedType()
  {
    return this.dbEncryptedType;
  }
  
  public void setDbEncryptedType(int dbEncryptedType)
  {
    this.dbEncryptedType = dbEncryptedType;
  }
  
  public boolean isDbRead()
  {
    return this.dbRead;
  }
  
  public void setDbRead(boolean isDBRead)
  {
    this.dbRead = isDBRead;
  }
  
  public boolean isDbInsertable()
  {
    return this.dbInsertable;
  }
  
  public void setDbInsertable(boolean insertable)
  {
    this.dbInsertable = insertable;
  }
  
  public boolean isDbUpdateable()
  {
    return this.dbUpdateable;
  }
  
  public void setDbUpdateable(boolean updateable)
  {
    this.dbUpdateable = updateable;
  }
  
  public boolean isTransient()
  {
    return this.isTransient;
  }
  
  public void setTransient(boolean isTransient)
  {
    this.isTransient = isTransient;
  }
  
  public void setReadMethod(Method readMethod)
  {
    this.readMethod = readMethod;
  }
  
  public void setWriteMethod(Method writeMethod)
  {
    this.writeMethod = writeMethod;
  }
  
  public Class<?> getPropertyType()
  {
    return this.propertyType;
  }
  
  public boolean isId()
  {
    return this.id;
  }
  
  public void setId(boolean id)
  {
    this.id = id;
  }
  
  public boolean isEmbedded()
  {
    return this.embedded;
  }
  
  public void setEmbedded(boolean embedded)
  {
    this.embedded = embedded;
  }
  
  public Map<String, String> getExtraAttributeMap()
  {
    return this.extraAttributeMap;
  }
  
  public String getExtraAttribute(String key)
  {
    return (String)this.extraAttributeMap.get(key);
  }
  
  public void setExtraAttribute(String key, String value)
  {
    this.extraAttributeMap.put(key, value);
  }
  
  public Object getDefaultValue()
  {
    return this.defaultValue;
  }
  
  public void setDefaultValue(Object defaultValue)
  {
    this.defaultValue = defaultValue;
  }
  
  public String toString()
  {
    return this.desc.getFullName() + "." + this.name;
  }
}
