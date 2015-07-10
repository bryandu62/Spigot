package net.minecraft.server.v1_8_R3;

import java.util.List;

public class TileEntityBanner
  extends TileEntity
{
  public int color;
  public NBTTagList patterns;
  private boolean g;
  private List<EnumBannerPatternType> h;
  private List<EnumColor> i;
  private String j;
  
  public static enum EnumBannerPatternType
  {
    private String N;
    private String O;
    private String[] P = new String[3];
    private ItemStack Q;
    
    private EnumBannerPatternType(String ☃, String ☃)
    {
      this.N = ☃;
      this.O = ☃;
    }
    
    private EnumBannerPatternType(String ☃, String ☃, ItemStack ☃)
    {
      this(☃, ☃);
      this.Q = ☃;
    }
    
    private EnumBannerPatternType(String ☃, String ☃, String ☃, String ☃, String ☃)
    {
      this(☃, ☃);
      this.P[0] = ☃;
      this.P[1] = ☃;
      this.P[2] = ☃;
    }
    
    public String b()
    {
      return this.O;
    }
    
    public String[] c()
    {
      return this.P;
    }
    
    public boolean d()
    {
      return (this.Q != null) || (this.P[0] != null);
    }
    
    public boolean e()
    {
      return this.Q != null;
    }
    
    public ItemStack f()
    {
      return this.Q;
    }
  }
  
  public void a(ItemStack ☃)
  {
    this.patterns = null;
    if ((☃.hasTag()) && (☃.getTag().hasKeyOfType("BlockEntityTag", 10)))
    {
      NBTTagCompound ☃ = ☃.getTag().getCompound("BlockEntityTag");
      if (☃.hasKey("Patterns")) {
        this.patterns = ((NBTTagList)☃.getList("Patterns", 10).clone());
      }
      if (☃.hasKeyOfType("Base", 99)) {
        this.color = ☃.getInt("Base");
      } else {
        this.color = (☃.getData() & 0xF);
      }
    }
    else
    {
      this.color = (☃.getData() & 0xF);
    }
    this.h = null;
    this.i = null;
    this.j = "";
    this.g = true;
  }
  
  public void b(NBTTagCompound ☃)
  {
    super.b(☃);
    
    a(☃, this.color, this.patterns);
  }
  
  public static void a(NBTTagCompound ☃, int ☃, NBTTagList ☃)
  {
    ☃.setInt("Base", ☃);
    if (☃ != null) {
      ☃.set("Patterns", ☃);
    }
  }
  
  public void a(NBTTagCompound ☃)
  {
    super.a(☃);
    
    this.color = ☃.getInt("Base");
    this.patterns = ☃.getList("Patterns", 10);
    
    this.h = null;
    this.i = null;
    this.j = null;
    this.g = true;
  }
  
  public Packet getUpdatePacket()
  {
    NBTTagCompound ☃ = new NBTTagCompound();
    b(☃);
    return new PacketPlayOutTileEntityData(this.position, 6, ☃);
  }
  
  public int b()
  {
    return this.color;
  }
  
  public static int b(ItemStack ☃)
  {
    NBTTagCompound ☃ = ☃.a("BlockEntityTag", false);
    if ((☃ != null) && (☃.hasKey("Base"))) {
      return ☃.getInt("Base");
    }
    return ☃.getData();
  }
  
  public static int c(ItemStack ☃)
  {
    NBTTagCompound ☃ = ☃.a("BlockEntityTag", false);
    if ((☃ != null) && (☃.hasKey("Patterns"))) {
      return ☃.getList("Patterns", 10).size();
    }
    return 0;
  }
  
  public NBTTagList d()
  {
    return this.patterns;
  }
  
  public static void e(ItemStack ☃)
  {
    NBTTagCompound ☃ = ☃.a("BlockEntityTag", false);
    if ((☃ == null) || (!☃.hasKeyOfType("Patterns", 9))) {
      return;
    }
    NBTTagList ☃ = ☃.getList("Patterns", 10);
    if (☃.size() <= 0) {
      return;
    }
    ☃.a(☃.size() - 1);
    if (☃.isEmpty())
    {
      ☃.getTag().remove("BlockEntityTag");
      if (☃.getTag().isEmpty()) {
        ☃.setTag(null);
      }
    }
  }
}
