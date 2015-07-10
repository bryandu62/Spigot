package com.avaje.ebeaninternal.server.type;

public class ScalarTypeLdapBoolean
  extends ScalarTypeBoolean.StringBoolean
{
  public ScalarTypeLdapBoolean()
  {
    super("TRUE", "FALSE");
  }
}
