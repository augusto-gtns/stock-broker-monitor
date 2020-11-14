package hbm.processor;

import java.nio.file.Paths;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.util.FileUtil;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class DeleteFileProcessor implements Processor {

	public static final String NAME = "DeleteFileProcessor";
	
	@Override
	public void process(Exchange exchange) throws Exception {
		String filePathAndName = (String) exchange.getIn().getHeader(Exchange.FILE_PATH);
		String fileName = (String) exchange.getIn().getHeader(Exchange.FILE_NAME_ONLY);

		boolean deleted = false;
		try {
			deleted = FileUtil.deleteFile(Paths.get(filePathAndName).toFile());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		log.info("File: '" + fileName + "' was " + (deleted ? "deleted" : "not deleted"));
	}

}
