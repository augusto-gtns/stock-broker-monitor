package hbm.processor;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hbm.model.StockHistory;
import hbm.util.HBMMonitorUtil;
import lombok.extern.log4j.Log4j2;

@Component(HomeBrokerFileSplitProcessor.NAME)
@Log4j2
public class HomeBrokerFileSplitProcessor implements Processor {

	public static final String STOCK_ITEM_FOLDER = "stockItem";

	public static final String NAME = "HomeBrokerFileSplitProcessor";

	@Value("${hbmonitor.hb.folder}")
	private String folder;

	@Override
	public void process(Exchange exchange) throws Exception {
		String body = exchange.getIn().getBody(String.class);
		String filePath = (String) exchange.getIn().getHeader(Exchange.FILE_PATH);
		String fileName = (String) exchange.getIn().getHeader(Exchange.FILE_NAME_ONLY);

		List<StockHistory> itens = HBMMonitorUtil.parseJsonStockHistoryList(body);

		Matcher matcher = Pattern.compile("\\d+").matcher(fileName);
		boolean founded = matcher.find();
		String id = founded ? matcher.group(0) : String.valueOf(System.currentTimeMillis());

		Files.createDirectories(Paths.get(generateNewFilePath(filePath)));

		for (StockHistory iten : itens) {
			try {
				FileOutputStream out = new FileOutputStream(
						generateNewFilePath(filePath) + generateNewFileName(iten.getStockDescription(), id));
				out.write(HBMMonitorUtil.gsonConverter().toJson(iten).getBytes());
				out.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}

			if (!HBMMonitorUtil.isEnvPRD())
				break;
		}

		log.info("File '" + fileName + "' splited");
	}

	private String generateNewFilePath(String filePath) {
		return Paths.get(filePath).getParent() + File.separator + STOCK_ITEM_FOLDER + File.separator;
	}

	private String generateNewFileName(String stockName, String id) {
		return stockName + "-" + id + ".txt";
	}

}
