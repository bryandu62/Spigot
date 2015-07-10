package com.avaje.ebean.config;

import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

final class PropertyExpression
{
  private static final Logger logger = Logger.getLogger(PropertyExpression.class.getName());
  private static final String JAVA_COMP_ENV = "java:comp/env/";
  private static String START = "${";
  private static String END = "}";
  
  static String eval(String val, PropertyMap map)
  {
    if (val == null) {
      return null;
    }
    int sp = val.indexOf(START);
    if (sp > -1)
    {
      int ep = val.indexOf(END, sp + 1);
      if (ep > -1) {
        return eval(val, sp, ep, map);
      }
    }
    return val;
  }
  
  private static String evaluateExpression(String exp, PropertyMap map)
  {
    if (isJndiExpression(exp))
    {
      String val = getJndiProperty(exp);
      if (val != null) {
        return val;
      }
    }
    String val = System.getenv(exp);
    if (val == null) {
      val = System.getProperty(exp);
    }
    if ((val == null) && (map != null)) {
      val = map.get(exp);
    }
    if (val != null) {
      return val;
    }
    logger.fine("Unable to evaluate expression [" + exp + "]");
    return null;
  }
  
  private static String eval(String val, int sp, int ep, PropertyMap map)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(val.substring(0, sp));
    
    String cal = evalExpression(val, sp, ep, map);
    sb.append(cal);
    
    eval(val, ep + 1, sb, map);
    
    return sb.toString();
  }
  
  private static void eval(String val, int startPos, StringBuilder sb, PropertyMap map)
  {
    if (startPos < val.length())
    {
      int sp = val.indexOf(START, startPos);
      if (sp > -1)
      {
        sb.append(val.substring(startPos, sp));
        int ep = val.indexOf(END, sp + 1);
        if (ep > -1)
        {
          String cal = evalExpression(val, sp, ep, map);
          sb.append(cal);
          eval(val, ep + 1, sb, map);
          return;
        }
      }
    }
    sb.append(val.substring(startPos));
  }
  
  private static String evalExpression(String val, int sp, int ep, PropertyMap map)
  {
    String exp = val.substring(sp + START.length(), ep);
    
    String evaled = evaluateExpression(exp, map);
    if (evaled != null) {
      return evaled;
    }
    return START + exp + END;
  }
  
  private static boolean isJndiExpression(String exp)
  {
    if (exp.startsWith("JNDI:")) {
      return true;
    }
    if (exp.startsWith("jndi:")) {
      return true;
    }
    return false;
  }
  
  private static String getJndiProperty(String key)
  {
    try
    {
      key = key.substring(5);
      
      return (String)getJndiObject(key);
    }
    catch (NamingException ex) {}
    return null;
  }
  
  private static Object getJndiObject(String key)
    throws NamingException
  {
    InitialContext ctx = new InitialContext();
    return ctx.lookup("java:comp/env/" + key);
  }
}
