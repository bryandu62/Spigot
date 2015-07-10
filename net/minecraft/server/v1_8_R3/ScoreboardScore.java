package net.minecraft.server.v1_8_R3;

import java.util.Comparator;
import java.util.List;

public class ScoreboardScore
{
  public static final Comparator<ScoreboardScore> a = new Comparator()
  {
    public int a(ScoreboardScore ☃, ScoreboardScore ☃)
    {
      if (☃.getScore() > ☃.getScore()) {
        return 1;
      }
      if (☃.getScore() < ☃.getScore()) {
        return -1;
      }
      return ☃.getPlayerName().compareToIgnoreCase(☃.getPlayerName());
    }
  };
  private final Scoreboard b;
  private final ScoreboardObjective c;
  private final String playerName;
  private int score;
  private boolean f;
  private boolean g;
  
  public ScoreboardScore(Scoreboard ☃, ScoreboardObjective ☃, String ☃)
  {
    this.b = ☃;
    this.c = ☃;
    this.playerName = ☃;
    this.g = true;
  }
  
  public void addScore(int ☃)
  {
    if (this.c.getCriteria().isReadOnly()) {
      throw new IllegalStateException("Cannot modify read-only score");
    }
    setScore(getScore() + ☃);
  }
  
  public void removeScore(int ☃)
  {
    if (this.c.getCriteria().isReadOnly()) {
      throw new IllegalStateException("Cannot modify read-only score");
    }
    setScore(getScore() - ☃);
  }
  
  public void incrementScore()
  {
    if (this.c.getCriteria().isReadOnly()) {
      throw new IllegalStateException("Cannot modify read-only score");
    }
    addScore(1);
  }
  
  public int getScore()
  {
    return this.score;
  }
  
  public void setScore(int ☃)
  {
    int ☃ = this.score;
    this.score = ☃;
    if ((☃ != ☃) || (this.g))
    {
      this.g = false;
      f().handleScoreChanged(this);
    }
  }
  
  public ScoreboardObjective getObjective()
  {
    return this.c;
  }
  
  public String getPlayerName()
  {
    return this.playerName;
  }
  
  public Scoreboard f()
  {
    return this.b;
  }
  
  public boolean g()
  {
    return this.f;
  }
  
  public void a(boolean ☃)
  {
    this.f = ☃;
  }
  
  public void updateForList(List<EntityHuman> ☃)
  {
    setScore(this.c.getCriteria().getScoreModifier(☃));
  }
}
