package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Vector;
import javax.swing.JList;

public class PlayerListBox
  extends JList
  implements IUpdatePlayerListBox
{
  private MinecraftServer a;
  private int b;
  
  public PlayerListBox(MinecraftServer ☃)
  {
    this.a = ☃;
    ☃.a(this);
  }
  
  public void c()
  {
    if (this.b++ % 20 == 0)
    {
      Vector<String> ☃ = new Vector();
      for (int ☃ = 0; ☃ < this.a.getPlayerList().v().size(); ☃++) {
        ☃.add(((EntityPlayer)this.a.getPlayerList().v().get(☃)).getName());
      }
      setListData(☃);
    }
  }
}
