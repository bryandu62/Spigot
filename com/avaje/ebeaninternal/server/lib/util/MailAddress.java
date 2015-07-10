package com.avaje.ebeaninternal.server.lib.util;

public class MailAddress
{
  String alias;
  String emailAddress;
  
  public MailAddress(String alias, String emailAddress)
  {
    this.alias = alias;
    this.emailAddress = emailAddress;
  }
  
  public String getAlias()
  {
    if (this.alias == null) {
      return "";
    }
    return this.alias;
  }
  
  public String getEmailAddress()
  {
    return this.emailAddress;
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append(getAlias()).append(" ").append("<").append(getEmailAddress()).append(">");
    return sb.toString();
  }
}
