package net.minecraft.server.v1_8_R3;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum EnumProtocol
{
  private static int e;
  private static int f;
  private static final EnumProtocol[] g;
  private static final Map<Class<? extends Packet>, EnumProtocol> h;
  private final int i;
  private final Map<EnumProtocolDirection, BiMap<Integer, Class<? extends Packet>>> j = Maps.newEnumMap(EnumProtocolDirection.class);
  
  private EnumProtocol(int ☃)
  {
    this.i = ☃;
  }
  
  protected EnumProtocol a(EnumProtocolDirection ☃, Class<? extends Packet> ☃)
  {
    BiMap<Integer, Class<? extends Packet>> ☃ = (BiMap)this.j.get(☃);
    if (☃ == null)
    {
      ☃ = HashBiMap.create();
      this.j.put(☃, ☃);
    }
    if (☃.containsValue(☃))
    {
      String ☃ = ☃ + " packet " + ☃ + " is already known to ID " + ☃.inverse().get(☃);
      LogManager.getLogger().fatal(☃);
      throw new IllegalArgumentException(☃);
    }
    ☃.put(Integer.valueOf(☃.size()), ☃);
    return this;
  }
  
  public Integer a(EnumProtocolDirection ☃, Packet ☃)
  {
    return (Integer)((BiMap)this.j.get(☃)).inverse().get(☃.getClass());
  }
  
  public Packet a(EnumProtocolDirection ☃, int ☃)
    throws IllegalAccessException, InstantiationException
  {
    Class<? extends Packet> ☃ = (Class)((BiMap)this.j.get(☃)).get(Integer.valueOf(☃));
    if (☃ == null) {
      return null;
    }
    return (Packet)☃.newInstance();
  }
  
  public int a()
  {
    return this.i;
  }
  
  static
  {
    e = -1;
    f = 2;
    g = new EnumProtocol[f - e + 1];
    h = Maps.newHashMap();
    EnumProtocol ☃;
    for (☃ : values())
    {
      int ☃ = ☃.a();
      if ((☃ < e) || (☃ > f)) {
        throw new Error("Invalid protocol ID " + Integer.toString(☃));
      }
      g[(☃ - e)] = ☃;
      for (EnumProtocolDirection ☃ : ☃.j.keySet()) {
        for (Class<? extends Packet> ☃ : ((BiMap)☃.j.get(☃)).values())
        {
          if ((h.containsKey(☃)) && (h.get(☃) != ☃)) {
            throw new Error("Packet " + ☃ + " is already assigned to protocol " + h.get(☃) + " - can't reassign to " + ☃);
          }
          try
          {
            ☃.newInstance();
          }
          catch (Throwable ☃)
          {
            throw new Error("Packet " + ☃ + " fails instantiation checks! " + ☃);
          }
          h.put(☃, ☃);
        }
      }
    }
  }
  
  public static EnumProtocol a(int ☃)
  {
    if ((☃ < e) || (☃ > f)) {
      return null;
    }
    return g[(☃ - e)];
  }
  
  public static EnumProtocol a(Packet ☃)
  {
    return (EnumProtocol)h.get(☃.getClass());
  }
}
