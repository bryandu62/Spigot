package com.avaje.ebean.bean;

import com.avaje.ebean.Ebean;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

public final class EntityBeanIntercept
  implements Serializable
{
  private static final long serialVersionUID = -3664031775464862648L;
  private transient NodeUsageCollector nodeUsageCollector;
  private transient PropertyChangeSupport pcs;
  private transient PersistenceContext persistenceContext;
  private transient BeanLoader beanLoader;
  private int beanLoaderIndex;
  private String ebeanServerName;
  private EntityBean owner;
  private Object parentBean;
  private volatile boolean loaded;
  private boolean disableLazyLoad;
  private boolean lazyLoadFailure;
  private boolean intercepting;
  private boolean readOnly;
  private Object oldValues;
  private volatile Set<String> loadedProps;
  private HashSet<String> changedProps;
  private String lazyLoadProperty;
  
  public EntityBeanIntercept(Object owner)
  {
    this.owner = ((EntityBean)owner);
  }
  
  public void copyStateTo(EntityBeanIntercept dest)
  {
    dest.loadedProps = this.loadedProps;
    dest.ebeanServerName = this.ebeanServerName;
    if (this.loaded) {
      dest.setLoaded();
    }
  }
  
  public EntityBean getOwner()
  {
    return this.owner;
  }
  
  public String toString()
  {
    if (!this.loaded) {
      return "Reference...";
    }
    return "OldValues: " + this.oldValues;
  }
  
  public PersistenceContext getPersistenceContext()
  {
    return this.persistenceContext;
  }
  
  public void setPersistenceContext(PersistenceContext persistenceContext)
  {
    this.persistenceContext = persistenceContext;
  }
  
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    if (this.pcs == null) {
      this.pcs = new PropertyChangeSupport(this.owner);
    }
    this.pcs.addPropertyChangeListener(listener);
  }
  
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    if (this.pcs == null) {
      this.pcs = new PropertyChangeSupport(this.owner);
    }
    this.pcs.addPropertyChangeListener(propertyName, listener);
  }
  
  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    if (this.pcs != null) {
      this.pcs.removePropertyChangeListener(listener);
    }
  }
  
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    if (this.pcs != null) {
      this.pcs.removePropertyChangeListener(propertyName, listener);
    }
  }
  
  public void setNodeUsageCollector(NodeUsageCollector usageCollector)
  {
    this.nodeUsageCollector = usageCollector;
  }
  
  public Object getParentBean()
  {
    return this.parentBean;
  }
  
  public void setParentBean(Object parentBean)
  {
    this.parentBean = parentBean;
  }
  
  public int getBeanLoaderIndex()
  {
    return this.beanLoaderIndex;
  }
  
  public void setBeanLoaderByServerName(String ebeanServerName)
  {
    this.beanLoaderIndex = 0;
    this.beanLoader = null;
    this.ebeanServerName = ebeanServerName;
  }
  
  public void setBeanLoader(int index, BeanLoader beanLoader, PersistenceContext ctx)
  {
    this.beanLoaderIndex = index;
    this.beanLoader = beanLoader;
    this.persistenceContext = ctx;
    this.ebeanServerName = beanLoader.getName();
  }
  
  public boolean isDirty()
  {
    if (this.oldValues != null) {
      return true;
    }
    return this.owner._ebean_isEmbeddedNewOrDirty();
  }
  
  public boolean isNew()
  {
    return (!this.intercepting) && (!this.loaded);
  }
  
  public boolean isNewOrDirty()
  {
    return (isNew()) || (isDirty());
  }
  
  public boolean isReference()
  {
    return (this.intercepting) && (!this.loaded);
  }
  
  public void setReference()
  {
    this.loaded = false;
    this.intercepting = true;
  }
  
  public Object getOldValues()
  {
    return this.oldValues;
  }
  
  public boolean isReadOnly()
  {
    return this.readOnly;
  }
  
  public void setReadOnly(boolean readOnly)
  {
    this.readOnly = readOnly;
  }
  
  public boolean isIntercepting()
  {
    return this.intercepting;
  }
  
  public void setIntercepting(boolean intercepting)
  {
    this.intercepting = intercepting;
  }
  
  public boolean isLoaded()
  {
    return this.loaded;
  }
  
  public void setLoaded()
  {
    this.loaded = true;
    this.oldValues = null;
    this.intercepting = true;
    this.owner._ebean_setEmbeddedLoaded();
    this.lazyLoadProperty = null;
    this.changedProps = null;
  }
  
  public void setLoadedLazy()
  {
    this.loaded = true;
    this.intercepting = true;
    this.lazyLoadProperty = null;
  }
  
  public void setLazyLoadFailure()
  {
    this.lazyLoadFailure = true;
  }
  
  public boolean isLazyLoadFailure()
  {
    return this.lazyLoadFailure;
  }
  
  public boolean isDisableLazyLoad()
  {
    return this.disableLazyLoad;
  }
  
  public void setDisableLazyLoad(boolean disableLazyLoad)
  {
    this.disableLazyLoad = disableLazyLoad;
  }
  
  public void setEmbeddedLoaded(Object embeddedBean)
  {
    if ((embeddedBean instanceof EntityBean))
    {
      EntityBean eb = (EntityBean)embeddedBean;
      eb._ebean_getIntercept().setLoaded();
    }
  }
  
  public boolean isEmbeddedNewOrDirty(Object embeddedBean)
  {
    if (embeddedBean == null) {
      return false;
    }
    if ((embeddedBean instanceof EntityBean)) {
      return ((EntityBean)embeddedBean)._ebean_getIntercept().isNewOrDirty();
    }
    return true;
  }
  
  public void setLoadedProps(Set<String> loadedPropertyNames)
  {
    this.loadedProps = loadedPropertyNames;
  }
  
  public Set<String> getLoadedProps()
  {
    return this.loadedProps;
  }
  
  public Set<String> getChangedProps()
  {
    return this.changedProps;
  }
  
  public String getLazyLoadProperty()
  {
    return this.lazyLoadProperty;
  }
  
  protected void loadBean(String loadProperty)
  {
    synchronized (this)
    {
      if (this.beanLoader == null)
      {
        BeanLoader serverLoader = (BeanLoader)Ebean.getServer(this.ebeanServerName);
        if (serverLoader == null) {
          throw new PersistenceException("Server [" + this.ebeanServerName + "] was not found?");
        }
        loadBeanInternal(loadProperty, serverLoader);
        return;
      }
    }
    synchronized (this.beanLoader)
    {
      loadBeanInternal(loadProperty, this.beanLoader);
    }
  }
  
  private void loadBeanInternal(String loadProperty, BeanLoader loader)
  {
    if ((this.loaded) && ((this.loadedProps == null) || (this.loadedProps.contains(loadProperty)))) {
      return;
    }
    if (this.disableLazyLoad)
    {
      this.loaded = true;
      return;
    }
    if (this.lazyLoadFailure) {
      throw new EntityNotFoundException("Bean has been deleted - lazy loading failed");
    }
    if (this.lazyLoadProperty == null)
    {
      this.lazyLoadProperty = loadProperty;
      if (this.nodeUsageCollector != null) {
        this.nodeUsageCollector.setLoadProperty(this.lazyLoadProperty);
      }
      loader.loadBean(this);
      if (this.lazyLoadFailure) {
        throw new EntityNotFoundException("Bean has been deleted - lazy loading failed");
      }
    }
  }
  
  protected void createOldValues()
  {
    this.oldValues = this.owner._ebean_createCopy();
    if (this.nodeUsageCollector != null) {
      this.nodeUsageCollector.setModified();
    }
  }
  
  public Object writeReplaceIntercept()
    throws ObjectStreamException
  {
    if (!SerializeControl.isVanillaBeans()) {
      return this.owner;
    }
    return this.owner._ebean_createCopy();
  }
  
  protected boolean areEqual(Object obj1, Object obj2)
  {
    if (obj1 == null) {
      return obj2 == null;
    }
    if (obj2 == null) {
      return false;
    }
    if (obj1 == obj2) {
      return true;
    }
    if ((obj1 instanceof BigDecimal))
    {
      if ((obj2 instanceof BigDecimal))
      {
        Comparable com1 = (Comparable)obj1;
        return com1.compareTo(obj2) == 0;
      }
      return false;
    }
    if ((obj1 instanceof URL)) {
      return obj1.toString().equals(obj2.toString());
    }
    return obj1.equals(obj2);
  }
  
  public void preGetter(String propertyName)
  {
    if (!this.intercepting) {
      return;
    }
    if (!this.loaded) {
      loadBean(propertyName);
    } else if ((this.loadedProps != null) && (!this.loadedProps.contains(propertyName))) {
      loadBean(propertyName);
    }
    if ((this.nodeUsageCollector != null) && (this.loaded)) {
      this.nodeUsageCollector.addUsed(propertyName);
    }
  }
  
  public void postSetter(PropertyChangeEvent event)
  {
    if ((this.pcs != null) && (event != null)) {
      this.pcs.firePropertyChange(event);
    }
  }
  
  public void postSetter(PropertyChangeEvent event, Object newValue)
  {
    if ((this.pcs != null) && (event != null)) {
      if ((newValue != null) && (newValue.equals(event.getNewValue()))) {
        this.pcs.firePropertyChange(event);
      } else {
        this.pcs.firePropertyChange(event.getPropertyName(), event.getOldValue(), newValue);
      }
    }
  }
  
  public PropertyChangeEvent preSetterMany(boolean interceptField, String propertyName, Object oldValue, Object newValue)
  {
    if (this.pcs != null) {
      return new PropertyChangeEvent(this.owner, propertyName, oldValue, newValue);
    }
    return null;
  }
  
  private final void addDirty(String propertyName)
  {
    if (!this.intercepting) {
      return;
    }
    if (this.readOnly) {
      throw new IllegalStateException("This bean is readOnly");
    }
    if (this.loaded)
    {
      if (this.oldValues == null) {
        createOldValues();
      }
      if (this.changedProps == null) {
        this.changedProps = new HashSet();
      }
      this.changedProps.add(propertyName);
    }
  }
  
  public PropertyChangeEvent preSetter(boolean intercept, String propertyName, Object oldValue, Object newValue)
  {
    boolean changed = !areEqual(oldValue, newValue);
    if ((intercept) && (changed)) {
      addDirty(propertyName);
    }
    if ((changed) && (this.pcs != null)) {
      return new PropertyChangeEvent(this.owner, propertyName, oldValue, newValue);
    }
    return null;
  }
  
  public PropertyChangeEvent preSetter(boolean intercept, String propertyName, boolean oldValue, boolean newValue)
  {
    boolean changed = oldValue != newValue;
    if ((intercept) && (changed)) {
      addDirty(propertyName);
    }
    if ((changed) && (this.pcs != null)) {
      return new PropertyChangeEvent(this.owner, propertyName, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
    }
    return null;
  }
  
  public PropertyChangeEvent preSetter(boolean intercept, String propertyName, int oldValue, int newValue)
  {
    boolean changed = oldValue != newValue;
    if ((intercept) && (changed)) {
      addDirty(propertyName);
    }
    if ((changed) && (this.pcs != null)) {
      return new PropertyChangeEvent(this.owner, propertyName, Integer.valueOf(oldValue), Integer.valueOf(newValue));
    }
    return null;
  }
  
  public PropertyChangeEvent preSetter(boolean intercept, String propertyName, long oldValue, long newValue)
  {
    boolean changed = oldValue != newValue;
    if ((intercept) && (changed)) {
      addDirty(propertyName);
    }
    if ((changed) && (this.pcs != null)) {
      return new PropertyChangeEvent(this.owner, propertyName, Long.valueOf(oldValue), Long.valueOf(newValue));
    }
    return null;
  }
  
  public PropertyChangeEvent preSetter(boolean intercept, String propertyName, double oldValue, double newValue)
  {
    boolean changed = oldValue != newValue;
    if ((intercept) && (changed)) {
      addDirty(propertyName);
    }
    if ((changed) && (this.pcs != null)) {
      return new PropertyChangeEvent(this.owner, propertyName, Double.valueOf(oldValue), Double.valueOf(newValue));
    }
    return null;
  }
  
  public PropertyChangeEvent preSetter(boolean intercept, String propertyName, float oldValue, float newValue)
  {
    boolean changed = oldValue != newValue;
    if ((intercept) && (changed)) {
      addDirty(propertyName);
    }
    if ((changed) && (this.pcs != null)) {
      return new PropertyChangeEvent(this.owner, propertyName, Float.valueOf(oldValue), Float.valueOf(newValue));
    }
    return null;
  }
  
  public PropertyChangeEvent preSetter(boolean intercept, String propertyName, short oldValue, short newValue)
  {
    boolean changed = oldValue != newValue;
    if ((intercept) && (changed)) {
      addDirty(propertyName);
    }
    if ((changed) && (this.pcs != null)) {
      return new PropertyChangeEvent(this.owner, propertyName, Short.valueOf(oldValue), Short.valueOf(newValue));
    }
    return null;
  }
  
  public PropertyChangeEvent preSetter(boolean intercept, String propertyName, char oldValue, char newValue)
  {
    boolean changed = oldValue != newValue;
    if ((intercept) && (changed)) {
      addDirty(propertyName);
    }
    if ((changed) && (this.pcs != null)) {
      return new PropertyChangeEvent(this.owner, propertyName, Character.valueOf(oldValue), Character.valueOf(newValue));
    }
    return null;
  }
  
  public PropertyChangeEvent preSetter(boolean intercept, String propertyName, byte oldValue, byte newValue)
  {
    boolean changed = oldValue != newValue;
    if ((intercept) && (changed)) {
      addDirty(propertyName);
    }
    if ((changed) && (this.pcs != null)) {
      return new PropertyChangeEvent(this.owner, propertyName, Byte.valueOf(oldValue), Byte.valueOf(newValue));
    }
    return null;
  }
  
  public PropertyChangeEvent preSetter(boolean intercept, String propertyName, char[] oldValue, char[] newValue)
  {
    boolean changed = !areEqualChars(oldValue, newValue);
    if ((intercept) && (changed)) {
      addDirty(propertyName);
    }
    if ((changed) && (this.pcs != null)) {
      return new PropertyChangeEvent(this.owner, propertyName, oldValue, newValue);
    }
    return null;
  }
  
  public PropertyChangeEvent preSetter(boolean intercept, String propertyName, byte[] oldValue, byte[] newValue)
  {
    boolean changed = !areEqualBytes(oldValue, newValue);
    if ((intercept) && (changed)) {
      addDirty(propertyName);
    }
    if ((changed) && (this.pcs != null)) {
      return new PropertyChangeEvent(this.owner, propertyName, oldValue, newValue);
    }
    return null;
  }
  
  private static boolean areEqualBytes(byte[] b1, byte[] b2)
  {
    if (b1 == null) {
      return b2 == null;
    }
    if (b2 == null) {
      return false;
    }
    if (b1 == b2) {
      return true;
    }
    if (b1.length != b2.length) {
      return false;
    }
    for (int i = 0; i < b1.length; i++) {
      if (b1[i] != b2[i]) {
        return false;
      }
    }
    return true;
  }
  
  private static boolean areEqualChars(char[] b1, char[] b2)
  {
    if (b1 == null) {
      return b2 == null;
    }
    if (b2 == null) {
      return false;
    }
    if (b1 == b2) {
      return true;
    }
    if (b1.length != b2.length) {
      return false;
    }
    for (int i = 0; i < b1.length; i++) {
      if (b1[i] != b2[i]) {
        return false;
      }
    }
    return true;
  }
}
