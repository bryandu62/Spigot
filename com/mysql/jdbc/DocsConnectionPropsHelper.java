package com.mysql.jdbc;

import java.io.PrintStream;

public class DocsConnectionPropsHelper
  extends ConnectionPropertiesImpl
{
  public static void main(String[] args)
    throws Exception
  {
    System.out.println(new DocsConnectionPropsHelper().exposeAsXml());
  }
}
