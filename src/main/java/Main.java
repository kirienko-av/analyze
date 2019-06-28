import org.apache.commons.cli.*;
import service.AvailabilityIntervalService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Double maxRuntime = 45D;
        Double minAvailabilityLevel = 99.9;
        Options options = new Options();
        options.addOption("u", true, "Minimum acceptable level of availability.");
        options.addOption("t", true, "Acceptable response time (milliseconds).");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            maxRuntime = Optional.ofNullable(cmd.getOptionValue("t"))
                    .map(Double::parseDouble)
                    .orElse(maxRuntime);
            minAvailabilityLevel = Optional.ofNullable(cmd.getOptionValue("u"))
                    .map(Double::parseDouble)
                    .orElse(minAvailabilityLevel);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        AvailabilityIntervalService analyzer = new AvailabilityIntervalService(maxRuntime, minAvailabilityLevel);
        analyzer.process(bufferedReader.lines())
                .map(analyzer::toMessage)
                .forEach(System.out::println);
    }
}
