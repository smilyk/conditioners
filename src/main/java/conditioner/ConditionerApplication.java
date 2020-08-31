package conditioner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class ConditionerApplication  implements ApplicationRunner {
	private static final Logger logger = LogManager.getLogger(ConditionerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ConditionerApplication.class, args);
	}
	@Override
	public void run(ApplicationArguments applicationArguments){
		logger.info("Logger start");
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}


}
