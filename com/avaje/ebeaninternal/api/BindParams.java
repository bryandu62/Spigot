package com.avaje.ebeaninternal.api;

import com.avaje.ebeaninternal.server.querydefn.NaturalKeyBindParam;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class BindParams
  implements Serializable
{
  private static final long serialVersionUID = 4541081933302086285L;
  private ArrayList<Param> positionedParameters;
  private HashMap<String, Param> namedParameters;
  private int queryPlanHash;
  private String preparedSql;
  
  public BindParams()
  {
    this.positionedParameters = new ArrayList();
    
    this.namedParameters = new HashMap();
    
    this.queryPlanHash = 1;
  }
  
  public BindParams copy()
  {
    BindParams copy = new BindParams();
    for (Param p : this.positionedParameters) {
      copy.positionedParameters.add(p.copy());
    }
    Iterator<Map.Entry<String, Param>> it = this.namedParameters.entrySet().iterator();
    while (it.hasNext())
    {
      Map.Entry<String, Param> entry = (Map.Entry)it.next();
      copy.namedParameters.put(entry.getKey(), ((Param)entry.getValue()).copy());
    }
    return copy;
  }
  
  public int queryBindHash()
  {
    int hc = this.namedParameters.hashCode();
    for (int i = 0; i < this.positionedParameters.size(); i++) {
      hc = hc * 31 + ((Param)this.positionedParameters.get(i)).hashCode();
    }
    return hc;
  }
  
  public int hashCode()
  {
    int hc = getClass().hashCode();
    hc = hc * 31 + this.namedParameters.hashCode();
    for (int i = 0; i < this.positionedParameters.size(); i++) {
      hc = hc * 31 + ((Param)this.positionedParameters.get(i)).hashCode();
    }
    hc = hc * 31 + (this.preparedSql == null ? 0 : this.preparedSql.hashCode());
    return hc;
  }
  
  public boolean equals(Object o)
  {
    if (o == null) {
      return false;
    }
    if (o == this) {
      return true;
    }
    if ((o instanceof BindParams)) {
      return hashCode() == o.hashCode();
    }
    return false;
  }
  
  public boolean isEmpty()
  {
    return (this.positionedParameters.isEmpty()) && (this.namedParameters.isEmpty());
  }
  
  public NaturalKeyBindParam getNaturalKeyBindParam()
  {
    if (this.positionedParameters != null) {
      return null;
    }
    if ((this.namedParameters != null) && (this.namedParameters.size() == 1))
    {
      Map.Entry<String, Param> e = (Map.Entry)this.namedParameters.entrySet().iterator().next();
      return new NaturalKeyBindParam((String)e.getKey(), ((Param)e.getValue()).getInValue());
    }
    return null;
  }
  
  public int size()
  {
    return this.positionedParameters.size();
  }
  
  public boolean requiresNamedParamsPrepare()
  {
    return (!this.namedParameters.isEmpty()) && (this.positionedParameters.isEmpty());
  }
  
  public void setNullParameter(int position, int jdbcType)
  {
    Param p = getParam(position);
    p.setInNullType(jdbcType);
  }
  
  public void setParameter(int position, Object value, int outType)
  {
    addToQueryPlanHash(String.valueOf(position), value);
    
    Param p = getParam(position);
    p.setInValue(value);
    p.setOutType(outType);
  }
  
  public void setParameter(int position, Object value)
  {
    addToQueryPlanHash(String.valueOf(position), value);
    
    Param p = getParam(position);
    p.setInValue(value);
  }
  
  public void registerOut(int position, int outType)
  {
    Param p = getParam(position);
    p.setOutType(outType);
  }
  
  private Param getParam(String name)
  {
    Param p = (Param)this.namedParameters.get(name);
    if (p == null)
    {
      p = new Param();
      this.namedParameters.put(name, p);
    }
    return p;
  }
  
  private Param getParam(int position)
  {
    int more = position - this.positionedParameters.size();
    if (more > 0) {
      for (int i = 0; i < more; i++) {
        this.positionedParameters.add(new Param());
      }
    }
    return (Param)this.positionedParameters.get(position - 1);
  }
  
  public void setParameter(String name, Object value, int outType)
  {
    addToQueryPlanHash(name, value);
    
    Param p = getParam(name);
    p.setInValue(value);
    p.setOutType(outType);
  }
  
  public void setNullParameter(String name, int jdbcType)
  {
    Param p = getParam(name);
    p.setInNullType(jdbcType);
  }
  
  public Param setParameter(String name, Object value)
  {
    addToQueryPlanHash(name, value);
    
    Param p = getParam(name);
    p.setInValue(value);
    return p;
  }
  
  private void addToQueryPlanHash(String name, Object value)
  {
    if ((value != null) && 
      ((value instanceof Collection)))
    {
      this.queryPlanHash = (this.queryPlanHash * 31 + name.hashCode());
      this.queryPlanHash = (this.queryPlanHash * 31 + ((Collection)value).size());
    }
  }
  
  public int getQueryPlanHash()
  {
    return this.queryPlanHash;
  }
  
  public Param setEncryptionKey(String name, Object value)
  {
    Param p = getParam(name);
    p.setEncryptionKey(value);
    return p;
  }
  
  public void registerOut(String name, int outType)
  {
    Param p = getParam(name);
    p.setOutType(outType);
  }
  
  public Param getParameter(int position)
  {
    return getParam(position);
  }
  
  public Param getParameter(String name)
  {
    return getParam(name);
  }
  
  public List<Param> positionedParameters()
  {
    return this.positionedParameters;
  }
  
  public void setPreparedSql(String preparedSql)
  {
    this.preparedSql = preparedSql;
  }
  
  public String getPreparedSql()
  {
    return this.preparedSql;
  }
  
  public static final class OrderedList
  {
    final List<BindParams.Param> paramList;
    final StringBuilder preparedSql;
    
    public OrderedList()
    {
      this(new ArrayList());
    }
    
    public OrderedList(List<BindParams.Param> paramList)
    {
      this.paramList = paramList;
      this.preparedSql = new StringBuilder();
    }
    
    public void add(BindParams.Param param)
    {
      this.paramList.add(param);
    }
    
    public int size()
    {
      return this.paramList.size();
    }
    
    public List<BindParams.Param> list()
    {
      return this.paramList;
    }
    
    public void appendSql(String parsedSql)
    {
      this.preparedSql.append(parsedSql);
    }
    
    public String getPreparedSql()
    {
      return this.preparedSql.toString();
    }
  }
  
  public static final class Param
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    private boolean encryptionKey;
    private boolean isInParam;
    private boolean isOutParam;
    private int type;
    private Object inValue;
    private Object outValue;
    private int textLocation;
    
    public Param copy()
    {
      Param copy = new Param();
      copy.isInParam = this.isInParam;
      copy.isOutParam = this.isOutParam;
      copy.type = this.type;
      copy.inValue = this.inValue;
      copy.outValue = this.outValue;
      return copy;
    }
    
    public int hashCode()
    {
      int hc = getClass().hashCode();
      hc = hc * 31 + (this.isInParam ? 0 : 1);
      hc = hc * 31 + (this.isOutParam ? 0 : 1);
      hc = hc * 31 + this.type;
      hc = hc * 31 + (this.inValue == null ? 0 : this.inValue.hashCode());
      return hc;
    }
    
    public boolean equals(Object o)
    {
      if (o == null) {
        return false;
      }
      if (o == this) {
        return true;
      }
      if ((o instanceof Param)) {
        return hashCode() == o.hashCode();
      }
      return false;
    }
    
    public boolean isInParam()
    {
      return this.isInParam;
    }
    
    public boolean isOutParam()
    {
      return this.isOutParam;
    }
    
    public int getType()
    {
      return this.type;
    }
    
    public void setOutType(int type)
    {
      this.type = type;
      this.isOutParam = true;
    }
    
    public void setInValue(Object in)
    {
      this.inValue = in;
      this.isInParam = true;
    }
    
    public void setEncryptionKey(Object in)
    {
      this.inValue = in;
      this.isInParam = true;
      this.encryptionKey = true;
    }
    
    public void setInNullType(int type)
    {
      this.type = type;
      this.inValue = null;
      this.isInParam = true;
    }
    
    public Object getOutValue()
    {
      return this.outValue;
    }
    
    public Object getInValue()
    {
      return this.inValue;
    }
    
    public void setOutValue(Object out)
    {
      this.outValue = out;
    }
    
    public int getTextLocation()
    {
      return this.textLocation;
    }
    
    public void setTextLocation(int textLocation)
    {
      this.textLocation = textLocation;
    }
    
    public boolean isEncryptionKey()
    {
      return this.encryptionKey;
    }
  }
}
