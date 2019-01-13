package com.cloudcmr.app.member.web.rest.fileParser;

import com.cloudcmr.app.member.domain.Address;
import com.cloudcmr.app.member.domain.Member;
import com.cloudcmr.app.member.domain.Phone;
import com.cloudcmr.app.domain.enumeration.GenderType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

public class CSVParser implements Parser {

    private static final DateTimeFormatter birthDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter licenceDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final String MALE = "M";

    private final InputStream inputStream;

    public CSVParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public List<Member> parse() throws IOException {
        List<Member> members = new ArrayList<>();
        LineReader lineReader = new LineReader(new BufferedReader(new InputStreamReader(inputStream)));
        while (lineReader.hasLine()) {
            String[] columns = lineReader.next().split();
            if(isHeaderLine(columns[0])) {
                continue;
            }
            Member member = new Member();
            member.setLicenceNumber(columns[0]);
            member.setLastName(columns[2]);
            member.setFirstName(columns[3]);
            member.setGender(MALE.equalsIgnoreCase(columns[4]) ? GenderType.MALE : GenderType.FEMALE);
            member.setBirthDate(LocalDate.parse(columns[6], birthDateFormatter));
            member.setEmail(columns[15].toLowerCase());
            member.setUscaNumber(columns[23]);

            TemporalAccessor temporalAccessor = licenceDateFormatter.parse(columns[19] + " " + columns[20]);
            LocalDateTime localDateTime = LocalDateTime.from(temporalAccessor);
            ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
            Instant result = Instant.from(zonedDateTime);
            member.setLicenceCreationDate(result);

            member.setSubscription(columns[17]);
            member.setSeason(Integer.parseInt(columns[18]));

            Address address = new Address();
            address.setAddress1(columns[7]);
            address.setAddress2(columns[8]);
            address.setAddress3(columns[9]);
            address.setZipcode(columns[10]);
            address.setCity(columns[11]);
            member.addAddresses(address);

            Phone phone1 = new Phone();
            phone1.setPhoneNumber(columns[13]);
            member.addPhones(phone1);
            Phone phone2 = new Phone();
            phone2.setPhoneNumber(columns[14]);
            member.addPhones(phone2);

            member.setSFF(true);
            members.add(member);
        }
        return members;
    }

    private boolean isHeaderLine(String column) {
        return column.contains("licence");
    }

    private class Line {
        private static final String COLUMN_SEPARATOR = ";";
        private final String line;

        public Line(String line) {
            this.line = line;
        }

        private String[] split() {
            return line.split(COLUMN_SEPARATOR);
        }

        public boolean isEmpty() {
            return line == null;
        }
    }

    private class LineReader {
        private final BufferedReader br;
        private Line currentLine;

        public LineReader(BufferedReader bufferedReader) throws IOException {
            this.br = bufferedReader;
            currentLine = new Line(br.readLine());
        }

        public boolean hasLine() {
            return !currentLine.isEmpty();
        }

        public Line next() throws IOException {
            Line prevLine = currentLine;
            currentLine = new Line(br.readLine());
            return prevLine;
        }
    }
}
