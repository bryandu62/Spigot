package org.bukkit.block;

import java.util.HashMap;
import java.util.Map;

public enum PistonMoveReaction
{
  MOVE(
  
    0),  BREAK(
  
    1),  BLOCK(
  
    2);
  
  private int id;
  private static Map<Integer, PistonMoveReaction> byId;
  
  static
  {
    byId = new HashMap();
    PistonMoveReaction[] arrayOfPistonMoveReaction;
    int i = (arrayOfPistonMoveReaction = values()).length;
    for (int j = 0; j < i; j++)
    {
      PistonMoveReaction reaction = arrayOfPistonMoveReaction[j];
      byId.put(Integer.valueOf(reaction.id), reaction);
    }
  }
  
  private PistonMoveReaction(int id)
  {
    this.id = id;
  }
  
  @Deprecated
  public int getId()
  {
    return this.id;
  }
  
  @Deprecated
  public static PistonMoveReaction getById(int id)
  {
    return (PistonMoveReaction)byId.get(Integer.valueOf(id));
  }
}
