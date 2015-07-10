package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.vehicle.VehicleUpdateEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

public abstract class EntityMinecartAbstract
  extends Entity
  implements INamableTileEntity
{
  private boolean a;
  private String b;
  private static final int[][][] matrix = { { { 0, 0, -1 }, { 0, 0, 1 } }, { { -1 }, { 1 } }, { { -1, -1 }, { 1 } }, { { -1 }, { 1, -1 } }, { { 0, 0, -1 }, { 0, -1, 1 } }, { { 0, -1, -1 }, { 0, 0, 1 } }, { { 0, 0, 1 }, { 1 } }, { { 0, 0, 1 }, { -1 } }, { { 0, 0, -1 }, { -1 } }, { { 0, 0, -1 }, { 1 } } };
  private int d;
  private double e;
  private double f;
  private double g;
  private double h;
  private double i;
  public boolean slowWhenEmpty = true;
  private double derailedX = 0.5D;
  private double derailedY = 0.5D;
  private double derailedZ = 0.5D;
  private double flyingX = 0.95D;
  private double flyingY = 0.95D;
  private double flyingZ = 0.95D;
  public double maxSpeed = 0.4D;
  
  public EntityMinecartAbstract(World world)
  {
    super(world);
    this.k = true;
    setSize(0.98F, 0.7F);
  }
  
  public static EntityMinecartAbstract a(World world, double d0, double d1, double d2, EnumMinecartType entityminecartabstract_enumminecarttype)
  {
    switch (SyntheticClass_1.a[entityminecartabstract_enumminecarttype.ordinal()])
    {
    case 1: 
      return new EntityMinecartChest(world, d0, d1, d2);
    case 2: 
      return new EntityMinecartFurnace(world, d0, d1, d2);
    case 3: 
      return new EntityMinecartTNT(world, d0, d1, d2);
    case 4: 
      return new EntityMinecartMobSpawner(world, d0, d1, d2);
    case 5: 
      return new EntityMinecartHopper(world, d0, d1, d2);
    case 6: 
      return new EntityMinecartCommandBlock(world, d0, d1, d2);
    }
    return new EntityMinecartRideable(world, d0, d1, d2);
  }
  
  protected boolean s_()
  {
    return false;
  }
  
  protected void h()
  {
    this.datawatcher.a(17, new Integer(0));
    this.datawatcher.a(18, new Integer(1));
    this.datawatcher.a(19, new Float(0.0F));
    this.datawatcher.a(20, new Integer(0));
    this.datawatcher.a(21, new Integer(6));
    this.datawatcher.a(22, Byte.valueOf((byte)0));
  }
  
  public AxisAlignedBB j(Entity entity)
  {
    return entity.ae() ? entity.getBoundingBox() : null;
  }
  
  public AxisAlignedBB S()
  {
    return null;
  }
  
  public boolean ae()
  {
    return true;
  }
  
  public EntityMinecartAbstract(World world, double d0, double d1, double d2)
  {
    this(world);
    setPosition(d0, d1, d2);
    this.motX = 0.0D;
    this.motY = 0.0D;
    this.motZ = 0.0D;
    this.lastX = d0;
    this.lastY = d1;
    this.lastZ = d2;
    
    this.world.getServer().getPluginManager().callEvent(new VehicleCreateEvent((Vehicle)getBukkitEntity()));
  }
  
  public double an()
  {
    return 0.0D;
  }
  
  public boolean damageEntity(DamageSource damagesource, float f)
  {
    if ((!this.world.isClientSide) && (!this.dead))
    {
      if (isInvulnerable(damagesource)) {
        return false;
      }
      Vehicle vehicle = (Vehicle)getBukkitEntity();
      org.bukkit.entity.Entity passenger = damagesource.getEntity() == null ? null : damagesource.getEntity().getBukkitEntity();
      
      VehicleDamageEvent event = new VehicleDamageEvent(vehicle, passenger, f);
      this.world.getServer().getPluginManager().callEvent(event);
      if (event.isCancelled()) {
        return true;
      }
      f = (float)event.getDamage();
      
      k(-r());
      j(10);
      ac();
      setDamage(getDamage() + f * 10.0F);
      boolean flag = ((damagesource.getEntity() instanceof EntityHuman)) && (((EntityHuman)damagesource.getEntity()).abilities.canInstantlyBuild);
      if ((flag) || (getDamage() > 40.0F))
      {
        VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, passenger);
        this.world.getServer().getPluginManager().callEvent(destroyEvent);
        if (destroyEvent.isCancelled())
        {
          setDamage(40.0F);
          return true;
        }
        if (this.passenger != null) {
          this.passenger.mount(null);
        }
        if ((flag) && (!hasCustomName())) {
          die();
        } else {
          a(damagesource);
        }
      }
      return true;
    }
    return true;
  }
  
  public void a(DamageSource damagesource)
  {
    die();
    if (this.world.getGameRules().getBoolean("doEntityDrops"))
    {
      ItemStack itemstack = new ItemStack(Items.MINECART, 1);
      if (this.b != null) {
        itemstack.c(this.b);
      }
      a(itemstack, 0.0F);
    }
  }
  
  public boolean ad()
  {
    return !this.dead;
  }
  
  public void die()
  {
    super.die();
  }
  
  public void t_()
  {
    double prevX = this.locX;
    double prevY = this.locY;
    double prevZ = this.locZ;
    float prevYaw = this.yaw;
    float prevPitch = this.pitch;
    if (getType() > 0) {
      j(getType() - 1);
    }
    if (getDamage() > 0.0F) {
      setDamage(getDamage() - 1.0F);
    }
    if (this.locY < -64.0D) {
      O();
    }
    if ((!this.world.isClientSide) && ((this.world instanceof WorldServer)))
    {
      this.world.methodProfiler.a("portal");
      MinecraftServer minecraftserver = ((WorldServer)this.world).getMinecraftServer();
      
      int i = L();
      if (this.ak)
      {
        if ((this.vehicle == null) && (this.al++ >= i))
        {
          this.al = i;
          this.portalCooldown = aq();
          byte b0;
          byte b0;
          if (this.world.worldProvider.getDimension() == -1) {
            b0 = 0;
          } else {
            b0 = -1;
          }
          c(b0);
        }
        this.ak = false;
      }
      else
      {
        if (this.al > 0) {
          this.al -= 4;
        }
        if (this.al < 0) {
          this.al = 0;
        }
      }
      if (this.portalCooldown > 0) {
        this.portalCooldown -= 1;
      }
      this.world.methodProfiler.b();
    }
    if (this.world.isClientSide)
    {
      if (this.d > 0)
      {
        double d0 = this.locX + (this.e - this.locX) / this.d;
        double d1 = this.locY + (this.f - this.locY) / this.d;
        double d2 = this.locZ + (this.g - this.locZ) / this.d;
        double d3 = MathHelper.g(this.h - this.yaw);
        
        this.yaw = ((float)(this.yaw + d3 / this.d));
        this.pitch = ((float)(this.pitch + (this.i - this.pitch) / this.d));
        this.d -= 1;
        setPosition(d0, d1, d2);
        setYawPitch(this.yaw, this.pitch);
      }
      else
      {
        setPosition(this.locX, this.locY, this.locZ);
        setYawPitch(this.yaw, this.pitch);
      }
    }
    else
    {
      this.lastX = this.locX;
      this.lastY = this.locY;
      this.lastZ = this.locZ;
      this.motY -= 0.03999999910593033D;
      int j = MathHelper.floor(this.locX);
      
      int i = MathHelper.floor(this.locY);
      int k = MathHelper.floor(this.locZ);
      if (BlockMinecartTrackAbstract.e(this.world, new BlockPosition(j, i - 1, k))) {
        i--;
      }
      BlockPosition blockposition = new BlockPosition(j, i, k);
      IBlockData iblockdata = this.world.getType(blockposition);
      if (BlockMinecartTrackAbstract.d(iblockdata))
      {
        a(blockposition, iblockdata);
        if (iblockdata.getBlock() == Blocks.ACTIVATOR_RAIL) {
          a(j, i, k, ((Boolean)iblockdata.get(BlockPoweredRail.POWERED)).booleanValue());
        }
      }
      else
      {
        n();
      }
      checkBlockCollisions();
      this.pitch = 0.0F;
      double d4 = this.lastX - this.locX;
      double d5 = this.lastZ - this.locZ;
      if (d4 * d4 + d5 * d5 > 0.001D)
      {
        this.yaw = ((float)(MathHelper.b(d5, d4) * 180.0D / 3.141592653589793D));
        if (this.a) {
          this.yaw += 180.0F;
        }
      }
      double d6 = MathHelper.g(this.yaw - this.lastYaw);
      if ((d6 < -170.0D) || (d6 >= 170.0D))
      {
        this.yaw += 180.0F;
        this.a = (!this.a);
      }
      setYawPitch(this.yaw, this.pitch);
      
      org.bukkit.World bworld = this.world.getWorld();
      Location from = new Location(bworld, prevX, prevY, prevZ, prevYaw, prevPitch);
      Location to = new Location(bworld, this.locX, this.locY, this.locZ, this.yaw, this.pitch);
      Vehicle vehicle = (Vehicle)getBukkitEntity();
      
      this.world.getServer().getPluginManager().callEvent(new VehicleUpdateEvent(vehicle));
      if (!from.equals(to)) {
        this.world.getServer().getPluginManager().callEvent(new VehicleMoveEvent(vehicle, from, to));
      }
      Iterator iterator = this.world.getEntities(this, getBoundingBox().grow(0.20000000298023224D, 0.0D, 0.20000000298023224D)).iterator();
      while (iterator.hasNext())
      {
        Entity entity = (Entity)iterator.next();
        if ((entity != this.passenger) && (entity.ae()) && ((entity instanceof EntityMinecartAbstract))) {
          entity.collide(this);
        }
      }
      if ((this.passenger != null) && (this.passenger.dead))
      {
        if (this.passenger.vehicle == this) {
          this.passenger.vehicle = null;
        }
        this.passenger = null;
      }
      W();
    }
  }
  
  protected double m()
  {
    return this.maxSpeed;
  }
  
  public void a(int i, int j, int k, boolean flag) {}
  
  protected void n()
  {
    double d0 = m();
    
    this.motX = MathHelper.a(this.motX, -d0, d0);
    this.motZ = MathHelper.a(this.motZ, -d0, d0);
    if (this.onGround)
    {
      this.motX *= this.derailedX;
      this.motY *= this.derailedY;
      this.motZ *= this.derailedZ;
    }
    move(this.motX, this.motY, this.motZ);
    if (!this.onGround)
    {
      this.motX *= this.flyingX;
      this.motY *= this.flyingY;
      this.motZ *= this.flyingZ;
    }
  }
  
  protected void a(BlockPosition blockposition, IBlockData iblockdata)
  {
    this.fallDistance = 0.0F;
    Vec3D vec3d = k(this.locX, this.locY, this.locZ);
    
    this.locY = blockposition.getY();
    boolean flag = false;
    boolean flag1 = false;
    BlockMinecartTrackAbstract blockminecarttrackabstract = (BlockMinecartTrackAbstract)iblockdata.getBlock();
    if (blockminecarttrackabstract == Blocks.GOLDEN_RAIL)
    {
      flag = ((Boolean)iblockdata.get(BlockPoweredRail.POWERED)).booleanValue();
      flag1 = !flag;
    }
    BlockMinecartTrackAbstract.EnumTrackPosition blockminecarttrackabstract_enumtrackposition = (BlockMinecartTrackAbstract.EnumTrackPosition)iblockdata.get(blockminecarttrackabstract.n());
    switch (SyntheticClass_1.b[blockminecarttrackabstract_enumtrackposition.ordinal()])
    {
    case 1: 
      this.motX -= 0.0078125D;
      this.locY += 1.0D;
      break;
    case 2: 
      this.motX += 0.0078125D;
      this.locY += 1.0D;
      break;
    case 3: 
      this.motZ += 0.0078125D;
      this.locY += 1.0D;
      break;
    case 4: 
      this.motZ -= 0.0078125D;
      this.locY += 1.0D;
    }
    int[][] aint = matrix[blockminecarttrackabstract_enumtrackposition.a()];
    double d1 = aint[1][0] - aint[0][0];
    double d2 = aint[1][2] - aint[0][2];
    double d3 = Math.sqrt(d1 * d1 + d2 * d2);
    double d4 = this.motX * d1 + this.motZ * d2;
    if (d4 < 0.0D)
    {
      d1 = -d1;
      d2 = -d2;
    }
    double d5 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
    if (d5 > 2.0D) {
      d5 = 2.0D;
    }
    this.motX = (d5 * d1 / d3);
    this.motZ = (d5 * d2 / d3);
    if ((this.passenger instanceof EntityLiving))
    {
      double d6 = ((EntityLiving)this.passenger).ba;
      if (d6 > 0.0D)
      {
        double d7 = -Math.sin(this.passenger.yaw * 3.1415927F / 180.0F);
        double d8 = Math.cos(this.passenger.yaw * 3.1415927F / 180.0F);
        double d9 = this.motX * this.motX + this.motZ * this.motZ;
        if (d9 < 0.01D)
        {
          this.motX += d7 * 0.1D;
          this.motZ += d8 * 0.1D;
          flag1 = false;
        }
      }
    }
    if (flag1)
    {
      double d6 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
      if (d6 < 0.03D)
      {
        this.motX *= 0.0D;
        this.motY *= 0.0D;
        this.motZ *= 0.0D;
      }
      else
      {
        this.motX *= 0.5D;
        this.motY *= 0.0D;
        this.motZ *= 0.5D;
      }
    }
    double d6 = 0.0D;
    double d7 = blockposition.getX() + 0.5D + aint[0][0] * 0.5D;
    double d8 = blockposition.getZ() + 0.5D + aint[0][2] * 0.5D;
    double d9 = blockposition.getX() + 0.5D + aint[1][0] * 0.5D;
    double d10 = blockposition.getZ() + 0.5D + aint[1][2] * 0.5D;
    
    d1 = d9 - d7;
    d2 = d10 - d8;
    if (d1 == 0.0D)
    {
      this.locX = (blockposition.getX() + 0.5D);
      d6 = this.locZ - blockposition.getZ();
    }
    else if (d2 == 0.0D)
    {
      this.locZ = (blockposition.getZ() + 0.5D);
      d6 = this.locX - blockposition.getX();
    }
    else
    {
      double d11 = this.locX - d7;
      double d12 = this.locZ - d8;
      d6 = (d11 * d1 + d12 * d2) * 2.0D;
    }
    this.locX = (d7 + d1 * d6);
    this.locZ = (d8 + d2 * d6);
    setPosition(this.locX, this.locY, this.locZ);
    double d11 = this.motX;
    double d12 = this.motZ;
    if (this.passenger != null)
    {
      d11 *= 0.75D;
      d12 *= 0.75D;
    }
    double d13 = m();
    
    d11 = MathHelper.a(d11, -d13, d13);
    d12 = MathHelper.a(d12, -d13, d13);
    move(d11, 0.0D, d12);
    if ((aint[0][1] != 0) && (MathHelper.floor(this.locX) - blockposition.getX() == aint[0][0]) && (MathHelper.floor(this.locZ) - blockposition.getZ() == aint[0][2])) {
      setPosition(this.locX, this.locY + aint[0][1], this.locZ);
    } else if ((aint[1][1] != 0) && (MathHelper.floor(this.locX) - blockposition.getX() == aint[1][0]) && (MathHelper.floor(this.locZ) - blockposition.getZ() == aint[1][2])) {
      setPosition(this.locX, this.locY + aint[1][1], this.locZ);
    }
    o();
    Vec3D vec3d1 = k(this.locX, this.locY, this.locZ);
    if ((vec3d1 != null) && (vec3d != null))
    {
      double d14 = (vec3d.b - vec3d1.b) * 0.05D;
      
      d5 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
      if (d5 > 0.0D)
      {
        this.motX = (this.motX / d5 * (d5 + d14));
        this.motZ = (this.motZ / d5 * (d5 + d14));
      }
      setPosition(this.locX, vec3d1.b, this.locZ);
    }
    int i = MathHelper.floor(this.locX);
    int j = MathHelper.floor(this.locZ);
    if ((i != blockposition.getX()) || (j != blockposition.getZ()))
    {
      d5 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
      this.motX = (d5 * (i - blockposition.getX()));
      this.motZ = (d5 * (j - blockposition.getZ()));
    }
    if (flag)
    {
      double d15 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
      if (d15 > 0.01D)
      {
        double d16 = 0.06D;
        
        this.motX += this.motX / d15 * d16;
        this.motZ += this.motZ / d15 * d16;
      }
      else if (blockminecarttrackabstract_enumtrackposition == BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST)
      {
        if (this.world.getType(blockposition.west()).getBlock().isOccluding()) {
          this.motX = 0.02D;
        } else if (this.world.getType(blockposition.east()).getBlock().isOccluding()) {
          this.motX = -0.02D;
        }
      }
      else if (blockminecarttrackabstract_enumtrackposition == BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH)
      {
        if (this.world.getType(blockposition.north()).getBlock().isOccluding()) {
          this.motZ = 0.02D;
        } else if (this.world.getType(blockposition.south()).getBlock().isOccluding()) {
          this.motZ = -0.02D;
        }
      }
    }
  }
  
  protected void o()
  {
    if ((this.passenger != null) || (!this.slowWhenEmpty))
    {
      this.motX *= 0.996999979019165D;
      this.motY *= 0.0D;
      this.motZ *= 0.996999979019165D;
    }
    else
    {
      this.motX *= 0.9599999785423279D;
      this.motY *= 0.0D;
      this.motZ *= 0.9599999785423279D;
    }
  }
  
  public void setPosition(double d0, double d1, double d2)
  {
    this.locX = d0;
    this.locY = d1;
    this.locZ = d2;
    float f = this.width / 2.0F;
    float f1 = this.length;
    
    a(new AxisAlignedBB(d0 - f, d1, d2 - f, d0 + f, d1 + f1, d2 + f));
  }
  
  public Vec3D k(double d0, double d1, double d2)
  {
    int i = MathHelper.floor(d0);
    int j = MathHelper.floor(d1);
    int k = MathHelper.floor(d2);
    if (BlockMinecartTrackAbstract.e(this.world, new BlockPosition(i, j - 1, k))) {
      j--;
    }
    IBlockData iblockdata = this.world.getType(new BlockPosition(i, j, k));
    if (BlockMinecartTrackAbstract.d(iblockdata))
    {
      BlockMinecartTrackAbstract.EnumTrackPosition blockminecarttrackabstract_enumtrackposition = (BlockMinecartTrackAbstract.EnumTrackPosition)iblockdata.get(((BlockMinecartTrackAbstract)iblockdata.getBlock()).n());
      int[][] aint = matrix[blockminecarttrackabstract_enumtrackposition.a()];
      double d3 = 0.0D;
      double d4 = i + 0.5D + aint[0][0] * 0.5D;
      double d5 = j + 0.0625D + aint[0][1] * 0.5D;
      double d6 = k + 0.5D + aint[0][2] * 0.5D;
      double d7 = i + 0.5D + aint[1][0] * 0.5D;
      double d8 = j + 0.0625D + aint[1][1] * 0.5D;
      double d9 = k + 0.5D + aint[1][2] * 0.5D;
      double d10 = d7 - d4;
      double d11 = (d8 - d5) * 2.0D;
      double d12 = d9 - d6;
      if (d10 == 0.0D)
      {
        d0 = i + 0.5D;
        d3 = d2 - k;
      }
      else if (d12 == 0.0D)
      {
        d2 = k + 0.5D;
        d3 = d0 - i;
      }
      else
      {
        double d13 = d0 - d4;
        double d14 = d2 - d6;
        
        d3 = (d13 * d10 + d14 * d12) * 2.0D;
      }
      d0 = d4 + d10 * d3;
      d1 = d5 + d11 * d3;
      d2 = d6 + d12 * d3;
      if (d11 < 0.0D) {
        d1 += 1.0D;
      }
      if (d11 > 0.0D) {
        d1 += 0.5D;
      }
      return new Vec3D(d0, d1, d2);
    }
    return null;
  }
  
  protected void a(NBTTagCompound nbttagcompound)
  {
    if (nbttagcompound.getBoolean("CustomDisplayTile"))
    {
      int i = nbttagcompound.getInt("DisplayData");
      if (nbttagcompound.hasKeyOfType("DisplayTile", 8))
      {
        Block block = Block.getByName(nbttagcompound.getString("DisplayTile"));
        if (block == null) {
          setDisplayBlock(Blocks.AIR.getBlockData());
        } else {
          setDisplayBlock(block.fromLegacyData(i));
        }
      }
      else
      {
        Block block = Block.getById(nbttagcompound.getInt("DisplayTile"));
        if (block == null) {
          setDisplayBlock(Blocks.AIR.getBlockData());
        } else {
          setDisplayBlock(block.fromLegacyData(i));
        }
      }
      SetDisplayBlockOffset(nbttagcompound.getInt("DisplayOffset"));
    }
    if ((nbttagcompound.hasKeyOfType("CustomName", 8)) && (nbttagcompound.getString("CustomName").length() > 0)) {
      this.b = nbttagcompound.getString("CustomName");
    }
  }
  
  protected void b(NBTTagCompound nbttagcompound)
  {
    if (x())
    {
      nbttagcompound.setBoolean("CustomDisplayTile", true);
      IBlockData iblockdata = getDisplayBlock();
      MinecraftKey minecraftkey = (MinecraftKey)Block.REGISTRY.c(iblockdata.getBlock());
      
      nbttagcompound.setString("DisplayTile", minecraftkey == null ? "" : minecraftkey.toString());
      nbttagcompound.setInt("DisplayData", iblockdata.getBlock().toLegacyData(iblockdata));
      nbttagcompound.setInt("DisplayOffset", getDisplayBlockOffset());
    }
    if ((this.b != null) && (this.b.length() > 0)) {
      nbttagcompound.setString("CustomName", this.b);
    }
  }
  
  public void collide(Entity entity)
  {
    if ((!this.world.isClientSide) && 
      (!entity.noclip) && (!this.noclip) && 
      (entity != this.passenger))
    {
      Vehicle vehicle = (Vehicle)getBukkitEntity();
      org.bukkit.entity.Entity hitEntity = entity == null ? null : entity.getBukkitEntity();
      
      VehicleEntityCollisionEvent collisionEvent = new VehicleEntityCollisionEvent(vehicle, hitEntity);
      this.world.getServer().getPluginManager().callEvent(collisionEvent);
      if (collisionEvent.isCancelled()) {
        return;
      }
      if (((entity instanceof EntityLiving)) && (!(entity instanceof EntityHuman)) && (!(entity instanceof EntityIronGolem)) && (s() == EnumMinecartType.RIDEABLE) && (this.motX * this.motX + this.motZ * this.motZ > 0.01D) && (this.passenger == null) && (entity.vehicle == null)) {
        entity.mount(this);
      }
      double d0 = entity.locX - this.locX;
      double d1 = entity.locZ - this.locZ;
      double d2 = d0 * d0 + d1 * d1;
      if ((d2 >= 9.999999747378752E-5D) && (!collisionEvent.isCollisionCancelled()))
      {
        d2 = MathHelper.sqrt(d2);
        d0 /= d2;
        d1 /= d2;
        double d3 = 1.0D / d2;
        if (d3 > 1.0D) {
          d3 = 1.0D;
        }
        d0 *= d3;
        d1 *= d3;
        d0 *= 0.10000000149011612D;
        d1 *= 0.10000000149011612D;
        d0 *= (1.0F - this.U);
        d1 *= (1.0F - this.U);
        d0 *= 0.5D;
        d1 *= 0.5D;
        if ((entity instanceof EntityMinecartAbstract))
        {
          double d4 = entity.locX - this.locX;
          double d5 = entity.locZ - this.locZ;
          Vec3D vec3d = new Vec3D(d4, 0.0D, d5).a();
          Vec3D vec3d1 = new Vec3D(MathHelper.cos(this.yaw * 3.1415927F / 180.0F), 0.0D, MathHelper.sin(this.yaw * 3.1415927F / 180.0F)).a();
          double d6 = Math.abs(vec3d.b(vec3d1));
          if (d6 < 0.800000011920929D) {
            return;
          }
          double d7 = entity.motX + this.motX;
          double d8 = entity.motZ + this.motZ;
          if ((((EntityMinecartAbstract)entity).s() == EnumMinecartType.FURNACE) && (s() != EnumMinecartType.FURNACE))
          {
            this.motX *= 0.20000000298023224D;
            this.motZ *= 0.20000000298023224D;
            g(entity.motX - d0, 0.0D, entity.motZ - d1);
            entity.motX *= 0.949999988079071D;
            entity.motZ *= 0.949999988079071D;
          }
          else if ((((EntityMinecartAbstract)entity).s() != EnumMinecartType.FURNACE) && (s() == EnumMinecartType.FURNACE))
          {
            entity.motX *= 0.20000000298023224D;
            entity.motZ *= 0.20000000298023224D;
            entity.g(this.motX + d0, 0.0D, this.motZ + d1);
            this.motX *= 0.949999988079071D;
            this.motZ *= 0.949999988079071D;
          }
          else
          {
            d7 /= 2.0D;
            d8 /= 2.0D;
            this.motX *= 0.20000000298023224D;
            this.motZ *= 0.20000000298023224D;
            g(d7 - d0, 0.0D, d8 - d1);
            entity.motX *= 0.20000000298023224D;
            entity.motZ *= 0.20000000298023224D;
            entity.g(d7 + d0, 0.0D, d8 + d1);
          }
        }
        else
        {
          g(-d0, 0.0D, -d1);
          entity.g(d0 / 4.0D, 0.0D, d1 / 4.0D);
        }
      }
    }
  }
  
  public void setDamage(float f)
  {
    this.datawatcher.watch(19, Float.valueOf(f));
  }
  
  public float getDamage()
  {
    return this.datawatcher.getFloat(19);
  }
  
  public void j(int i)
  {
    this.datawatcher.watch(17, Integer.valueOf(i));
  }
  
  public int getType()
  {
    return this.datawatcher.getInt(17);
  }
  
  public void k(int i)
  {
    this.datawatcher.watch(18, Integer.valueOf(i));
  }
  
  public int r()
  {
    return this.datawatcher.getInt(18);
  }
  
  public abstract EnumMinecartType s();
  
  public IBlockData getDisplayBlock()
  {
    return !x() ? u() : Block.getByCombinedId(getDataWatcher().getInt(20));
  }
  
  public IBlockData u()
  {
    return Blocks.AIR.getBlockData();
  }
  
  public int getDisplayBlockOffset()
  {
    return !x() ? w() : getDataWatcher().getInt(21);
  }
  
  public int w()
  {
    return 6;
  }
  
  public void setDisplayBlock(IBlockData iblockdata)
  {
    getDataWatcher().watch(20, Integer.valueOf(Block.getCombinedId(iblockdata)));
    a(true);
  }
  
  public void SetDisplayBlockOffset(int i)
  {
    getDataWatcher().watch(21, Integer.valueOf(i));
    a(true);
  }
  
  public boolean x()
  {
    return getDataWatcher().getByte(22) == 1;
  }
  
  public void a(boolean flag)
  {
    getDataWatcher().watch(22, Byte.valueOf((byte)(flag ? 1 : 0)));
  }
  
  public void setCustomName(String s)
  {
    this.b = s;
  }
  
  public String getName()
  {
    return this.b != null ? this.b : super.getName();
  }
  
  public boolean hasCustomName()
  {
    return this.b != null;
  }
  
  public String getCustomName()
  {
    return this.b;
  }
  
  public IChatBaseComponent getScoreboardDisplayName()
  {
    if (hasCustomName())
    {
      ChatComponentText chatcomponenttext = new ChatComponentText(this.b);
      
      chatcomponenttext.getChatModifier().setChatHoverable(aQ());
      chatcomponenttext.getChatModifier().setInsertion(getUniqueID().toString());
      return chatcomponenttext;
    }
    ChatMessage chatmessage = new ChatMessage(getName(), new Object[0]);
    
    chatmessage.getChatModifier().setChatHoverable(aQ());
    chatmessage.getChatModifier().setInsertion(getUniqueID().toString());
    return chatmessage;
  }
  
  static class SyntheticClass_1
  {
    static final int[] a;
    static final int[] b = new int[BlockMinecartTrackAbstract.EnumTrackPosition.values().length];
    
    static
    {
      try
      {
        b[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_EAST.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        b[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_WEST.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        b[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        b[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      a = new int[EntityMinecartAbstract.EnumMinecartType.values().length];
      try
      {
        a[EntityMinecartAbstract.EnumMinecartType.CHEST.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      try
      {
        a[EntityMinecartAbstract.EnumMinecartType.FURNACE.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
      try
      {
        a[EntityMinecartAbstract.EnumMinecartType.TNT.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError7) {}
      try
      {
        a[EntityMinecartAbstract.EnumMinecartType.SPAWNER.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError8) {}
      try
      {
        a[EntityMinecartAbstract.EnumMinecartType.HOPPER.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError9) {}
      try
      {
        a[EntityMinecartAbstract.EnumMinecartType.COMMAND_BLOCK.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError10) {}
    }
  }
  
  public static enum EnumMinecartType
  {
    RIDEABLE(0, "MinecartRideable"),  CHEST(1, "MinecartChest"),  FURNACE(2, "MinecartFurnace"),  TNT(3, "MinecartTNT"),  SPAWNER(4, "MinecartSpawner"),  HOPPER(5, "MinecartHopper"),  COMMAND_BLOCK(6, "MinecartCommandBlock");
    
    private static final Map<Integer, EnumMinecartType> h;
    private final int i;
    private final String j;
    
    private EnumMinecartType(int i, String s)
    {
      this.i = i;
      this.j = s;
    }
    
    public int a()
    {
      return this.i;
    }
    
    public String b()
    {
      return this.j;
    }
    
    public static EnumMinecartType a(int i)
    {
      EnumMinecartType entityminecartabstract_enumminecarttype = (EnumMinecartType)h.get(Integer.valueOf(i));
      
      return entityminecartabstract_enumminecarttype == null ? RIDEABLE : entityminecartabstract_enumminecarttype;
    }
    
    static
    {
      h = Maps.newHashMap();
      
      EnumMinecartType[] aentityminecartabstract_enumminecarttype = values();
      int i = aentityminecartabstract_enumminecarttype.length;
      for (int j = 0; j < i; j++)
      {
        EnumMinecartType entityminecartabstract_enumminecarttype = aentityminecartabstract_enumminecarttype[j];
        
        h.put(Integer.valueOf(entityminecartabstract_enumminecarttype.a()), entityminecartabstract_enumminecarttype);
      }
    }
  }
  
  public Vector getFlyingVelocityMod()
  {
    return new Vector(this.flyingX, this.flyingY, this.flyingZ);
  }
  
  public void setFlyingVelocityMod(Vector flying)
  {
    this.flyingX = flying.getX();
    this.flyingY = flying.getY();
    this.flyingZ = flying.getZ();
  }
  
  public Vector getDerailedVelocityMod()
  {
    return new Vector(this.derailedX, this.derailedY, this.derailedZ);
  }
  
  public void setDerailedVelocityMod(Vector derailed)
  {
    this.derailedX = derailed.getX();
    this.derailedY = derailed.getY();
    this.derailedZ = derailed.getZ();
  }
}
