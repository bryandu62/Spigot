package net.minecraft.server.v1_8_R3;

public abstract interface PacketListenerPlayIn
  extends PacketListener
{
  public abstract void a(PacketPlayInArmAnimation paramPacketPlayInArmAnimation);
  
  public abstract void a(PacketPlayInChat paramPacketPlayInChat);
  
  public abstract void a(PacketPlayInTabComplete paramPacketPlayInTabComplete);
  
  public abstract void a(PacketPlayInClientCommand paramPacketPlayInClientCommand);
  
  public abstract void a(PacketPlayInSettings paramPacketPlayInSettings);
  
  public abstract void a(PacketPlayInTransaction paramPacketPlayInTransaction);
  
  public abstract void a(PacketPlayInEnchantItem paramPacketPlayInEnchantItem);
  
  public abstract void a(PacketPlayInWindowClick paramPacketPlayInWindowClick);
  
  public abstract void a(PacketPlayInCloseWindow paramPacketPlayInCloseWindow);
  
  public abstract void a(PacketPlayInCustomPayload paramPacketPlayInCustomPayload);
  
  public abstract void a(PacketPlayInUseEntity paramPacketPlayInUseEntity);
  
  public abstract void a(PacketPlayInKeepAlive paramPacketPlayInKeepAlive);
  
  public abstract void a(PacketPlayInFlying paramPacketPlayInFlying);
  
  public abstract void a(PacketPlayInAbilities paramPacketPlayInAbilities);
  
  public abstract void a(PacketPlayInBlockDig paramPacketPlayInBlockDig);
  
  public abstract void a(PacketPlayInEntityAction paramPacketPlayInEntityAction);
  
  public abstract void a(PacketPlayInSteerVehicle paramPacketPlayInSteerVehicle);
  
  public abstract void a(PacketPlayInHeldItemSlot paramPacketPlayInHeldItemSlot);
  
  public abstract void a(PacketPlayInSetCreativeSlot paramPacketPlayInSetCreativeSlot);
  
  public abstract void a(PacketPlayInUpdateSign paramPacketPlayInUpdateSign);
  
  public abstract void a(PacketPlayInBlockPlace paramPacketPlayInBlockPlace);
  
  public abstract void a(PacketPlayInSpectate paramPacketPlayInSpectate);
  
  public abstract void a(PacketPlayInResourcePackStatus paramPacketPlayInResourcePackStatus);
}
