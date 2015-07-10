package net.minecraft.server.v1_8_R3;

public class RegistryDefault<K, V>
  extends RegistrySimple<K, V>
{
  private final V a;
  
  public RegistryDefault(V ☃)
  {
    this.a = ☃;
  }
  
  public V get(K ☃)
  {
    V ☃ = super.get(☃);
    return (V)(☃ == null ? this.a : ☃);
  }
}
