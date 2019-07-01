import model.AvailabilityInterval;
import org.apache.commons.cli.*;
import service.AvailabilityIntervalService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Locale;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Double minAvailabilityLevel = 99.9;
        Double maxRuntime = 45D;
        Options options = new Options();
        options.addOption("u", true,
                String.format(Locale.US, "Minimum acceptable level of availability with float-point (default value %.2f.)", minAvailabilityLevel));
        options.addOption("t", true,
                String.format(Locale.US, "Acceptable response time in milliseconds with float-point (default value: %.2f; max value 100).", maxRuntime));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            minAvailabilityLevel = getOptionDoubleValue(cmd, "u", maxRuntime, 100D);
            maxRuntime = getOptionDoubleValue(cmd, "t", maxRuntime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        AvailabilityIntervalService analyzer = new AvailabilityIntervalService(maxRuntime, minAvailabilityLevel);
        analyzer.process(bufferedReader.lines())
                .sorted(Comparator.comparing(AvailabilityInterval::getStartDate))
                .map(analyzer::toMessage)
                .forEach(System.out::println);
    }

    private static Double getOptionDoubleValue(CommandLine commandLine, String key, Double defaultValue, Double maxValue){
        final Double value = defaultValue;
        String numberPattern = "^\\d+\\.?\\d+$";
        return Optional.ofNullable(commandLine.getOptionValue(key))
                .map(String::trim)
                .map(r -> r.matches(numberPattern)?r:null)
                .map(Double::parseDouble)
                .map(v -> {
                    if(maxValue != null)
                        return (v <= maxValue)?v:maxValue;
                    else
                        return v;
                })
                .orElseGet(() ->{
                    System.out.println("The default value " + value +" for the " + key + " parameter  is set.");
                    return value;
                });
    }

    private static Double getOptionDoubleValue(CommandLine commandLine, String key, Double defaultValue){
        return getOptionDoubleValue(commandLine, key, defaultValue, null);
    }
}
