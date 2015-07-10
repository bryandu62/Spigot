package org.apache.logging.log4j.core.helpers;

import java.security.MessageDigest;

public final class NameUtil
{
  private static final int MASK = 255;
  
  public static String getSubName(String name)
  {
    if (name.isEmpty()) {
      return null;
    }
    int i = name.lastIndexOf('.');
    return i > 0 ? name.substring(0, i) : "";
  }
  
  public static String md5(String string)
  {
    try
    {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      digest.update(string.getBytes());
      byte[] bytes = digest.digest();
      StringBuilder md5 = new StringBuilder();
      for (byte b : bytes)
      {
        String hex = Integer.toHexString(0xFF & b);
        if (hex.length() == 1) {
          md5.append('0');
        }
        md5.append(hex);
      }
      return md5.toString();
    }
    catch (Exception ex) {}
    return string;
  }
}
