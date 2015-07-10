package org.bukkit.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event.Result;
import org.bukkit.inventory.InventoryView;

public abstract class InventoryInteractEvent
  extends InventoryEvent
  implements Cancellable
{
  private Event.Result result = Event.Result.DEFAULT;
  
  public InventoryInteractEvent(InventoryView transaction)
  {
    super(transaction);
  }
  
  public HumanEntity getWhoClicked()
  {
    return getView().getPlayer();
  }
  
  public void setResult(Event.Result newResult)
  {
    this.result = newResult;
  }
  
  public Event.Result getResult()
  {
    return this.result;
  }
  
  public boolean isCancelled()
  {
    return getResult() == Event.Result.DENY;
  }
  
  public void setCancelled(boolean toCancel)
  {
    setResult(toCancel ? Event.Result.DENY : Event.Result.ALLOW);
  }
}
