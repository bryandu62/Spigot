package com.avaje.ebean.text.csv;

import com.avaje.ebean.text.StringParser;
import java.io.Reader;
import java.util.Locale;

public abstract interface CsvReader<T>
{
  public abstract void setDefaultLocale(Locale paramLocale);
  
  public abstract void setDefaultTimeFormat(String paramString);
  
  public abstract void setDefaultDateFormat(String paramString);
  
  public abstract void setDefaultTimestampFormat(String paramString);
  
  public abstract void setPersistBatchSize(int paramInt);
  
  public abstract void setHasHeader(boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void setAddPropertiesFromHeader();
  
  public abstract void setIgnoreHeader();
  
  public abstract void setLogInfoFrequency(int paramInt);
  
  public abstract void addIgnore();
  
  public abstract void addProperty(String paramString);
  
  public abstract void addReference(String paramString);
  
  public abstract void addProperty(String paramString, StringParser paramStringParser);
  
  public abstract void addDateTime(String paramString1, String paramString2);
  
  public abstract void addDateTime(String paramString1, String paramString2, Locale paramLocale);
  
  public abstract void process(Reader paramReader)
    throws Exception;
  
  public abstract void process(Reader paramReader, CsvCallback<T> paramCsvCallback)
    throws Exception;
}
