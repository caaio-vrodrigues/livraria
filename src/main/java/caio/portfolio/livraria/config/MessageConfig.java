package caio.portfolio.livraria.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageConfig {
	
	@Bean
    public MessageSource salableBookMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = 
        	new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:message/salable_book_messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setAlwaysUseMessageFormat(true);
        return messageSource;
    }
	
	@Bean
    public MessageSource countryMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = 
        	new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:message/country_messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setAlwaysUseMessageFormat(true);
        return messageSource;
    }
	
	@Bean
    public MessageSource authorMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = 
        	new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:message/author_messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setAlwaysUseMessageFormat(true);
        return messageSource;
    }
	
	@Bean
    public MessageSource publisherMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = 
        	new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:message/publisher_messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setAlwaysUseMessageFormat(true);
        return messageSource;
    }
	
	@Bean
    public MessageSource exceptionHandlerMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = 
        	new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:message/exception_handler_messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setAlwaysUseMessageFormat(true);
        return messageSource;
    }
}