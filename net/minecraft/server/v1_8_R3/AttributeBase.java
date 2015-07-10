package net.minecraft.server.v1_8_R3;

public abstract class AttributeBase
  implements IAttribute
{
  private final IAttribute a;
  private final String b;
  private final double c;
  private boolean d;
  
  protected AttributeBase(IAttribute ☃, String ☃, double ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
    if (☃ == null) {
      throw new IllegalArgumentException("Name cannot be null!");
    }
  }
  
  public String getName()
  {
    return this.b;
  }
  
  public double b()
  {
    return this.c;
  }
  
  public boolean c()
  {
    return this.d;
  }
  
  public AttributeBase a(boolean ☃)
  {
    this.d = ☃;
    return this;
  }
  
  public IAttribute d()
  {
    return this.a;
  }
  
  public int hashCode()
  {
    return this.b.hashCode();
  }
  
  public boolean equals(Object ☃)
  {
    return ((☃ instanceof IAttribute)) && (this.b.equals(((IAttribute)☃).getName()));
  }
}
