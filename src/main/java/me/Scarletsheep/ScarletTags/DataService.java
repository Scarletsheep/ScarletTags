package me.Scarletsheep.ScarletTags;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.*;
import java.util.*;

public class DataService {
    private static Map<String, String> database;

    public static void getData() {

        // Database variable initialization/reset
        database = new HashMap<>();

        try {

            // CSV File check. Created if not found
            File databaseFile = new File(Main.plugin.getDataFolder(), "database.csv");
            if (!databaseFile.exists()) {
                databaseFile.createNewFile();
            }

            // CSV Reading with univocity's library
            Reader inputReader = new InputStreamReader(new FileInputStream(databaseFile));
            CsvParser parser = new CsvParser(new CsvParserSettings());
            List<String[]> tempDatabase = parser.parseAll(inputReader);

            // Database transformation in map
            for (String[] variable : tempDatabase) {
                // Format: uuid.variableName, value
                database.put(variable[0] + variable[1], variable[2]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveData() {
        try {

            // CSV File check. Created if not found
            File databaseFile = new File(Main.plugin.getDataFolder(), "database.csv");
            if (!databaseFile.exists()) {
                databaseFile.createNewFile();
            }

            // Database transformation in string list
            List<String[]> tempDatabase = new ArrayList<>();
            for (Map.Entry<String, String> entry : database.entrySet()) {
                tempDatabase.add(new String[] {entry.getKey(), entry.getValue()});
            }

            // CSV Writing with univocity's library
            Writer outputWriter = new OutputStreamWriter(new FileOutputStream(databaseFile));
            CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
            for (String[] line : tempDatabase) {
                writer.writeRow(line);
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveVariable(UUID uuid, String variableName, String value) {
        database.put(uuid.toString() + '.' + variableName, value);
    }

    public static boolean checkVariable(UUID uuid, String variableName) {
        // Checks if provided variable is present
        String playerVariable = uuid.toString() + ".tag";
        return database.containsKey(playerVariable);
    }

    public static String getVariable(UUID uuid, String variableName) {
        String playerVariable = uuid.toString() + ".tag";
        return database.get(playerVariable);
    }

    public static void removeVariable(UUID uuid, String variableName) {
        String playerVariable = uuid.toString() + ".tag";
        database.remove(playerVariable);
    }
}
