import service.AvailabilityIntervalService;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
                ("192.168.32.181 - - [14/06/2017:16:46:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0\n" +
                        "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0\n" +
                        "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=7ae28555 HTTP/1.1\" 200 2 23.251219 \"-\" \"@list-item-updater\" prio:0\n" +
                        "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=7ae28555 HTTP/1.1\" 200 2 23.251219 \"-\" \"@list-item-updater\" prio:0\n" +
                        "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=7ae28555 HTTP/1.1\" 200 2 33.251219 \"-\" \"@list-item-updater\" prio:0\n" +
                        "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=e356713 HTTP/1.1\" 200 2 21.164372 \"-\" \"@list-item-updater\" prio:0").getBytes())));

        AvailabilityIntervalService analyzer = new AvailabilityIntervalService(25D, 99D);
        analyzer.process(bufferedReader.lines())
                .forEach(System.out::println);
    }
}
