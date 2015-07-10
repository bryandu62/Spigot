package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TileEntitySkull
  extends TileEntity
{
  private int a;
  private int rotation;
  private GameProfile g = null;
  public static final Executor executor = Executors.newFixedThreadPool(3, 
    new ThreadFactoryBuilder()
    .setNameFormat("Head Conversion Thread - %1$d")
    .build());
  public static final LoadingCache<String, GameProfile> skinCache = CacheBuilder.newBuilder()
    .maximumSize(5000L)
    .expireAfterAccess(60L, TimeUnit.MINUTES)
    .build(new CacheLoader()
  {
    public GameProfile load(String key)
      throws Exception
    {
      final GameProfile[] profiles = new GameProfile[1];
      ProfileLookupCallback gameProfileLookup = new ProfileLookupCallback()
      {
        public void onProfileLookupSucceeded(GameProfile gp)
        {
          profiles[0] = gp;
        }
        
        public void onProfileLookupFailed(GameProfile gp, Exception excptn)
        {
          profiles[0] = gp;
        }
      };
      MinecraftServer.getServer().getGameProfileRepository().findProfilesByNames(new String[] { key }, Agent.MINECRAFT, gameProfileLookup);
      
      GameProfile profile = profiles[0];
      if (profile == null)
      {
        UUID uuid = EntityHuman.a(new GameProfile(null, key));
        profile = new GameProfile(uuid, key);
        
        gameProfileLookup.onProfileLookupSucceeded(profile);
      }
      else
      {
        Property property = (Property)Iterables.getFirst(profile.getProperties().get("textures"), null);
        if (property == null) {
          profile = MinecraftServer.getServer().aD().fillProfileProperties(profile, true);
        }
      }
      return profile;
    }
  });
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setByte("SkullType", (byte)(this.a & 0xFF));
    nbttagcompound.setByte("Rot", (byte)(this.rotation & 0xFF));
    if (this.g != null)
    {
      NBTTagCompound nbttagcompound1 = new NBTTagCompound();
      
      GameProfileSerializer.serialize(nbttagcompound1, this.g);
      nbttagcompound.set("Owner", nbttagcompound1);
    }
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    this.a = nbttagcompound.getByte("SkullType");
    this.rotation = nbttagcompound.getByte("Rot");
    if (this.a == 3) {
      if (nbttagcompound.hasKeyOfType("Owner", 10))
      {
        this.g = GameProfileSerializer.deserialize(nbttagcompound.getCompound("Owner"));
      }
      else if (nbttagcompound.hasKeyOfType("ExtraType", 8))
      {
        String s = nbttagcompound.getString("ExtraType");
        if (!UtilColor.b(s))
        {
          this.g = new GameProfile(null, s);
          e();
        }
      }
    }
  }
  
  public GameProfile getGameProfile()
  {
    return this.g;
  }
  
  public Packet getUpdatePacket()
  {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    
    b(nbttagcompound);
    return new PacketPlayOutTileEntityData(this.position, 4, nbttagcompound);
  }
  
  public void setSkullType(int i)
  {
    this.a = i;
    this.g = null;
  }
  
  public void setGameProfile(GameProfile gameprofile)
  {
    this.a = 3;
    this.g = gameprofile;
    e();
  }
  
  private void e()
  {
    GameProfile profile = this.g;
    setSkullType(0);
    b(profile, new Predicate()
    {
      public boolean apply(GameProfile input)
      {
        TileEntitySkull.this.setSkullType(3);
        TileEntitySkull.this.g = input;
        TileEntitySkull.this.update();
        if (TileEntitySkull.this.world != null) {
          TileEntitySkull.this.world.notify(TileEntitySkull.this.position);
        }
        return false;
      }
    });
  }
  
  public static void b(GameProfile gameprofile, final Predicate<GameProfile> callback)
  {
    if ((gameprofile != null) && (!UtilColor.b(gameprofile.getName())))
    {
      if ((gameprofile.isComplete()) && (gameprofile.getProperties().containsKey("textures")))
      {
        callback.apply(gameprofile);
      }
      else if (MinecraftServer.getServer() == null)
      {
        callback.apply(gameprofile);
      }
      else
      {
        GameProfile profile = (GameProfile)skinCache.getIfPresent(gameprofile.getName());
        if ((profile != null) && (Iterables.getFirst(profile.getProperties().get("textures"), null) != null)) {
          callback.apply(profile);
        } else {
          executor.execute(new Runnable()
          {
            public void run()
            {
              final GameProfile profile = (GameProfile)TileEntitySkull.skinCache.getUnchecked(TileEntitySkull.this.getName().toLowerCase());
              MinecraftServer.getServer().processQueue.add(new Runnable()
              {
                public void run()
                {
                  if (profile == null) {
                    this.val$callback.apply(this.val$gameprofile);
                  } else {
                    this.val$callback.apply(profile);
                  }
                }
              });
            }
          });
        }
      }
    }
    else {
      callback.apply(gameprofile);
    }
  }
  
  public int getSkullType()
  {
    return this.a;
  }
  
  public void setRotation(int i)
  {
    this.rotation = i;
  }
  
  public int getRotation()
  {
    return this.rotation;
  }
}
