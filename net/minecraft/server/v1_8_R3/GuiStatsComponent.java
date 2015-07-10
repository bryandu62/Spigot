package net.minecraft.server.v1_8_R3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JComponent;
import javax.swing.Timer;

public class GuiStatsComponent
  extends JComponent
{
  private static final DecimalFormat a = new DecimalFormat("########0.000");
  private int[] b = new int['Ā'];
  private int c;
  private String[] d = new String[11];
  private final MinecraftServer e;
  
  public GuiStatsComponent(MinecraftServer ☃)
  {
    this.e = ☃;
    setPreferredSize(new Dimension(456, 246));
    setMinimumSize(new Dimension(456, 246));
    setMaximumSize(new Dimension(456, 246));
    new Timer(500, new ActionListener()
    {
      public void actionPerformed(ActionEvent ☃)
      {
        GuiStatsComponent.a(GuiStatsComponent.this);
      }
    }).start();
    setBackground(Color.BLACK);
  }
  
  private void a()
  {
    long ☃ = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    System.gc();
    this.d[0] = ("Memory use: " + ☃ / 1024L / 1024L + " mb (" + Runtime.getRuntime().freeMemory() * 100L / Runtime.getRuntime().maxMemory() + "% free)");
    this.d[1] = ("Avg tick: " + a.format(a(this.e.h) * 1.0E-6D) + " ms");
    repaint();
  }
  
  private double a(long[] ☃)
  {
    long ☃ = 0L;
    for (int ☃ = 0; ☃ < ☃.length; ☃++) {
      ☃ += ☃[☃];
    }
    return ☃ / ☃.length;
  }
  
  public void paint(Graphics ☃)
  {
    ☃.setColor(new Color(16777215));
    ☃.fillRect(0, 0, 456, 246);
    for (int ☃ = 0; ☃ < 256; ☃++)
    {
      int ☃ = this.b[(☃ + this.c & 0xFF)];
      ☃.setColor(new Color(☃ + 28 << 16));
      ☃.fillRect(☃, 100 - ☃, 1, ☃);
    }
    ☃.setColor(Color.BLACK);
    for (int ☃ = 0; ☃ < this.d.length; ☃++)
    {
      String ☃ = this.d[☃];
      if (☃ != null) {
        ☃.drawString(☃, 32, 116 + ☃ * 16);
      }
    }
  }
}
