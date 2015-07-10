package net.minecraft.server.v1_8_R3;

import org.apache.commons.lang3.Validate;

public class RegistryBlocks<K, V>
  extends RegistryMaterials<K, V>
{
  private final K d;
  private V e;
  
  public RegistryBlocks(K ☃)
  {
    this.d = ☃;
  }
  
  public void a(int ☃, K ☃, V ☃)
  {
    if (this.d.equals(☃)) {
      this.e = ☃;
    }
    super.a(☃, ☃, ☃);
  }
  
  public void a()
  {
    Validate.notNull(this.d);
  }
  
  public V get(K ☃)
  {
    V ☃ = super.get(☃);
    return (V)(☃ == null ? this.e : ☃);
  }
  
  public V a(int ☃)
  {
    V ☃ = super.a(☃);
    return (V)(☃ == null ? this.e : ☃);
  }
}
