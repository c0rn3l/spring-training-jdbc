package learn.learn4;

import learn.Config;
import learn.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@Import(Config.class)
public class Learn4Config {

    @Bean
    public SimpleJdbcInsert insertBenefit(DataSource ds) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(ds);
        simpleJdbcInsert.withTableName("benefits");
        simpleJdbcInsert.usingColumns("full_name", "percent");
        return simpleJdbcInsert;
    }
}
