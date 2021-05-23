package log;

import configuration.Configuration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum LogEngine {
    instance;

    private BufferedWriter bufferedWriter;

    public void init() {
        try {
            FileWriter fileWriter = new FileWriter(Configuration.instance.logfilePath, true);
            bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public String getCurrentDate() {
        Date date = new Date();
        // SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.S");
        return simpleDateFormat.format(date);
    }

    public void write(String text) {
        try {
            bufferedWriter.write(getCurrentDate() + " : " + text + "\n");
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
    public void spacer() {
        write("----------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public void close() {
        try {
            bufferedWriter.close();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}