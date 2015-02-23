/*
 * Created on 27.07.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ctvdkip.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

/**
 * Splash Window
 * This Class implements a SplasWindow
 * @author Ralf Bust
 * @version 1.0
 */

public class SplashWindow extends JWindow {

	private static final long serialVersionUID = 1L;
	
public SplashWindow(URL p_url, int p_waitTime){
      Image image = Toolkit.getDefaultToolkit().getImage(p_url);
      new SplashWindow(new ImageIcon(image),p_waitTime);
  };
/**
 * 
 * @param p_image The Image for the Splash Window
 * @param p_waitTime
 */
  public SplashWindow(ImageIcon p_image, int p_waitTime)
  {
    JLabel l = new JLabel(p_image);
    getContentPane().add(l, BorderLayout.CENTER);
    pack();
    Dimension screenSize =
        Toolkit.getDefaultToolkit().getScreenSize();
    Dimension labelSize = l.getPreferredSize();
    setLocation(screenSize.width/2 - (labelSize.width/2),
                screenSize.height/2 - (labelSize.height/2));
    addMouseListener(new MouseAdapter()
    {
      public void mousePressed(MouseEvent e)
      {
        setVisible(false);
        dispose();
      }
    });
    final int pause = p_waitTime;
    final Runnable closerRunner = new Runnable()
    {
      public void run()
      {
        setVisible(false);
        dispose();
      }
    };
    Runnable waitRunner = new Runnable()
    {
      public void run()
      {
        try
        {
          Thread.sleep(pause);
          SwingUtilities.invokeAndWait(closerRunner);
        }
        catch(Exception e)
        {
          e.printStackTrace();
          // can catch InvocationTargetException
          // can catch InterruptedException
        }
      }
    };
    setVisible(true);
    Thread splashThread = new Thread(waitRunner, "SplashThread");
    splashThread.start();
  }

}