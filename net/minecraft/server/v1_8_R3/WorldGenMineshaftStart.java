package net.minecraft.server.v1_8_R3;

import java.util.LinkedList;
import java.util.Random;

public class WorldGenMineshaftStart
  extends StructureStart
{
  public WorldGenMineshaftStart() {}
  
  public WorldGenMineshaftStart(World ☃, Random ☃, int ☃, int ☃)
  {
    super(☃, ☃);
    
    WorldGenMineshaftPieces.WorldGenMineshaftRoom ☃ = new WorldGenMineshaftPieces.WorldGenMineshaftRoom(0, ☃, (☃ << 4) + 2, (☃ << 4) + 2);
    this.a.add(☃);
    ☃.a(☃, this.a, ☃);
    
    c();
    a(☃, ☃, 10);
  }
}
