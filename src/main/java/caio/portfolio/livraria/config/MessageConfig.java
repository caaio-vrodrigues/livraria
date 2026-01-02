package caio.portfolio.livraria.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageConfig {
	
	@Bean
    public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = 
			new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames(
			"classpath:message/salable_book_messages",
			"classpath:message/salable_book_messages",
			"classpath:message/country_messages",
			"classpath:message/author_messages",
			"classpath:message/publisher_messages",
			"classpath:message/exception_handler_messages",
			"classpath:message/controller_messages",
			"classpath:message/serialization_messages",
			"classpath:ValidationMessages"
		);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setAlwaysUseMessageFormat(true);
        return messageSource;
    }
}