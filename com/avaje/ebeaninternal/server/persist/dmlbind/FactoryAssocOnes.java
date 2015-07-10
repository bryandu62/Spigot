package com.avaje.ebeaninternal.server.persist.dmlbind;

public class FactoryAssocOnes
{
  /* Error */
  public java.util.List<Bindable> create(java.util.List<Bindable> list, com.avaje.ebeaninternal.server.deploy.BeanDescriptor<?> desc, com.avaje.ebeaninternal.server.persist.dml.DmlMode mode)
  {
    // Byte code:
    //   0: aload_2
    //   1: invokevirtual 21	com/avaje/ebeaninternal/server/deploy/BeanDescriptor:propertiesOneImported	()[Lcom/avaje/ebeaninternal/server/deploy/BeanPropertyAssocOne;
    //   4: astore 4
    //   6: iconst_0
    //   7: istore 5
    //   9: iload 5
    //   11: aload 4
    //   13: arraylength
    //   14: if_icmpge +106 -> 120
    //   17: aload 4
    //   19: iload 5
    //   21: aaload
    //   22: invokevirtual 27	com/avaje/ebeaninternal/server/deploy/BeanPropertyAssocOne:isImportedPrimaryKey	()Z
    //   25: ifeq +6 -> 31
    //   28: goto +86 -> 114
    //   31: getstatic 31	com/avaje/ebeaninternal/server/persist/dmlbind/FactoryAssocOnes$1:$SwitchMap$com$avaje$ebeaninternal$server$persist$dml$DmlMode	[I
    //   34: aload_3
    //   35: invokevirtual 37	com/avaje/ebeaninternal/server/persist/dml/DmlMode:ordinal	()I
    //   38: iaload
    //   39: tableswitch	default:+56->95, 1:+25->64, 2:+28->67, 3:+42->81
    //   64: goto +31 -> 95
    //   67: aload 4
    //   69: iload 5
    //   71: aaload
    //   72: invokevirtual 40	com/avaje/ebeaninternal/server/deploy/BeanPropertyAssocOne:isInsertable	()Z
    //   75: ifne +20 -> 95
    //   78: goto +36 -> 114
    //   81: aload 4
    //   83: iload 5
    //   85: aaload
    //   86: invokevirtual 43	com/avaje/ebeaninternal/server/deploy/BeanPropertyAssocOne:isUpdateable	()Z
    //   89: ifne +6 -> 95
    //   92: goto +22 -> 114
    //   95: aload_1
    //   96: new 45	com/avaje/ebeaninternal/server/persist/dmlbind/BindableAssocOne
    //   99: dup
    //   100: aload 4
    //   102: iload 5
    //   104: aaload
    //   105: invokespecial 48	com/avaje/ebeaninternal/server/persist/dmlbind/BindableAssocOne:<init>	(Lcom/avaje/ebeaninternal/server/deploy/BeanPropertyAssocOne;)V
    //   108: invokeinterface 54 2 0
    //   113: pop
    //   114: iinc 5 1
    //   117: goto -108 -> 9
    //   120: aload_1
    //   121: areturn
    // Line number table:
    //   Java source line #41	-> byte code offset #0
    //   Java source line #43	-> byte code offset #6
    //   Java source line #44	-> byte code offset #17
    //   Java source line #48	-> byte code offset #31
    //   Java source line #50	-> byte code offset #64
    //   Java source line #52	-> byte code offset #67
    //   Java source line #53	-> byte code offset #78
    //   Java source line #57	-> byte code offset #81
    //   Java source line #58	-> byte code offset #92
    //   Java source line #62	-> byte code offset #95
    //   Java source line #43	-> byte code offset #114
    //   Java source line #66	-> byte code offset #120
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	122	0	this	FactoryAssocOnes
    //   0	122	1	list	java.util.List<Bindable>
    //   0	122	2	desc	com.avaje.ebeaninternal.server.deploy.BeanDescriptor<?>
    //   0	122	3	mode	com.avaje.ebeaninternal.server.persist.dml.DmlMode
    //   4	97	4	ones	com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne<?>[]
    //   7	108	5	i	int
  }
}
