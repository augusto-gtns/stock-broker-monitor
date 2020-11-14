package hbm.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class HBMMonitorUtil {
	
	private static Gson gsonConverter;
	
	public static Gson gsonConverter() {
		return gsonConverter;
	}
	
	@Autowired
	private void configGsonConverter() {

		class BigDecimalTypeAdapter extends TypeAdapter<BigDecimal> {

			@Override
			public void write(JsonWriter writer, BigDecimal value) throws IOException {
				if (value == null) {
					writer.nullValue();
					return;
				}
				writer.value(value);
			}

			@Override
			public BigDecimal read(JsonReader reader) throws IOException {
				if (reader.peek() == JsonToken.NULL) {
					reader.nextNull();
					return null;
				}

				String stringValue = reader.nextString();
				try {

					if (StringUtils.isNotEmpty(stringValue))
						return new BigDecimal(stringValue);
					
				} catch (NumberFormatException e) {
					log.error(e.getMessage(), e);
				}
				
				return null;
			}

		}

		gsonConverter = new GsonBuilder().registerTypeAdapter(BigDecimal.class, new BigDecimalTypeAdapter()).create();
	}
	
	public static String toRoutePath(String path) throws FileNotFoundException {
		String newPath = ResourceUtils.getFile(path).getAbsolutePath().replace("\\", "/");
		newPath = newPath.endsWith("/") ? newPath : newPath + "/";
		return newPath;
	}


}
