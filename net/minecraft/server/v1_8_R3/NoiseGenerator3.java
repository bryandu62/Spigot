package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class NoiseGenerator3
  extends NoiseGenerator
{
  private NoiseGenerator3Handler[] a;
  private int b;
  
  public NoiseGenerator3(Random ☃, int ☃)
  {
    this.b = ☃;
    this.a = new NoiseGenerator3Handler[☃];
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      this.a[☃] = new NoiseGenerator3Handler(☃);
    }
  }
  
  public double a(double ☃, double ☃)
  {
    double ☃ = 0.0D;
    double ☃ = 1.0D;
    for (int ☃ = 0; ☃ < this.b; ☃++)
    {
      ☃ += this.a[☃].a(☃ * ☃, ☃ * ☃) / ☃;
      ☃ /= 2.0D;
    }
    return ☃;
  }
  
  public double[] a(double[] ☃, double ☃, double ☃, int ☃, int ☃, double ☃, double ☃, double ☃)
  {
    return a(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, 0.5D);
  }
  
  public double[] a(double[] ☃, double ☃, double ☃, int ☃, int ☃, double ☃, double ☃, double ☃, double ☃)
  {
    if ((☃ == null) || (☃.length < ☃ * ☃)) {
      ☃ = new double[☃ * ☃];
    } else {
      for (int ☃ = 0; ☃ < ☃.length; ☃++) {
        ☃[☃] = 0.0D;
      }
    }
    double ☃ = 1.0D;
    double ☃ = 1.0D;
    for (int ☃ = 0; ☃ < this.b; ☃++)
    {
      this.a[☃].a(☃, ☃, ☃, ☃, ☃, ☃ * ☃ * ☃, ☃ * ☃ * ☃, 0.55D / ☃);
      ☃ *= ☃;
      ☃ *= ☃;
    }
    return ☃;
  }
}
