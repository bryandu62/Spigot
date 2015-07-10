package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.Query;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.BeanCollectionAdd;
import com.avaje.ebean.bean.BeanCollectionLoader;
import com.avaje.ebeaninternal.server.text.json.WriteJsonContext;
import java.util.ArrayList;
import java.util.Iterator;

public abstract interface BeanCollectionHelp<T>
{
  public abstract void setLoader(BeanCollectionLoader paramBeanCollectionLoader);
  
  public abstract BeanCollectionAdd getBeanCollectionAdd(Object paramObject, String paramString);
  
  public abstract Object createEmpty(boolean paramBoolean);
  
  public abstract Iterator<?> getIterator(Object paramObject);
  
  public abstract void add(BeanCollection<?> paramBeanCollection, Object paramObject);
  
  public abstract BeanCollection<T> createReference(Object paramObject, String paramString);
  
  public abstract ArrayList<InvalidValue> validate(Object paramObject);
  
  public abstract void refresh(EbeanServer paramEbeanServer, Query<?> paramQuery, Transaction paramTransaction, Object paramObject);
  
  public abstract void refresh(BeanCollection<?> paramBeanCollection, Object paramObject);
  
  public abstract void jsonWrite(WriteJsonContext paramWriteJsonContext, String paramString, Object paramObject, boolean paramBoolean);
}
