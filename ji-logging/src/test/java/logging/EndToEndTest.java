package logging;

import common.Logger;

public class EndToEndTest {

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger("logger");
		logger.info("message");
	}
	
}
