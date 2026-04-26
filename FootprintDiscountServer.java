import java.io.*;
import java.net.*;
import java.util.Random;

public class FootprintDiscountServer {
    private static final int PORT = 6700; // Documented Port

    /**
     * Starts the discount server which listens for incoming connections and processes discount requests.
     * This server runs indefinitely until manually stopped. It handles one client connection at a time.
     * Each client request should contain a single line with the CO2 emission value in kg. The server responds with a discount percentage and the discounted emission value.
     * 
     * @param args
     */

    public static void main(String[] args) {
        System.out.println("[SERVER] Discount Server started on port " + PORT);
        Random random = new Random();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Accept one client connection at a time in a loop
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    // Read emission value from client
                    String input = in.readLine();
                    if (input != null) {
                        double totalEmission = Double.parseDouble(input);

                        if (totalEmission < 0) { throw new IOException("Negative emission value received"); }
                        
                        // Generate random discount 1-30
                        int discountPct = random.nextInt(30) + 1;

                        if (discountPct < 1 || discountPct > 30) { throw new IllegalArgumentException("Discount must be between 1 and 30, but was: " + discountPct); }
                        double discountedValue = totalEmission * (1.0 - discountPct / 100.0);

                        // Format response: DISCOUNT:pct:value
                        String response = String.format("DISCOUNT:%d:%.2f", discountPct, discountedValue);
                        out.println(response);

                        // Console Log
                        System.out.printf("[SERVER] Client connected | Sent: %.2f | Discount: %d%% | Result: %.2f%n", 
                                          totalEmission, discountPct, discountedValue);
                    }
                } catch (IOException | NumberFormatException e) {
                    System.out.println("[SERVER] Error processing transaction: " + e.getMessage());
                    // Loop continues so server doesn't crash
                }
            }
        } catch (IOException e) {
            System.out.println("[SERVER] Could not listen on port " + PORT);
        }
    }
}