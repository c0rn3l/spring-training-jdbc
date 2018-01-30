package learn.learn3;

import learn.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class Learn3Processor implements Processor<String, List<String>> {

    final NamedParameterJdbcTemplate template;

    @Autowired
    public Learn3Processor(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<String> process(String in) {
        List<String> dates = Arrays.asList(in.split(";"));
        return template.queryForList("SELECT full_name FROM log_data where entry_date in (:dates)",
               Collections.singletonMap("dates", dates), String.class);
    }
}
