package org.bukkit.craftbukkit.libs.joptsimple;

abstract interface OptionSpecVisitor
{
  public abstract void visit(NoArgumentOptionSpec paramNoArgumentOptionSpec);
  
  public abstract void visit(RequiredArgumentOptionSpec<?> paramRequiredArgumentOptionSpec);
  
  public abstract void visit(OptionalArgumentOptionSpec<?> paramOptionalArgumentOptionSpec);
  
  public abstract void visit(AlternativeLongOptionSpec paramAlternativeLongOptionSpec);
}
