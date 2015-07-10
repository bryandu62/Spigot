package com.avaje.ebeaninternal.server.lib.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MimeTypeHelper
{
  public static String getMimeType(String filePath)
  {
    int lastPeriod = filePath.lastIndexOf(".");
    if (lastPeriod > -1) {
      filePath = filePath.substring(lastPeriod + 1);
    }
    try
    {
      return resources.getString(filePath.toLowerCase());
    }
    catch (MissingResourceException e) {}
    return null;
  }
  
  private static ResourceBundle resources = ResourceBundle.getBundle("com.avaje.lib.util.mimetypes");
}
