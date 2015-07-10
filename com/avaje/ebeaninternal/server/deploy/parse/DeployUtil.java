package com.avaje.ebeaninternal.server.deploy.parse;

import com.avaje.ebean.config.EncryptDeploy;
import com.avaje.ebean.config.EncryptDeployManager;
import com.avaje.ebean.config.EncryptKeyManager;
import com.avaje.ebean.config.Encryptor;
import com.avaje.ebean.config.NamingConvention;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.TableName;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.validation.factory.Validator;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyCompound;
import com.avaje.ebeaninternal.server.type.DataEncryptSupport;
import com.avaje.ebeaninternal.server.type.ScalarType;
import com.avaje.ebeaninternal.server.type.ScalarTypeEnumStandard.OrdinalEnum;
import com.avaje.ebeaninternal.server.type.ScalarTypeEnumStandard.StringEnum;
import com.avaje.ebeaninternal.server.type.SimpleAesEncryptor;
import com.avaje.ebeaninternal.server.type.TypeManager;
import java.lang.annotation.Annotation;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PersistenceException;

public class DeployUtil
{
  private static final Logger logger = Logger.getLogger(DeployUtil.class.getName());
  private static final int dbCLOBType = 2005;
  private static final int dbBLOBType = 2004;
  private final NamingConvention namingConvention;
  private final TypeManager typeManager;
  private final ValidatorFactoryManager validatorFactoryManager;
  private final String manyToManyAlias;
  private final DatabasePlatform dbPlatform;
  private final EncryptDeployManager encryptDeployManager;
  private final EncryptKeyManager encryptKeyManager;
  private final Encryptor bytesEncryptor;
  
  public DeployUtil(TypeManager typeMgr, ServerConfig serverConfig)
  {
    this.typeManager = typeMgr;
    this.namingConvention = serverConfig.getNamingConvention();
    this.dbPlatform = serverConfig.getDatabasePlatform();
    this.encryptDeployManager = serverConfig.getEncryptDeployManager();
    this.encryptKeyManager = serverConfig.getEncryptKeyManager();
    
    Encryptor be = serverConfig.getEncryptor();
    this.bytesEncryptor = (be != null ? be : new SimpleAesEncryptor());
    
    this.manyToManyAlias = "zzzzzz";
    
    this.validatorFactoryManager = new ValidatorFactoryManager();
  }
  
  public TypeManager getTypeManager()
  {
    return this.typeManager;
  }
  
  public DatabasePlatform getDbPlatform()
  {
    return this.dbPlatform;
  }
  
  public NamingConvention getNamingConvention()
  {
    return this.namingConvention;
  }
  
  public void checkEncryptKeyManagerDefined(String fullPropName)
  {
    if (this.encryptKeyManager == null)
    {
      String msg = "Using encryption on " + fullPropName + " but no EncryptKeyManager defined!";
      throw new PersistenceException(msg);
    }
  }
  
  public EncryptDeploy getEncryptDeploy(TableName table, String column)
  {
    if (this.encryptDeployManager == null) {
      return EncryptDeploy.ANNOTATION;
    }
    return this.encryptDeployManager.getEncryptDeploy(table, column);
  }
  
  public DataEncryptSupport createDataEncryptSupport(String table, String column)
  {
    return new DataEncryptSupport(this.encryptKeyManager, this.bytesEncryptor, table, column);
  }
  
  public String getManyToManyAlias()
  {
    return this.manyToManyAlias;
  }
  
  public void createValidator(DeployBeanProperty prop, Annotation ann)
  {
    try
    {
      Validator validator = this.validatorFactoryManager.create(ann, prop.getPropertyType());
      if (validator != null) {
        prop.addValidator(validator);
      }
    }
    catch (Exception e)
    {
      String msg = "Error creating a validator on " + prop.getFullBeanName();
      logger.log(Level.SEVERE, msg, e);
    }
  }
  
  public ScalarType<?> setEnumScalarType(Enumerated enumerated, DeployBeanProperty prop)
  {
    Class<?> enumType = prop.getPropertyType();
    if (!enumType.isEnum()) {
      throw new IllegalArgumentException("Class [" + enumType + "] is Not a Enum?");
    }
    ScalarType<?> scalarType = this.typeManager.getScalarType(enumType);
    if (scalarType == null)
    {
      scalarType = this.typeManager.createEnumScalarType(enumType);
      if (scalarType == null)
      {
        EnumType type = enumerated != null ? enumerated.value() : null;
        scalarType = createEnumScalarTypePerSpec(enumType, type, prop.getDbType());
      }
      this.typeManager.add(scalarType);
    }
    prop.setScalarType(scalarType);
    prop.setDbType(scalarType.getJdbcType());
    return scalarType;
  }
  
  private ScalarType<?> createEnumScalarTypePerSpec(Class<?> enumType, EnumType type, int dbType)
  {
    if (type == null) {
      return new ScalarTypeEnumStandard.OrdinalEnum(enumType);
    }
    if (type == EnumType.ORDINAL) {
      return new ScalarTypeEnumStandard.OrdinalEnum(enumType);
    }
    return new ScalarTypeEnumStandard.StringEnum(enumType);
  }
  
  public void setScalarType(DeployBeanProperty property)
  {
    if (property.getScalarType() != null) {
      return;
    }
    if ((property instanceof DeployBeanPropertyCompound)) {
      return;
    }
    ScalarType<?> scalarType = getScalarType(property);
    if (scalarType != null)
    {
      property.setDbType(scalarType.getJdbcType());
      property.setScalarType(scalarType);
    }
  }
  
  private ScalarType<?> getScalarType(DeployBeanProperty property)
  {
    Class<?> propType = property.getPropertyType();
    ScalarType<?> scalarType = this.typeManager.getScalarType(propType, property.getDbType());
    if (scalarType != null) {
      return scalarType;
    }
    String msg = property.getFullBeanName() + " has no ScalarType - type[" + propType.getName() + "]";
    if (!property.isTransient()) {
      throw new PersistenceException(msg);
    }
    logger.finest("... transient property " + msg);
    return null;
  }
  
  public void setLobType(DeployBeanProperty prop)
  {
    Class<?> type = prop.getPropertyType();
    
    int lobType = isClobType(type) ? 2005 : 2004;
    
    ScalarType<?> scalarType = this.typeManager.getScalarType(type, lobType);
    if (scalarType == null) {
      throw new RuntimeException("No ScalarType for LOB type [" + type + "] [" + lobType + "]");
    }
    prop.setDbType(lobType);
    prop.setScalarType(scalarType);
  }
  
  public boolean isClobType(Class<?> type)
  {
    if (type.equals(String.class)) {
      return true;
    }
    return false;
  }
}
