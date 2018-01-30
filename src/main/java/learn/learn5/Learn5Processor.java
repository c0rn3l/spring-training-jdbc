package learn.learn5;

import learn.Processor;
import learn.learn4.Learn4Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component
public class Learn5Processor implements Processor <String, List<Integer>> {

    JdbcTemplate template;

    @Autowired
    public Learn5Processor (JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Integer> process(String in) {
        List<Object[]> batches = template.query(
                "SELECT full_name, COUNT(*) as `day_count` FROM log_data GROUP BY full_name",
                (rs, i) ->
                        new Object[]{rs.getString("full_name"), rs.getInt("day_count")});

        batch2(batches, 50);
        return null;
    }

    private void batch2(List<Object[]> batches, int maxPerBatch) {
        template.batchUpdate("INSERT INTO benefits (full_name, percent) values (?, ?)", batches,
                maxPerBatch, (ps, arg) -> {
                    ps.setString(1, Objects.toString(arg[0]));
                    ps.setInt(2, (Integer) arg[1]);
                });
    }

    public List<Integer> batch1(List<Object[]> batches) {
        int[] affected = template.batchUpdate("INSERT INTO benefits (full_name, percent) values (?, ?)", batches);
        List<Integer> result = new LinkedList<>();
        for (int j = 0; j < affected.length; j++) {
            result.add(affected[j]);
        }

        return result;
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
