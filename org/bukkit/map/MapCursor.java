package org.bukkit.map;

public final class MapCursor
{
  private byte x;
  private byte y;
  private byte direction;
  private byte type;
  private boolean visible;
  
  @Deprecated
  public MapCursor(byte x, byte y, byte direction, byte type, boolean visible)
  {
    this.x = x;
    this.y = y;
    setDirection(direction);
    setRawType(type);
    this.visible = visible;
  }
  
  public byte getX()
  {
    return this.x;
  }
  
  public byte getY()
  {
    return this.y;
  }
  
  public byte getDirection()
  {
    return this.direction;
  }
  
  public Type getType()
  {
    return Type.byValue(this.type);
  }
  
  @Deprecated
  public byte getRawType()
  {
    return this.type;
  }
  
  public boolean isVisible()
  {
    return this.visible;
  }
  
  public void setX(byte x)
  {
    this.x = x;
  }
  
  public void setY(byte y)
  {
    this.y = y;
  }
  
  public void setDirection(byte direction)
  {
    if ((direction < 0) || (direction > 15)) {
      throw new IllegalArgumentException("Direction must be in the range 0-15");
    }
    this.direction = direction;
  }
  
  public void setType(Type type)
  {
    setRawType(type.value);
  }
  
  @Deprecated
  public void setRawType(byte type)
  {
    if ((type < 0) || (type > 15)) {
      throw new IllegalArgumentException("Type must be in the range 0-15");
    }
    this.type = type;
  }
  
  public void setVisible(boolean visible)
  {
    this.visible = visible;
  }
  
  public static enum Type
  {
    WHITE_POINTER(0),  GREEN_POINTER(1),  RED_POINTER(2),  BLUE_POINTER(3),  WHITE_CROSS(4);
    
    private byte value;
    
    private Type(int value)
    {
      this.value = ((byte)value);
    }
    
    @Deprecated
    public byte getValue()
    {
      return this.value;
    }
    
    @Deprecated
    public static Type byValue(byte value)
    {
      Type[] arrayOfType;
      int i = (arrayOfType = values()).length;
      for (int j = 0; j < i; j++)
      {
        Type t = arrayOfType[j];
        if (t.value == value) {
          return t;
        }
      }
      return null;
    }
  }
}
