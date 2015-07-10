package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.util.Map;

public class ChatClickable
{
  private final EnumClickAction a;
  private final String b;
  
  public ChatClickable(EnumClickAction ☃, String ☃)
  {
    this.a = ☃;
    this.b = ☃;
  }
  
  public EnumClickAction a()
  {
    return this.a;
  }
  
  public String b()
  {
    return this.b;
  }
  
  public boolean equals(Object ☃)
  {
    if (this == ☃) {
      return true;
    }
    if ((☃ == null) || (getClass() != ☃.getClass())) {
      return false;
    }
    ChatClickable ☃ = (ChatClickable)☃;
    if (this.a != ☃.a) {
      return false;
    }
    if (this.b != null ? !this.b.equals(☃.b) : ☃.b != null) {
      return false;
    }
    return true;
  }
  
  public String toString()
  {
    return "ClickEvent{action=" + this.a + ", value='" + this.b + '\'' + '}';
  }
  
  public int hashCode()
  {
    int ☃ = this.a.hashCode();
    ☃ = 31 * ☃ + (this.b != null ? this.b.hashCode() : 0);
    return ☃;
  }
  
  public static enum EnumClickAction
  {
    private static final Map<String, EnumClickAction> g;
    private final boolean h;
    private final String i;
    
    private EnumClickAction(String ☃, boolean ☃)
    {
      this.i = ☃;
      this.h = ☃;
    }
    
    public boolean a()
    {
      return this.h;
    }
    
    public String b()
    {
      return this.i;
    }
    
    static
    {
      g = Maps.newHashMap();
      for (EnumClickAction ☃ : values()) {
        g.put(☃.b(), ☃);
      }
    }
    
    public static EnumClickAction a(String ☃)
    {
      return (EnumClickAction)g.get(☃);
    }
  }
}
