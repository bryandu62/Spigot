package com.avaje.ebeaninternal.server.lib.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MailMessage
{
  ArrayList<String> bodylines;
  MailAddress senderAddress;
  HashMap<String, String> header = new HashMap();
  MailAddress currentRecipient;
  ArrayList<MailAddress> recipientList = new ArrayList();
  
  public MailMessage()
  {
    this.bodylines = new ArrayList();
  }
  
  public void setCurrentRecipient(MailAddress currentRecipient)
  {
    this.currentRecipient = currentRecipient;
  }
  
  public MailAddress getCurrentRecipient()
  {
    return this.currentRecipient;
  }
  
  public void addRecipient(String alias, String emailAddress)
  {
    this.recipientList.add(new MailAddress(alias, emailAddress));
  }
  
  public void setSender(String alias, String senderEmail)
  {
    this.senderAddress = new MailAddress(alias, senderEmail);
  }
  
  public MailAddress getSender()
  {
    return this.senderAddress;
  }
  
  public Iterator<MailAddress> getRecipientList()
  {
    return this.recipientList.iterator();
  }
  
  public void addHeader(String key, String val)
  {
    this.header.put(key, val);
  }
  
  public void setSubject(String subject)
  {
    addHeader("Subject", subject);
  }
  
  public String getSubject()
  {
    return getHeader("Subject");
  }
  
  public void addBodyLine(String line)
  {
    this.bodylines.add(line);
  }
  
  public Iterator<String> getBodyLines()
  {
    return this.bodylines.iterator();
  }
  
  public Iterator<String> getHeaderFields()
  {
    return this.header.keySet().iterator();
  }
  
  public String getHeader(String key)
  {
    return (String)this.header.get(key);
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder(100);
    sb.append("Sender: " + this.senderAddress + "\tRecipient: " + this.recipientList + "\n");
    Iterator<String> hi = this.header.keySet().iterator();
    while (hi.hasNext())
    {
      String key = (String)hi.next();
      String hline = key + ": " + (String)this.header.get(key) + "\n";
      sb.append(hline);
    }
    sb.append("\n");
    Iterator<String> e = this.bodylines.iterator();
    while (e.hasNext()) {
      sb.append((String)e.next()).append("\n");
    }
    return sb.toString();
  }
}
