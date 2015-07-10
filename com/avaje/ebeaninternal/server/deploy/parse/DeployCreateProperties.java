package com.avaje.ebeaninternal.server.deploy.parse;

import com.avaje.ebean.config.ScalarTypeConverter;
import com.avaje.ebeaninternal.server.core.Message;
import com.avaje.ebeaninternal.server.deploy.DetermineManyType;
import com.avaje.ebeaninternal.server.deploy.ManyType;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyCompound;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertySimpleCollection;
import com.avaje.ebeaninternal.server.type.CtCompoundType;
import com.avaje.ebeaninternal.server.type.ScalaOptionTypeConverter;
import com.avaje.ebeaninternal.server.type.ScalarType;
import com.avaje.ebeaninternal.server.type.TypeManager;
import com.avaje.ebeaninternal.server.type.reflect.CheckImmutableResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import javax.persistence.Transient;

public class DeployCreateProperties
{
  private static final Logger logger = Logger.getLogger(DeployCreateProperties.class.getName());
  private final Class<?> scalaOptionClass;
  private final ScalarTypeConverter scalaOptionTypeConverter;
  private final DetermineManyType determineManyType;
  private final TypeManager typeManager;
  
  public DeployCreateProperties(TypeManager typeManager)
  {
    this.typeManager = typeManager;
    
    Class<?> tmpOptionClass = DetectScala.getScalaOptionClass();
    if (tmpOptionClass == null)
    {
      this.scalaOptionClass = null;
      this.scalaOptionTypeConverter = null;
    }
    else
    {
      this.scalaOptionClass = tmpOptionClass;
      this.scalaOptionTypeConverter = new ScalaOptionTypeConverter();
    }
    this.determineManyType = new DetermineManyType(tmpOptionClass != null);
  }
  
  public void createProperties(DeployBeanDescriptor<?> desc)
  {
    createProperties(desc, desc.getBeanType(), 0);
    desc.sortProperties();
    
    Iterator<DeployBeanProperty> it = desc.propertiesAll();
    while (it.hasNext())
    {
      DeployBeanProperty prop = (DeployBeanProperty)it.next();
      if (prop.isTransient()) {
        if ((prop.getWriteMethod() == null) || (prop.getReadMethod() == null))
        {
          logger.finest("... transient: " + prop.getFullBeanName());
        }
        else
        {
          String msg = Message.msg("deploy.property.nofield", desc.getFullName(), prop.getName());
          logger.warning(msg);
        }
      }
    }
  }
  
  private boolean ignoreFieldByName(String fieldName)
  {
    if (fieldName.startsWith("_ebean_")) {
      return true;
    }
    if (fieldName.startsWith("ajc$instance$")) {
      return true;
    }
    return false;
  }
  
  private void createProperties(DeployBeanDescriptor<?> desc, Class<?> beanType, int level)
  {
    boolean scalaObject = desc.isScalaObject();
    try
    {
      Method[] declaredMethods = beanType.getDeclaredMethods();
      Field[] fields = beanType.getDeclaredFields();
      for (int i = 0; i < fields.length; i++)
      {
        Field field = fields[i];
        if (!Modifier.isStatic(field.getModifiers())) {
          if (Modifier.isTransient(field.getModifiers()))
          {
            logger.finer("Skipping transient field " + field.getName() + " in " + beanType.getName());
          }
          else if (!ignoreFieldByName(field.getName()))
          {
            String fieldName = getFieldName(field, beanType);
            String initFieldName = initCap(fieldName);
            
            Method getter = findGetter(field, initFieldName, declaredMethods, scalaObject);
            Method setter = findSetter(field, initFieldName, declaredMethods, scalaObject);
            
            DeployBeanProperty prop = createProp(level, desc, field, beanType, getter, setter);
            if (prop != null)
            {
              int sortOverride = prop.getSortOverride();
              prop.setSortOrder(level * 10000 + 100 - i + sortOverride);
              
              DeployBeanProperty replaced = desc.addBeanProperty(prop);
              if ((replaced != null) && 
                (!replaced.isTransient()))
              {
                String msg = "Huh??? property " + prop.getFullBeanName() + " being defined twice";
                msg = msg + " but replaced property was not transient? This is not expected?";
                logger.warning(msg);
              }
            }
          }
        }
      }
      Class<?> superClass = beanType.getSuperclass();
      if (!superClass.equals(Object.class)) {
        createProperties(desc, superClass, level + 1);
      }
    }
    catch (PersistenceException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new PersistenceException(ex);
    }
  }
  
  private String initCap(String str)
  {
    if (str.length() > 1) {
      return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
    return str.toUpperCase();
  }
  
  private String getFieldName(Field field, Class<?> beanType)
  {
    String name = field.getName();
    if (((Boolean.class.equals(field.getType())) || (Boolean.TYPE.equals(field.getType()))) && (name.startsWith("is")) && (name.length() > 2))
    {
      char c = name.charAt(2);
      if (Character.isUpperCase(c))
      {
        String msg = "trimming off 'is' from boolean field name " + name + " in class " + beanType.getName();
        logger.log(Level.INFO, msg);
        
        return name.substring(2);
      }
    }
    return name;
  }
  
  private Method findGetter(Field field, String initFieldName, Method[] declaredMethods, boolean scalaObject)
  {
    String methGetName = "get" + initFieldName;
    String methIsName = "is" + initFieldName;
    String scalaGet = field.getName();
    for (int i = 0; i < declaredMethods.length; i++)
    {
      Method m = declaredMethods[i];
      if (((scalaObject) && (m.getName().equals(scalaGet))) || (m.getName().equals(methGetName)) || (m.getName().equals(methIsName)))
      {
        Class<?>[] params = m.getParameterTypes();
        if ((params.length == 0) && 
          (field.getType().equals(m.getReturnType())))
        {
          int modifiers = m.getModifiers();
          if ((Modifier.isPublic(modifiers)) && (!Modifier.isStatic(modifiers))) {
            return m;
          }
        }
      }
    }
    return null;
  }
  
  private Method findSetter(Field field, String initFieldName, Method[] declaredMethods, boolean scalaObject)
  {
    String methSetName = "set" + initFieldName;
    String scalaSetName = field.getName() + "_$eq";
    for (int i = 0; i < declaredMethods.length; i++)
    {
      Method m = declaredMethods[i];
      if (((scalaObject) && (m.getName().equals(scalaSetName))) || (m.getName().equals(methSetName)))
      {
        Class<?>[] params = m.getParameterTypes();
        if ((params.length == 1) && (field.getType().equals(params[0])) && 
          (Void.TYPE.equals(m.getReturnType())))
        {
          int modifiers = m.getModifiers();
          if ((Modifier.isPublic(modifiers)) && (!Modifier.isStatic(modifiers))) {
            return m;
          }
        }
      }
    }
    return null;
  }
  
  private DeployBeanProperty createManyType(DeployBeanDescriptor<?> desc, Class<?> targetType, ManyType manyType)
  {
    ScalarType<?> scalarType = this.typeManager.getScalarType(targetType);
    if (scalarType != null) {
      return new DeployBeanPropertySimpleCollection(desc, targetType, scalarType, manyType);
    }
    return new DeployBeanPropertyAssocMany(desc, targetType, manyType);
  }
  
  private DeployBeanProperty createProp(DeployBeanDescriptor<?> desc, Field field)
  {
    Class<?> propertyType = field.getType();
    Class<?> innerType = propertyType;
    ScalarTypeConverter<?, ?> typeConverter = null;
    if (propertyType.equals(this.scalaOptionClass))
    {
      innerType = determineTargetType(field);
      typeConverter = this.scalaOptionTypeConverter;
    }
    ManyType manyType = this.determineManyType.getManyType(propertyType);
    if (manyType != null)
    {
      Class<?> targetType = determineTargetType(field);
      if (targetType == null)
      {
        Transient transAnnotation = (Transient)field.getAnnotation(Transient.class);
        if (transAnnotation != null) {
          return null;
        }
        logger.warning("Could not find parameter type (via reflection) on " + desc.getFullName() + " " + field.getName());
      }
      return createManyType(desc, targetType, manyType);
    }
    if ((innerType.isEnum()) || (innerType.isPrimitive())) {
      return new DeployBeanProperty(desc, propertyType, null, typeConverter);
    }
    ScalarType<?> scalarType = this.typeManager.getScalarType(innerType);
    if (scalarType != null) {
      return new DeployBeanProperty(desc, propertyType, scalarType, typeConverter);
    }
    CtCompoundType<?> compoundType = this.typeManager.getCompoundType(innerType);
    if (compoundType != null) {
      return new DeployBeanPropertyCompound(desc, propertyType, compoundType, typeConverter);
    }
    if (!isTransientField(field)) {
      try
      {
        CheckImmutableResponse checkImmutable = this.typeManager.checkImmutable(innerType);
        if (checkImmutable.isImmutable()) {
          if (checkImmutable.isCompoundType())
          {
            this.typeManager.recursiveCreateScalarDataReader(innerType);
            compoundType = this.typeManager.getCompoundType(innerType);
            if (compoundType != null) {
              return new DeployBeanPropertyCompound(desc, propertyType, compoundType, typeConverter);
            }
          }
          else
          {
            scalarType = this.typeManager.recursiveCreateScalarTypes(innerType);
            return new DeployBeanProperty(desc, propertyType, scalarType, typeConverter);
          }
        }
      }
      catch (Exception e)
      {
        logger.log(Level.SEVERE, "Error with " + desc + " field:" + field.getName(), e);
      }
    }
    return new DeployBeanPropertyAssocOne(desc, propertyType);
  }
  
  private boolean isTransientField(Field field)
  {
    Transient t = (Transient)field.getAnnotation(Transient.class);
    return t != null;
  }
  
  private DeployBeanProperty createProp(int level, DeployBeanDescriptor<?> desc, Field field, Class<?> beanType, Method getter, Method setter)
  {
    DeployBeanProperty prop = createProp(desc, field);
    if (prop == null) {
      return null;
    }
    prop.setOwningType(beanType);
    prop.setName(field.getName());
    
    prop.setReadMethod(getter);
    prop.setWriteMethod(setter);
    prop.setField(field);
    return prop;
  }
  
  private Class<?> determineTargetType(Field field)
  {
    Type genType = field.getGenericType();
    if ((genType instanceof ParameterizedType))
    {
      ParameterizedType ptype = (ParameterizedType)genType;
      
      Type[] typeArgs = ptype.getActualTypeArguments();
      if (typeArgs.length == 1)
      {
        if ((typeArgs[0] instanceof Class)) {
          return (Class)typeArgs[0];
        }
        throw new RuntimeException("Unexpected Parameterised Type? " + typeArgs[0]);
      }
      if (typeArgs.length == 2)
      {
        if ((typeArgs[1] instanceof ParameterizedType)) {
          return null;
        }
        return (Class)typeArgs[1];
      }
    }
    return null;
  }
}
