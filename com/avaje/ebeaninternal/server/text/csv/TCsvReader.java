package com.avaje.ebeaninternal.server.text.csv;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.text.StringParser;
import com.avaje.ebean.text.TextException;
import com.avaje.ebean.text.TimeStringParser;
import com.avaje.ebean.text.csv.CsvCallback;
import com.avaje.ebean.text.csv.CsvReader;
import com.avaje.ebean.text.csv.DefaultCsvCallback;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.id.IdBinder;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TCsvReader<T>
  implements CsvReader<T>
{
  private static final TimeStringParser TIME_PARSER = new TimeStringParser();
  private final EbeanServer server;
  private final BeanDescriptor<T> descriptor;
  private final List<CsvColumn> columnList = new ArrayList();
  private final CsvColumn ignoreColumn = new CsvColumn(null);
  private boolean treatEmptyStringAsNull = true;
  private boolean hasHeader;
  private int logInfoFrequency = 1000;
  private String defaultTimeFormat = "HH:mm:ss";
  private String defaultDateFormat = "yyyy-MM-dd";
  private String defaultTimestampFormat = "yyyy-MM-dd hh:mm:ss.fffffffff";
  private Locale defaultLocale = Locale.getDefault();
  protected int persistBatchSize = 30;
  private boolean addPropertiesFromHeader;
  
  public TCsvReader(EbeanServer server, BeanDescriptor<T> descriptor)
  {
    this.server = server;
    this.descriptor = descriptor;
  }
  
  public void setDefaultLocale(Locale defaultLocale)
  {
    this.defaultLocale = defaultLocale;
  }
  
  public void setDefaultTimeFormat(String defaultTimeFormat)
  {
    this.defaultTimeFormat = defaultTimeFormat;
  }
  
  public void setDefaultDateFormat(String defaultDateFormat)
  {
    this.defaultDateFormat = defaultDateFormat;
  }
  
  public void setDefaultTimestampFormat(String defaultTimestampFormat)
  {
    this.defaultTimestampFormat = defaultTimestampFormat;
  }
  
  public void setPersistBatchSize(int persistBatchSize)
  {
    this.persistBatchSize = persistBatchSize;
  }
  
  public void setIgnoreHeader()
  {
    setHasHeader(true, false);
  }
  
  public void setAddPropertiesFromHeader()
  {
    setHasHeader(true, true);
  }
  
  public void setHasHeader(boolean hasHeader, boolean addPropertiesFromHeader)
  {
    this.hasHeader = hasHeader;
    this.addPropertiesFromHeader = addPropertiesFromHeader;
  }
  
  public void setLogInfoFrequency(int logInfoFrequency)
  {
    this.logInfoFrequency = logInfoFrequency;
  }
  
  public void addIgnore()
  {
    this.columnList.add(this.ignoreColumn);
  }
  
  public void addProperty(String propertyName)
  {
    addProperty(propertyName, null);
  }
  
  public void addReference(String propertyName)
  {
    addProperty(propertyName, null, true);
  }
  
  public void addProperty(String propertyName, StringParser parser)
  {
    addProperty(propertyName, parser, false);
  }
  
  public void addDateTime(String propertyName, String dateTimeFormat)
  {
    addDateTime(propertyName, dateTimeFormat, Locale.getDefault());
  }
  
  public void addDateTime(String propertyName, String dateTimeFormat, Locale locale)
  {
    ElPropertyValue elProp = this.descriptor.getElGetValue(propertyName);
    if (!elProp.isDateTimeCapable()) {
      throw new TextException("Property " + propertyName + " is not DateTime capable");
    }
    if (dateTimeFormat == null) {
      dateTimeFormat = getDefaultDateTimeFormat(elProp.getJdbcType());
    }
    if (locale == null) {
      locale = this.defaultLocale;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat, locale);
    DateTimeParser parser = new DateTimeParser(sdf, dateTimeFormat, elProp);
    
    CsvColumn column = new CsvColumn(elProp, parser, false);
    this.columnList.add(column);
  }
  
  private String getDefaultDateTimeFormat(int jdbcType)
  {
    switch (jdbcType)
    {
    case 92: 
      return this.defaultTimeFormat;
    case 91: 
      return this.defaultDateFormat;
    case 93: 
      return this.defaultTimestampFormat;
    }
    throw new RuntimeException("Expected java.sql.Types TIME,DATE or TIMESTAMP but got [" + jdbcType + "]");
  }
  
  public void addProperty(String propertyName, StringParser parser, boolean reference)
  {
    ElPropertyValue elProp = this.descriptor.getElGetValue(propertyName);
    if (parser == null) {
      parser = elProp.getStringParser();
    }
    CsvColumn column = new CsvColumn(elProp, parser, reference);
    this.columnList.add(column);
  }
  
  public void process(Reader reader)
    throws Exception
  {
    DefaultCsvCallback<T> callback = new DefaultCsvCallback(this.persistBatchSize, this.logInfoFrequency);
    process(reader, callback);
  }
  
  public void process(Reader reader, CsvCallback<T> callback)
    throws Exception
  {
    if (reader == null) {
      throw new NullPointerException("reader is null?");
    }
    if (callback == null) {
      throw new NullPointerException("callback is null?");
    }
    CsvUtilReader utilReader = new CsvUtilReader(reader);
    
    callback.begin(this.server);
    
    int row = 0;
    if (this.hasHeader)
    {
      String[] line = utilReader.readNext();
      if (this.addPropertiesFromHeader) {
        addPropertiesFromHeader(line);
      }
      callback.readHeader(line);
    }
    try
    {
      for (;;)
      {
        row++;
        String[] line = utilReader.readNext();
        if (line == null)
        {
          row--;
          break;
        }
        if (callback.processLine(row, line))
        {
          if (line.length != this.columnList.size())
          {
            String msg = "Error at line " + row + ". Expected [" + this.columnList.size() + "] columns " + "but instead we have [" + line.length + "].  Line[" + Arrays.toString(line) + "]";
            
            throw new TextException(msg);
          }
          T bean = buildBeanFromLineContent(row, line);
          
          callback.processBean(row, line, bean);
        }
      }
      callback.end(row);
    }
    catch (Exception e)
    {
      callback.endWithError(row, e);
      throw e;
    }
  }
  
  private void addPropertiesFromHeader(String[] line)
  {
    for (int i = 0; i < line.length; i++)
    {
      ElPropertyValue elProp = this.descriptor.getElGetValue(line[i]);
      if (elProp == null) {
        throw new TextException("Property [" + line[i] + "] not found");
      }
      if (92 == elProp.getJdbcType())
      {
        addProperty(line[i], TIME_PARSER);
      }
      else if (isDateTimeType(elProp.getJdbcType()))
      {
        addDateTime(line[i], null, null);
      }
      else if (elProp.isAssocProperty())
      {
        BeanPropertyAssocOne<?> assocOne = (BeanPropertyAssocOne)elProp.getBeanProperty();
        String idProp = assocOne.getBeanDescriptor().getIdBinder().getIdProperty();
        addReference(line[i] + "." + idProp);
      }
      else
      {
        addProperty(line[i]);
      }
    }
  }
  
  private boolean isDateTimeType(int t)
  {
    if ((t == 93) || (t == 91) || (t == 92)) {
      return true;
    }
    return false;
  }
  
  protected T buildBeanFromLineContent(int row, String[] line)
  {
    try
    {
      EntityBean entityBean = this.descriptor.createEntityBean();
      T bean = entityBean;
      for (int columnPos = 0; columnPos < line.length; columnPos++) {
        convertAndSetColumn(columnPos, line[columnPos], entityBean);
      }
      return bean;
    }
    catch (RuntimeException e)
    {
      String msg = "Error at line: " + row + " line[" + Arrays.toString(line) + "]";
      throw new RuntimeException(msg, e);
    }
  }
  
  protected void convertAndSetColumn(int columnPos, String strValue, Object bean)
  {
    strValue = strValue.trim();
    if ((strValue.length() == 0) && (this.treatEmptyStringAsNull)) {
      return;
    }
    CsvColumn c = (CsvColumn)this.columnList.get(columnPos);
    c.convertAndSet(strValue, bean);
  }
  
  public static class CsvColumn
  {
    private final ElPropertyValue elProp;
    private final StringParser parser;
    private final boolean ignore;
    private final boolean reference;
    
    private CsvColumn()
    {
      this.elProp = null;
      this.parser = null;
      this.reference = false;
      this.ignore = true;
    }
    
    public CsvColumn(ElPropertyValue elProp, StringParser parser, boolean reference)
    {
      this.elProp = elProp;
      this.parser = parser;
      this.reference = reference;
      this.ignore = false;
    }
    
    public void convertAndSet(String strValue, Object bean)
    {
      if (!this.ignore)
      {
        Object value = this.parser.parse(strValue);
        this.elProp.elSetValue(bean, value, true, this.reference);
      }
    }
  }
  
  private static class DateTimeParser
    implements StringParser
  {
    private final DateFormat dateFormat;
    private final ElPropertyValue elProp;
    private final String format;
    
    DateTimeParser(DateFormat dateFormat, String format, ElPropertyValue elProp)
    {
      this.dateFormat = dateFormat;
      this.elProp = elProp;
      this.format = format;
    }
    
    public Object parse(String value)
    {
      try
      {
        Date dt = this.dateFormat.parse(value);
        return this.elProp.parseDateTime(dt.getTime());
      }
      catch (ParseException e)
      {
        throw new TextException("Error parsing [" + value + "] using format[" + this.format + "]", e);
      }
    }
  }
}
