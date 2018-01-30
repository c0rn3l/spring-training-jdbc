package learn;

import org.h2.tools.Server;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public class Launcher {
    public static void main(String[] args) throws IOException {
        String pkg = "learn." + args[0];
        try(AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(pkg)) {
            startConsole(context.getBean(DataSource.class));

            System.out.print("Result: ");
            System.out.println(context.getBean(Processor.class).process(args.length > 1 ? args[1] : null));

            System.out.println("Press any key to exit");
            System.in.read();
        }
        System.exit(0);
    }

    public static void startConsole(DataSource dataSource){
        Thread t = new Thread(() -> {
            try {
                Server.startWebServer(dataSource.getConnection());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, "h2-console");
        t.setDaemon(true);
        t.start();
    }
}
