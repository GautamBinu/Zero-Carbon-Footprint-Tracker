import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    
    private final String LOG_FILE;

    public Logger() {
        // Constructor can be used to initialize any necessary resources or configurations for the logger if needed.
        this.LOG_FILE = "greenprint_log.txt";
    }

    

    public void log(String operation, String details) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {


            writer.write(String.format("{%s} : %s : [%s]\n", operation, details, java.time.LocalDateTime.now().toString()));
            writer.flush();

        } catch (IOException e) {
            System.out.println("Error writing to log file greenprint: " + e.getMessage());
        }
    }

}
