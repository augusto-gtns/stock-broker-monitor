package hbm.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import hbm.model.StockHistory;
import hbm.util.HBMCommonUtil;

@Component
public class HBMMonitorUtil extends HBMCommonUtil {

	private static Integer TODAY_ADJUST;

	@Value("${hbmonitor.todayAdjust:0}")
	private void setTodayAdjust(Integer todayAdjust) {
		HBMMonitorUtil.TODAY_ADJUST = todayAdjust;
	}

	public static Date getToday() {
		return DateUtils.addDays(new Date(), HBMMonitorUtil.TODAY_ADJUST);
	}
	
	@Autowired
	public static Gson gsonConverter() {
		
		 class BigDecimalTypeAdapter extends TypeAdapter<BigDecimal>{

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
				if(reader.peek() == JsonToken.NULL){
		            reader.nextNull();
		            return null;
		        }
				
		        String stringValue = reader.nextString();
		        try{
		        	if( StringUtils.isNotEmpty(stringValue))
		        	return new BigDecimal(stringValue);
		        }catch(NumberFormatException e){}
		        
		        return null;
			}
			 
		 }
		 
		 return new GsonBuilder().registerTypeAdapter(BigDecimal.class, new BigDecimalTypeAdapter()).create();
	}

	public static List<StockHistory> parseJsonStockHistoryList(String json) {
		return gsonConverter().fromJson(json, new TypeToken<List<StockHistory>>() {
		}.getType());
	}

	public static StockHistory parseJsonStockHistory(String json) {
		return gsonConverter().fromJson(json, new TypeToken<StockHistory>() {
		}.getType());
	}

	public static String toRoutePath(String path) throws FileNotFoundException {
		String newPath = ResourceUtils.getFile(path).getAbsolutePath().replace("\\", "/");
		newPath = newPath.endsWith("/") ? newPath : newPath + "/";
		return newPath;
	}

}
