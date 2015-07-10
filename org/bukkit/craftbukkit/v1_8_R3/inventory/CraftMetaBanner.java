package org.bukkit.craftbukkit.v1_8_R3.inventory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.BannerMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaBanner
  extends CraftMetaItem
  implements BannerMeta
{
  static final CraftMetaItem.ItemMetaKey BASE = new CraftMetaItem.ItemMetaKey("Base", "base-color");
  static final CraftMetaItem.ItemMetaKey PATTERNS = new CraftMetaItem.ItemMetaKey("Patterns", "patterns");
  static final CraftMetaItem.ItemMetaKey COLOR = new CraftMetaItem.ItemMetaKey("Color", "color");
  static final CraftMetaItem.ItemMetaKey PATTERN = new CraftMetaItem.ItemMetaKey("Pattern", "pattern");
  private DyeColor base;
  private List<Pattern> patterns = new ArrayList();
  
  CraftMetaBanner(CraftMetaItem meta)
  {
    super(meta);
    if (!(meta instanceof CraftMetaBanner)) {
      return;
    }
    CraftMetaBanner banner = (CraftMetaBanner)meta;
    this.base = banner.base;
    this.patterns = new ArrayList(banner.patterns);
  }
  
  CraftMetaBanner(NBTTagCompound tag)
  {
    super(tag);
    if (!tag.hasKey("BlockEntityTag")) {
      return;
    }
    NBTTagCompound entityTag = tag.getCompound("BlockEntityTag");
    
    this.base = (entityTag.hasKey(BASE.NBT) ? DyeColor.getByDyeData((byte)entityTag.getInt(BASE.NBT)) : null);
    if (entityTag.hasKey(PATTERNS.NBT))
    {
      NBTTagList patterns = entityTag.getList(PATTERNS.NBT, 10);
      for (int i = 0; i < patterns.size(); i++)
      {
        NBTTagCompound p = patterns.get(i);
        this.patterns.add(new Pattern(DyeColor.getByDyeData((byte)p.getInt(COLOR.NBT)), PatternType.getByIdentifier(p.getString(PATTERN.NBT))));
      }
    }
  }
  
  CraftMetaBanner(Map<String, Object> map)
  {
    super(map);
    
    String baseStr = CraftMetaItem.SerializableMeta.getString(map, BASE.BUKKIT, true);
    if (baseStr != null) {
      this.base = DyeColor.valueOf(baseStr);
    }
    Iterable<?> rawPatternList = (Iterable)CraftMetaItem.SerializableMeta.getObject(Iterable.class, map, PATTERNS.BUKKIT, true);
    if (rawPatternList == null) {
      return;
    }
    for (Object obj : rawPatternList)
    {
      if (!(obj instanceof Pattern)) {
        throw new IllegalArgumentException("Object in pattern list is not valid. " + obj.getClass());
      }
      addPattern((Pattern)obj);
    }
  }
  
  void applyToItem(NBTTagCompound tag)
  {
    super.applyToItem(tag);
    
    NBTTagCompound entityTag = new NBTTagCompound();
    if (this.base != null) {
      entityTag.setInt(BASE.NBT, this.base.getDyeData());
    }
    NBTTagList newPatterns = new NBTTagList();
    for (Pattern p : this.patterns)
    {
      NBTTagCompound compound = new NBTTagCompound();
      compound.setInt(COLOR.NBT, p.getColor().getDyeData());
      compound.setString(PATTERN.NBT, p.getPattern().getIdentifier());
      newPatterns.add(compound);
    }
    entityTag.set(PATTERNS.NBT, newPatterns);
    
    tag.set("BlockEntityTag", entityTag);
  }
  
  public DyeColor getBaseColor()
  {
    return this.base;
  }
  
  public void setBaseColor(DyeColor color)
  {
    this.base = color;
  }
  
  public List<Pattern> getPatterns()
  {
    return new ArrayList(this.patterns);
  }
  
  public void setPatterns(List<Pattern> patterns)
  {
    this.patterns = new ArrayList(patterns);
  }
  
  public void addPattern(Pattern pattern)
  {
    this.patterns.add(pattern);
  }
  
  public Pattern getPattern(int i)
  {
    return (Pattern)this.patterns.get(i);
  }
  
  public Pattern removePattern(int i)
  {
    return (Pattern)this.patterns.remove(i);
  }
  
  public void setPattern(int i, Pattern pattern)
  {
    this.patterns.set(i, pattern);
  }
  
  public int numberOfPatterns()
  {
    return this.patterns.size();
  }
  
  ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder)
  {
    super.serialize(builder);
    if (this.base != null) {
      builder.put(BASE.BUKKIT, this.base.toString());
    }
    if (!this.patterns.isEmpty()) {
      builder.put(PATTERNS.BUKKIT, ImmutableList.copyOf(this.patterns));
    }
    return builder;
  }
  
  int applyHash()
  {
    int original;
    int hash = original = super.applyHash();
    if (this.base != null) {
      hash = 31 * hash + this.base.hashCode();
    }
    if (!this.patterns.isEmpty()) {
      hash = 31 * hash + this.patterns.hashCode();
    }
    return original != hash ? CraftMetaBanner.class.hashCode() ^ hash : hash;
  }
  
  public boolean equalsCommon(CraftMetaItem meta)
  {
    if (!super.equalsCommon(meta)) {
      return false;
    }
    if ((meta instanceof CraftMetaBanner))
    {
      CraftMetaBanner that = (CraftMetaBanner)meta;
      
      return (this.base == that.base) && (this.patterns.equals(that.patterns));
    }
    return true;
  }
  
  boolean notUncommon(CraftMetaItem meta)
  {
    return (super.notUncommon(meta)) && (((meta instanceof CraftMetaBanner)) || ((this.patterns.isEmpty()) && (this.base == null)));
  }
  
  boolean isEmpty()
  {
    return (super.isEmpty()) && (this.patterns.isEmpty()) && (this.base == null);
  }
  
  boolean applicableTo(Material type)
  {
    return type == Material.BANNER;
  }
}
