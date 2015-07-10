package org.bukkit.craftbukkit.v1_8_R3.metadata;

import java.util.UUID;
import org.bukkit.World;
import org.bukkit.metadata.MetadataStore;
import org.bukkit.metadata.MetadataStoreBase;

public class WorldMetadataStore
  extends MetadataStoreBase<World>
  implements MetadataStore<World>
{
  protected String disambiguate(World world, String metadataKey)
  {
    return world.getUID().toString() + ":" + metadataKey;
  }
}
