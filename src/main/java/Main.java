import org.apache.commons.cli.*;
import service.AvailabilityIntervalService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Double maxRuntime = 45D;
        Double minAccessLevel = 99.9;
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
            minAccessLevel = Optional.ofNullable(cmd.getOptionValue("u"))
                    .map(Double::parseDouble)
                    .orElse(minAccessLevel);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        AvailabilityIntervalService analyzer = new AvailabilityIntervalService(maxRuntime, minAccessLevel);
        analyzer.process(bufferedReader.lines())
                .forEach(System.out::println);
    }
}
