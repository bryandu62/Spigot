package com.avaje.ebeaninternal.server.lib.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class BusyConnectionBuffer
{
  private PooledConnection[] slots;
  private int growBy;
  private int size;
  private int pos = -1;
  
  protected BusyConnectionBuffer(int capacity, int growBy)
  {
    this.slots = new PooledConnection[capacity];
    this.growBy = growBy;
  }
  
  private void setCapacity(int newCapacity)
  {
    if (newCapacity > this.slots.length)
    {
      PooledConnection[] current = this.slots;
      this.slots = new PooledConnection[newCapacity];
      System.arraycopy(current, 0, this.slots, 0, current.length);
    }
  }
  
  public String toString()
  {
    return Arrays.toString(this.slots);
  }
  
  protected int getCapacity()
  {
    return this.slots.length;
  }
  
  protected int size()
  {
    return this.size;
  }
  
  protected boolean isEmpty()
  {
    return this.size == 0;
  }
  
  protected int add(PooledConnection pc)
  {
    if (this.size == this.slots.length) {
      setCapacity(this.slots.length + this.growBy);
    }
    this.size += 1;
    int slot = nextEmptySlot();
    pc.setSlotId(slot);
    this.slots[slot] = pc;
    return this.size;
  }
  
  protected boolean remove(PooledConnection pc)
  {
    this.size -= 1;
    int slotId = pc.getSlotId();
    if (this.slots[slotId] != pc) {
      return false;
    }
    this.slots[slotId] = null;
    return true;
  }
  
  protected List<PooledConnection> getShallowCopy()
  {
    ArrayList<PooledConnection> tmp = new ArrayList();
    for (int i = 0; i < this.slots.length; i++) {
      if (this.slots[i] != null) {
        tmp.add(this.slots[i]);
      }
    }
    return Collections.unmodifiableList(tmp);
  }
  
  private int nextEmptySlot()
  {
    while (++this.pos < this.slots.length) {
      if (this.slots[this.pos] == null) {
        return this.pos;
      }
    }
    this.pos = -1;
    while (++this.pos < this.slots.length) {
      if (this.slots[this.pos] == null) {
        return this.pos;
      }
    }
    throw new RuntimeException("No Empty Slot Found?");
  }
}
