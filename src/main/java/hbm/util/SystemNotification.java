package hbm.util;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hbm.util.ExceptionUtil;

public class SystemNotification {

	private static final Logger log = LoggerFactory.getLogger(SystemNotification.class);;

	private static TrayIcon notifier = null;

	public static void send(String msg) {
		try {
			if (notifier == null) {
				SystemTray tray = SystemTray.getSystemTray();

				Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
				// Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

				TrayIcon trayIcon = new TrayIcon(image, "HBM");
				trayIcon.setImageAutoSize(true);
				
				trayIcon.addMouseListener(new MouseAdapter() {
				    public void mouseClicked(MouseEvent e) {
				    	System.out.println("Clicked!");
				    }
				}); 
				
				tray.add(trayIcon);
				
				notifier = trayIcon;
			}

			notifier.displayMessage("HBM Warning", msg.replace("|", "\n"), MessageType.INFO);

		} catch (Throwable e) {
			log.error("System notification not supported");
			log.error(ExceptionUtil.getStackTraceMinified(e));
		}
	}
}
