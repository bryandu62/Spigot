package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.server.v1_8_R3.InventoryMerchant;
import org.bukkit.inventory.MerchantInventory;

public class CraftInventoryMerchant
  extends CraftInventory
  implements MerchantInventory
{
  public CraftInventoryMerchant(InventoryMerchant merchant)
  {
    super(merchant);
  }
}
