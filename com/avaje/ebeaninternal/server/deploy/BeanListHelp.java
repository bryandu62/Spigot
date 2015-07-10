package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.Query;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.BeanCollectionAdd;
import com.avaje.ebean.bean.BeanCollectionLoader;
import com.avaje.ebean.common.BeanList;
import com.avaje.ebeaninternal.server.text.json.WriteJsonContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BeanListHelp<T>
  implements BeanCollectionHelp<T>
{
  private final BeanPropertyAssocMany<T> many;
  private final BeanDescriptor<T> targetDescriptor;
  private BeanCollectionLoader loader;
  
  public BeanListHelp(BeanPropertyAssocMany<T> many)
  {
    this.many = many;
    this.targetDescriptor = many.getTargetDescriptor();
  }
  
  public BeanListHelp()
  {
    this.many = null;
    this.targetDescriptor = null;
  }
  
  public void setLoader(BeanCollectionLoader loader)
  {
    this.loader = loader;
  }
  
  public void add(BeanCollection<?> collection, Object bean)
  {
    collection.internalAdd(bean);
  }
  
  public BeanCollectionAdd getBeanCollectionAdd(Object bc, String mapKey)
  {
    if ((bc instanceof BeanList))
    {
      BeanList<?> bl = (BeanList)bc;
      if (bl.getActualList() == null) {
        bl.setActualList(new ArrayList());
      }
      return bl;
    }
    if ((bc instanceof List)) {
      return new VanillaAdd((List)bc, null);
    }
    throw new RuntimeException("Unhandled type " + bc);
  }
  
  static class VanillaAdd
    implements BeanCollectionAdd
  {
    private final List list;
    
    private VanillaAdd(List<?> list)
    {
      this.list = list;
    }
    
    public void addBean(Object bean)
    {
      this.list.add(bean);
    }
  }
  
  public Iterator<?> getIterator(Object collection)
  {
    return ((List)collection).iterator();
  }
  
  public Object createEmpty(boolean vanilla)
  {
    return vanilla ? new ArrayList() : new BeanList();
  }
  
  public BeanCollection<T> createReference(Object parentBean, String propertyName)
  {
    return new BeanList(this.loader, parentBean, propertyName);
  }
  
  public ArrayList<InvalidValue> validate(Object manyValue)
  {
    ArrayList<InvalidValue> errs = null;
    
    List<?> l = (List)manyValue;
    for (int i = 0; i < l.size(); i++)
    {
      Object detailBean = l.get(i);
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
    BeanList<?> newBeanList = (BeanList)server.findList(query, t);
    refresh(newBeanList, parentBean);
  }
  
  public void refresh(BeanCollection<?> bc, Object parentBean)
  {
    BeanList<?> newBeanList = (BeanList)bc;
    
    List<?> currentList = (List)this.many.getValueUnderlying(parentBean);
    
    newBeanList.setModifyListening(this.many.getModifyListenMode());
    if (currentList == null)
    {
      this.many.setValue(parentBean, newBeanList);
    }
    else if ((currentList instanceof BeanList))
    {
      BeanList<?> currentBeanList = (BeanList)currentList;
      currentBeanList.setActualList(newBeanList.getActualList());
      currentBeanList.setModifyListening(this.many.getModifyListenMode());
    }
    else
    {
      this.many.setValue(parentBean, newBeanList);
    }
  }
  
  public void jsonWrite(WriteJsonContext ctx, String name, Object collection, boolean explicitInclude)
  {
    List<?> list;
    List<?> list;
    if ((collection instanceof BeanCollection))
    {
      BeanList<?> beanList = (BeanList)collection;
      if (!beanList.isPopulated()) {
        if (explicitInclude) {
          beanList.size();
        } else {
          return;
        }
      }
      list = beanList.getActualList();
    }
    else
    {
      list = (List)collection;
    }
    ctx.beginAssocMany(name);
    for (int j = 0; j < list.size(); j++)
    {
      if (j > 0) {
        ctx.appendComma();
      }
      Object detailBean = list.get(j);
      this.targetDescriptor.jsonWrite(ctx, detailBean);
    }
    ctx.endAssocMany();
  }
}
