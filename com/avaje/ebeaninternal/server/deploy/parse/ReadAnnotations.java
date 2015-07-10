package com.avaje.ebeaninternal.server.deploy.parse;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptorManager;

public class ReadAnnotations
{
  public void readInitial(DeployBeanInfo<?> info)
  {
    try
    {
      new AnnotationClass(info).parse();
      new AnnotationFields(info).parse();
    }
    catch (RuntimeException e)
    {
      String msg = "Error reading annotations for " + info;
      throw new RuntimeException(msg, e);
    }
  }
  
  public void readAssociations(DeployBeanInfo<?> info, BeanDescriptorManager factory)
  {
    try
    {
      new AnnotationAssocOnes(info, factory).parse();
      new AnnotationAssocManys(info, factory).parse();
      
      new AnnotationSql(info).parse();
    }
    catch (RuntimeException e)
    {
      String msg = "Error reading annotations for " + info;
      throw new RuntimeException(msg, e);
    }
  }
}
