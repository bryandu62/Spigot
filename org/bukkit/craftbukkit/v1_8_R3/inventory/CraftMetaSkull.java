package org.bukkit.craftbukkit.v1_8_R3.inventory;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.authlib.GameProfile;
import java.util.Map;
import net.minecraft.server.v1_8_R3.GameProfileSerializer;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.TileEntitySkull;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.SkullMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
class CraftMetaSkull
  extends CraftMetaItem
  implements SkullMeta
{
  static final CraftMetaItem.ItemMetaKey SKULL_PROFILE = new CraftMetaItem.ItemMetaKey("SkullProfile");
  static final CraftMetaItem.ItemMetaKey SKULL_OWNER = new CraftMetaItem.ItemMetaKey("SkullOwner", "skull-owner");
  static final int MAX_OWNER_LENGTH = 16;
  private GameProfile profile;
  
  CraftMetaSkull(CraftMetaItem meta)
  {
    super(meta);
    if (!(meta instanceof CraftMetaSkull)) {
      return;
    }
    CraftMetaSkull skullMeta = (CraftMetaSkull)meta;
    this.profile = skullMeta.profile;
  }
  
  CraftMetaSkull(NBTTagCompound tag)
  {
    super(tag);
    if (tag.hasKeyOfType(SKULL_OWNER.NBT, 10)) {
      this.profile = GameProfileSerializer.deserialize(tag.getCompound(SKULL_OWNER.NBT));
    } else if ((tag.hasKeyOfType(SKULL_OWNER.NBT, 8)) && (!tag.getString(SKULL_OWNER.NBT).isEmpty())) {
      this.profile = new GameProfile(null, tag.getString(SKULL_OWNER.NBT));
    }
  }
  
  CraftMetaSkull(Map<String, Object> map)
  {
    super(map);
    if (this.profile == null) {
      setOwner(CraftMetaItem.SerializableMeta.getString(map, SKULL_OWNER.BUKKIT, true));
    }
  }
  
  void deserializeInternal(NBTTagCompound tag)
  {
    if (tag.hasKeyOfType(SKULL_PROFILE.NBT, 10)) {
      this.profile = GameProfileSerializer.deserialize(tag.getCompound(SKULL_PROFILE.NBT));
    }
  }
  
  void serializeInternal(Map<String, NBTBase> internalTags)
  {
    if (this.profile != null)
    {
      NBTTagCompound nbtData = new NBTTagCompound();
      GameProfileSerializer.serialize(nbtData, this.profile);
      internalTags.put(SKULL_PROFILE.NBT, nbtData);
    }
  }
  
  void applyToItem(final NBTTagCompound tag)
  {
    super.applyToItem(tag);
    if (hasOwner())
    {
      NBTTagCompound owner = new NBTTagCompound();
      GameProfileSerializer.serialize(owner, this.profile);
      tag.set(SKULL_OWNER.NBT, owner);
      
      TileEntitySkull.b(this.profile, new Predicate()
      {
        public boolean apply(GameProfile input)
        {
          NBTTagCompound owner = new NBTTagCompound();
          GameProfileSerializer.serialize(owner, input);
          tag.set(CraftMetaSkull.SKULL_OWNER.NBT, owner);
          return false;
        }
      });
    }
  }
  
  boolean isEmpty()
  {
    return (super.isEmpty()) && (isSkullEmpty());
  }
  
  boolean isSkullEmpty()
  {
    return !hasOwner();
  }
  
  boolean applicableTo(Material type)
  {
    switch (type)
    {
    case STONE_BUTTON: 
      return true;
    }
    return false;
  }
  
  public CraftMetaSkull clone()
  {
    return (CraftMetaSkull)super.clone();
  }
  
  public boolean hasOwner()
  {
    return this.profile != null;
  }
  
  public String getOwner()
  {
    return hasOwner() ? this.profile.getName() : null;
  }
  
  public boolean setOwner(String name)
  {
    if ((name != null) && (name.length() > 16)) {
      return false;
    }
    if (name == null) {
      this.profile = null;
    } else {
      this.profile = new GameProfile(null, name);
    }
    return true;
  }
  
  int applyHash()
  {
    int original;
    int hash = original = super.applyHash();
    if (hasOwner()) {
      hash = 61 * hash + this.profile.hashCode();
    }
    return original != hash ? CraftMetaSkull.class.hashCode() ^ hash : hash;
  }
  
  boolean equalsCommon(CraftMetaItem meta)
  {
    if (!super.equalsCommon(meta)) {
      return false;
    }
    if ((meta instanceof CraftMetaSkull))
    {
      CraftMetaSkull that = (CraftMetaSkull)meta;
      
      return (that.hasOwner()) && (this.profile.equals(that.profile));
    }
    return true;
  }
  
  boolean notUncommon(CraftMetaItem meta)
  {
    return (super.notUncommon(meta)) && (((meta instanceof CraftMetaSkull)) || (isSkullEmpty()));
  }
  
  ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder)
  {
    super.serialize(builder);
    if (hasOwner()) {
      return builder.put(SKULL_OWNER.BUKKIT, this.profile.getName());
    }
    return builder;
  }
}
