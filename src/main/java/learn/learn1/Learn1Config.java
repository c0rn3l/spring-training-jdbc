package learn.learn1;

import learn.Config;
import learn.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@Import(Config.class)
public class Learn1Config {

    @Bean
    Processor<String, Integer> processor(JdbcTemplate template) {
        return s -> template.queryForObject("Select count(*) from LOG_DATA", Integer.class);
    }
}
