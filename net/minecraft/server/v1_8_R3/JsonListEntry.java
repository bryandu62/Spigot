package net.minecraft.server.v1_8_R3;

import com.google.gson.JsonObject;

public class JsonListEntry<T>
{
  private final T a;
  
  public JsonListEntry(T ☃)
  {
    this.a = ☃;
  }
  
  protected JsonListEntry(T ☃, JsonObject ☃)
  {
    this.a = ☃;
  }
  
  public T getKey()
  {
    return (T)this.a;
  }
  
  boolean hasExpired()
  {
    return false;
  }
  
  protected void a(JsonObject ☃) {}
}