package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
public class RequestResult {
    private OffsetDateTime dateTime;
    private Short statusCode;
    private Double runtime;

    public static RequestResult parse(String string) {
        final Pattern pattern = Pattern.compile("^.+\\[(\\d{2}\\/\\d{2}\\/\\d{4}:\\d{2}:\\d{2}:\\d{2}\\s\\+\\d{4})\\].+\\\".+\\\"\\s(\\d{3})\\s\\d+\\s(\\d+\\.\\d+).+$");
        final Matcher matcher = pattern.matcher(string);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy:HH:mm:ss Z");

        if (matcher.matches()) {
            return new RequestResult(OffsetDateTime.parse(matcher.group(1), dateTimeFormatter),
                    Short.parseShort(matcher.group(2)),
                    Double.parseDouble(matcher.group(3)));
        }
        return null;
    }
}
