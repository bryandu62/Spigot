package org.bukkit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Firework")
public final class FireworkEffect
  implements ConfigurationSerializable
{
  private static final String FLICKER = "flicker";
  private static final String TRAIL = "trail";
  private static final String COLORS = "colors";
  private static final String FADE_COLORS = "fade-colors";
  private static final String TYPE = "type";
  private final boolean flicker;
  private final boolean trail;
  private final ImmutableList<Color> colors;
  private final ImmutableList<Color> fadeColors;
  private final Type type;
  
  public static enum Type
  {
    BALL,  BALL_LARGE,  STAR,  BURST,  CREEPER;
  }
  
  public static Builder builder()
  {
    return new Builder();
  }
  
  public static final class Builder
  {
    boolean flicker = false;
    boolean trail = false;
    final ImmutableList.Builder<Color> colors = ImmutableList.builder();
    ImmutableList.Builder<Color> fadeColors = null;
    FireworkEffect.Type type = FireworkEffect.Type.BALL;
    
    public Builder with(FireworkEffect.Type type)
      throws IllegalArgumentException
    {
      Validate.notNull(type, "Cannot have null type");
      this.type = type;
      return this;
    }
    
    public Builder withFlicker()
    {
      this.flicker = true;
      return this;
    }
    
    public Builder flicker(boolean flicker)
    {
      this.flicker = flicker;
      return this;
    }
    
    public Builder withTrail()
    {
      this.trail = true;
      return this;
    }
    
    public Builder trail(boolean trail)
    {
      this.trail = trail;
      return this;
    }
    
    public Builder withColor(Color color)
      throws IllegalArgumentException
    {
      Validate.notNull(color, "Cannot have null color");
      
      this.colors.add(color);
      
      return this;
    }
    
    public Builder withColor(Color... colors)
      throws IllegalArgumentException
    {
      Validate.notNull(colors, "Cannot have null colors");
      if (colors.length == 0) {
        return this;
      }
      ImmutableList.Builder<Color> list = this.colors;
      Color[] arrayOfColor;
      int i = (arrayOfColor = colors).length;
      for (int j = 0; j < i; j++)
      {
        Color color = arrayOfColor[j];
        Validate.notNull(color, "Color cannot be null");
        list.add(color);
      }
      return this;
    }
    
    public Builder withColor(Iterable<?> colors)
      throws IllegalArgumentException
    {
      Validate.notNull(colors, "Cannot have null colors");
      
      ImmutableList.Builder<Color> list = this.colors;
      for (Object color : colors)
      {
        if (!(color instanceof Color)) {
          throw new IllegalArgumentException(color + " is not a Color in " + colors);
        }
        list.add((Color)color);
      }
      return this;
    }
    
    public Builder withFade(Color color)
      throws IllegalArgumentException
    {
      Validate.notNull(color, "Cannot have null color");
      if (this.fadeColors == null) {
        this.fadeColors = ImmutableList.builder();
      }
      this.fadeColors.add(color);
      
      return this;
    }
    
    public Builder withFade(Color... colors)
      throws IllegalArgumentException
    {
      Validate.notNull(colors, "Cannot have null colors");
      if (colors.length == 0) {
        return this;
      }
      ImmutableList.Builder<Color> list = this.fadeColors;
      if (list == null) {
        list = this.fadeColors = ImmutableList.builder();
      }
      Color[] arrayOfColor;
      int i = (arrayOfColor = colors).length;
      for (int j = 0; j < i; j++)
      {
        Color color = arrayOfColor[j];
        Validate.notNull(color, "Color cannot be null");
        list.add(color);
      }
      return this;
    }
    
    public Builder withFade(Iterable<?> colors)
      throws IllegalArgumentException
    {
      Validate.notNull(colors, "Cannot have null colors");
      
      ImmutableList.Builder<Color> list = this.fadeColors;
      if (list == null) {
        list = this.fadeColors = ImmutableList.builder();
      }
      for (Object color : colors)
      {
        if (!(color instanceof Color)) {
          throw new IllegalArgumentException(color + " is not a Color in " + colors);
        }
        list.add((Color)color);
      }
      return this;
    }
    
    public FireworkEffect build()
    {
      return new FireworkEffect(
        this.flicker, 
        this.trail, 
        this.colors.build(), 
        this.fadeColors == null ? ImmutableList.of() : this.fadeColors.build(), 
        this.type);
    }
  }
  
  private String string = null;
  
  FireworkEffect(boolean flicker, boolean trail, ImmutableList<Color> colors, ImmutableList<Color> fadeColors, Type type)
  {
    if (colors.isEmpty()) {
      throw new IllegalStateException("Cannot make FireworkEffect without any color");
    }
    this.flicker = flicker;
    this.trail = trail;
    this.colors = colors;
    this.fadeColors = fadeColors;
    this.type = type;
  }
  
  public boolean hasFlicker()
  {
    return this.flicker;
  }
  
  public boolean hasTrail()
  {
    return this.trail;
  }
  
  public List<Color> getColors()
  {
    return this.colors;
  }
  
  public List<Color> getFadeColors()
  {
    return this.fadeColors;
  }
  
  public Type getType()
  {
    return this.type;
  }
  
  public static ConfigurationSerializable deserialize(Map<String, Object> map)
  {
    Type type = Type.valueOf((String)map.get("type"));
    if (type == null) {
      throw new IllegalArgumentException(map.get("type") + " is not a valid Type");
    }
    return 
    
      builder().flicker(((Boolean)map.get("flicker")).booleanValue()).trail(((Boolean)map.get("trail")).booleanValue()).withColor((Iterable)map.get("colors")).withFade((Iterable)map.get("fade-colors")).with(type).build();
  }
  
  public Map<String, Object> serialize()
  {
    return ImmutableMap.of(
      "flicker", Boolean.valueOf(this.flicker), 
      "trail", Boolean.valueOf(this.trail), 
      "colors", this.colors, 
      "fade-colors", this.fadeColors, 
      "type", this.type.name());
  }
  
  public String toString()
  {
    String string = this.string;
    if (string == null) {
      return this.string = "FireworkEffect:" + serialize();
    }
    return string;
  }
  
  public int hashCode()
  {
    int hash = 1;
    hash = hash * 31 + (this.flicker ? 1231 : 1237);
    hash = hash * 31 + (this.trail ? 1231 : 1237);
    hash = hash * 31 + this.type.hashCode();
    hash = hash * 31 + this.colors.hashCode();
    hash = hash * 31 + this.fadeColors.hashCode();
    return hash;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof FireworkEffect)) {
      return false;
    }
    FireworkEffect that = (FireworkEffect)obj;
    return (this.flicker == that.flicker) && 
      (this.trail == that.trail) && 
      (this.type == that.type) && 
      (this.colors.equals(that.colors)) && 
      (this.fadeColors.equals(that.fadeColors));
  }
}
