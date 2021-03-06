package gnu.trove.decorator;

import gnu.trove.iterator.TDoubleLongIterator;
import gnu.trove.map.TDoubleLongMap;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TDoubleLongMapDecorator
  extends AbstractMap<Double, Long>
  implements Map<Double, Long>, Externalizable, Cloneable
{
  static final long serialVersionUID = 1L;
  protected TDoubleLongMap _map;
  
  public TDoubleLongMapDecorator() {}
  
  public TDoubleLongMapDecorator(TDoubleLongMap map)
  {
    this._map = map;
  }
  
  public TDoubleLongMap getMap()
  {
    return this._map;
  }
  
  public Long put(Double key, Long value)
  {
    double k;
    double k;
    if (key == null) {
      k = this._map.getNoEntryKey();
    } else {
      k = unwrapKey(key);
    }
    long v;
    long v;
    if (value == null) {
      v = this._map.getNoEntryValue();
    } else {
      v = unwrapValue(value);
    }
    long retval = this._map.put(k, v);
    if (retval == this._map.getNoEntryValue()) {
      return null;
    }
    return wrapValue(retval);
  }
  
  public Long get(Object key)
  {
    double k;
    if (key != null)
    {
      double k;
      if ((key instanceof Double)) {
        k = unwrapKey(key);
      } else {
        return null;
      }
    }
    else
    {
      k = this._map.getNoEntryKey();
    }
    long v = this._map.get(k);
    if (v == this._map.getNoEntryValue()) {
      return null;
    }
    return wrapValue(v);
  }
  
  public void clear()
  {
    this._map.clear();
  }
  
  public Long remove(Object key)
  {
    double k;
    if (key != null)
    {
      double k;
      if ((key instanceof Double)) {
        k = unwrapKey(key);
      } else {
        return null;
      }
    }
    else
    {
      k = this._map.getNoEntryKey();
    }
    long v = this._map.remove(k);
    if (v == this._map.getNoEntryValue()) {
      return null;
    }
    return wrapValue(v);
  }
  
  public Set<Map.Entry<Double, Long>> entrySet()
  {
    new AbstractSet()
    {
      public int size()
      {
        return TDoubleLongMapDecorator.this._map.size();
      }
      
      public boolean isEmpty()
      {
        return TDoubleLongMapDecorator.this.isEmpty();
      }
      
      public boolean contains(Object o)
      {
        if ((o instanceof Map.Entry))
        {
          Object k = ((Map.Entry)o).getKey();
          Object v = ((Map.Entry)o).getValue();
          return (TDoubleLongMapDecorator.this.containsKey(k)) && (TDoubleLongMapDecorator.this.get(k).equals(v));
        }
        return false;
      }
      
      public Iterator<Map.Entry<Double, Long>> iterator()
      {
        new Iterator()
        {
          private final TDoubleLongIterator it = TDoubleLongMapDecorator.this._map.iterator();
          
          public Map.Entry<Double, Long> next()
          {
            this.it.advance();
            double ik = this.it.key();
            final Double key = ik == TDoubleLongMapDecorator.this._map.getNoEntryKey() ? null : TDoubleLongMapDecorator.this.wrapKey(ik);
            long iv = this.it.value();
            final Long v = iv == TDoubleLongMapDecorator.this._map.getNoEntryValue() ? null : TDoubleLongMapDecorator.this.wrapValue(iv);
            new Map.Entry()
            {
              private Long val = v;
              
              public boolean equals(Object o)
              {
                return ((o instanceof Map.Entry)) && (((Map.Entry)o).getKey().equals(key)) && (((Map.Entry)o).getValue().equals(this.val));
              }
              
              public Double getKey()
              {
                return key;
              }
              
              public Long getValue()
              {
                return this.val;
              }
              
              public int hashCode()
              {
                return key.hashCode() + this.val.hashCode();
              }
              
              public Long setValue(Long value)
              {
                this.val = value;
                return TDoubleLongMapDecorator.this.put(key, value);
              }
            };
          }
          
          public boolean hasNext()
          {
            return this.it.hasNext();
          }
          
          public void remove()
          {
            this.it.remove();
          }
        };
      }
      
      public boolean add(Map.Entry<Double, Long> o)
      {
        throw new UnsupportedOperationException();
      }
      
      public boolean remove(Object o)
      {
        boolean modified = false;
        if (contains(o))
        {
          Double key = (Double)((Map.Entry)o).getKey();
          TDoubleLongMapDecorator.this._map.remove(TDoubleLongMapDecorator.this.unwrapKey(key));
          modified = true;
        }
        return modified;
      }
      
      public boolean addAll(Collection<? extends Map.Entry<Double, Long>> c)
      {
        throw new UnsupportedOperationException();
      }
      
      public void clear()
      {
        TDoubleLongMapDecorator.this.clear();
      }
    };
  }
  
  public boolean containsValue(Object val)
  {
    return ((val instanceof Long)) && (this._map.containsValue(unwrapValue(val)));
  }
  
  public boolean containsKey(Object key)
  {
    if (key == null) {
      return this._map.containsKey(this._map.getNoEntryKey());
    }
    return ((key instanceof Double)) && (this._map.containsKey(unwrapKey(key)));
  }
  
  public int size()
  {
    return this._map.size();
  }
  
  public boolean isEmpty()
  {
    return size() == 0;
  }
  
  public void putAll(Map<? extends Double, ? extends Long> map)
  {
    Iterator<? extends Map.Entry<? extends Double, ? extends Long>> it = map.entrySet().iterator();
    for (int i = map.size(); i-- > 0;)
    {
      Map.Entry<? extends Double, ? extends Long> e = (Map.Entry)it.next();
      put((Double)e.getKey(), (Long)e.getValue());
    }
  }
  
  protected Double wrapKey(double k)
  {
    return Double.valueOf(k);
  }
  
  protected double unwrapKey(Object key)
  {
    return ((Double)key).doubleValue();
  }
  
  protected Long wrapValue(long k)
  {
    return Long.valueOf(k);
  }
  
  protected long unwrapValue(Object value)
  {
    return ((Long)value).longValue();
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    
    this._map = ((TDoubleLongMap)in.readObject());
  }
  
  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(0);
    
    out.writeObject(this._map);
  }
}
