package com.avaje.ebeaninternal.server.lib.sql;

import java.util.ArrayList;
import java.util.List;

class FreeConnectionBuffer
{
  private PooledConnection[] conns;
  private int removeIndex;
  private int addIndex;
  private int size;
  
  protected FreeConnectionBuffer(int capacity)
  {
    this.conns = new PooledConnection[capacity];
  }
  
  protected int getCapacity()
  {
    return this.conns.length;
  }
  
  protected int size()
  {
    return this.size;
  }
  
  protected boolean isEmpty()
  {
    return this.size == 0;
  }
  
  protected void add(PooledConnection pc)
  {
    this.conns[this.addIndex] = pc;
    this.addIndex = inc(this.addIndex);
    this.size += 1;
  }
  
  protected PooledConnection remove()
  {
    PooledConnection[] items = this.conns;
    PooledConnection pc = items[this.removeIndex];
    items[this.removeIndex] = null;
    this.removeIndex = inc(this.removeIndex);
    this.size -= 1;
    return pc;
  }
  
  protected List<PooledConnection> getShallowCopy()
  {
    List<PooledConnection> copy = new ArrayList(this.conns.length);
    for (int i = 0; i < this.conns.length; i++) {
      if (this.conns[i] != null) {
        copy.add(this.conns[i]);
      }
    }
    return copy;
  }
  
  protected void setShallowCopy(List<PooledConnection> copy)
  {
    this.removeIndex = 0;
    this.addIndex = 0;
    this.size = 0;
    for (int i = 0; i < this.conns.length; i++) {
      this.conns[i] = null;
    }
    for (int i = 0; i < copy.size(); i++) {
      add((PooledConnection)copy.get(i));
    }
  }
  
  protected void setCapacity(int newCapacity)
  {
    if (newCapacity > this.conns.length)
    {
      List<PooledConnection> copy = getShallowCopy();
      
      this.removeIndex = 0;
      this.addIndex = 0;
      this.size = 0;
      
      this.conns = new PooledConnection[newCapacity];
      for (int i = 0; i < copy.size(); i++) {
        add((PooledConnection)copy.get(i));
      }
    }
  }
  
  private final int inc(int i)
  {
    i++;return i == this.conns.length ? 0 : i;
  }
}
