package hbm.util;

public class ExceptionUtil {

	public static String getStackTraceMinified(Throwable e) {
		String detail = e.getClass().getName() + ": " + e.getMessage();
		detail += buildMsg(e);

		while ((e = e.getCause()) != null) {
			detail += "\nCaused by: ";
			detail += buildMsg(e);
		}

		return detail;
	}

	private static String buildMsg(Throwable e) {
		String msg = "";

		int lines = 2;
		for (int i = 0; i < lines; i++) {
			try {
				StackTraceElement s = e.getStackTrace()[i];
				msg += "\n\t" + s.toString();
			} catch (Exception e2) {
				break;
			}
		}

		if (msg != "")
			msg += "...";

		return msg;
	}

}
