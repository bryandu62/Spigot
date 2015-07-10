package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.annotation.EnumMapping;
import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.config.CompoundType;
import com.avaje.ebean.config.CompoundTypeProperty;
import com.avaje.ebean.config.ScalarTypeConverter;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebeaninternal.api.ClassUtil;
import com.avaje.ebeaninternal.server.core.BootupClasses;
import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import com.avaje.ebeaninternal.server.type.reflect.CheckImmutable;
import com.avaje.ebeaninternal.server.type.reflect.CheckImmutableResponse;
import com.avaje.ebeaninternal.server.type.reflect.ImmutableMeta;
import com.avaje.ebeaninternal.server.type.reflect.ImmutableMetaFactory;
import com.avaje.ebeaninternal.server.type.reflect.KnownImmutable;
import com.avaje.ebeaninternal.server.type.reflect.ReflectionBasedCompoundType;
import com.avaje.ebeaninternal.server.type.reflect.ReflectionBasedCompoundTypeProperty;
import com.avaje.ebeaninternal.server.type.reflect.ReflectionBasedTypeBuilder;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public final class DefaultTypeManager
  implements TypeManager, KnownImmutable
{
  private static final Logger logger = Logger.getLogger(DefaultTypeManager.class.getName());
  private final ConcurrentHashMap<Class<?>, CtCompoundType<?>> compoundTypeMap;
  private final ConcurrentHashMap<Class<?>, ScalarType<?>> typeMap;
  private final ConcurrentHashMap<Integer, ScalarType<?>> nativeMap;
  private final DefaultTypeFactory extraTypeFactory;
  private final ScalarType<?> charType = new ScalarTypeChar();
  private final ScalarType<?> charArrayType = new ScalarTypeCharArray();
  private final ScalarType<?> longVarcharType = new ScalarTypeLongVarchar();
  private final ScalarType<?> clobType = new ScalarTypeClob();
  private final ScalarType<?> byteType = new ScalarTypeByte();
  private final ScalarType<?> binaryType = new ScalarTypeBytesBinary();
  private final ScalarType<?> blobType = new ScalarTypeBytesBlob();
  private final ScalarType<?> varbinaryType = new ScalarTypeBytesVarbinary();
  private final ScalarType<?> longVarbinaryType = new ScalarTypeBytesLongVarbinary();
  private final ScalarType<?> shortType = new ScalarTypeShort();
  private final ScalarType<?> integerType = new ScalarTypeInteger();
  private final ScalarType<?> longType = new ScalarTypeLong();
  private final ScalarType<?> doubleType = new ScalarTypeDouble();
  private final ScalarType<?> floatType = new ScalarTypeFloat();
  private final ScalarType<?> bigDecimalType = new ScalarTypeBigDecimal();
  private final ScalarType<?> timeType = new ScalarTypeTime();
  private final ScalarType<?> dateType = new ScalarTypeDate();
  private final ScalarType<?> timestampType = new ScalarTypeTimestamp();
  private final ScalarType<?> uuidType = new ScalarTypeUUID();
  private final ScalarType<?> urlType = new ScalarTypeURL();
  private final ScalarType<?> uriType = new ScalarTypeURI();
  private final ScalarType<?> localeType = new ScalarTypeLocale();
  private final ScalarType<?> currencyType = new ScalarTypeCurrency();
  private final ScalarType<?> timeZoneType = new ScalarTypeTimeZone();
  private final ScalarType<?> stringType = new ScalarTypeString();
  private final ScalarType<?> classType = new ScalarTypeClass();
  private final ScalarTypeLongToTimestamp longToTimestamp = new ScalarTypeLongToTimestamp();
  private final List<ScalarType<?>> customScalarTypes = new ArrayList();
  private final CheckImmutable checkImmutable;
  private final ImmutableMetaFactory immutableMetaFactory = new ImmutableMetaFactory();
  private final ReflectionBasedTypeBuilder reflectScalarBuilder;
  
  public DefaultTypeManager(ServerConfig config, BootupClasses bootupClasses)
  {
    int clobType = config == null ? 2005 : config.getDatabasePlatform().getClobDbType();
    int blobType = config == null ? 2004 : config.getDatabasePlatform().getBlobDbType();
    
    this.checkImmutable = new CheckImmutable(this);
    this.reflectScalarBuilder = new ReflectionBasedTypeBuilder(this);
    
    this.compoundTypeMap = new ConcurrentHashMap();
    this.typeMap = new ConcurrentHashMap();
    this.nativeMap = new ConcurrentHashMap();
    
    this.extraTypeFactory = new DefaultTypeFactory(config);
    
    initialiseStandard(clobType, blobType);
    initialiseJodaTypes();
    if (bootupClasses != null)
    {
      initialiseCustomScalarTypes(bootupClasses);
      initialiseScalarConverters(bootupClasses);
      initialiseCompoundTypes(bootupClasses);
    }
  }
  
  public boolean isKnownImmutable(Class<?> cls)
  {
    if (cls == null) {
      return true;
    }
    if ((cls.isPrimitive()) || (Object.class.equals(cls))) {
      return true;
    }
    ScalarDataReader<?> scalarDataReader = getScalarDataReader(cls);
    return scalarDataReader != null;
  }
  
  public CheckImmutableResponse checkImmutable(Class<?> cls)
  {
    return this.checkImmutable.checkImmutable(cls);
  }
  
  private ScalarType<?> register(ScalarType<?> st)
  {
    add(st);
    logger.fine("Registering ScalarType for " + st.getType() + " implemented using reflection");
    return st;
  }
  
  public ScalarDataReader<?> recursiveCreateScalarDataReader(Class<?> cls)
  {
    ScalarDataReader<?> scalarReader = getScalarDataReader(cls);
    if (scalarReader != null) {
      return scalarReader;
    }
    ImmutableMeta meta = this.immutableMetaFactory.createImmutableMeta(cls);
    if (!meta.isCompoundType()) {
      return register(this.reflectScalarBuilder.buildScalarType(meta));
    }
    ReflectionBasedCompoundType compoundType = this.reflectScalarBuilder.buildCompound(meta);
    Class<?> compoundTypeClass = compoundType.getCompoundType();
    
    return createCompoundScalarDataReader(compoundTypeClass, compoundType, " using reflection");
  }
  
  public ScalarType<?> recursiveCreateScalarTypes(Class<?> cls)
  {
    ScalarType<?> scalarType = getScalarType(cls);
    if (scalarType != null) {
      return scalarType;
    }
    ImmutableMeta meta = this.immutableMetaFactory.createImmutableMeta(cls);
    if (!meta.isCompoundType()) {
      return register(this.reflectScalarBuilder.buildScalarType(meta));
    }
    throw new RuntimeException("Not allowed compound types here");
  }
  
  public void add(ScalarType<?> scalarType)
  {
    this.typeMap.put(scalarType.getType(), scalarType);
    logAdd(scalarType);
  }
  
  protected void logAdd(ScalarType<?> scalarType)
  {
    if (logger.isLoggable(Level.FINE))
    {
      String msg = "ScalarType register [" + scalarType.getClass().getName() + "]";
      msg = msg + " for [" + scalarType.getType().getName() + "]";
      logger.fine(msg);
    }
  }
  
  public CtCompoundType<?> getCompoundType(Class<?> type)
  {
    return (CtCompoundType)this.compoundTypeMap.get(type);
  }
  
  public ScalarType<?> getScalarType(int jdbcType)
  {
    return (ScalarType)this.nativeMap.get(Integer.valueOf(jdbcType));
  }
  
  public <T> ScalarType<T> getScalarType(Class<T> type)
  {
    return (ScalarType)this.typeMap.get(type);
  }
  
  public ScalarDataReader<?> getScalarDataReader(Class<?> propertyType, int sqlType)
  {
    if (sqlType == 0) {
      return recursiveCreateScalarDataReader(propertyType);
    }
    for (int i = 0; i < this.customScalarTypes.size(); i++)
    {
      ScalarType<?> customScalarType = (ScalarType)this.customScalarTypes.get(i);
      if ((sqlType == customScalarType.getJdbcType()) && (propertyType.equals(customScalarType.getType()))) {
        return customScalarType;
      }
    }
    String msg = "Unable to find a custom ScalarType with type [" + propertyType + "] and java.sql.Type [" + sqlType + "]";
    throw new RuntimeException(msg);
  }
  
  public ScalarDataReader<?> getScalarDataReader(Class<?> type)
  {
    ScalarDataReader<?> reader = (ScalarDataReader)this.typeMap.get(type);
    if (reader == null) {
      reader = (ScalarDataReader)this.compoundTypeMap.get(type);
    }
    return reader;
  }
  
  public <T> ScalarType<T> getScalarType(Class<T> type, int jdbcType)
  {
    ScalarType<?> scalarType = getLobTypes(jdbcType);
    if (scalarType != null) {
      return scalarType;
    }
    scalarType = (ScalarType)this.typeMap.get(type);
    if ((scalarType != null) && (
      (jdbcType == 0) || (scalarType.getJdbcType() == jdbcType))) {
      return scalarType;
    }
    if (type.equals(java.util.Date.class)) {
      return this.extraTypeFactory.createUtilDate(jdbcType);
    }
    if (type.equals(Calendar.class)) {
      return this.extraTypeFactory.createCalendar(jdbcType);
    }
    String msg = "Unmatched ScalarType for " + type + " jdbcType:" + jdbcType;
    throw new RuntimeException(msg);
  }
  
  private ScalarType<?> getLobTypes(int jdbcType)
  {
    return getScalarType(jdbcType);
  }
  
  public Object convert(Object value, int toJdbcType)
  {
    if (value == null) {
      return null;
    }
    ScalarType<?> type = (ScalarType)this.nativeMap.get(Integer.valueOf(toJdbcType));
    if (type != null) {
      return type.toJdbcType(value);
    }
    return value;
  }
  
  private boolean isIntegerType(String s)
  {
    try
    {
      Integer.parseInt(s);
      return true;
    }
    catch (NumberFormatException e) {}
    return false;
  }
  
  private ScalarType<?> createEnumScalarType2(Class<?> enumType)
  {
    boolean integerType = true;
    
    Map<String, String> nameValueMap = new HashMap();
    
    Field[] fields = enumType.getDeclaredFields();
    for (int i = 0; i < fields.length; i++)
    {
      EnumValue enumValue = (EnumValue)fields[i].getAnnotation(EnumValue.class);
      if (enumValue != null)
      {
        nameValueMap.put(fields[i].getName(), enumValue.value());
        if ((integerType) && (!isIntegerType(enumValue.value()))) {
          integerType = false;
        }
      }
    }
    if (nameValueMap.isEmpty()) {
      return null;
    }
    return createEnumScalarType(enumType, nameValueMap, integerType, 0);
  }
  
  public ScalarType<?> createEnumScalarType(Class<?> enumType)
  {
    EnumMapping enumMapping = (EnumMapping)enumType.getAnnotation(EnumMapping.class);
    if (enumMapping == null) {
      return createEnumScalarType2(enumType);
    }
    String nameValuePairs = enumMapping.nameValuePairs();
    boolean integerType = enumMapping.integerType();
    int dbColumnLength = enumMapping.length();
    
    Map<String, String> nameValueMap = StringHelper.delimitedToMap(nameValuePairs, ",", "=");
    
    return createEnumScalarType(enumType, nameValueMap, integerType, dbColumnLength);
  }
  
  private ScalarType<?> createEnumScalarType(Class enumType, Map<String, String> nameValueMap, boolean integerType, int dbColumnLength)
  {
    EnumToDbValueMap<?> beanDbMap = EnumToDbValueMap.create(integerType);
    
    int maxValueLen = 0;
    
    Iterator it = nameValueMap.entrySet().iterator();
    while (it.hasNext())
    {
      Map.Entry entry = (Map.Entry)it.next();
      String name = (String)entry.getKey();
      String value = (String)entry.getValue();
      
      maxValueLen = Math.max(maxValueLen, value.length());
      
      Object enumValue = Enum.valueOf(enumType, name.trim());
      beanDbMap.add(enumValue, value.trim());
    }
    if ((dbColumnLength == 0) && (!integerType)) {
      dbColumnLength = maxValueLen;
    }
    return new ScalarTypeEnumWithMapping(beanDbMap, enumType, dbColumnLength);
  }
  
  protected void initialiseCustomScalarTypes(BootupClasses bootupClasses)
  {
    this.customScalarTypes.add(this.longToTimestamp);
    
    List<Class<?>> foundTypes = bootupClasses.getScalarTypes();
    for (int i = 0; i < foundTypes.size(); i++)
    {
      Class<?> cls = (Class)foundTypes.get(i);
      try
      {
        ScalarType<?> scalarType = (ScalarType)cls.newInstance();
        add(scalarType);
        
        this.customScalarTypes.add(scalarType);
      }
      catch (Exception e)
      {
        String msg = "Error loading ScalarType [" + cls.getName() + "]";
        logger.log(Level.SEVERE, msg, e);
      }
    }
  }
  
  protected void initialiseScalarConverters(BootupClasses bootupClasses)
  {
    List<Class<?>> foundTypes = bootupClasses.getScalarConverters();
    for (int i = 0; i < foundTypes.size(); i++)
    {
      Class<?> cls = (Class)foundTypes.get(i);
      try
      {
        Class<?>[] paramTypes = TypeReflectHelper.getParams(cls, ScalarTypeConverter.class);
        if (paramTypes.length != 2) {
          throw new IllegalStateException("Expected 2 generics paramtypes but got: " + Arrays.toString(paramTypes));
        }
        Class<?> logicalType = paramTypes[0];
        Class<?> persistType = paramTypes[1];
        
        ScalarType<?> wrappedType = getScalarType(persistType);
        if (wrappedType == null) {
          throw new IllegalStateException("Could not find ScalarType for: " + paramTypes[1]);
        }
        ScalarTypeConverter converter = (ScalarTypeConverter)cls.newInstance();
        ScalarTypeWrapper stw = new ScalarTypeWrapper(logicalType, wrappedType, converter);
        
        logger.fine("Register ScalarTypeWrapper from " + logicalType + " -> " + persistType + " using:" + cls);
        
        add(stw);
      }
      catch (Exception e)
      {
        String msg = "Error loading ScalarType [" + cls.getName() + "]";
        logger.log(Level.SEVERE, msg, e);
      }
    }
  }
  
  protected void initialiseCompoundTypes(BootupClasses bootupClasses)
  {
    ArrayList<Class<?>> compoundTypes = bootupClasses.getCompoundTypes();
    for (int j = 0; j < compoundTypes.size(); j++)
    {
      Class<?> type = (Class)compoundTypes.get(j);
      try
      {
        Class<?>[] paramTypes = TypeReflectHelper.getParams(type, CompoundType.class);
        if (paramTypes.length != 1) {
          throw new RuntimeException("Expecting 1 generic paramter type but got " + Arrays.toString(paramTypes) + " for " + type);
        }
        Class<?> compoundTypeClass = paramTypes[0];
        
        CompoundType<?> compoundType = (CompoundType)type.newInstance();
        createCompoundScalarDataReader(compoundTypeClass, compoundType, "");
      }
      catch (Exception e)
      {
        String msg = "Error initialising component " + type;
        throw new RuntimeException(msg, e);
      }
    }
  }
  
  protected CtCompoundType createCompoundScalarDataReader(Class<?> compoundTypeClass, CompoundType<?> compoundType, String info)
  {
    CtCompoundType<?> ctCompoundType = (CtCompoundType)this.compoundTypeMap.get(compoundTypeClass);
    if (ctCompoundType != null)
    {
      logger.info("Already registered compound type " + compoundTypeClass);
      return ctCompoundType;
    }
    CompoundTypeProperty<?, ?>[] cprops = compoundType.getProperties();
    
    ScalarDataReader[] dataReaders = new ScalarDataReader[cprops.length];
    for (int i = 0; i < cprops.length; i++)
    {
      Class<?> propertyType = getCompoundPropertyType(cprops[i]);
      
      ScalarDataReader<?> scalarDataReader = getScalarDataReader(propertyType, cprops[i].getDbType());
      if (scalarDataReader == null) {
        throw new RuntimeException("Could not find ScalarDataReader for " + propertyType);
      }
      dataReaders[i] = scalarDataReader;
    }
    CtCompoundType ctType = new CtCompoundType(compoundTypeClass, compoundType, dataReaders);
    
    logger.fine("Registering CompoundType " + compoundTypeClass + " " + info);
    this.compoundTypeMap.put(compoundTypeClass, ctType);
    
    return ctType;
  }
  
  private Class<?> getCompoundPropertyType(CompoundTypeProperty<?, ?> prop)
  {
    if ((prop instanceof ReflectionBasedCompoundTypeProperty)) {
      return ((ReflectionBasedCompoundTypeProperty)prop).getPropertyType();
    }
    Class<?>[] propParamTypes = TypeReflectHelper.getParams(prop.getClass(), CompoundTypeProperty.class);
    if (propParamTypes.length != 2) {
      throw new RuntimeException("Expecting 2 generic paramter types but got " + Arrays.toString(propParamTypes) + " for " + prop.getClass());
    }
    return propParamTypes[1];
  }
  
  protected void initialiseJodaTypes()
  {
    if (ClassUtil.isPresent("org.joda.time.LocalDateTime", getClass()))
    {
      logger.fine("Registering Joda data types");
      this.typeMap.put(LocalDateTime.class, new ScalarTypeJodaLocalDateTime());
      this.typeMap.put(LocalDate.class, new ScalarTypeJodaLocalDate());
      this.typeMap.put(LocalTime.class, new ScalarTypeJodaLocalTime());
      this.typeMap.put(DateTime.class, new ScalarTypeJodaDateTime());
      this.typeMap.put(DateMidnight.class, new ScalarTypeJodaDateMidnight());
    }
  }
  
  protected void initialiseStandard(int platformClobType, int platformBlobType)
  {
    ScalarType<?> utilDateType = this.extraTypeFactory.createUtilDate();
    this.typeMap.put(java.util.Date.class, utilDateType);
    
    ScalarType<?> calType = this.extraTypeFactory.createCalendar();
    this.typeMap.put(Calendar.class, calType);
    
    ScalarType<?> mathBigIntType = this.extraTypeFactory.createMathBigInteger();
    this.typeMap.put(BigInteger.class, mathBigIntType);
    
    ScalarType<?> booleanType = this.extraTypeFactory.createBoolean();
    this.typeMap.put(Boolean.class, booleanType);
    this.typeMap.put(Boolean.TYPE, booleanType);
    
    this.nativeMap.put(Integer.valueOf(16), booleanType);
    if (booleanType.getJdbcType() == -7) {
      this.nativeMap.put(Integer.valueOf(-7), booleanType);
    }
    this.typeMap.put(Locale.class, this.localeType);
    this.typeMap.put(Currency.class, this.currencyType);
    this.typeMap.put(TimeZone.class, this.timeZoneType);
    this.typeMap.put(UUID.class, this.uuidType);
    this.typeMap.put(URL.class, this.urlType);
    this.typeMap.put(URI.class, this.uriType);
    
    this.typeMap.put(char[].class, this.charArrayType);
    this.typeMap.put(Character.TYPE, this.charType);
    this.typeMap.put(String.class, this.stringType);
    this.nativeMap.put(Integer.valueOf(12), this.stringType);
    this.nativeMap.put(Integer.valueOf(1), this.stringType);
    this.nativeMap.put(Integer.valueOf(-1), this.longVarcharType);
    
    this.typeMap.put(Class.class, this.classType);
    if (platformClobType == 2005)
    {
      this.nativeMap.put(Integer.valueOf(2005), this.clobType);
    }
    else
    {
      ScalarType<?> platClobScalarType = (ScalarType)this.nativeMap.get(Integer.valueOf(platformClobType));
      if (platClobScalarType == null) {
        throw new IllegalArgumentException("Type for dbPlatform clobType [" + this.clobType + "] not found.");
      }
      this.nativeMap.put(Integer.valueOf(2005), platClobScalarType);
    }
    this.typeMap.put(byte[].class, this.varbinaryType);
    this.nativeMap.put(Integer.valueOf(-2), this.binaryType);
    this.nativeMap.put(Integer.valueOf(-3), this.varbinaryType);
    this.nativeMap.put(Integer.valueOf(-4), this.longVarbinaryType);
    if (platformBlobType == 2004)
    {
      this.nativeMap.put(Integer.valueOf(2004), this.blobType);
    }
    else
    {
      ScalarType<?> platBlobScalarType = (ScalarType)this.nativeMap.get(Integer.valueOf(platformBlobType));
      if (platBlobScalarType == null) {
        throw new IllegalArgumentException("Type for dbPlatform blobType [" + this.blobType + "] not found.");
      }
      this.nativeMap.put(Integer.valueOf(2004), platBlobScalarType);
    }
    this.typeMap.put(Byte.class, this.byteType);
    this.typeMap.put(Byte.TYPE, this.byteType);
    this.nativeMap.put(Integer.valueOf(-6), this.byteType);
    
    this.typeMap.put(Short.class, this.shortType);
    this.typeMap.put(Short.TYPE, this.shortType);
    this.nativeMap.put(Integer.valueOf(5), this.shortType);
    
    this.typeMap.put(Integer.class, this.integerType);
    this.typeMap.put(Integer.TYPE, this.integerType);
    this.nativeMap.put(Integer.valueOf(4), this.integerType);
    
    this.typeMap.put(Long.class, this.longType);
    this.typeMap.put(Long.TYPE, this.longType);
    this.nativeMap.put(Integer.valueOf(-5), this.longType);
    
    this.typeMap.put(Double.class, this.doubleType);
    this.typeMap.put(Double.TYPE, this.doubleType);
    this.nativeMap.put(Integer.valueOf(6), this.doubleType);
    this.nativeMap.put(Integer.valueOf(8), this.doubleType);
    
    this.typeMap.put(Float.class, this.floatType);
    this.typeMap.put(Float.TYPE, this.floatType);
    this.nativeMap.put(Integer.valueOf(7), this.floatType);
    
    this.typeMap.put(BigDecimal.class, this.bigDecimalType);
    this.nativeMap.put(Integer.valueOf(3), this.bigDecimalType);
    this.nativeMap.put(Integer.valueOf(2), this.bigDecimalType);
    
    this.typeMap.put(Time.class, this.timeType);
    this.nativeMap.put(Integer.valueOf(92), this.timeType);
    this.typeMap.put(java.sql.Date.class, this.dateType);
    this.nativeMap.put(Integer.valueOf(91), this.dateType);
    this.typeMap.put(Timestamp.class, this.timestampType);
    this.nativeMap.put(Integer.valueOf(93), this.timestampType);
  }
}
