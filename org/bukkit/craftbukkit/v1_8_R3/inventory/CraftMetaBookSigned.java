package org.bukkit.craftbukkit.v1_8_R3.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.List;
import java.util.Map;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.BookMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
class CraftMetaBookSigned
  extends CraftMetaBook
  implements BookMeta
{
  CraftMetaBookSigned(CraftMetaItem meta)
  {
    super(meta);
  }
  
  CraftMetaBookSigned(NBTTagCompound tag)
  {
    super(tag, false);
    
    boolean resolved = true;
    if (tag.hasKey(RESOLVED.NBT)) {
      resolved = tag.getBoolean(RESOLVED.NBT);
    }
    if (tag.hasKey(BOOK_PAGES.NBT))
    {
      NBTTagList pages = tag.getList(BOOK_PAGES.NBT, 8);
      for (int i = 0; i < pages.size(); i++)
      {
        String page = pages.getString(i);
        if (resolved) {
          try
          {
            this.pages.add(IChatBaseComponent.ChatSerializer.a(page));
          }
          catch (Exception localException) {}
        } else {
          addPage(new String[] { page });
        }
      }
    }
  }
  
  CraftMetaBookSigned(Map<String, Object> map)
  {
    super(map);
  }
  
  void applyToItem(NBTTagCompound itemData)
  {
    super.applyToItem(itemData, false);
    if (hasTitle()) {
      itemData.setString(BOOK_TITLE.NBT, this.title);
    } else {
      itemData.setString(BOOK_TITLE.NBT, " ");
    }
    if (hasAuthor()) {
      itemData.setString(BOOK_AUTHOR.NBT, this.author);
    } else {
      itemData.setString(BOOK_AUTHOR.NBT, " ");
    }
    if (hasPages())
    {
      NBTTagList list = new NBTTagList();
      for (IChatBaseComponent page : this.pages) {
        list.add(new NBTTagString(
          IChatBaseComponent.ChatSerializer.a(page)));
      }
      itemData.set(BOOK_PAGES.NBT, list);
    }
    itemData.setBoolean(RESOLVED.NBT, true);
    if (this.generation != null) {
      itemData.setInt(GENERATION.NBT, this.generation.intValue());
    } else {
      itemData.setInt(GENERATION.NBT, 0);
    }
  }
  
  boolean isEmpty()
  {
    return super.isEmpty();
  }
  
  boolean applicableTo(Material type)
  {
    switch (type)
    {
    case SPRUCE_WOOD_STAIRS: 
    case STAINED_CLAY: 
      return true;
    }
    return false;
  }
  
  public CraftMetaBookSigned clone()
  {
    CraftMetaBookSigned meta = (CraftMetaBookSigned)super.clone();
    return meta;
  }
  
  int applyHash()
  {
    int original;
    int hash = original = super.applyHash();
    return original != hash ? CraftMetaBookSigned.class.hashCode() ^ hash : hash;
  }
  
  boolean equalsCommon(CraftMetaItem meta)
  {
    return super.equalsCommon(meta);
  }
  
  boolean notUncommon(CraftMetaItem meta)
  {
    return (super.notUncommon(meta)) && (((meta instanceof CraftMetaBookSigned)) || (isBookEmpty()));
  }
  
  ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder)
  {
    super.serialize(builder);
    return builder;
  }
}
