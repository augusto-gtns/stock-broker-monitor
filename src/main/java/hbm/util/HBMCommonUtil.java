package hbm.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

public abstract class HBMCommonUtil {
	
	private static String ENV;

	@Value("${spring.profiles.active}")
	private void setEnv(String env) {
		HBMCommonUtil.ENV = env;
	}

	public static boolean isEnvPRD() {
		try {
			String prd = new String("prd");
			List<String> envs = Arrays.asList(ENV.split(","));
			for (String env : envs)
				if (env.trim().equals(prd))
					return true;
		} catch (Exception e) {}

		return false;
	}
	
}
