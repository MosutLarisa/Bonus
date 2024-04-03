import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InfoTari {
    private String writeFilePath;
    private List<Info> info = new ArrayList<>();

    public InfoTari(String writeFilePath) {
        this.writeFilePath = writeFilePath;
    }

    public static void main(String[] args) {
        new InfoTari("date.csv").run();
    }

    public void run() {
        process();
        write();
    }

    private void process() {
        try {
            URL address = new URL("https://operationworld.org/locations/europe/");
            Scanner scan = new Scanner(address.openStream());
            String pageContent = scan.useDelimiter("\\A").next();

            String country = extractInfo(pageContent, "Pray for:", "</h1>");
            String population = extractInfo(pageContent, "<th scope=\"row\">Population:</th>", "</td>");
            String evangelical = extractInfo(pageContent, "<th scope=\"row\">% Evangelical:</th>", "</td>");

            info.add(new Info(country, population, evangelical));
        } 
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String extractInfo(String pageContent, String start, String end) {
        int startIndex = pageContent.indexOf(start);
        if (startIndex != -1) {
            startIndex = pageContent.indexOf(":", startIndex) + 1;
            int endIndex = pageContent.indexOf(end, startIndex);
            return pageContent.substring(startIndex, endIndex).trim().replaceAll("<[^>]*>", "").replaceAll("\\s+", " ");
        }
        return "Nu s-au găsit date";
    }

    private void write() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(writeFilePath))) {
            for (Info info : info) {
                writer.write(info.getName() + "," + info.getPopulation() + "," + info.getEvangelicalPercent() + "\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

class Info {
    private String name;
    private String population;
    private String evangelicalPercent;

    public Info(String name, String population, String evangelicalPercent) {
        this.name = name;
        this.population = population;
        this.evangelicalPercent = evangelicalPercent;
    }

    public String getName() {
        return name;
    }

    public String getPopulation() {
        return population;
    }

    public String getEvangelicalPercent() {
        return evangelicalPercent;
    }
}
