package net.minecraft.server.v1_8_R3;

public class ChatComponentScore
  extends ChatBaseComponent
{
  private final String b;
  private final String c;
  private String d = "";
  
  public ChatComponentScore(String ☃, String ☃)
  {
    this.b = ☃;
    this.c = ☃;
  }
  
  public String g()
  {
    return this.b;
  }
  
  public String h()
  {
    return this.c;
  }
  
  public void b(String ☃)
  {
    this.d = ☃;
  }
  
  public String getText()
  {
    MinecraftServer ☃ = MinecraftServer.getServer();
    if ((☃ != null) && (☃.O()) && (UtilColor.b(this.d)))
    {
      Scoreboard ☃ = ☃.getWorldServer(0).getScoreboard();
      ScoreboardObjective ☃ = ☃.getObjective(this.c);
      if (☃.b(this.b, ☃))
      {
        ScoreboardScore ☃ = ☃.getPlayerScoreForObjective(this.b, ☃);
        b(String.format("%d", new Object[] { Integer.valueOf(☃.getScore()) }));
      }
      else
      {
        this.d = "";
      }
    }
    return this.d;
  }
  
  public ChatComponentScore i()
  {
    ChatComponentScore ☃ = new ChatComponentScore(this.b, this.c);
    ☃.b(this.d);
    ☃.setChatModifier(getChatModifier().clone());
    for (IChatBaseComponent ☃ : a()) {
      ☃.addSibling(☃.f());
    }
    return ☃;
  }
  
  public boolean equals(Object ☃)
  {
    if (this == ☃) {
      return true;
    }
    if ((☃ instanceof ChatComponentScore))
    {
      ChatComponentScore ☃ = (ChatComponentScore)☃;
      return (this.b.equals(☃.b)) && (this.c.equals(☃.c)) && (super.equals(☃));
    }
    return false;
  }
  
  public String toString()
  {
    return "ScoreComponent{name='" + this.b + '\'' + "objective='" + this.c + '\'' + ", siblings=" + this.a + ", style=" + getChatModifier() + '}';
  }
}
