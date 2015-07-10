package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebeaninternal.server.el.ElPropertyDeploy;
import java.util.Set;

public final class DeployUpdateParser
  extends DeployParser
{
  private final BeanDescriptor<?> beanDescriptor;
  
  public DeployUpdateParser(BeanDescriptor<?> beanDescriptor)
  {
    this.beanDescriptor = beanDescriptor;
  }
  
  public Set<String> getIncludes()
  {
    return null;
  }
  
  public String convertWord()
  {
    String dbWord = getDeployWord(this.word);
    if (dbWord != null) {
      return dbWord;
    }
    return convertSubword(0, this.word, null);
  }
  
  private String convertSubword(int start, String currentWord, StringBuilder localBuffer)
  {
    int dotPos = currentWord.indexOf('.', start);
    if ((start == 0) && (dotPos == -1)) {
      return currentWord;
    }
    if (start == 0) {
      localBuffer = new StringBuilder();
    }
    if (dotPos == -1)
    {
      localBuffer.append(currentWord.substring(start));
      return localBuffer.toString();
    }
    localBuffer.append(currentWord.substring(start, dotPos + 1));
    if (dotPos == currentWord.length() - 1) {
      return localBuffer.toString();
    }
    start = dotPos + 1;
    String remainder = currentWord.substring(start, currentWord.length());
    
    String dbWord = getDeployWord(remainder);
    if (dbWord != null)
    {
      localBuffer.append(dbWord);
      return localBuffer.toString();
    }
    return convertSubword(start, currentWord, localBuffer);
  }
  
  public String getDeployWord(String expression)
  {
    if (expression.equalsIgnoreCase(this.beanDescriptor.getName())) {
      return this.beanDescriptor.getBaseTable();
    }
    ElPropertyDeploy elProp = this.beanDescriptor.getElPropertyDeploy(expression);
    if (elProp != null) {
      return elProp.getDbColumn();
    }
    return null;
  }
}
