package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.TextException;
import java.net.URI;
import java.net.URISyntaxException;

public class ScalarTypeURI
  extends ScalarTypeBaseVarchar<URI>
{
  public ScalarTypeURI()
  {
    super(URI.class);
  }
  
  public URI convertFromDbString(String dbValue)
  {
    try
    {
      return new URI(dbValue);
    }
    catch (URISyntaxException e)
    {
      throw new RuntimeException("Error with URI [" + dbValue + "] " + e);
    }
  }
  
  public String convertToDbString(URI beanValue)
  {
    return beanValue.toString();
  }
  
  public String formatValue(URI v)
  {
    return v.toString();
  }
  
  public URI parse(String value)
  {
    try
    {
      return new URI(value);
    }
    catch (URISyntaxException e)
    {
      throw new TextException("Error with URI [" + value + "] ", e);
    }
  }
}
