package com.avaje.ebeaninternal.server.el;

import java.util.ArrayList;
import java.util.List;

public class ElPropertyChainBuilder
{
  private final String expression;
  private final List<ElPropertyValue> chain = new ArrayList();
  private final boolean embedded;
  private boolean containsMany;
  
  public ElPropertyChainBuilder(boolean embedded, String expression)
  {
    this.embedded = embedded;
    this.expression = expression;
  }
  
  public boolean isContainsMany()
  {
    return this.containsMany;
  }
  
  public void setContainsMany(boolean containsMany)
  {
    this.containsMany = containsMany;
  }
  
  public String getExpression()
  {
    return this.expression;
  }
  
  public ElPropertyChainBuilder add(ElPropertyValue element)
  {
    if (element == null) {
      throw new NullPointerException("element null in expression " + this.expression);
    }
    this.chain.add(element);
    return this;
  }
  
  public ElPropertyChain build()
  {
    return new ElPropertyChain(this.containsMany, this.embedded, this.expression, (ElPropertyValue[])this.chain.toArray(new ElPropertyValue[this.chain.size()]));
  }
}
