package net.minecraft.server.v1_8_R3;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Iterator;
import java.util.Map;

public class RegistryMaterials<K, V>
  extends RegistrySimple<K, V>
  implements Registry<V>
{
  protected final RegistryID<V> a = new RegistryID();
  protected final Map<V, K> b;
  
  public RegistryMaterials()
  {
    this.b = ((BiMap)this.c).inverse();
  }
  
  public void a(int ☃, K ☃, V ☃)
  {
    this.a.a(☃, ☃);
    a(☃, ☃);
  }
  
  protected Map<K, V> b()
  {
    return HashBiMap.create();
  }
  
  public V get(K ☃)
  {
    return (V)super.get(☃);
  }
  
  public K c(V ☃)
  {
    return (K)this.b.get(☃);
  }
  
  public boolean d(K ☃)
  {
    return super.d(☃);
  }
  
  public int b(V ☃)
  {
    return this.a.b(☃);
  }
  
  public V a(int ☃)
  {
    return (V)this.a.a(☃);
  }
  
  public Iterator<V> iterator()
  {
    return this.a.iterator();
  }
}
