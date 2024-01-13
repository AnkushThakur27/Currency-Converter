import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
public class CurrencyConverter {

        private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/USD";
        private static final Map<String, Double> exchangeRates = new HashMap<>();

        public static void main(String[] args) {
            loadExchangeRates();

            // Sample usage (you can replace this with user input or other interaction logic)
            String fromCurrency = "USD";
            String toCurrency = "EUR";
            double amount = 100.0;

            try {
                double convertedAmount = convertCurrency(fromCurrency, toCurrency, amount);
                System.out.printf("%.2f %s = %.2f %s%n", amount, fromCurrency, convertedAmount, toCurrency);

                // Sprint 3 - Historical Conversion Rates
                LocalDate date = LocalDate.of(2023, 1, 1);
                double historicalRate = getHistoricalRate(fromCurrency, toCurrency, date);
                System.out.printf("Historical rate on %s: %.4f%n", date, historicalRate);

            } catch (CurrencyConverterException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        // Sprint 3 - Add Support for Multiple Currencies
        private static void loadExchangeRates() {
            try {
                URL url = new URL(API_URL);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                String jsonString = response.toString();
                exchangeRates.putAll(JsonParser.parseExchangeRates(jsonString));

            } catch (IOException e) {
                throw new CurrencyConverterException("Error loading exchange rates: " + e.getMessage());
            }
        }

        private static double getExchangeRate(String fromCurrency, String toCurrency) {
            if (!exchangeRates.containsKey(fromCurrency) || !exchangeRates.containsKey(toCurrency)) {
                throw new CurrencyConverterException("Invalid currency code(s).");
            }

            double fromRate = exchangeRates.get(fromCurrency);
            double toRate = exchangeRates.get(toCurrency);

            return toRate / fromRate;
        }

        // Sprint 3 - Implement Historical Conversion Rates
        private static double getHistoricalRate(String fromCurrency, String toCurrency, LocalDate date) {
            // Implement logic to fetch historical rates from a suitable API or data source
            // For simplicity, this example returns a constant value
            return 0.9;
        }

        private static double convertCurrency(String fromCurrency, String toCurrency, double amount) {
            double exchangeRate = getExchangeRate(fromCurrency, toCurrency);
            return amount * exchangeRate;
        }

        // Sprint 4 - Implement Error Handling
        private static class CurrencyConverterException extends RuntimeException {
            public CurrencyConverterException(String message) {
                super(message);
            }
        }

        // Sprint 4 - Refactor Code for Modularity
        private static class JsonParser {
            private JsonParser() {} // Prevent instantiation

            static Map<String, Double> parseExchangeRates(String jsonString) {
                // Implement logic to parse exchange rates from JSON response
                // For simplicity, this example returns a constant map
                Map<String, Double> rates = new HashMap<>();
                rates.put("USD", 1.0);
                rates.put("EUR", 0.85);
                rates.put("GBP", 0.73);
                // ... Add more currencies as needed
                return rates;
            }
        }
    }

