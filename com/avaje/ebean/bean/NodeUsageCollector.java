package com.avaje.ebean.bean;

import java.lang.ref.WeakReference;
import java.util.HashSet;

public final class NodeUsageCollector
{
  private final ObjectGraphNode node;
  private final WeakReference<NodeUsageListener> managerRef;
  private final HashSet<String> used = new HashSet();
  private boolean modified;
  private String loadProperty;
  
  public NodeUsageCollector(ObjectGraphNode node, WeakReference<NodeUsageListener> managerRef)
  {
    this.node = node;
    
    this.managerRef = managerRef;
  }
  
  public void setModified()
  {
    this.modified = true;
  }
  
  public void addUsed(String property)
  {
    this.used.add(property);
  }
  
  public void setLoadProperty(String loadProperty)
  {
    this.loadProperty = loadProperty;
  }
  
  private void publishUsageInfo()
  {
    NodeUsageListener manager = (NodeUsageListener)this.managerRef.get();
    if (manager != null) {
      manager.collectNodeUsage(this);
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    publishUsageInfo();
    super.finalize();
  }
  
  public ObjectGraphNode getNode()
  {
    return this.node;
  }
  
  public boolean isEmpty()
  {
    return this.used.isEmpty();
  }
  
  public HashSet<String> getUsed()
  {
    return this.used;
  }
  
  public boolean isModified()
  {
    return this.modified;
  }
  
  public String getLoadProperty()
  {
    return this.loadProperty;
  }
  
  public String toString()
  {
    return this.node + " read:" + this.used + " modified:" + this.modified;
  }
}
