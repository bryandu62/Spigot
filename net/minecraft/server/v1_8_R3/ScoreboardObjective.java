package net.minecraft.server.v1_8_R3;

public class ScoreboardObjective
{
  private final Scoreboard a;
  private final String b;
  private final IScoreboardCriteria c;
  private IScoreboardCriteria.EnumScoreboardHealthDisplay d;
  private String e;
  
  public ScoreboardObjective(Scoreboard ☃, String ☃, IScoreboardCriteria ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
    this.e = ☃;
    this.d = ☃.c();
  }
  
  public String getName()
  {
    return this.b;
  }
  
  public IScoreboardCriteria getCriteria()
  {
    return this.c;
  }
  
  public String getDisplayName()
  {
    return this.e;
  }
  
  public void setDisplayName(String ☃)
  {
    this.e = ☃;
    this.a.handleObjectiveChanged(this);
  }
  
  public IScoreboardCriteria.EnumScoreboardHealthDisplay e()
  {
    return this.d;
  }
  
  public void a(IScoreboardCriteria.EnumScoreboardHealthDisplay ☃)
  {
    this.d = ☃;
    this.a.handleObjectiveChanged(this);
  }
}
