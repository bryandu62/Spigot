package com.avaje.ebeaninternal.server.text.json;

import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.text.PathProperties;
import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebean.text.json.JsonWriteBeanVisitor;
import com.avaje.ebean.text.json.JsonWriteOptions;
import com.avaje.ebean.text.json.JsonWriter;
import com.avaje.ebeaninternal.server.type.EscapeJson;
import com.avaje.ebeaninternal.server.type.ScalarType;
import com.avaje.ebeaninternal.server.util.ArrayStack;
import java.util.Map;
import java.util.Set;

public class WriteJsonContext
  implements JsonWriter
{
  private final WriteJsonBuffer buffer;
  private final boolean pretty;
  private final JsonValueAdapter valueAdapter;
  private final ArrayStack<Object> parentBeans = new ArrayStack();
  private final PathProperties pathProperties;
  private final Map<String, JsonWriteBeanVisitor<?>> visitorMap;
  private final String callback;
  private final PathStack pathStack;
  private WriteBeanState beanState;
  private int depthOffset;
  boolean assocOne;
  
  public WriteJsonContext(WriteJsonBuffer buffer, boolean pretty, JsonValueAdapter dfltValueAdapter, JsonWriteOptions options, String requestCallback)
  {
    this.buffer = buffer;
    this.pretty = pretty;
    this.pathStack = new PathStack();
    this.callback = getCallback(requestCallback, options);
    if (options == null)
    {
      this.valueAdapter = dfltValueAdapter;
      this.visitorMap = null;
      this.pathProperties = null;
    }
    else
    {
      this.valueAdapter = getValueAdapter(dfltValueAdapter, options.getValueAdapter());
      this.visitorMap = emptyToNull(options.getVisitorMap());
      this.pathProperties = emptyToNull(options.getPathProperties());
    }
    if (this.callback != null) {
      buffer.append(requestCallback).append("(");
    }
  }
  
  public void appendRawValue(String key, String rawJsonValue)
  {
    appendKeyWithComma(key, true);
    this.buffer.append(rawJsonValue);
  }
  
  public void appendQuoteEscapeValue(String key, String valueToEscape)
  {
    appendKeyWithComma(key, true);
    EscapeJson.escapeQuote(valueToEscape, this.buffer);
  }
  
  public void end()
  {
    if (this.callback != null) {
      this.buffer.append(")");
    }
  }
  
  private <MK, MV> Map<MK, MV> emptyToNull(Map<MK, MV> m)
  {
    if ((m == null) || (m.isEmpty())) {
      return null;
    }
    return m;
  }
  
  private PathProperties emptyToNull(PathProperties m)
  {
    if ((m == null) || (m.isEmpty())) {
      return null;
    }
    return m;
  }
  
  private String getCallback(String requestCallback, JsonWriteOptions options)
  {
    if (requestCallback != null) {
      return requestCallback;
    }
    if (options != null) {
      return options.getCallback();
    }
    return null;
  }
  
  private JsonValueAdapter getValueAdapter(JsonValueAdapter dfltValueAdapter, JsonValueAdapter valueAdapter)
  {
    return valueAdapter == null ? dfltValueAdapter : valueAdapter;
  }
  
  public Set<String> getIncludeProperties()
  {
    if (this.pathProperties != null)
    {
      String path = (String)this.pathStack.peekWithNull();
      return this.pathProperties.get(path);
    }
    return null;
  }
  
  public JsonWriteBeanVisitor<?> getBeanVisitor()
  {
    if (this.visitorMap != null)
    {
      String path = (String)this.pathStack.peekWithNull();
      return (JsonWriteBeanVisitor)this.visitorMap.get(path);
    }
    return null;
  }
  
  public String getJson()
  {
    return this.buffer.toString();
  }
  
  private void appendIndent()
  {
    this.buffer.append("\n");
    int depth = this.depthOffset + this.parentBeans.size();
    for (int i = 0; i < depth; i++) {
      this.buffer.append("    ");
    }
  }
  
  public void appendObjectBegin()
  {
    if ((this.pretty) && (!this.assocOne)) {
      appendIndent();
    }
    this.buffer.append("{");
  }
  
  public void appendObjectEnd()
  {
    this.buffer.append("}");
  }
  
  public void appendArrayBegin()
  {
    if (this.pretty) {
      appendIndent();
    }
    this.buffer.append("[");
    this.depthOffset += 1;
  }
  
  public void appendArrayEnd()
  {
    this.depthOffset -= 1;
    if (this.pretty) {
      appendIndent();
    }
    this.buffer.append("]");
  }
  
  public void appendComma()
  {
    this.buffer.append(",");
  }
  
  public void addDepthOffset(int offset)
  {
    this.depthOffset += offset;
  }
  
  public void beginAssocOneIsNull(String key)
  {
    this.depthOffset += 1;
    internalAppendKeyBegin(key);
    appendNull();
    this.depthOffset -= 1;
  }
  
  public void beginAssocOne(String key)
  {
    this.pathStack.pushPathKey(key);
    
    internalAppendKeyBegin(key);
    this.assocOne = true;
  }
  
  public void endAssocOne()
  {
    this.pathStack.pop();
    this.assocOne = false;
  }
  
  public Boolean includeMany(String key)
  {
    if (this.pathProperties != null)
    {
      String fullPath = this.pathStack.peekFullPath(key);
      return Boolean.valueOf(this.pathProperties.hasPath(fullPath));
    }
    return null;
  }
  
  public void beginAssocMany(String key)
  {
    this.pathStack.pushPathKey(key);
    
    this.depthOffset -= 1;
    internalAppendKeyBegin(key);
    this.depthOffset += 1;
    this.buffer.append("[");
  }
  
  public void endAssocMany()
  {
    this.pathStack.pop();
    if (this.pretty)
    {
      this.depthOffset -= 1;
      appendIndent();
      this.depthOffset += 1;
    }
    this.buffer.append("]");
  }
  
  private void internalAppendKeyBegin(String key)
  {
    if (!this.beanState.isFirstKey()) {
      this.buffer.append(",");
    }
    if (this.pretty) {
      appendIndent();
    }
    appendKeyWithComma(key, false);
  }
  
  public <T> void appendNameValue(String key, ScalarType<T> scalarType, T value)
  {
    appendKeyWithComma(key, true);
    scalarType.jsonWrite(this.buffer, value, getValueAdapter());
  }
  
  public void appendDiscriminator(String key, String discValue)
  {
    appendKeyWithComma(key, true);
    this.buffer.append("\"");
    this.buffer.append(discValue);
    this.buffer.append("\"");
  }
  
  private void appendKeyWithComma(String key, boolean withComma)
  {
    if ((withComma) && 
      (!this.beanState.isFirstKey())) {
      this.buffer.append(",");
    }
    this.buffer.append("\"");
    if (key == null) {
      this.buffer.append("null");
    } else {
      this.buffer.append(key);
    }
    this.buffer.append("\":");
  }
  
  public void appendNull(String key)
  {
    appendKeyWithComma(key, true);
    this.buffer.append("null");
  }
  
  public void appendNull()
  {
    this.buffer.append("null");
  }
  
  public JsonValueAdapter getValueAdapter()
  {
    return this.valueAdapter;
  }
  
  public String toString()
  {
    return this.buffer.toString();
  }
  
  public void popParentBean()
  {
    this.parentBeans.pop();
  }
  
  public void pushParentBean(Object parentBean)
  {
    this.parentBeans.push(parentBean);
  }
  
  public void popParentBeanMany()
  {
    this.parentBeans.pop();
    this.depthOffset -= 1;
  }
  
  public void pushParentBeanMany(Object parentBean)
  {
    this.parentBeans.push(parentBean);
    this.depthOffset += 1;
  }
  
  public boolean isParentBean(Object bean)
  {
    if (this.parentBeans.isEmpty()) {
      return false;
    }
    return this.parentBeans.contains(bean);
  }
  
  public WriteBeanState pushBeanState(Object bean)
  {
    WriteBeanState newState = new WriteBeanState(bean);
    WriteBeanState prevState = this.beanState;
    this.beanState = newState;
    return prevState;
  }
  
  public void pushPreviousState(WriteBeanState previousState)
  {
    this.beanState = previousState;
  }
  
  public boolean isReferenceBean()
  {
    return this.beanState.isReferenceBean();
  }
  
  public boolean includedProp(String name)
  {
    return this.beanState.includedProp(name);
  }
  
  public Set<String> getLoadedProps()
  {
    return this.beanState.getLoadedProps();
  }
  
  public static class WriteBeanState
  {
    private final EntityBeanIntercept ebi;
    private final Set<String> loadedProps;
    private final boolean referenceBean;
    private boolean firstKeyOut;
    
    public WriteBeanState(Object bean)
    {
      if ((bean instanceof EntityBean))
      {
        this.ebi = ((EntityBean)bean)._ebean_getIntercept();
        this.loadedProps = this.ebi.getLoadedProps();
        this.referenceBean = this.ebi.isReference();
      }
      else
      {
        this.ebi = null;
        this.loadedProps = null;
        this.referenceBean = false;
      }
    }
    
    public Set<String> getLoadedProps()
    {
      return this.loadedProps;
    }
    
    public boolean includedProp(String name)
    {
      if ((this.loadedProps == null) || (this.loadedProps.contains(name))) {
        return true;
      }
      return false;
    }
    
    public boolean isReferenceBean()
    {
      return this.referenceBean;
    }
    
    public boolean isFirstKey()
    {
      if (!this.firstKeyOut)
      {
        this.firstKeyOut = true;
        return true;
      }
      return false;
    }
  }
}
