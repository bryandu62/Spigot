package com.avaje.ebean.config;

public class UnderscoreNamingConvention
  extends AbstractNamingConvention
{
  private boolean forceUpperCase = false;
  private boolean digitsCompressed = true;
  
  public UnderscoreNamingConvention(String sequenceFormat)
  {
    super(sequenceFormat);
  }
  
  public UnderscoreNamingConvention() {}
  
  public TableName getTableNameByConvention(Class<?> beanClass)
  {
    return new TableName(getCatalog(), getSchema(), toUnderscoreFromCamel(beanClass.getSimpleName()));
  }
  
  public String getColumnFromProperty(Class<?> beanClass, String propertyName)
  {
    return toUnderscoreFromCamel(propertyName);
  }
  
  public String getPropertyFromColumn(Class<?> beanClass, String dbColumnName)
  {
    return toCamelFromUnderscore(dbColumnName);
  }
  
  public boolean isForceUpperCase()
  {
    return this.forceUpperCase;
  }
  
  public void setForceUpperCase(boolean forceUpperCase)
  {
    this.forceUpperCase = forceUpperCase;
  }
  
  public boolean isDigitsCompressed()
  {
    return this.digitsCompressed;
  }
  
  public void setDigitsCompressed(boolean digitsCompressed)
  {
    this.digitsCompressed = digitsCompressed;
  }
  
  protected String toUnderscoreFromCamel(String camelCase)
  {
    int lastUpper = -1;
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < camelCase.length(); i++)
    {
      char c = camelCase.charAt(i);
      if ('_' == c)
      {
        sb.append(c);
        lastUpper = i;
      }
      else if (Character.isDigit(c))
      {
        if ((i > lastUpper + 1) && (!this.digitsCompressed)) {
          sb.append("_");
        }
        sb.append(c);
        lastUpper = i;
      }
      else if (Character.isUpperCase(c))
      {
        if (i > lastUpper + 1) {
          sb.append("_");
        }
        sb.append(Character.toLowerCase(c));
        lastUpper = i;
      }
      else
      {
        sb.append(c);
      }
    }
    String ret = sb.toString();
    if (this.forceUpperCase) {
      ret = ret.toUpperCase();
    }
    return ret;
  }
  
  protected String toCamelFromUnderscore(String underscore)
  {
    StringBuffer result = new StringBuffer();
    String[] vals = underscore.split("_");
    for (int i = 0; i < vals.length; i++)
    {
      String lower = vals[i].toLowerCase();
      if (i > 0)
      {
        char c = Character.toUpperCase(lower.charAt(0));
        result.append(c);
        result.append(lower.substring(1));
      }
      else
      {
        result.append(lower);
      }
    }
    return result.toString();
  }
}
