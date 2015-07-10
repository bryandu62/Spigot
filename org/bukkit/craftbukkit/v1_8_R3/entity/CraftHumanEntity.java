package org.bukkit.craftbukkit.v1_8_R3.entity;

import java.util.Set;
import net.minecraft.server.v1_8_R3.BlockAnvil.TileEntityContainerAnvil;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.BlockWorkbench.TileEntityContainerWorkbench;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityMinecartHopper;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.ITileEntityContainer;
import net.minecraft.server.v1_8_R3.PacketPlayInCloseWindow;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.TileEntity;
import net.minecraft.server.v1_8_R3.TileEntityBeacon;
import net.minecraft.server.v1_8_R3.TileEntityBrewingStand;
import net.minecraft.server.v1_8_R3.TileEntityDispenser;
import net.minecraft.server.v1_8_R3.TileEntityEnchantTable;
import net.minecraft.server.v1_8_R3.TileEntityFurnace;
import net.minecraft.server.v1_8_R3.TileEntityHopper;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftContainer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class CraftHumanEntity
  extends CraftLivingEntity
  implements HumanEntity
{
  private CraftInventoryPlayer inventory;
  private final CraftInventory enderChest;
  protected final PermissibleBase perm = new PermissibleBase(this);
  private boolean op;
  private GameMode mode;
  
  public CraftHumanEntity(CraftServer server, EntityHuman entity)
  {
    super(server, entity);
    this.mode = server.getDefaultGameMode();
    this.inventory = new CraftInventoryPlayer(entity.inventory);
    this.enderChest = new CraftInventory(entity.getEnderChest());
  }
  
  public String getName()
  {
    return getHandle().getName();
  }
  
  public org.bukkit.inventory.PlayerInventory getInventory()
  {
    return this.inventory;
  }
  
  public EntityEquipment getEquipment()
  {
    return this.inventory;
  }
  
  public Inventory getEnderChest()
  {
    return this.enderChest;
  }
  
  public org.bukkit.inventory.ItemStack getItemInHand()
  {
    return getInventory().getItemInHand();
  }
  
  public void setItemInHand(org.bukkit.inventory.ItemStack item)
  {
    getInventory().setItemInHand(item);
  }
  
  public org.bukkit.inventory.ItemStack getItemOnCursor()
  {
    return CraftItemStack.asCraftMirror(getHandle().inventory.getCarried());
  }
  
  public void setItemOnCursor(org.bukkit.inventory.ItemStack item)
  {
    net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
    getHandle().inventory.setCarried(stack);
    if ((this instanceof CraftPlayer)) {
      ((EntityPlayer)getHandle()).broadcastCarriedItem();
    }
  }
  
  public boolean isSleeping()
  {
    return getHandle().sleeping;
  }
  
  public int getSleepTicks()
  {
    return getHandle().sleepTicks;
  }
  
  public boolean isOp()
  {
    return this.op;
  }
  
  public boolean isPermissionSet(String name)
  {
    return this.perm.isPermissionSet(name);
  }
  
  public boolean isPermissionSet(Permission perm)
  {
    return this.perm.isPermissionSet(perm);
  }
  
  public boolean hasPermission(String name)
  {
    return this.perm.hasPermission(name);
  }
  
  public boolean hasPermission(Permission perm)
  {
    return this.perm.hasPermission(perm);
  }
  
  public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value)
  {
    return this.perm.addAttachment(plugin, name, value);
  }
  
  public PermissionAttachment addAttachment(Plugin plugin)
  {
    return this.perm.addAttachment(plugin);
  }
  
  public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks)
  {
    return this.perm.addAttachment(plugin, name, value, ticks);
  }
  
  public PermissionAttachment addAttachment(Plugin plugin, int ticks)
  {
    return this.perm.addAttachment(plugin, ticks);
  }
  
  public void removeAttachment(PermissionAttachment attachment)
  {
    this.perm.removeAttachment(attachment);
  }
  
  public void recalculatePermissions()
  {
    this.perm.recalculatePermissions();
  }
  
  public void setOp(boolean value)
  {
    this.op = value;
    this.perm.recalculatePermissions();
  }
  
  public Set<PermissionAttachmentInfo> getEffectivePermissions()
  {
    return this.perm.getEffectivePermissions();
  }
  
  public GameMode getGameMode()
  {
    return this.mode;
  }
  
  public void setGameMode(GameMode mode)
  {
    if (mode == null) {
      throw new IllegalArgumentException("Mode cannot be null");
    }
    this.mode = mode;
  }
  
  public EntityHuman getHandle()
  {
    return (EntityHuman)this.entity;
  }
  
  public void setHandle(EntityHuman entity)
  {
    super.setHandle(entity);
    this.inventory = new CraftInventoryPlayer(entity.inventory);
  }
  
  public String toString()
  {
    return "CraftHumanEntity{id=" + getEntityId() + "name=" + getName() + '}';
  }
  
  public InventoryView getOpenInventory()
  {
    return getHandle().activeContainer.getBukkitView();
  }
  
  public InventoryView openInventory(Inventory inventory)
  {
    if (!(getHandle() instanceof EntityPlayer)) {
      return null;
    }
    EntityPlayer player = (EntityPlayer)getHandle();
    InventoryType type = inventory.getType();
    Container formerContainer = getHandle().activeContainer;
    
    CraftInventory craftinv = (CraftInventory)inventory;
    switch (type)
    {
    case ANVIL: 
    case ENCHANTING: 
    case HOPPER: 
      getHandle().openContainer(craftinv.getInventory());
      break;
    case BEACON: 
      if ((craftinv.getInventory() instanceof TileEntityDispenser)) {
        getHandle().openContainer((TileEntityDispenser)craftinv.getInventory());
      } else {
        openCustomInventory(inventory, player, "minecraft:dispenser");
      }
      break;
    case CHEST: 
      if ((craftinv.getInventory() instanceof TileEntityFurnace)) {
        getHandle().openContainer((TileEntityFurnace)craftinv.getInventory());
      } else {
        openCustomInventory(inventory, player, "minecraft:furnace");
      }
      break;
    case CRAFTING: 
      openCustomInventory(inventory, player, "minecraft:crafting_table");
      break;
    case DROPPER: 
      if ((craftinv.getInventory() instanceof TileEntityBrewingStand)) {
        getHandle().openContainer((TileEntityBrewingStand)craftinv.getInventory());
      } else {
        openCustomInventory(inventory, player, "minecraft:brewing_stand");
      }
      break;
    case DISPENSER: 
      openCustomInventory(inventory, player, "minecraft:enchanting_table");
      break;
    case WORKBENCH: 
      if ((craftinv.getInventory() instanceof TileEntityHopper)) {
        getHandle().openContainer((TileEntityHopper)craftinv.getInventory());
      } else if ((craftinv.getInventory() instanceof EntityMinecartHopper)) {
        getHandle().openContainer((EntityMinecartHopper)craftinv.getInventory());
      } else {
        openCustomInventory(inventory, player, "minecraft:hopper");
      }
      break;
    case PLAYER: 
      if ((craftinv.getInventory() instanceof TileEntityBeacon)) {
        getHandle().openContainer((TileEntityBeacon)craftinv.getInventory());
      } else {
        openCustomInventory(inventory, player, "minecraft:beacon");
      }
      break;
    case MERCHANT: 
      if ((craftinv.getInventory() instanceof BlockAnvil.TileEntityContainerAnvil)) {
        getHandle().openTileEntity((BlockAnvil.TileEntityContainerAnvil)craftinv.getInventory());
      } else {
        openCustomInventory(inventory, player, "minecraft:anvil");
      }
      break;
    case CREATIVE: 
    case ENDER_CHEST: 
      throw new IllegalArgumentException("Can't open a " + type + " inventory!");
    }
    if (getHandle().activeContainer == formerContainer) {
      return null;
    }
    getHandle().activeContainer.checkReachable = false;
    return getHandle().activeContainer.getBukkitView();
  }
  
  private void openCustomInventory(Inventory inventory, EntityPlayer player, String windowType)
  {
    if (player.playerConnection == null) {
      return;
    }
    Container container = new CraftContainer(inventory, this, player.nextContainerCounter());
    
    container = CraftEventFactory.callInventoryOpenEvent(player, container);
    if (container == null) {
      return;
    }
    String title = container.getBukkitView().getTitle();
    int size = container.getBukkitView().getTopInventory().getSize();
    if ((windowType.equals("minecraft:crafting_table")) || 
      (windowType.equals("minecraft:anvil")) || 
      (windowType.equals("minecraft:enchanting_table"))) {
      size = 0;
    }
    player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(container.windowId, windowType, new ChatComponentText(title), size));
    getHandle().activeContainer = container;
    getHandle().activeContainer.addSlotListener(player);
  }
  
  public InventoryView openWorkbench(Location location, boolean force)
  {
    if (!force)
    {
      Block block = location.getBlock();
      if (block.getType() != Material.WORKBENCH) {
        return null;
      }
    }
    if (location == null) {
      location = getLocation();
    }
    getHandle().openTileEntity(new BlockWorkbench.TileEntityContainerWorkbench(getHandle().world, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ())));
    if (force) {
      getHandle().activeContainer.checkReachable = false;
    }
    return getHandle().activeContainer.getBukkitView();
  }
  
  public InventoryView openEnchanting(Location location, boolean force)
  {
    if (!force)
    {
      Block block = location.getBlock();
      if (block.getType() != Material.ENCHANTMENT_TABLE) {
        return null;
      }
    }
    if (location == null) {
      location = getLocation();
    }
    TileEntity container = getHandle().world.getTileEntity(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    if ((container == null) && (force)) {
      container = new TileEntityEnchantTable();
    }
    getHandle().openTileEntity((ITileEntityContainer)container);
    if (force) {
      getHandle().activeContainer.checkReachable = false;
    }
    return getHandle().activeContainer.getBukkitView();
  }
  
  public void openInventory(InventoryView inventory)
  {
    if (!(getHandle() instanceof EntityPlayer)) {
      return;
    }
    if (((EntityPlayer)getHandle()).playerConnection == null) {
      return;
    }
    if (getHandle().activeContainer != getHandle().defaultContainer) {
      ((EntityPlayer)getHandle()).playerConnection.a(new PacketPlayInCloseWindow(getHandle().activeContainer.windowId));
    }
    EntityPlayer player = (EntityPlayer)getHandle();
    Container container;
    if ((inventory instanceof CraftInventoryView)) {
      container = ((CraftInventoryView)inventory).getHandle();
    } else {
      container = new CraftContainer(inventory, player.nextContainerCounter());
    }
    Container container = CraftEventFactory.callInventoryOpenEvent(player, container);
    if (container == null) {
      return;
    }
    InventoryType type = inventory.getType();
    String windowType = CraftContainer.getNotchInventoryType(type);
    String title = inventory.getTitle();
    int size = inventory.getTopInventory().getSize();
    player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(container.windowId, windowType, new ChatComponentText(title), size));
    player.activeContainer = container;
    player.activeContainer.addSlotListener(player);
  }
  
  public void closeInventory()
  {
    getHandle().closeInventory();
  }
  
  public boolean isBlocking()
  {
    return getHandle().isBlocking();
  }
  
  public boolean setWindowProperty(InventoryView.Property prop, int value)
  {
    return false;
  }
  
  public int getExpToLevel()
  {
    return getHandle().getExpToLevel();
  }
}
