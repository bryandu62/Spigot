package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebeaninternal.server.el.ElPropertyDeploy;
import java.util.HashSet;
import java.util.Set;

public final class DeployPropertyParser
  extends DeployParser
{
  private final BeanDescriptor<?> beanDescriptor;
  private final Set<String> includes = new HashSet();
  
  public DeployPropertyParser(BeanDescriptor<?> beanDescriptor)
  {
    this.beanDescriptor = beanDescriptor;
  }
  
  public Set<String> getIncludes()
  {
    return this.includes;
  }
  
  public String getDeployWord(String expression)
  {
    ElPropertyDeploy elProp = this.beanDescriptor.getElPropertyDeploy(expression);
    if (elProp == null) {
      return null;
    }
    addIncludes(elProp.getElPrefix());
    return elProp.getElPlaceholder(this.encrypted);
  }
  
  public String convertWord()
  {
    String r = getDeployWord(this.word);
    return r == null ? this.word : r;
  }
  
  private void addIncludes(String prefix)
  {
    if (prefix != null) {
      this.includes.add(prefix);
    }
  }
}
