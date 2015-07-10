package net.minecraft.server.v1_8_R3;

public abstract interface PacketListenerPlayOut
  extends PacketListener
{
  public abstract void a(PacketPlayOutSpawnEntity paramPacketPlayOutSpawnEntity);
  
  public abstract void a(PacketPlayOutSpawnEntityExperienceOrb paramPacketPlayOutSpawnEntityExperienceOrb);
  
  public abstract void a(PacketPlayOutSpawnEntityWeather paramPacketPlayOutSpawnEntityWeather);
  
  public abstract void a(PacketPlayOutSpawnEntityLiving paramPacketPlayOutSpawnEntityLiving);
  
  public abstract void a(PacketPlayOutScoreboardObjective paramPacketPlayOutScoreboardObjective);
  
  public abstract void a(PacketPlayOutSpawnEntityPainting paramPacketPlayOutSpawnEntityPainting);
  
  public abstract void a(PacketPlayOutNamedEntitySpawn paramPacketPlayOutNamedEntitySpawn);
  
  public abstract void a(PacketPlayOutAnimation paramPacketPlayOutAnimation);
  
  public abstract void a(PacketPlayOutStatistic paramPacketPlayOutStatistic);
  
  public abstract void a(PacketPlayOutBlockBreakAnimation paramPacketPlayOutBlockBreakAnimation);
  
  public abstract void a(PacketPlayOutOpenSignEditor paramPacketPlayOutOpenSignEditor);
  
  public abstract void a(PacketPlayOutTileEntityData paramPacketPlayOutTileEntityData);
  
  public abstract void a(PacketPlayOutBlockAction paramPacketPlayOutBlockAction);
  
  public abstract void a(PacketPlayOutBlockChange paramPacketPlayOutBlockChange);
  
  public abstract void a(PacketPlayOutChat paramPacketPlayOutChat);
  
  public abstract void a(PacketPlayOutTabComplete paramPacketPlayOutTabComplete);
  
  public abstract void a(PacketPlayOutMultiBlockChange paramPacketPlayOutMultiBlockChange);
  
  public abstract void a(PacketPlayOutMap paramPacketPlayOutMap);
  
  public abstract void a(PacketPlayOutTransaction paramPacketPlayOutTransaction);
  
  public abstract void a(PacketPlayOutCloseWindow paramPacketPlayOutCloseWindow);
  
  public abstract void a(PacketPlayOutWindowItems paramPacketPlayOutWindowItems);
  
  public abstract void a(PacketPlayOutOpenWindow paramPacketPlayOutOpenWindow);
  
  public abstract void a(PacketPlayOutWindowData paramPacketPlayOutWindowData);
  
  public abstract void a(PacketPlayOutSetSlot paramPacketPlayOutSetSlot);
  
  public abstract void a(PacketPlayOutCustomPayload paramPacketPlayOutCustomPayload);
  
  public abstract void a(PacketPlayOutKickDisconnect paramPacketPlayOutKickDisconnect);
  
  public abstract void a(PacketPlayOutBed paramPacketPlayOutBed);
  
  public abstract void a(PacketPlayOutEntityStatus paramPacketPlayOutEntityStatus);
  
  public abstract void a(PacketPlayOutAttachEntity paramPacketPlayOutAttachEntity);
  
  public abstract void a(PacketPlayOutExplosion paramPacketPlayOutExplosion);
  
  public abstract void a(PacketPlayOutGameStateChange paramPacketPlayOutGameStateChange);
  
  public abstract void a(PacketPlayOutKeepAlive paramPacketPlayOutKeepAlive);
  
  public abstract void a(PacketPlayOutMapChunk paramPacketPlayOutMapChunk);
  
  public abstract void a(PacketPlayOutMapChunkBulk paramPacketPlayOutMapChunkBulk);
  
  public abstract void a(PacketPlayOutWorldEvent paramPacketPlayOutWorldEvent);
  
  public abstract void a(PacketPlayOutLogin paramPacketPlayOutLogin);
  
  public abstract void a(PacketPlayOutEntity paramPacketPlayOutEntity);
  
  public abstract void a(PacketPlayOutPosition paramPacketPlayOutPosition);
  
  public abstract void a(PacketPlayOutWorldParticles paramPacketPlayOutWorldParticles);
  
  public abstract void a(PacketPlayOutAbilities paramPacketPlayOutAbilities);
  
  public abstract void a(PacketPlayOutPlayerInfo paramPacketPlayOutPlayerInfo);
  
  public abstract void a(PacketPlayOutEntityDestroy paramPacketPlayOutEntityDestroy);
  
  public abstract void a(PacketPlayOutRemoveEntityEffect paramPacketPlayOutRemoveEntityEffect);
  
  public abstract void a(PacketPlayOutRespawn paramPacketPlayOutRespawn);
  
  public abstract void a(PacketPlayOutEntityHeadRotation paramPacketPlayOutEntityHeadRotation);
  
  public abstract void a(PacketPlayOutHeldItemSlot paramPacketPlayOutHeldItemSlot);
  
  public abstract void a(PacketPlayOutScoreboardDisplayObjective paramPacketPlayOutScoreboardDisplayObjective);
  
  public abstract void a(PacketPlayOutEntityMetadata paramPacketPlayOutEntityMetadata);
  
  public abstract void a(PacketPlayOutEntityVelocity paramPacketPlayOutEntityVelocity);
  
  public abstract void a(PacketPlayOutEntityEquipment paramPacketPlayOutEntityEquipment);
  
  public abstract void a(PacketPlayOutExperience paramPacketPlayOutExperience);
  
  public abstract void a(PacketPlayOutUpdateHealth paramPacketPlayOutUpdateHealth);
  
  public abstract void a(PacketPlayOutScoreboardTeam paramPacketPlayOutScoreboardTeam);
  
  public abstract void a(PacketPlayOutScoreboardScore paramPacketPlayOutScoreboardScore);
  
  public abstract void a(PacketPlayOutSpawnPosition paramPacketPlayOutSpawnPosition);
  
  public abstract void a(PacketPlayOutUpdateTime paramPacketPlayOutUpdateTime);
  
  public abstract void a(PacketPlayOutUpdateSign paramPacketPlayOutUpdateSign);
  
  public abstract void a(PacketPlayOutNamedSoundEffect paramPacketPlayOutNamedSoundEffect);
  
  public abstract void a(PacketPlayOutCollect paramPacketPlayOutCollect);
  
  public abstract void a(PacketPlayOutEntityTeleport paramPacketPlayOutEntityTeleport);
  
  public abstract void a(PacketPlayOutUpdateAttributes paramPacketPlayOutUpdateAttributes);
  
  public abstract void a(PacketPlayOutEntityEffect paramPacketPlayOutEntityEffect);
  
  public abstract void a(PacketPlayOutCombatEvent paramPacketPlayOutCombatEvent);
  
  public abstract void a(PacketPlayOutServerDifficulty paramPacketPlayOutServerDifficulty);
  
  public abstract void a(PacketPlayOutCamera paramPacketPlayOutCamera);
  
  public abstract void a(PacketPlayOutWorldBorder paramPacketPlayOutWorldBorder);
  
  public abstract void a(PacketPlayOutTitle paramPacketPlayOutTitle);
  
  public abstract void a(PacketPlayOutSetCompression paramPacketPlayOutSetCompression);
  
  public abstract void a(PacketPlayOutPlayerListHeaderFooter paramPacketPlayOutPlayerListHeaderFooter);
  
  public abstract void a(PacketPlayOutResourcePackSend paramPacketPlayOutResourcePackSend);
  
  public abstract void a(PacketPlayOutUpdateEntityNBT paramPacketPlayOutUpdateEntityNBT);
}
