package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;

public class NBTTagCompound
  extends NBTBase
{
  private Map<String, NBTBase> map = Maps.newHashMap();
  
  void write(DataOutput ☃)
    throws IOException
  {
    for (String ☃ : this.map.keySet())
    {
      NBTBase ☃ = (NBTBase)this.map.get(☃);
      a(☃, ☃, ☃);
    }
    ☃.writeByte(0);
  }
  
  void load(DataInput ☃, int ☃, NBTReadLimiter ☃)
    throws IOException
  {
    ☃.a(384L);
    if (☃ > 512) {
      throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
    }
    this.map.clear();
    byte ☃;
    while ((☃ = a(☃, ☃)) != 0)
    {
      String ☃ = b(☃, ☃);
      ☃.a(224 + 16 * ☃.length());
      
      NBTBase ☃ = a(☃, ☃, ☃, ☃ + 1, ☃);
      if (this.map.put(☃, ☃) != null) {
        ☃.a(288L);
      }
    }
  }
  
  public Set<String> c()
  {
    return this.map.keySet();
  }
  
  public byte getTypeId()
  {
    return 10;
  }
  
  public void set(String ☃, NBTBase ☃)
  {
    this.map.put(☃, ☃);
  }
  
  public void setByte(String ☃, byte ☃)
  {
    this.map.put(☃, new NBTTagByte(☃));
  }
  
  public void setShort(String ☃, short ☃)
  {
    this.map.put(☃, new NBTTagShort(☃));
  }
  
  public void setInt(String ☃, int ☃)
  {
    this.map.put(☃, new NBTTagInt(☃));
  }
  
  public void setLong(String ☃, long ☃)
  {
    this.map.put(☃, new NBTTagLong(☃));
  }
  
  public void setFloat(String ☃, float ☃)
  {
    this.map.put(☃, new NBTTagFloat(☃));
  }
  
  public void setDouble(String ☃, double ☃)
  {
    this.map.put(☃, new NBTTagDouble(☃));
  }
  
  public void setString(String ☃, String ☃)
  {
    this.map.put(☃, new NBTTagString(☃));
  }
  
  public void setByteArray(String ☃, byte[] ☃)
  {
    this.map.put(☃, new NBTTagByteArray(☃));
  }
  
  public void setIntArray(String ☃, int[] ☃)
  {
    this.map.put(☃, new NBTTagIntArray(☃));
  }
  
  public void setBoolean(String ☃, boolean ☃)
  {
    setByte(☃, (byte)(☃ ? 1 : 0));
  }
  
  public NBTBase get(String ☃)
  {
    return (NBTBase)this.map.get(☃);
  }
  
  public byte b(String ☃)
  {
    NBTBase ☃ = (NBTBase)this.map.get(☃);
    if (☃ != null) {
      return ☃.getTypeId();
    }
    return 0;
  }
  
  public boolean hasKey(String ☃)
  {
    return this.map.containsKey(☃);
  }
  
  public boolean hasKeyOfType(String ☃, int ☃)
  {
    int ☃ = b(☃);
    if (☃ == ☃) {
      return true;
    }
    if (☃ == 99) {
      return (☃ == 1) || (☃ == 2) || (☃ == 3) || (☃ == 4) || (☃ == 5) || (☃ == 6);
    }
    if (☃ > 0) {}
    return false;
  }
  
  public byte getByte(String ☃)
  {
    try
    {
      if (!hasKeyOfType(☃, 99)) {
        return 0;
      }
      return ((NBTBase.NBTNumber)this.map.get(☃)).f();
    }
    catch (ClassCastException ☃) {}
    return 0;
  }
  
  public short getShort(String ☃)
  {
    try
    {
      if (!hasKeyOfType(☃, 99)) {
        return 0;
      }
      return ((NBTBase.NBTNumber)this.map.get(☃)).e();
    }
    catch (ClassCastException ☃) {}
    return 0;
  }
  
  public int getInt(String ☃)
  {
    try
    {
      if (!hasKeyOfType(☃, 99)) {
        return 0;
      }
      return ((NBTBase.NBTNumber)this.map.get(☃)).d();
    }
    catch (ClassCastException ☃) {}
    return 0;
  }
  
  public long getLong(String ☃)
  {
    try
    {
      if (!hasKeyOfType(☃, 99)) {
        return 0L;
      }
      return ((NBTBase.NBTNumber)this.map.get(☃)).c();
    }
    catch (ClassCastException ☃) {}
    return 0L;
  }
  
  public float getFloat(String ☃)
  {
    try
    {
      if (!hasKeyOfType(☃, 99)) {
        return 0.0F;
      }
      return ((NBTBase.NBTNumber)this.map.get(☃)).h();
    }
    catch (ClassCastException ☃) {}
    return 0.0F;
  }
  
  public double getDouble(String ☃)
  {
    try
    {
      if (!hasKeyOfType(☃, 99)) {
        return 0.0D;
      }
      return ((NBTBase.NBTNumber)this.map.get(☃)).g();
    }
    catch (ClassCastException ☃) {}
    return 0.0D;
  }
  
  public String getString(String ☃)
  {
    try
    {
      if (!hasKeyOfType(☃, 8)) {
        return "";
      }
      return ((NBTBase)this.map.get(☃)).a_();
    }
    catch (ClassCastException ☃) {}
    return "";
  }
  
  public byte[] getByteArray(String ☃)
  {
    try
    {
      if (!hasKeyOfType(☃, 7)) {
        return new byte[0];
      }
      return ((NBTTagByteArray)this.map.get(☃)).c();
    }
    catch (ClassCastException ☃)
    {
      throw new ReportedException(a(☃, 7, ☃));
    }
  }
  
  public int[] getIntArray(String ☃)
  {
    try
    {
      if (!hasKeyOfType(☃, 11)) {
        return new int[0];
      }
      return ((NBTTagIntArray)this.map.get(☃)).c();
    }
    catch (ClassCastException ☃)
    {
      throw new ReportedException(a(☃, 11, ☃));
    }
  }
  
  public NBTTagCompound getCompound(String ☃)
  {
    try
    {
      if (!hasKeyOfType(☃, 10)) {
        return new NBTTagCompound();
      }
      return (NBTTagCompound)this.map.get(☃);
    }
    catch (ClassCastException ☃)
    {
      throw new ReportedException(a(☃, 10, ☃));
    }
  }
  
  public NBTTagList getList(String ☃, int ☃)
  {
    try
    {
      if (b(☃) != 9) {
        return new NBTTagList();
      }
      NBTTagList ☃ = (NBTTagList)this.map.get(☃);
      if ((☃.size() > 0) && (☃.f() != ☃)) {
        return new NBTTagList();
      }
      return ☃;
    }
    catch (ClassCastException ☃)
    {
      throw new ReportedException(a(☃, 9, ☃));
    }
  }
  
  public boolean getBoolean(String ☃)
  {
    return getByte(☃) != 0;
  }
  
  public void remove(String ☃)
  {
    this.map.remove(☃);
  }
  
  public String toString()
  {
    StringBuilder ☃ = new StringBuilder("{");
    for (Map.Entry<String, NBTBase> ☃ : this.map.entrySet())
    {
      if (☃.length() != 1) {
        ☃.append(',');
      }
      ☃.append((String)☃.getKey()).append(':').append(☃.getValue());
    }
    return '}';
  }
  
  public boolean isEmpty()
  {
    return this.map.isEmpty();
  }
  
  private CrashReport a(final String ☃, final int ☃, ClassCastException ☃)
  {
    CrashReport ☃ = CrashReport.a(☃, "Reading NBT data");
    CrashReportSystemDetails ☃ = ☃.a("Corrupt NBT tag", 1);
    
    ☃.a("Tag type found", new Callable()
    {
      public String a()
        throws Exception
      {
        return NBTBase.a[((NBTBase)NBTTagCompound.b(NBTTagCompound.this).get(☃)).getTypeId()];
      }
    });
    ☃.a("Tag type expected", new Callable()
    {
      public String a()
        throws Exception
      {
        return NBTBase.a[☃];
      }
    });
    ☃.a("Tag name", ☃);
    
    return ☃;
  }
  
  public NBTBase clone()
  {
    NBTTagCompound ☃ = new NBTTagCompound();
    for (String ☃ : this.map.keySet()) {
      ☃.set(☃, ((NBTBase)this.map.get(☃)).clone());
    }
    return ☃;
  }
  
  public boolean equals(Object ☃)
  {
    if (super.equals(☃))
    {
      NBTTagCompound ☃ = (NBTTagCompound)☃;
      return this.map.entrySet().equals(☃.map.entrySet());
    }
    return false;
  }
  
  public int hashCode()
  {
    return super.hashCode() ^ this.map.hashCode();
  }
  
  private static void a(String ☃, NBTBase ☃, DataOutput ☃)
    throws IOException
  {
    ☃.writeByte(☃.getTypeId());
    if (☃.getTypeId() == 0) {
      return;
    }
    ☃.writeUTF(☃);
    
    ☃.write(☃);
  }
  
  private static byte a(DataInput ☃, NBTReadLimiter ☃)
    throws IOException
  {
    return ☃.readByte();
  }
  
  private static String b(DataInput ☃, NBTReadLimiter ☃)
    throws IOException
  {
    return ☃.readUTF();
  }
  
  static NBTBase a(byte ☃, String ☃, DataInput ☃, int ☃, NBTReadLimiter ☃)
    throws IOException
  {
    NBTBase ☃ = NBTBase.createTag(☃);
    try
    {
      ☃.load(☃, ☃, ☃);
    }
    catch (IOException ☃)
    {
      CrashReport ☃ = CrashReport.a(☃, "Loading NBT data");
      CrashReportSystemDetails ☃ = ☃.a("NBT Tag");
      ☃.a("Tag name", ☃);
      ☃.a("Tag type", Byte.valueOf(☃));
      throw new ReportedException(☃);
    }
    return ☃;
  }
  
  public void a(NBTTagCompound ☃)
  {
    for (String ☃ : ☃.map.keySet())
    {
      NBTBase ☃ = (NBTBase)☃.map.get(☃);
      if (☃.getTypeId() == 10)
      {
        if (hasKeyOfType(☃, 10))
        {
          NBTTagCompound ☃ = getCompound(☃);
          ☃.a((NBTTagCompound)☃);
        }
        else
        {
          set(☃, ☃.clone());
        }
      }
      else {
        set(☃, ☃.clone());
      }
    }
  }
}
