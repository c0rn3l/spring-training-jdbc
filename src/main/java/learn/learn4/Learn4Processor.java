package learn.learn4;

import learn.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class Learn4Processor implements Processor<String, List<Learn4Processor.CountPerName>> {

    private final JdbcTemplate template;
    private final SimpleJdbcInsert insertBenefits;

    @Autowired
    public Learn4Processor (JdbcTemplate template, SimpleJdbcInsert insertBenefits) {
        this.template = template;
        this.insertBenefits = insertBenefits;
    }

    public List<CountPerName> process(String in) {
        List<CountPerName> counts = new LinkedList<>();
        template.query(
                "SELECT full_name, COUNT(*) as `day_count` FROM log_data GROUP BY full_name",
                rs -> {
                    int count = rs.getInt("day_count");
                    if(count > 1) {
                        counts.add(new CountPerName(rs.getString("full_name"), count));
                    }
                });

        insert2(counts);
        return counts;
    }

    private void insert1(List<CountPerName> counts) {
        int sum = 0;
        for (CountPerName count : counts) {
            sum += template.update("INSERT INTO benefits (full_name, percent) VALUES (?, ?)", count.getName(), count.getCount());
        }

        System.out.println("Total number of rows affected: " + sum);
    }

    private void insert2(List<CountPerName> counts){
        for (CountPerName count : counts) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("full_name", count.getName());
            parameters.put("percent", count.getCount());
            insertBenefits.execute(parameters);
        }
    }

    public static class CountPerName {
        private final String name;
        private final Integer count;

        public CountPerName(String name, Integer count) {
            this.count = count;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Integer getCount() {
            return count;
        }

        @Override
        public String toString() {
            return "CountPerName{" +
                    "name='" + name + '\'' +
                    ", count=" + count +
                    '}';
        }
    }
}
