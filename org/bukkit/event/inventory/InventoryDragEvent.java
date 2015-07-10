package org.bukkit.event.inventory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.Validate;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class InventoryDragEvent
  extends InventoryInteractEvent
{
  private static final HandlerList handlers = new HandlerList();
  private final DragType type;
  private final Map<Integer, ItemStack> addedItems;
  private final Set<Integer> containerSlots;
  private final ItemStack oldCursor;
  private ItemStack newCursor;
  
  public InventoryDragEvent(InventoryView what, ItemStack newCursor, ItemStack oldCursor, boolean right, Map<Integer, ItemStack> slots)
  {
    super(what);
    
    Validate.notNull(oldCursor);
    Validate.notNull(slots);
    
    this.type = (right ? DragType.SINGLE : DragType.EVEN);
    this.newCursor = newCursor;
    this.oldCursor = oldCursor;
    this.addedItems = slots;
    ImmutableSet.Builder<Integer> b = ImmutableSet.builder();
    for (Integer slot : slots.keySet()) {
      b.add(Integer.valueOf(what.convertSlot(slot.intValue())));
    }
    this.containerSlots = b.build();
  }
  
  public Map<Integer, ItemStack> getNewItems()
  {
    return Collections.unmodifiableMap(this.addedItems);
  }
  
  public Set<Integer> getRawSlots()
  {
    return this.addedItems.keySet();
  }
  
  public Set<Integer> getInventorySlots()
  {
    return this.containerSlots;
  }
  
  public ItemStack getCursor()
  {
    return this.newCursor;
  }
  
  public void setCursor(ItemStack newCursor)
  {
    this.newCursor = newCursor;
  }
  
  public ItemStack getOldCursor()
  {
    return this.oldCursor.clone();
  }
  
  public DragType getType()
  {
    return this.type;
  }
  
  public HandlerList getHandlers()
  {
    return handlers;
  }
  
  public static HandlerList getHandlerList()
  {
    return handlers;
  }
}
