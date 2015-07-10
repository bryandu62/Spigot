package com.avaje.ebeaninternal.api;

import com.avaje.ebean.ExpressionFactory;
import com.avaje.ebeaninternal.server.expression.FilterExprPath;

public abstract interface SpiExpressionFactory
  extends ExpressionFactory
{
  public abstract ExpressionFactory createExpressionFactory(FilterExprPath paramFilterExprPath);
}
