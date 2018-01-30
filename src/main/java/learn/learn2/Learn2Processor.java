package learn.learn2;

import learn.Processor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Learn2Processor implements Processor<String, List<String>> {

    final JdbcTemplate template;

    public Learn2Processor(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<String> process(String in) {
        return template.queryForList("SELECT full_name FROM log_data where entry_date = ?",
                new Object[]{in}, String.class);
    }
}
