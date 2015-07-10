package org.apache.logging.log4j.core.jmx;

import javax.management.ObjectName;
import org.apache.logging.log4j.core.helpers.Assert;
import org.apache.logging.log4j.core.selector.ContextSelector;

public class ContextSelectorAdmin
  implements ContextSelectorAdminMBean
{
  private final ObjectName objectName;
  private final ContextSelector selector;
  
  public ContextSelectorAdmin(ContextSelector selector)
  {
    this.selector = ((ContextSelector)Assert.isNotNull(selector, "ContextSelector"));
    try
    {
      this.objectName = new ObjectName("org.apache.logging.log4j2:type=ContextSelector");
    }
    catch (Exception e)
    {
      throw new IllegalStateException(e);
    }
  }
  
  public ObjectName getObjectName()
  {
    return this.objectName;
  }
  
  public String getImplementationClassName()
  {
    return this.selector.getClass().getName();
  }
}
