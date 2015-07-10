package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;

public class CombatTracker
{
  private final List<CombatEntry> a = Lists.newArrayList();
  private final EntityLiving b;
  private int c;
  private int d;
  private int e;
  private boolean f;
  private boolean g;
  private String h;
  
  public CombatTracker(EntityLiving ☃)
  {
    this.b = ☃;
  }
  
  public void a()
  {
    j();
    if (this.b.k_())
    {
      Block ☃ = this.b.world.getType(new BlockPosition(this.b.locX, this.b.getBoundingBox().b, this.b.locZ)).getBlock();
      if (☃ == Blocks.LADDER) {
        this.h = "ladder";
      } else if (☃ == Blocks.VINE) {
        this.h = "vines";
      }
    }
    else if (this.b.V())
    {
      this.h = "water";
    }
  }
  
  public void a(DamageSource ☃, float ☃, float ☃)
  {
    g();
    a();
    
    CombatEntry ☃ = new CombatEntry(☃, this.b.ticksLived, ☃, ☃, this.h, this.b.fallDistance);
    
    this.a.add(☃);
    this.c = this.b.ticksLived;
    this.g = true;
    if ((☃.f()) && (!this.f) && (this.b.isAlive()))
    {
      this.f = true;
      this.d = this.b.ticksLived;
      this.e = this.d;
      this.b.enterCombat();
    }
  }
  
  public IChatBaseComponent b()
  {
    if (this.a.size() == 0) {
      return new ChatMessage("death.attack.generic", new Object[] { this.b.getScoreboardDisplayName() });
    }
    CombatEntry ☃ = i();
    CombatEntry ☃ = (CombatEntry)this.a.get(this.a.size() - 1);
    
    IChatBaseComponent ☃ = ☃.h();
    Entity ☃ = ☃.a().getEntity();
    IChatBaseComponent ☃;
    IChatBaseComponent ☃;
    if ((☃ != null) && (☃.a() == DamageSource.FALL))
    {
      IChatBaseComponent ☃ = ☃.h();
      IChatBaseComponent ☃;
      if ((☃.a() == DamageSource.FALL) || (☃.a() == DamageSource.OUT_OF_WORLD))
      {
        ☃ = new ChatMessage("death.fell.accident." + a(☃), new Object[] { this.b.getScoreboardDisplayName() });
      }
      else
      {
        IChatBaseComponent ☃;
        if ((☃ != null) && ((☃ == null) || (!☃.equals(☃))))
        {
          Entity ☃ = ☃.a().getEntity();
          ItemStack ☃ = (☃ instanceof EntityLiving) ? ((EntityLiving)☃).bA() : null;
          IChatBaseComponent ☃;
          if ((☃ != null) && (☃.hasName())) {
            ☃ = new ChatMessage("death.fell.assist.item", new Object[] { this.b.getScoreboardDisplayName(), ☃, ☃.C() });
          } else {
            ☃ = new ChatMessage("death.fell.assist", new Object[] { this.b.getScoreboardDisplayName(), ☃ });
          }
        }
        else
        {
          IChatBaseComponent ☃;
          if (☃ != null)
          {
            ItemStack ☃ = (☃ instanceof EntityLiving) ? ((EntityLiving)☃).bA() : null;
            IChatBaseComponent ☃;
            if ((☃ != null) && (☃.hasName())) {
              ☃ = new ChatMessage("death.fell.finish.item", new Object[] { this.b.getScoreboardDisplayName(), ☃, ☃.C() });
            } else {
              ☃ = new ChatMessage("death.fell.finish", new Object[] { this.b.getScoreboardDisplayName(), ☃ });
            }
          }
          else
          {
            ☃ = new ChatMessage("death.fell.killer", new Object[] { this.b.getScoreboardDisplayName() });
          }
        }
      }
    }
    else
    {
      ☃ = ☃.a().getLocalizedDeathMessage(this.b);
    }
    return ☃;
  }
  
  public EntityLiving c()
  {
    EntityLiving ☃ = null;
    EntityHuman ☃ = null;
    float ☃ = 0.0F;
    float ☃ = 0.0F;
    for (CombatEntry ☃ : this.a)
    {
      if (((☃.a().getEntity() instanceof EntityHuman)) && ((☃ == null) || (☃.c() > ☃)))
      {
        ☃ = ☃.c();
        ☃ = (EntityHuman)☃.a().getEntity();
      }
      if (((☃.a().getEntity() instanceof EntityLiving)) && ((☃ == null) || (☃.c() > ☃)))
      {
        ☃ = ☃.c();
        ☃ = (EntityLiving)☃.a().getEntity();
      }
    }
    if ((☃ != null) && (☃ >= ☃ / 3.0F)) {
      return ☃;
    }
    return ☃;
  }
  
  private CombatEntry i()
  {
    CombatEntry ☃ = null;
    CombatEntry ☃ = null;
    int ☃ = 0;
    float ☃ = 0.0F;
    for (int ☃ = 0; ☃ < this.a.size(); ☃++)
    {
      CombatEntry ☃ = (CombatEntry)this.a.get(☃);
      CombatEntry ☃ = ☃ > 0 ? (CombatEntry)this.a.get(☃ - 1) : null;
      if (((☃.a() == DamageSource.FALL) || (☃.a() == DamageSource.OUT_OF_WORLD)) && (☃.i() > 0.0F) && ((☃ == null) || (☃.i() > ☃)))
      {
        if (☃ > 0) {
          ☃ = ☃;
        } else {
          ☃ = ☃;
        }
        ☃ = ☃.i();
      }
      if ((☃.g() != null) && ((☃ == null) || (☃.c() > ☃))) {
        ☃ = ☃;
      }
    }
    if ((☃ > 5.0F) && (☃ != null)) {
      return ☃;
    }
    if ((☃ > 5) && (☃ != null)) {
      return ☃;
    }
    return null;
  }
  
  private String a(CombatEntry ☃)
  {
    return ☃.g() == null ? "generic" : ☃.g();
  }
  
  public int f()
  {
    if (this.f) {
      return this.b.ticksLived - this.d;
    }
    return this.e - this.d;
  }
  
  private void j()
  {
    this.h = null;
  }
  
  public void g()
  {
    int ☃ = this.f ? 300 : 100;
    if ((this.g) && ((!this.b.isAlive()) || (this.b.ticksLived - this.c > ☃)))
    {
      boolean ☃ = this.f;
      this.g = false;
      this.f = false;
      this.e = this.b.ticksLived;
      if (☃) {
        this.b.exitCombat();
      }
      this.a.clear();
    }
  }
  
  public EntityLiving h()
  {
    return this.b;
  }
}
