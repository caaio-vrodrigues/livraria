package caio.portfolio.livraria.config;

import java.util.Locale;
import java.util.Set;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class CountryValidatorConfig {

	@Bean
	@ConditionalOnMissingBean
	Set<String> validIsoCodes(){
		return Set.of(Locale.getISOCountries());
	}
	
	@Bean
	@Profile(value="test")
	Set<String> validIsoCodesTest(){
		return Set.of("BR", "IT", "AR", "FR", "US");
	}
}
