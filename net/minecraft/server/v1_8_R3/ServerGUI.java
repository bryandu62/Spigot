package net.minecraft.server.v1_8_R3;

import com.mojang.util.QueueLogAppender;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerGUI
  extends JComponent
{
  private static final Font a = new Font("Monospaced", 0, 12);
  private static final Logger b = LogManager.getLogger();
  private DedicatedServer c;
  
  public static void a(DedicatedServer ☃)
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception localException) {}
    ServerGUI ☃ = new ServerGUI(☃);
    JFrame ☃ = new JFrame("Minecraft server");
    ☃.add(☃);
    ☃.pack();
    ☃.setLocationRelativeTo(null);
    ☃.setVisible(true);
    ☃.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent ☃)
      {
        this.a.safeShutdown();
        while (!this.a.isStopped()) {
          try
          {
            Thread.sleep(100L);
          }
          catch (InterruptedException ☃)
          {
            ☃.printStackTrace();
          }
        }
        System.exit(0);
      }
    });
  }
  
  public ServerGUI(DedicatedServer ☃)
  {
    this.c = ☃;
    setPreferredSize(new Dimension(854, 480));
    
    setLayout(new BorderLayout());
    try
    {
      add(c(), "Center");
      add(a(), "West");
    }
    catch (Exception ☃)
    {
      b.error("Couldn't build server GUI", ☃);
    }
  }
  
  private JComponent a()
    throws Exception
  {
    JPanel ☃ = new JPanel(new BorderLayout());
    ☃.add(new GuiStatsComponent(this.c), "North");
    ☃.add(b(), "Center");
    ☃.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
    return ☃;
  }
  
  private JComponent b()
    throws Exception
  {
    JList ☃ = new PlayerListBox(this.c);
    JScrollPane ☃ = new JScrollPane(☃, 22, 30);
    ☃.setBorder(new TitledBorder(new EtchedBorder(), "Players"));
    
    return ☃;
  }
  
  private JComponent c()
    throws Exception
  {
    JPanel ☃ = new JPanel(new BorderLayout());
    final JTextArea ☃ = new JTextArea();
    final JScrollPane ☃ = new JScrollPane(☃, 22, 30);
    ☃.setEditable(false);
    ☃.setFont(a);
    
    final JTextField ☃ = new JTextField();
    ☃.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent ☃)
      {
        String ☃ = ☃.getText().trim();
        if (☃.length() > 0) {
          ServerGUI.a(ServerGUI.this).issueCommand(☃, MinecraftServer.getServer());
        }
        ☃.setText("");
      }
    });
    ☃.addFocusListener(new FocusAdapter()
    {
      public void focusGained(FocusEvent ☃) {}
    });
    ☃.add(☃, "Center");
    ☃.add(☃, "South");
    ☃.setBorder(new TitledBorder(new EtchedBorder(), "Log and chat"));
    
    Thread ☃ = new Thread(new Runnable()
    {
      public void run()
      {
        String ☃;
        while ((☃ = QueueLogAppender.getNextLogEvent("ServerGuiConsole")) != null) {
          ServerGUI.this.a(☃, ☃, ☃);
        }
      }
    });
    ☃.setDaemon(true);
    ☃.start();
    
    return ☃;
  }
  
  public void a(final JTextArea ☃, final JScrollPane ☃, final String ☃)
  {
    if (!SwingUtilities.isEventDispatchThread())
    {
      SwingUtilities.invokeLater(new Runnable()
      {
        public void run()
        {
          ServerGUI.this.a(☃, ☃, ☃);
        }
      });
      return;
    }
    Document ☃ = ☃.getDocument();
    JScrollBar ☃ = ☃.getVerticalScrollBar();
    boolean ☃ = false;
    if (☃.getViewport().getView() == ☃) {
      ☃ = ☃.getValue() + ☃.getSize().getHeight() + a.getSize() * 4 > ☃.getMaximum();
    }
    try
    {
      ☃.insertString(☃.getLength(), ☃, null);
    }
    catch (BadLocationException localBadLocationException) {}
    if (☃) {
      ☃.setValue(Integer.MAX_VALUE);
    }
  }
}
