package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class RequestResultTest {

    @Test
    @DisplayName("parse success test")
    void successParseTest() {
        String sourceString = "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=cceed874 HTTP/1.1\" 200 2 35.249855 \"-\" \"@list-item-updater\" prio:0";
        RequestResult expected = new RequestResult(
                OffsetDateTime.of(LocalDateTime.of(2017, 06, 14, 16, 47, 02),
                ZoneOffset.ofHours(10)),
                (short) 200, 35.249855);

        assertEquals(RequestResult.parse(sourceString), expected);
        assertEquals(Objects.requireNonNull(RequestResult.parse(sourceString)).getDateTime(), expected.getDateTime());
        assertEquals(Objects.requireNonNull(RequestResult.parse(sourceString)).getDateTime(), expected.getDateTime());
        assertEquals(Objects.requireNonNull(RequestResult.parse(sourceString)).getDateTime(), expected.getDateTime());
    }

    @Test
    @DisplayName("parse fail test")
    void failParseTest() {
        String invalidDateSource = "192.168.32.181 - - [14/06/:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=cceed874 HTTP/1.1\" 200 2 35.249855 \"-\" \"@list-item-updater\" prio:0";
        String invalidCodeSource1 = "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=cceed874 HTTP/1.1\" 2000 2 35.249855 \"-\" \"@list-item-updater\" prio:0";
        String invalidCodeSource2 = "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=cceed874 HTTP/1.1\" 2X0 2 35.249855 \"-\" \"@list-item-updater\" prio:0";
        String invalidCodeSource3 = "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=cceed874 HTTP/1.1\" 20 2 35.249855 \"-\" \"@list-item-updater\" prio:0";
        String invalidTimeSource = "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=cceed874 HTTP/1.1\" 200 2 3X.249855 \"-\" \"@list-item-updater\" prio:0";

        assertNull(RequestResult.parse(invalidDateSource));
        assertNull(RequestResult.parse(invalidCodeSource1));
        assertNull(RequestResult.parse(invalidCodeSource2));
        assertNull(RequestResult.parse(invalidCodeSource3));
        assertNull(RequestResult.parse(invalidTimeSource));


    }
}