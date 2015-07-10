package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class PacketPlayOutStatistic
  implements Packet<PacketListenerPlayOut>
{
  private Map<Statistic, Integer> a;
  
  public PacketPlayOutStatistic() {}
  
  public PacketPlayOutStatistic(Map<Statistic, Integer> ☃)
  {
    this.a = ☃;
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    int ☃ = ☃.e();
    this.a = Maps.newHashMap();
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      Statistic ☃ = StatisticList.getStatistic(☃.c(32767));
      int ☃ = ☃.e();
      if (☃ != null) {
        this.a.put(☃, Integer.valueOf(☃));
      }
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a.size());
    for (Map.Entry<Statistic, Integer> ☃ : this.a.entrySet())
    {
      ☃.a(((Statistic)☃.getKey()).name);
      ☃.b(((Integer)☃.getValue()).intValue());
    }
  }
}
