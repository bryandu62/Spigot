package net.minecraft.server.v1_8_R3;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.AntiXray;
import org.spigotmc.SpigotWorldConfig;

public class PlayerInteractManager
{
  public World world;
  public EntityPlayer player;
  private WorldSettings.EnumGamemode gamemode;
  private boolean d;
  private int lastDigTick;
  private BlockPosition f;
  private int currentTick;
  private boolean h;
  private BlockPosition i;
  private int j;
  private int k;
  
  public PlayerInteractManager(World world)
  {
    this.gamemode = WorldSettings.EnumGamemode.NOT_SET;
    this.f = BlockPosition.ZERO;
    this.i = BlockPosition.ZERO;
    this.k = -1;
    this.world = world;
  }
  
  public void setGameMode(WorldSettings.EnumGamemode worldsettings_enumgamemode)
  {
    this.gamemode = worldsettings_enumgamemode;
    worldsettings_enumgamemode.a(this.player.abilities);
    this.player.updateAbilities();
    this.player.server.getPlayerList().sendAll(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_GAME_MODE, new EntityPlayer[] { this.player }), this.player);
  }
  
  public WorldSettings.EnumGamemode getGameMode()
  {
    return this.gamemode;
  }
  
  public boolean c()
  {
    return this.gamemode.e();
  }
  
  public boolean isCreative()
  {
    return this.gamemode.d();
  }
  
  public void b(WorldSettings.EnumGamemode worldsettings_enumgamemode)
  {
    if (this.gamemode == WorldSettings.EnumGamemode.NOT_SET) {
      this.gamemode = worldsettings_enumgamemode;
    }
    setGameMode(this.gamemode);
  }
  
  public void a()
  {
    this.currentTick = MinecraftServer.currentTick;
    if (this.h)
    {
      int j = this.currentTick - this.j;
      Block block = this.world.getType(this.i).getBlock();
      if (block.getMaterial() == Material.AIR)
      {
        this.h = false;
      }
      else
      {
        float f = block.getDamage(this.player, this.player.world, this.i) * (j + 1);
        int i = (int)(f * 10.0F);
        if (i != this.k)
        {
          this.world.c(this.player.getId(), this.i, i);
          this.k = i;
        }
        if (f >= 1.0F)
        {
          this.h = false;
          breakBlock(this.i);
        }
      }
    }
    else if (this.d)
    {
      Block block1 = this.world.getType(this.f).getBlock();
      if (block1.getMaterial() == Material.AIR)
      {
        this.world.c(this.player.getId(), this.f, -1);
        this.k = -1;
        this.d = false;
      }
      else
      {
        int k = this.currentTick - this.lastDigTick;
        
        float f = block1.getDamage(this.player, this.player.world, this.i) * (k + 1);
        int i = (int)(f * 10.0F);
        if (i != this.k)
        {
          this.world.c(this.player.getId(), this.f, i);
          this.k = i;
        }
      }
    }
  }
  
  public void a(BlockPosition blockposition, EnumDirection enumdirection)
  {
    PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, blockposition, enumdirection, this.player.inventory.getItemInHand());
    if (event.isCancelled())
    {
      this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, blockposition));
      
      TileEntity tileentity = this.world.getTileEntity(blockposition);
      if (tileentity != null) {
        this.player.playerConnection.sendPacket(tileentity.getUpdatePacket());
      }
      return;
    }
    if (isCreative())
    {
      if (!this.world.douseFire(null, blockposition, enumdirection)) {
        breakBlock(blockposition);
      }
    }
    else
    {
      Block block = this.world.getType(blockposition).getBlock();
      if (this.gamemode.c())
      {
        if (this.gamemode == WorldSettings.EnumGamemode.SPECTATOR) {
          return;
        }
        if (!this.player.cn())
        {
          ItemStack itemstack = this.player.bZ();
          if (itemstack == null) {
            return;
          }
          if (!itemstack.c(block)) {
            return;
          }
        }
      }
      this.lastDigTick = this.currentTick;
      float f = 1.0F;
      if (event.useInteractedBlock() == Event.Result.DENY)
      {
        IBlockData data = this.world.getType(blockposition);
        if (block == Blocks.WOODEN_DOOR)
        {
          boolean bottom = data.get(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER;
          this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, blockposition));
          this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, bottom ? blockposition.up() : blockposition.down()));
        }
        else if (block == Blocks.TRAPDOOR)
        {
          this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, blockposition));
        }
      }
      else if (block.getMaterial() != Material.AIR)
      {
        block.attack(this.world, blockposition, this.player);
        f = block.getDamage(this.player, this.player.world, blockposition);
        
        this.world.douseFire(null, blockposition, enumdirection);
      }
      if (event.useItemInHand() == Event.Result.DENY)
      {
        if (f > 1.0F) {
          this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, blockposition));
        }
        return;
      }
      BlockDamageEvent blockEvent = CraftEventFactory.callBlockDamageEvent(this.player, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this.player.inventory.getItemInHand(), f >= 1.0F);
      if (blockEvent.isCancelled())
      {
        this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, blockposition));
        return;
      }
      if (blockEvent.getInstaBreak()) {
        f = 2.0F;
      }
      if ((block.getMaterial() != Material.AIR) && (f >= 1.0F))
      {
        breakBlock(blockposition);
      }
      else
      {
        this.d = true;
        this.f = blockposition;
        int i = (int)(f * 10.0F);
        
        this.world.c(this.player.getId(), blockposition, i);
        this.k = i;
      }
    }
    this.world.spigotConfig.antiXrayInstance.updateNearbyBlocks(this.world, blockposition);
  }
  
  public void a(BlockPosition blockposition)
  {
    if (blockposition.equals(this.f))
    {
      this.currentTick = MinecraftServer.currentTick;
      int i = this.currentTick - this.lastDigTick;
      Block block = this.world.getType(blockposition).getBlock();
      if (block.getMaterial() != Material.AIR)
      {
        float f = block.getDamage(this.player, this.player.world, blockposition) * (i + 1);
        if (f >= 0.7F)
        {
          this.d = false;
          this.world.c(this.player.getId(), blockposition, -1);
          breakBlock(blockposition);
        }
        else if (!this.h)
        {
          this.d = false;
          this.h = true;
          this.i = blockposition;
          this.j = this.lastDigTick;
        }
      }
    }
    else
    {
      this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, blockposition));
    }
  }
  
  public void e()
  {
    this.d = false;
    this.world.c(this.player.getId(), this.f, -1);
  }
  
  private boolean c(BlockPosition blockposition)
  {
    IBlockData iblockdata = this.world.getType(blockposition);
    
    iblockdata.getBlock().a(this.world, blockposition, iblockdata, this.player);
    boolean flag = this.world.setAir(blockposition);
    if (flag) {
      iblockdata.getBlock().postBreak(this.world, blockposition, iblockdata);
    }
    return flag;
  }
  
  public boolean breakBlock(BlockPosition blockposition)
  {
    BlockBreakEvent event = null;
    if ((this.player instanceof EntityPlayer))
    {
      org.bukkit.block.Block block = this.world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
      
      boolean isSwordNoBreak = (this.gamemode.d()) && (this.player.bA() != null) && ((this.player.bA().getItem() instanceof ItemSword));
      if ((this.world.getTileEntity(blockposition) == null) && (!isSwordNoBreak))
      {
        PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(this.world, blockposition);
        packet.block = Blocks.AIR.getBlockData();
        this.player.playerConnection.sendPacket(packet);
      }
      event = new BlockBreakEvent(block, this.player.getBukkitEntity());
      
      event.setCancelled(isSwordNoBreak);
      
      IBlockData nmsData = this.world.getType(blockposition);
      Block nmsBlock = nmsData.getBlock();
      if ((nmsBlock != null) && (!event.isCancelled()) && (!isCreative()) && (this.player.b(nmsBlock))) {
        if ((!nmsBlock.I()) || (!EnchantmentManager.hasSilkTouchEnchantment(this.player)))
        {
          block.getData();
          int bonusLevel = EnchantmentManager.getBonusBlockLootEnchantmentLevel(this.player);
          
          event.setExpToDrop(nmsBlock.getExpDrop(this.world, nmsData, bonusLevel));
        }
      }
      this.world.getServer().getPluginManager().callEvent(event);
      if (event.isCancelled())
      {
        if (isSwordNoBreak) {
          return false;
        }
        this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, blockposition));
        
        TileEntity tileentity = this.world.getTileEntity(blockposition);
        if (tileentity != null) {
          this.player.playerConnection.sendPacket(tileentity.getUpdatePacket());
        }
        return false;
      }
    }
    IBlockData iblockdata = this.world.getType(blockposition);
    if (iblockdata.getBlock() == Blocks.AIR) {
      return false;
    }
    TileEntity tileentity = this.world.getTileEntity(blockposition);
    if ((iblockdata.getBlock() == Blocks.SKULL) && (!isCreative()))
    {
      iblockdata.getBlock().dropNaturally(this.world, blockposition, iblockdata, 1.0F, 0);
      return c(blockposition);
    }
    if (this.gamemode.c())
    {
      if (this.gamemode == WorldSettings.EnumGamemode.SPECTATOR) {
        return false;
      }
      if (!this.player.cn())
      {
        ItemStack itemstack = this.player.bZ();
        if (itemstack == null) {
          return false;
        }
        if (!itemstack.c(iblockdata.getBlock())) {
          return false;
        }
      }
    }
    this.world.a(this.player, 2001, blockposition, Block.getCombinedId(iblockdata));
    boolean flag = c(blockposition);
    if (isCreative())
    {
      this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, blockposition));
    }
    else
    {
      ItemStack itemstack1 = this.player.bZ();
      boolean flag1 = this.player.b(iblockdata.getBlock());
      if (itemstack1 != null)
      {
        itemstack1.a(this.world, iblockdata.getBlock(), blockposition, this.player);
        if (itemstack1.count == 0) {
          this.player.ca();
        }
      }
      if ((flag) && (flag1)) {
        iblockdata.getBlock().a(this.world, this.player, blockposition, iblockdata, tileentity);
      }
    }
    if ((flag) && (event != null)) {
      iblockdata.getBlock().dropExperience(this.world, blockposition, event.getExpToDrop());
    }
    return flag;
  }
  
  public boolean useItem(EntityHuman entityhuman, World world, ItemStack itemstack)
  {
    if (this.gamemode == WorldSettings.EnumGamemode.SPECTATOR) {
      return false;
    }
    int i = itemstack.count;
    int j = itemstack.getData();
    ItemStack itemstack1 = itemstack.a(world, entityhuman);
    if ((itemstack1 == itemstack) && ((itemstack1 == null) || ((itemstack1.count == i) && (itemstack1.l() <= 0) && (itemstack1.getData() == j)))) {
      return false;
    }
    entityhuman.inventory.items[entityhuman.inventory.itemInHandIndex] = itemstack1;
    if (isCreative())
    {
      itemstack1.count = i;
      if (itemstack1.e()) {
        itemstack1.setData(j);
      }
    }
    if (itemstack1.count == 0) {
      entityhuman.inventory.items[entityhuman.inventory.itemInHandIndex] = null;
    }
    if (!entityhuman.bS()) {
      ((EntityPlayer)entityhuman).updateInventory(entityhuman.defaultContainer);
    }
    return true;
  }
  
  public boolean interactResult = false;
  public boolean firedInteract = false;
  
  public boolean interact(EntityHuman entityhuman, World world, ItemStack itemstack, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2)
  {
    IBlockData blockdata = world.getType(blockposition);
    boolean result = false;
    if (blockdata.getBlock() != Blocks.AIR)
    {
      boolean cancelledBlock = false;
      if (this.gamemode == WorldSettings.EnumGamemode.SPECTATOR)
      {
        TileEntity tileentity = world.getTileEntity(blockposition);
        cancelledBlock = (!(tileentity instanceof ITileInventory)) && (!(tileentity instanceof IInventory));
      }
      if ((!entityhuman.getBukkitEntity().isOp()) && (itemstack != null) && ((Block.asBlock(itemstack.getItem()) instanceof BlockCommand))) {
        cancelledBlock = true;
      }
      PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(entityhuman, Action.RIGHT_CLICK_BLOCK, blockposition, enumdirection, itemstack, cancelledBlock);
      this.firedInteract = true;
      this.interactResult = (event.useItemInHand() == Event.Result.DENY);
      if (event.useInteractedBlock() == Event.Result.DENY)
      {
        if ((blockdata.getBlock() instanceof BlockDoor))
        {
          boolean bottom = blockdata.get(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER;
          ((EntityPlayer)entityhuman).playerConnection.sendPacket(new PacketPlayOutBlockChange(world, bottom ? blockposition.up() : blockposition.down()));
        }
        result = event.useItemInHand() != Event.Result.ALLOW;
      }
      else
      {
        if (this.gamemode == WorldSettings.EnumGamemode.SPECTATOR)
        {
          TileEntity tileentity = world.getTileEntity(blockposition);
          if ((tileentity instanceof ITileInventory))
          {
            Block block = world.getType(blockposition).getBlock();
            ITileInventory itileinventory = (ITileInventory)tileentity;
            if (((itileinventory instanceof TileEntityChest)) && ((block instanceof BlockChest))) {
              itileinventory = ((BlockChest)block).f(world, blockposition);
            }
            if (itileinventory != null)
            {
              entityhuman.openContainer(itileinventory);
              return true;
            }
          }
          else if ((tileentity instanceof IInventory))
          {
            entityhuman.openContainer((IInventory)tileentity);
            return true;
          }
          return false;
        }
        if ((!entityhuman.isSneaking()) || (itemstack == null)) {
          result = blockdata.getBlock().interact(world, blockposition, blockdata, entityhuman, enumdirection, f, f1, f2);
        }
      }
      if ((itemstack != null) && (!result) && (!this.interactResult))
      {
        int j1 = itemstack.getData();
        int k1 = itemstack.count;
        
        result = itemstack.placeItem(entityhuman, world, blockposition, enumdirection, f, f1, f2);
        if (isCreative())
        {
          itemstack.setData(j1);
          itemstack.count = k1;
        }
      }
    }
    return result;
  }
  
  public void a(WorldServer worldserver)
  {
    this.world = worldserver;
  }
}
