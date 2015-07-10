package org.bukkit.craftbukkit.v1_8_R3.inventory;

import java.util.List;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.Slot;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class CraftContainer
  extends Container
{
  private final InventoryView view;
  private InventoryType cachedType;
  private String cachedTitle;
  private final int cachedSize;
  
  public CraftContainer(InventoryView view, int id)
  {
    this.view = view;
    this.windowId = id;
    
    IInventory top = ((CraftInventory)view.getTopInventory()).getInventory();
    IInventory bottom = ((CraftInventory)view.getBottomInventory()).getInventory();
    this.cachedType = view.getType();
    this.cachedTitle = view.getTitle();
    this.cachedSize = getSize();
    setupSlots(top, bottom);
  }
  
  public CraftContainer(Inventory inventory, final HumanEntity player, int id)
  {
    this(new InventoryView()
    {
      public Inventory getTopInventory()
      {
        return CraftContainer.this;
      }
      
      public Inventory getBottomInventory()
      {
        return player.getInventory();
      }
      
      public HumanEntity getPlayer()
      {
        return player;
      }
      
      public InventoryType getType()
      {
        return CraftContainer.this.getType();
      }
    }, id);
  }
  
  public InventoryView getBukkitView()
  {
    return this.view;
  }
  
  private int getSize()
  {
    return this.view.getTopInventory().getSize();
  }
  
  public boolean c(EntityHuman entityhuman)
  {
    if ((this.cachedType == this.view.getType()) && (this.cachedSize == getSize()) && (this.cachedTitle.equals(this.view.getTitle()))) {
      return true;
    }
    boolean typeChanged = this.cachedType != this.view.getType();
    this.cachedType = this.view.getType();
    this.cachedTitle = this.view.getTitle();
    if ((this.view.getPlayer() instanceof CraftPlayer))
    {
      CraftPlayer player = (CraftPlayer)this.view.getPlayer();
      String type = getNotchInventoryType(this.cachedType);
      IInventory top = ((CraftInventory)this.view.getTopInventory()).getInventory();
      IInventory bottom = ((CraftInventory)this.view.getBottomInventory()).getInventory();
      this.b.clear();
      this.c.clear();
      if (typeChanged) {
        setupSlots(top, bottom);
      }
      int size = getSize();
      player.getHandle().playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.windowId, type, new ChatComponentText(this.cachedTitle), size));
      player.updateInventory();
    }
    return true;
  }
  
  public static String getNotchInventoryType(InventoryType type)
  {
    switch (type)
    {
    case CRAFTING: 
      return "minecraft:crafting_table";
    case CHEST: 
      return "minecraft:furnace";
    case BEACON: 
      return "minecraft:dispenser";
    case DISPENSER: 
      return "minecraft:enchanting_table";
    case DROPPER: 
      return "minecraft:brewing_stand";
    case PLAYER: 
      return "minecraft:beacon";
    case MERCHANT: 
      return "minecraft:anvil";
    case WORKBENCH: 
      return "minecraft:hopper";
    }
    return "minecraft:chest";
  }
  
  private void setupSlots(IInventory top, IInventory bottom)
  {
    switch (this.cachedType)
    {
    case ENDER_CHEST: 
      break;
    case ANVIL: 
    case ENCHANTING: 
      setupChest(top, bottom);
      break;
    case BEACON: 
      setupDispenser(top, bottom);
      break;
    case CHEST: 
      setupFurnace(top, bottom);
      break;
    case CRAFTING: 
    case CREATIVE: 
      setupWorkbench(top, bottom);
      break;
    case DISPENSER: 
      setupEnchanting(top, bottom);
      break;
    case DROPPER: 
      setupBrewing(top, bottom);
      break;
    case WORKBENCH: 
      setupHopper(top, bottom);
    }
  }
  
  private void setupChest(IInventory top, IInventory bottom)
  {
    int rows = top.getSize() / 9;
    
    int i = (rows - 4) * 18;
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < 9; col++) {
        a(new Slot(top, col + row * 9, 8 + col * 18, 18 + row * 18));
      }
    }
    for (row = 0; row < 3; row++) {
      for (int col = 0; col < 9; col++) {
        a(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 103 + row * 18 + i));
      }
    }
    for (int col = 0; col < 9; col++) {
      a(new Slot(bottom, col, 8 + col * 18, 161 + i));
    }
  }
  
  private void setupWorkbench(IInventory top, IInventory bottom)
  {
    a(new Slot(top, 0, 124, 35));
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        a(new Slot(top, 1 + col + row * 3, 30 + col * 18, 17 + row * 18));
      }
    }
    for (row = 0; row < 3; row++) {
      for (int col = 0; col < 9; col++) {
        a(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
      }
    }
    for (int col = 0; col < 9; col++) {
      a(new Slot(bottom, col, 8 + col * 18, 142));
    }
  }
  
  private void setupFurnace(IInventory top, IInventory bottom)
  {
    a(new Slot(top, 0, 56, 17));
    a(new Slot(top, 1, 56, 53));
    a(new Slot(top, 2, 116, 35));
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 9; col++) {
        a(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
      }
    }
    for (int col = 0; col < 9; col++) {
      a(new Slot(bottom, col, 8 + col * 18, 142));
    }
  }
  
  private void setupDispenser(IInventory top, IInventory bottom)
  {
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        a(new Slot(top, col + row * 3, 61 + col * 18, 17 + row * 18));
      }
    }
    for (row = 0; row < 3; row++) {
      for (int col = 0; col < 9; col++) {
        a(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
      }
    }
    for (int col = 0; col < 9; col++) {
      a(new Slot(bottom, col, 8 + col * 18, 142));
    }
  }
  
  private void setupEnchanting(IInventory top, IInventory bottom)
  {
    a(new Slot(top, 0, 25, 47));
    for (int row = 0; row < 3; row++) {
      for (int i1 = 0; i1 < 9; i1++) {
        a(new Slot(bottom, i1 + row * 9 + 9, 8 + i1 * 18, 84 + row * 18));
      }
    }
    for (row = 0; row < 9; row++) {
      a(new Slot(bottom, row, 8 + row * 18, 142));
    }
  }
  
  private void setupBrewing(IInventory top, IInventory bottom)
  {
    a(new Slot(top, 0, 56, 46));
    a(new Slot(top, 1, 79, 53));
    a(new Slot(top, 2, 102, 46));
    a(new Slot(top, 3, 79, 17));
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        a(new Slot(bottom, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }
    }
    for (i = 0; i < 9; i++) {
      a(new Slot(bottom, i, 8 + i * 18, 142));
    }
  }
  
  private void setupHopper(IInventory top, IInventory bottom)
  {
    byte b0 = 51;
    for (int i = 0; i < top.getSize(); i++) {
      a(new Slot(top, i, 44 + i * 18, 20));
    }
    for (i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        a(new Slot(bottom, j + i * 9 + 9, 8 + j * 18, i * 18 + b0));
      }
    }
    for (i = 0; i < 9; i++) {
      a(new Slot(bottom, i, 8 + i * 18, 58 + b0));
    }
  }
  
  public boolean a(EntityHuman entity)
  {
    return true;
  }
}
