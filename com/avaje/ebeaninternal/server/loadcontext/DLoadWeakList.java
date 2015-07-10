package com.avaje.ebeaninternal.server.loadcontext;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DLoadWeakList<T>
  implements DLoadList<T>
{
  private static final Logger logger = Logger.getLogger(DLoadWeakList.class.getName());
  protected final ArrayList<WeakReference<T>> list = new ArrayList();
  protected int removedFromTop;
  
  public int add(T e)
  {
    synchronized (this)
    {
      int i = this.list.size();
      this.list.add(new WeakReference(e));
      return i;
    }
  }
  
  public void removeEntry(int position)
  {
    synchronized (this)
    {
      WeakReference<T> wref = (WeakReference)this.list.get(position);
      if (wref == null)
      {
        logger.log(Level.WARNING, "removeEntry found no WeakReference for position[" + position + "]");
      }
      else
      {
        this.list.set(position, null);
        T object = wref.get();
        if (object == null) {
          logger.log(Level.WARNING, "removeEntry found no Object held by WeakReference for position[" + position + "]");
        }
      }
      if (position == this.removedFromTop) {
        this.removedFromTop += 1;
      }
    }
  }
  
  public List<T> getNextBatch(int batchSize)
  {
    if (this.removedFromTop >= this.list.size()) {
      return new ArrayList(0);
    }
    return getLoadBatch(this.removedFromTop, batchSize, true);
  }
  
  public List<T> getLoadBatch(int position, int batchSize)
  {
    return getLoadBatch(position, batchSize, false);
  }
  
  private List<T> getLoadBatch(int position, int batchSize, boolean ignoreMissing)
  {
    synchronized (this)
    {
      if (batchSize < 1) {
        throw new RuntimeException("batchSize " + batchSize + " < 1 ??!!");
      }
      ArrayList<T> batch = new ArrayList();
      if ((!addObjectToBatchAt(batch, position)) && (!ignoreMissing))
      {
        String msg = "getLoadBatch position[" + position + "] didn't find a bean in the list?";
        throw new IllegalStateException(msg);
      }
      for (int i = position; i < this.list.size(); i++)
      {
        addObjectToBatchAt(batch, i);
        if (batch.size() == batchSize) {
          return batch;
        }
      }
      for (int i = this.removedFromTop; i < position; i++)
      {
        addObjectToBatchAt(batch, i);
        if (batch.size() == batchSize) {
          return batch;
        }
      }
      return batch;
    }
  }
  
  private boolean addObjectToBatchAt(ArrayList<T> batch, int i)
  {
    boolean found = false;
    WeakReference<T> wref = (WeakReference)this.list.get(i);
    if (wref != null)
    {
      T object = wref.get();
      if (object == null)
      {
        logger.log(Level.WARNING, "Bean is null from weak reference");
      }
      else
      {
        found = true;
        batch.add(object);
      }
      this.list.set(i, null);
    }
    if (i == this.removedFromTop) {
      this.removedFromTop += 1;
    }
    return found;
  }
}
