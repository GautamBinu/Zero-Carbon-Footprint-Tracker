import javafx.concurrent.Task;
import javafx.application.Platform;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * TCP client for requesting discounts from the FootprintDiscountServer.
 * All socket operations run on a background thread using JavaFX Task.
 * UI callbacks are marshalled back to the JavaFX Application Thread.
 */
public class FootprintDiscountClient {

    // Making our own functional interfaces to handle success and error callbacks with custom parameters
    public interface DiscountSuccessHandler {
        void handle(DiscountResult result);
    }

    
    public interface DiscountErrorHandler {
        void handle(String errorMessage);
    }

    /**
     * Requests a discount from the server for the given emission value.
     * All I/O operations run on a background thread. Callbacks are executed on the JavaFX Application Thread.
     *
     * @param emissionValue the CO2 emission amount to request a discount for
     * @param onSuccess callback executed on success, receives DiscountResult
     * @param onError callback executed on error, receives error message string
     */
    public static void requestDiscount(double emissionValue, DiscountSuccessHandler onSuccess, DiscountErrorHandler onError) {
    
    
    Thread backgroundThread = new Thread(new Runnable() {

        @Override
        public void run() {
            
            try {
                // 2. Try to get the data (Runs in background)
                DiscountResult result = performDiscountRequest(emissionValue);
                
                // If successful, safely update the UI 
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess.handle(result);
                    }
                });
                
            } catch (Exception e) {
                
                // 4. If it crashes, extract the error message (using ternary to handle null cases)
                String errorMessage = (e != null) ? e.getMessage() : "Unknown error occurred.";
                
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        onError.handle(errorMessage);
                    }
                });
            }
        }
    });
    
   
    backgroundThread.start();
}

    /**
     * Performs the actual socket communication with the discount server.
     * This method runs on a background thread and should NOT be called directly from the UI thread.
     *
     * @param emissionValue the CO2 emission value to send to the server
     * @return DiscountResult containing the server's response
     * @throws Exception if connection fails, timeout occurs, or response is malformed
     */
    private static DiscountResult performDiscountRequest(double emissionValue) throws Exception {
        String host = DiscountClientConfig.SERVER_HOST;
        int port = DiscountClientConfig.SERVER_PORT;
        int timeout = DiscountClientConfig.SOCKET_TIMEOUT_MS;

        try (Socket socket = new Socket(host, port)) {
            // Set socket timeout for read operations
            socket.setSoTimeout(timeout);

            // Send emission value to server (don't close the PrintWriter yet - it would close the socket)
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(String.valueOf(emissionValue));
           

            // Read response from server
            String response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                response = in.readLine();
            }

            // Validate and parse response
            if (response == null || response.trim().isEmpty()) {
                throw new IOException("Empty response from server");
            }

            // Parse response format: DISCOUNT:percentage:discountedValue
            return parseDiscountResponse(response, emissionValue);

        } catch (SocketTimeoutException e) {
            throw new IOException("Request timed out. Please try again.");
        } catch (java.net.ConnectException e) {
            throw new IOException("Could not reach server. Please try again later.");
        } catch (java.net.NoRouteToHostException | java.net.UnknownHostException e) {
            throw new IOException("Could not reach server. Please try again later.");
        }
    }

    /**
     * Parses the discount response from the server.
     * Expected format: DISCOUNT:percentage:discountedValue
     * Example: DISCOUNT:15:10.84
     *
     * @param response the raw response string from the server
     * @param originalValue the original emission value sent to the server
     * @return DiscountResult containing parsed values
     * @throws IOException if response format is invalid
     */
    private static DiscountResult parseDiscountResponse(String response, double originalValue) throws IOException {
        try {
            String[] parts = response.split(":");

            if (parts.length != 3) {
                throw new IOException("Invalid response from server. Please try again.");
            }

            if (!parts[0].equals("DISCOUNT")) {
                throw new IOException("Invalid response from server. Please try again.");
            }

            int discountPercentage = Integer.parseInt(parts[1]);
            double discountedValue = Double.parseDouble(parts[2]);

            // Validate discount percentage is in expected range (1-30)
            if (discountPercentage < 1 || discountPercentage > 30) {
                throw new IOException("Invalid discount percentage received from server.");
            }

            // Validate discounted value makes sense
            if (discountedValue < 0 || discountedValue > originalValue) {
                throw new IOException("Invalid discounted value received from server.");
            }

            return new DiscountResult(originalValue, discountPercentage, discountedValue);

        } catch (NumberFormatException e) {
            throw new IOException("Invalid response from server. Please try again.");
        }
    }
}
