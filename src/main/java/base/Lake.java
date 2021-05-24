package base;

import configuration.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Lake {
    private Field[][] lake;

    public Lake(String data) {
        try (final FileInputStream fileInputStream = new FileInputStream(data)) {
            // temporary arraylist fields to read in all chars
            ArrayList<Field> fields = new ArrayList<>();
            int content, height = 1;
            // read file char by char and only accept
            while ((content = fileInputStream.read()) != -1) {
                final char c = (char) content;
                // check for linebreak and increment height
                if (c == '\n') height++;
                // continue while if char is not a regular field
                if (Field.getFieldByLabel(c) == null) continue;
                fields.add(Field.getFieldByLabel(c));
            }

            // set lake dimensions
            Configuration.instance.setLakeSize(height, fields.size() / height);
            lake = new Field[Configuration.instance.lakeHeight][Configuration.instance.lakeWidth];
            int n = 0;
            // initialize two dimensional lake array
            for (int y = 0; y < Configuration.instance.lakeHeight; y++) {
                for (int x = 0; x < Configuration.instance.lakeWidth; x++) {
                    lake[y][x] = fields.get(n);
                    n++;
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public Field[][] getLake() {
        return lake;
    }

    // method to return lake as one dimensional array to write it to the log file
    public String[] getLakeAsStringArray() {
        String[] lakeString = new String[Configuration.instance.lakeHeight];
        for (int y = 0; y < Configuration.instance.lakeHeight; y++) {
            lakeString[y] = "";
            for (int x = 0; x < Configuration.instance.lakeWidth; x++) {
                lakeString[y] += lake[y][x].getLabel() + " ";
            }
        }
        return lakeString;
    }
}
