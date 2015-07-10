package net.minecraft.server.v1_8_R3;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class WorldGenMineshaft
  extends StructureGenerator
{
  private double d = 0.004D;
  
  public WorldGenMineshaft() {}
  
  public String a()
  {
    return "Mineshaft";
  }
  
  public WorldGenMineshaft(Map<String, String> ☃)
  {
    for (Map.Entry<String, String> ☃ : ☃.entrySet()) {
      if (((String)☃.getKey()).equals("chance")) {
        this.d = MathHelper.a((String)☃.getValue(), this.d);
      }
    }
  }
  
  protected boolean a(int ☃, int ☃)
  {
    return (this.b.nextDouble() < this.d) && (this.b.nextInt(80) < Math.max(Math.abs(☃), Math.abs(☃)));
  }
  
  protected StructureStart b(int ☃, int ☃)
  {
    return new WorldGenMineshaftStart(this.c, this.b, ☃, ☃);
  }
}
