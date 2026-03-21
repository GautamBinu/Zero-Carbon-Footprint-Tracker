/**
 * Main application class that initializes and manages the GreenPrint application.
 * Serves as the entry point and holds the global tracker instance.
 */
public class GreenPrintCLI {
    public static FootprintTracker tracker;
    private static Logger logger;

    static {
        // Initialize the tracker and logger
        tracker = new FootprintTracker();
        logger = new Logger();
        
        // Load any previously saved state
        Logger.loadState();
    }

    /**
     * Gets the global tracker instance
     * @return the FootprintTracker instance
     */
    public static FootprintTracker getTracker() {
        return tracker;
    }

    /**
     * Gets the logger instance
     * @return the Logger instance
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Saves the current state to file
     */
    public static void saveApplication() {
        Logger.saveState();
    }
}
