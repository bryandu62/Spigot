package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebean.event.BulkTableEvent;
import com.avaje.ebean.event.BulkTableEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BulkEventListenerMap
{
  private final HashMap<String, Entry> map = new HashMap();
  
  public BulkEventListenerMap(List<BulkTableEventListener> listeners)
  {
    Iterator i$;
    if (listeners != null) {
      for (i$ = listeners.iterator(); i$.hasNext();)
      {
        l = (BulkTableEventListener)i$.next();
        Set<String> tables = l.registeredTables();
        for (String tableName : tables) {
          register(tableName, l);
        }
      }
    }
    BulkTableEventListener l;
  }
  
  public boolean isEmpty()
  {
    return this.map.isEmpty();
  }
  
  public void process(BulkTableEvent event)
  {
    Entry entry = (Entry)this.map.get(event.getTableName());
    if (entry != null) {
      entry.process(event);
    }
  }
  
  private void register(String tableName, BulkTableEventListener l)
  {
    String upperTableName = tableName.trim().toUpperCase();
    Entry entry = (Entry)this.map.get(upperTableName);
    if (entry == null)
    {
      entry = new Entry(null);
      this.map.put(upperTableName, entry);
    }
    entry.add(l);
  }
  
  private static class Entry
  {
    List<BulkTableEventListener> listeners = new ArrayList();
    
    private void add(BulkTableEventListener l)
    {
      this.listeners.add(l);
    }
    
    private void process(BulkTableEvent event)
    {
      for (int i = 0; i < this.listeners.size(); i++) {
        ((BulkTableEventListener)this.listeners.get(i)).process(event);
      }
    }
  }
}
