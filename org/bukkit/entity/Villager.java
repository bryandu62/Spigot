package org.bukkit.entity;

public abstract interface Villager
  extends Ageable, NPC
{
  public abstract Profession getProfession();
  
  public abstract void setProfession(Profession paramProfession);
  
  public static enum Profession
  {
    FARMER(0),  LIBRARIAN(1),  PRIEST(2),  BLACKSMITH(3),  BUTCHER(4);
    
    private static final Profession[] professions;
    private final int id;
    
    static
    {
      professions = new Profession[values().length];
      Profession[] arrayOfProfession;
      int i = (arrayOfProfession = values()).length;
      for (int j = 0; j < i; j++)
      {
        Profession type = arrayOfProfession[j];
        professions[type.getId()] = type;
      }
    }
    
    private Profession(int id)
    {
      this.id = id;
    }
    
    @Deprecated
    public int getId()
    {
      return this.id;
    }
    
    @Deprecated
    public static Profession getProfession(int id)
    {
      return id >= professions.length ? null : professions[id];
    }
  }
}
