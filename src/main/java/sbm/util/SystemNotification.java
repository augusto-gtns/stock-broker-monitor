package sbm.util;

import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class SystemNotification {

	private static TrayIcon notifier;

	@Autowired
	private void configNotifier() {
		try {
			TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage("icon.png"), "HBM");
			trayIcon.setImageAutoSize(true);

			SystemTray tray = SystemTray.getSystemTray();
			tray.add(trayIcon);
			
			notifier = trayIcon;

		} catch (Throwable e) {
			log.error("System notification not supported");
		}
	}

	public static void send(String msg) {
		
		if( notifier !=null )
			notifier.displayMessage("SBM Warning", msg, MessageType.INFO);
	
	}
}
