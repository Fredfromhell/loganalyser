import loganalyzer.LogAnalyzer;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        float maxResponseTime;
        float permittedLevel;
        String fileName;

        Options options = new Options();
        options.addOption(Option.builder("t").hasArg().argName("t")
                .desc("Приемлемое время ответа ").required().build());
        options.addOption(Option.builder("u").hasArg().argName("u")
                .desc("Минимально допустимый уровень доступности ").required().build());
        options.addOption(Option.builder("f").hasArg().argName("f")
                .desc("Путь до вашего файла").required().build());

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            maxResponseTime = Float.parseFloat(cmd.getOptionValue('t'));
            permittedLevel = Float.parseFloat(cmd.getOptionValue('u'));
            fileName = cmd.getOptionValue("f");
            if (maxResponseTime <= 0 || permittedLevel < 0 || permittedLevel > 100 || fileName.length() == 0) {
                throw new IllegalArgumentException();
            }

            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            LogAnalyzer analyzer = new LogAnalyzer(reader, maxResponseTime, permittedLevel);
            analyzer.parse();
        } catch (ParseException | IllegalArgumentException e) {
            System.out.println("Ошибка ввода параметров");
            new HelpFormatter().printHelp("java -jar loganalyser.jar", options);
            System.exit(2);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

}
