package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.util.Map;

public class ChatHoverable
{
  private final EnumHoverAction a;
  private final IChatBaseComponent b;
  
  public ChatHoverable(EnumHoverAction ☃, IChatBaseComponent ☃)
  {
    this.a = ☃;
    this.b = ☃;
  }
  
  public EnumHoverAction a()
  {
    return this.a;
  }
  
  public IChatBaseComponent b()
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
    ChatHoverable ☃ = (ChatHoverable)☃;
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
    return "HoverEvent{action=" + this.a + ", value='" + this.b + '\'' + '}';
  }
  
  public int hashCode()
  {
    int ☃ = this.a.hashCode();
    ☃ = 31 * ☃ + (this.b != null ? this.b.hashCode() : 0);
    return ☃;
  }
  
  public static enum EnumHoverAction
  {
    private static final Map<String, EnumHoverAction> e;
    private final boolean f;
    private final String g;
    
    private EnumHoverAction(String ☃, boolean ☃)
    {
      this.g = ☃;
      this.f = ☃;
    }
    
    public boolean a()
    {
      return this.f;
    }
    
    public String b()
    {
      return this.g;
    }
    
    static
    {
      e = Maps.newHashMap();
      for (EnumHoverAction ☃ : values()) {
        e.put(☃.b(), ☃);
      }
    }
    
    public static EnumHoverAction a(String ☃)
    {
      return (EnumHoverAction)e.get(☃);
    }
  }
}
