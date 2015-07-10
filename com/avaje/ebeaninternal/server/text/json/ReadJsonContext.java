package com.avaje.ebeaninternal.server.text.json;

import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.text.json.JsonElement;
import com.avaje.ebean.text.json.JsonReadBeanVisitor;
import com.avaje.ebean.text.json.JsonReadOptions;
import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.util.ArrayStack;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ReadJsonContext
  extends ReadBasicJsonContext
{
  private final Map<String, JsonReadBeanVisitor<?>> visitorMap;
  private final JsonValueAdapter valueAdapter;
  private final PathStack pathStack;
  private final ArrayStack<ReadBeanState> beanState;
  private ReadBeanState currentState;
  
  public ReadJsonContext(ReadJsonSource src, JsonValueAdapter dfltValueAdapter, JsonReadOptions options)
  {
    super(src);
    this.beanState = new ArrayStack();
    if (options == null)
    {
      this.valueAdapter = dfltValueAdapter;
      this.visitorMap = null;
      this.pathStack = null;
    }
    else
    {
      this.valueAdapter = getValueAdapter(dfltValueAdapter, options.getValueAdapter());
      this.visitorMap = options.getVisitorMap();
      this.pathStack = ((this.visitorMap == null) || (this.visitorMap.isEmpty()) ? null : new PathStack());
    }
  }
  
  private JsonValueAdapter getValueAdapter(JsonValueAdapter dfltValueAdapter, JsonValueAdapter valueAdapter)
  {
    return valueAdapter == null ? dfltValueAdapter : valueAdapter;
  }
  
  public JsonValueAdapter getValueAdapter()
  {
    return this.valueAdapter;
  }
  
  public String readScalarValue()
  {
    ignoreWhiteSpace();
    
    char prevChar = nextChar();
    if ('"' == prevChar) {
      return readQuotedValue();
    }
    return readUnquotedValue(prevChar);
  }
  
  public void pushBean(Object bean, String path, BeanDescriptor<?> beanDescriptor)
  {
    this.currentState = new ReadBeanState(bean, beanDescriptor, null);
    this.beanState.push(this.currentState);
    if (this.pathStack != null) {
      this.pathStack.pushPathKey(path);
    }
  }
  
  public ReadBeanState popBeanState()
  {
    if (this.pathStack != null)
    {
      String path = (String)this.pathStack.peekWithNull();
      JsonReadBeanVisitor<?> beanVisitor = (JsonReadBeanVisitor)this.visitorMap.get(path);
      if (beanVisitor != null) {
        this.currentState.visit(beanVisitor);
      }
      this.pathStack.pop();
    }
    ReadBeanState s = this.currentState;
    
    this.beanState.pop();
    this.currentState = ((ReadBeanState)this.beanState.peekWithNull());
    return s;
  }
  
  public void setProperty(String propertyName)
  {
    this.currentState.setLoaded(propertyName);
  }
  
  public JsonElement readUnmappedJson(String key)
  {
    JsonElement rawJsonValue = ReadJsonRawReader.readJsonElement(this);
    if (this.visitorMap != null) {
      this.currentState.addUnmappedJson(key, rawJsonValue);
    }
    return rawJsonValue;
  }
  
  public static class ReadBeanState
    implements PropertyChangeListener
  {
    private final Object bean;
    private final BeanDescriptor<?> beanDescriptor;
    private final EntityBeanIntercept ebi;
    private final Set<String> loadedProps;
    private Map<String, JsonElement> unmapped;
    
    private ReadBeanState(Object bean, BeanDescriptor<?> beanDescriptor)
    {
      this.bean = bean;
      this.beanDescriptor = beanDescriptor;
      if ((bean instanceof EntityBean))
      {
        this.ebi = ((EntityBean)bean)._ebean_getIntercept();
        this.loadedProps = new HashSet();
      }
      else
      {
        this.ebi = null;
        this.loadedProps = null;
      }
    }
    
    public String toString()
    {
      return this.bean.getClass().getSimpleName() + " loaded:" + this.loadedProps;
    }
    
    public void setLoaded(String propertyName)
    {
      if (this.ebi != null) {
        this.loadedProps.add(propertyName);
      }
    }
    
    private void addUnmappedJson(String key, JsonElement value)
    {
      if (this.unmapped == null) {
        this.unmapped = new LinkedHashMap();
      }
      this.unmapped.put(key, value);
    }
    
    private <T> void visit(JsonReadBeanVisitor<T> beanVisitor)
    {
      if (this.ebi != null) {
        this.ebi.addPropertyChangeListener(this);
      }
      beanVisitor.visit(this.bean, this.unmapped);
      if (this.ebi != null) {
        this.ebi.removePropertyChangeListener(this);
      }
    }
    
    public void setLoadedState()
    {
      if (this.ebi != null) {
        this.beanDescriptor.setLoadedProps(this.ebi, this.loadedProps);
      }
    }
    
    public void propertyChange(PropertyChangeEvent evt)
    {
      String propName = evt.getPropertyName();
      this.loadedProps.add(propName);
    }
    
    public Object getBean()
    {
      return this.bean;
    }
  }
}
