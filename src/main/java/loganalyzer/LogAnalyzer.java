package loganalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogAnalyzer {
    private BufferedReader reader;
    private Float maxResponseTime;
    private Float permittedLevel;


    public LogAnalyzer(BufferedReader reader, Float maxResponseTime, Float permittedLevel) {
        this.reader = reader;
        this.maxResponseTime = maxResponseTime;
        this.permittedLevel = permittedLevel;
    }

    public void parse() throws IOException {
        List<OutPut> list = new ArrayList<>();
        String line;
        boolean serverState = true;
        LocalTime start = null, end;
        try {
            while ((line = reader.readLine()) != null) {
                LocalTime time = getTime(line);
                if (checkStatus(line) && serverState) {
                    serverState = false;
                    start = time;
                }
                if (!serverState && !checkStatus(line)) {
                    serverState = true;
                    end = time;
                    list.add(new OutPut(start, end, upLevel(start, end)));
                }

            }
            list.stream().filter(e -> e.getUpTime() < permittedLevel).forEach(System.out::println);

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка формата лога");
        }
    }

    private LocalTime getTime(String line) throws IllegalArgumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" dd/MM/yyyy:H:m:s");
        try {
            String[] s = line.split(" ");
            return LocalDateTime.parse(s[3].replace('[', ' '), formatter).toLocalTime();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    private Boolean checkStatus(String line) throws IllegalArgumentException {
        try {
            String[] l = line.split(" ");
            int code = new Integer(l[8]);
            float responseTime = new Float(l[10]);
            return code == 500 || responseTime > maxResponseTime;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    private float upLevel(LocalTime start, LocalTime end) {
        float downtime = Duration.between(start, end).getSeconds();
        float totalUptime = 24 * 60 * 60;
        return (totalUptime - downtime) / totalUptime * 100;
    }
    
}
