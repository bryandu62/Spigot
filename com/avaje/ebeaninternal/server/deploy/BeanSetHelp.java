package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.Query;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.BeanCollectionAdd;
import com.avaje.ebean.bean.BeanCollectionLoader;
import com.avaje.ebean.common.BeanSet;
import com.avaje.ebeaninternal.server.text.json.WriteJsonContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public final class BeanSetHelp<T>
  implements BeanCollectionHelp<T>
{
  private final BeanPropertyAssocMany<T> many;
  private final BeanDescriptor<T> targetDescriptor;
  private BeanCollectionLoader loader;
  
  public BeanSetHelp(BeanPropertyAssocMany<T> many)
  {
    this.many = many;
    this.targetDescriptor = many.getTargetDescriptor();
  }
  
  public BeanSetHelp()
  {
    this.many = null;
    this.targetDescriptor = null;
  }
  
  public void setLoader(BeanCollectionLoader loader)
  {
    this.loader = loader;
  }
  
  public Iterator<?> getIterator(Object collection)
  {
    return ((Set)collection).iterator();
  }
  
  public BeanCollectionAdd getBeanCollectionAdd(Object bc, String mapKey)
  {
    if ((bc instanceof BeanSet))
    {
      BeanSet<?> beanSet = (BeanSet)bc;
      if (beanSet.getActualSet() == null) {
        beanSet.setActualSet(new LinkedHashSet());
      }
      return beanSet;
    }
    if ((bc instanceof Set)) {
      return new VanillaAdd((Set)bc, null);
    }
    throw new RuntimeException("Unhandled type " + bc);
  }
  
  static class VanillaAdd
    implements BeanCollectionAdd
  {
    private final Set set;
    
    private VanillaAdd(Set<?> set)
    {
      this.set = set;
    }
    
    public void addBean(Object bean)
    {
      this.set.add(bean);
    }
  }
  
  public void add(BeanCollection<?> collection, Object bean)
  {
    collection.internalAdd(bean);
  }
  
  public Object createEmpty(boolean vanilla)
  {
    return vanilla ? new LinkedHashSet() : new BeanSet();
  }
  
  public BeanCollection<T> createReference(Object parentBean, String propertyName)
  {
    return new BeanSet(this.loader, parentBean, propertyName);
  }
  
  public ArrayList<InvalidValue> validate(Object manyValue)
  {
    ArrayList<InvalidValue> errs = null;
    
    Set<?> set = (Set)manyValue;
    Iterator<?> i = set.iterator();
    while (i.hasNext())
    {
      Object detailBean = i.next();
      InvalidValue invalid = this.targetDescriptor.validate(true, detailBean);
      if (invalid != null)
      {
        if (errs == null) {
          errs = new ArrayList();
        }
        errs.add(invalid);
      }
    }
    return errs;
  }
  
  public void refresh(EbeanServer server, Query<?> query, Transaction t, Object parentBean)
  {
    BeanSet<?> newBeanSet = (BeanSet)server.findSet(query, t);
    refresh(newBeanSet, parentBean);
  }
  
  public void refresh(BeanCollection<?> bc, Object parentBean)
  {
    BeanSet<?> newBeanSet = (BeanSet)bc;
    
    Set<?> current = (Set)this.many.getValueUnderlying(parentBean);
    
    newBeanSet.setModifyListening(this.many.getModifyListenMode());
    if (current == null)
    {
      this.many.setValue(parentBean, newBeanSet);
    }
    else if ((current instanceof BeanSet))
    {
      BeanSet<?> currentBeanSet = (BeanSet)current;
      currentBeanSet.setActualSet(newBeanSet.getActualSet());
      currentBeanSet.setModifyListening(this.many.getModifyListenMode());
    }
    else
    {
      this.many.setValue(parentBean, newBeanSet);
    }
  }
  
  public void jsonWrite(WriteJsonContext ctx, String name, Object collection, boolean explicitInclude)
  {
    Set<?> set;
    Set<?> set;
    if ((collection instanceof BeanCollection))
    {
      BeanSet<?> bc = (BeanSet)collection;
      if (!bc.isPopulated()) {
        if (explicitInclude) {
          bc.size();
        } else {
          return;
        }
      }
      set = bc.getActualSet();
    }
    else
    {
      set = (Set)collection;
    }
    int count = 0;
    ctx.beginAssocMany(name);
    Iterator<?> it = set.iterator();
    while (it.hasNext())
    {
      Object detailBean = it.next();
      if (count++ > 0) {
        ctx.appendComma();
      }
      this.targetDescriptor.jsonWrite(ctx, detailBean);
    }
    ctx.endAssocMany();
  }
}
