package org.bukkit.craftbukkit.v1_8_R3.inventory;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.server.v1_8_R3.BlockJukeBox.TileEntityRecordPlayer;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.TileEntity;
import net.minecraft.server.v1_8_R3.TileEntityBanner;
import net.minecraft.server.v1_8_R3.TileEntityBeacon;
import net.minecraft.server.v1_8_R3.TileEntityBrewingStand;
import net.minecraft.server.v1_8_R3.TileEntityChest;
import net.minecraft.server.v1_8_R3.TileEntityCommand;
import net.minecraft.server.v1_8_R3.TileEntityDispenser;
import net.minecraft.server.v1_8_R3.TileEntityDropper;
import net.minecraft.server.v1_8_R3.TileEntityFurnace;
import net.minecraft.server.v1_8_R3.TileEntityHopper;
import net.minecraft.server.v1_8_R3.TileEntityMobSpawner;
import net.minecraft.server.v1_8_R3.TileEntityNote;
import net.minecraft.server.v1_8_R3.TileEntitySign;
import net.minecraft.server.v1_8_R3.TileEntitySkull;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBanner;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBeacon;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBrewingStand;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftChest;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftCommandBlock;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftCreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftDispenser;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftDropper;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftFurnace;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftHopper;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftJukebox;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftNoteBlock;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftSign;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftSkull;
import org.bukkit.inventory.meta.BlockStateMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaBlockState
  extends CraftMetaItem
  implements BlockStateMeta
{
  static final CraftMetaItem.ItemMetaKey BLOCK_ENTITY_TAG = new CraftMetaItem.ItemMetaKey("BlockEntityTag");
  final Material material;
  NBTTagCompound blockEntityTag;
  
  CraftMetaBlockState(CraftMetaItem meta, Material material)
  {
    super(meta);
    this.material = material;
    if ((!(meta instanceof CraftMetaBlockState)) || 
      (((CraftMetaBlockState)meta).material != material) || 
      (material == Material.SIGN) || 
      (material == Material.COMMAND))
    {
      this.blockEntityTag = null;
      return;
    }
    CraftMetaBlockState te = (CraftMetaBlockState)meta;
    this.blockEntityTag = te.blockEntityTag;
  }
  
  CraftMetaBlockState(NBTTagCompound tag, Material material)
  {
    super(tag);
    this.material = material;
    if (tag.hasKeyOfType(BLOCK_ENTITY_TAG.NBT, 10)) {
      this.blockEntityTag = tag.getCompound(BLOCK_ENTITY_TAG.NBT);
    } else {
      this.blockEntityTag = null;
    }
  }
  
  CraftMetaBlockState(Map<String, Object> map)
  {
    super(map);
    String matName = CraftMetaItem.SerializableMeta.getString(map, "blockMaterial", true);
    Material m = Material.getMaterial(matName);
    if (m != null) {
      this.material = m;
    } else {
      this.material = Material.AIR;
    }
  }
  
  void applyToItem(NBTTagCompound tag)
  {
    super.applyToItem(tag);
    if (this.blockEntityTag != null) {
      tag.set(BLOCK_ENTITY_TAG.NBT, this.blockEntityTag);
    }
  }
  
  void deserializeInternal(NBTTagCompound tag)
  {
    if (tag.hasKeyOfType(BLOCK_ENTITY_TAG.NBT, 10)) {
      this.blockEntityTag = tag.getCompound(BLOCK_ENTITY_TAG.NBT);
    }
  }
  
  void serializeInternal(Map<String, NBTBase> internalTags)
  {
    if (this.blockEntityTag != null) {
      internalTags.put(BLOCK_ENTITY_TAG.NBT, this.blockEntityTag);
    }
  }
  
  ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder)
  {
    super.serialize(builder);
    builder.put("blockMaterial", this.material.name());
    return builder;
  }
  
  int applyHash()
  {
    int original;
    int hash = original = super.applyHash();
    if (this.blockEntityTag != null) {
      hash = 61 * hash + this.blockEntityTag.hashCode();
    }
    return original != hash ? CraftMetaBlockState.class.hashCode() ^ hash : hash;
  }
  
  public boolean equalsCommon(CraftMetaItem meta)
  {
    if (!super.equalsCommon(meta)) {
      return false;
    }
    if ((meta instanceof CraftMetaBlockState))
    {
      CraftMetaBlockState that = (CraftMetaBlockState)meta;
      
      return Objects.equal(this.blockEntityTag, that.blockEntityTag);
    }
    return true;
  }
  
  boolean notUncommon(CraftMetaItem meta)
  {
    return (super.notUncommon(meta)) && (((meta instanceof CraftMetaBlockState)) || (this.blockEntityTag == null));
  }
  
  boolean isEmpty()
  {
    return (super.isEmpty()) && (this.blockEntityTag == null);
  }
  
  boolean applicableTo(Material type)
  {
    switch (type)
    {
    case BLAZE_POWDER: 
    case BOAT: 
    case BREWING_STAND: 
    case CHAINMAIL_LEGGINGS: 
    case CLAY: 
    case COBBLESTONE_STAIRS: 
    case DIAMOND_AXE: 
    case ENDER_PORTAL_FRAME: 
    case GLASS_BOTTLE: 
    case GLOWING_REDSTONE_ORE: 
    case GOLD_BOOTS: 
    case GOLD_LEGGINGS: 
    case GOLD_PICKAXE: 
    case GOLD_SWORD: 
    case IRON_FENCE: 
    case QUARTZ_STAIRS: 
    case SPECKLED_MELON: 
    case STANDING_BANNER: 
    case STORAGE_MINECART: 
      return true;
    }
    return false;
  }
  
  public boolean hasBlockState()
  {
    return this.blockEntityTag != null;
  }
  
  public BlockState getBlockState()
  {
    TileEntity te = this.blockEntityTag == null ? null : TileEntity.c(this.blockEntityTag);
    switch (this.material)
    {
    case COCOA: 
    case COOKED_CHICKEN: 
    case QUARTZ_STAIRS: 
      if (te == null) {
        te = new TileEntitySign();
      }
      return new CraftSign(this.material, (TileEntitySign)te);
    case CLAY: 
    case GOLD_BOOTS: 
      if (te == null) {
        te = new TileEntityChest();
      }
      return new CraftChest(this.material, (TileEntityChest)te);
    case COBBLESTONE_STAIRS: 
    case COBBLE_WALL: 
      if (te == null) {
        te = new TileEntityFurnace();
      }
      return new CraftFurnace(this.material, (TileEntityFurnace)te);
    case BLAZE_POWDER: 
      if (te == null) {
        te = new TileEntityDispenser();
      }
      return new CraftDispenser(this.material, (TileEntityDispenser)te);
    case GOLD_SWORD: 
      if (te == null) {
        te = new TileEntityDispenser();
      }
      return new CraftDropper(this.material, (TileEntityDropper)te);
    case GOLD_PICKAXE: 
      if (te == null) {
        te = new TileEntityHopper();
      }
      return new CraftHopper(this.material, (TileEntityHopper)te);
    case CHAINMAIL_LEGGINGS: 
      if (te == null) {
        te = new TileEntityMobSpawner();
      }
      return new CraftCreatureSpawner(this.material, (TileEntityMobSpawner)te);
    case BOAT: 
      if (te == null) {
        te = new TileEntityNote();
      }
      return new CraftNoteBlock(this.material, (TileEntityNote)te);
    case DIAMOND_AXE: 
      if (te == null) {
        te = new BlockJukeBox.TileEntityRecordPlayer();
      }
      return new CraftJukebox(this.material, (BlockJukeBox.TileEntityRecordPlayer)te);
    case ENDER_STONE: 
      if (te == null) {
        te = new TileEntityBrewingStand();
      }
      return new CraftBrewingStand(this.material, (TileEntityBrewingStand)te);
    case GOLD_BARDING: 
      if (te == null) {
        te = new TileEntitySkull();
      }
      return new CraftSkull(this.material, (TileEntitySkull)te);
    case GLASS_BOTTLE: 
      if (te == null) {
        te = new TileEntityCommand();
      }
      return new CraftCommandBlock(this.material, (TileEntityCommand)te);
    case GLOWING_REDSTONE_ORE: 
      if (te == null) {
        te = new TileEntityBeacon();
      }
      return new CraftBeacon(this.material, (TileEntityBeacon)te);
    case IRON_DOOR: 
    case IRON_DOOR_BLOCK: 
    case WHEAT: 
      if (te == null) {
        te = new TileEntityBanner();
      }
      return new CraftBanner(this.material, (TileEntityBanner)te);
    }
    throw new IllegalStateException("Missing blockState for " + this.material);
  }
  
  public void setBlockState(BlockState blockState)
  {
    Validate.notNull(blockState, "blockState must not be null");
    TileEntity te = ((CraftBlockState)blockState).getTileEntity();
    Validate.notNull(te, "Invalid blockState");
    boolean valid;
    boolean valid;
    boolean valid;
    boolean valid;
    boolean valid;
    boolean valid;
    boolean valid;
    boolean valid;
    boolean valid;
    boolean valid;
    boolean valid;
    boolean valid;
    boolean valid;
    boolean valid;
    boolean valid;
    switch (this.material)
    {
    case COCOA: 
    case COOKED_CHICKEN: 
    case QUARTZ_STAIRS: 
      valid = te instanceof TileEntitySign;
      break;
    case CLAY: 
    case GOLD_BOOTS: 
      valid = te instanceof TileEntityChest;
      break;
    case COBBLESTONE_STAIRS: 
    case COBBLE_WALL: 
      valid = te instanceof TileEntityFurnace;
      break;
    case BLAZE_POWDER: 
      valid = te instanceof TileEntityDispenser;
      break;
    case GOLD_SWORD: 
      valid = te instanceof TileEntityDropper;
      break;
    case GOLD_PICKAXE: 
      valid = te instanceof TileEntityHopper;
      break;
    case CHAINMAIL_LEGGINGS: 
      valid = te instanceof TileEntityMobSpawner;
      break;
    case BOAT: 
      valid = te instanceof TileEntityNote;
      break;
    case DIAMOND_AXE: 
      valid = te instanceof BlockJukeBox.TileEntityRecordPlayer;
      break;
    case ENDER_STONE: 
      valid = te instanceof TileEntityBrewingStand;
      break;
    case GOLD_BARDING: 
      valid = te instanceof TileEntitySkull;
      break;
    case GLASS_BOTTLE: 
      valid = te instanceof TileEntityCommand;
      break;
    case GLOWING_REDSTONE_ORE: 
      valid = te instanceof TileEntityBeacon;
      break;
    case IRON_DOOR: 
    case IRON_DOOR_BLOCK: 
    case WHEAT: 
      valid = te instanceof TileEntityBanner;
      break;
    default: 
      valid = false;
    }
    Validate.isTrue(valid, "Invalid blockState for " + this.material);
    
    this.blockEntityTag = new NBTTagCompound();
    te.b(this.blockEntityTag);
  }
}
