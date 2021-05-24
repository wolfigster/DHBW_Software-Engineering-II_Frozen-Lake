package configuration;

import random.MersenneTwisterFast;

import java.util.HashMap;

public enum Configuration {
    instance;

    public final String userDirectory = System.getProperty("user.dir");
    public final String fileSeparator = System.getProperty("file.separator");
    public final String logDirectory = userDirectory + fileSeparator + "log" + fileSeparator;
    public final String logfilePath = logDirectory + fileSeparator + "rl_frozen_lake.log";
    public final String dataDirectory = userDirectory + fileSeparator + "data" + fileSeparator;
    public String data = dataDirectory + "frozen_lake.txt";

    public int lakeHeight = 4;
    public int lakeWidth = 4;
    public int states = lakeHeight * lakeWidth;
    public int cycles = 100;
    public double alpha = 0.8;
    public double gamma = 0.95;

    public final MersenneTwisterFast randomNumberGenerator = new MersenneTwisterFast(System.currentTimeMillis());

    // method to set the configuration with the console arguments
    public void set(String... args) {
        HashMap<String, String> values = new HashMap<>();
        if (args.length % 2 != 0) {
            System.out.println("Bitte Eingabewerte überprüfen");
            System.exit(1);
        }
        for (int i = 0; i < args.length; i++) {
            // check if arg is an indicator with '-'
            if (args[i].charAt(0) == '-') {
                values.put(args[i].substring(1), args[i+1]);
                i++;
            }
        }
        // try to get all the inputs from the hashmap and parse them to the correct datatype
        try {
            // set the associated value if set in arguments
            this.data = values.get("data") == null ? this.data : dataDirectory + values.get("data");
            this.cycles = values.get("cycles") == null ? this.cycles : Integer.parseInt(values.get("cycles"));
            this.alpha = values.get("alpha") == null ? this.alpha : Double.parseDouble(values.get("alpha"));
            this.gamma = values.get("gamma") == null ? this.gamma : Double.parseDouble(values.get("gamma"));
        } catch (NumberFormatException nfe) {
            System.out.println("Ein oder mehrere Parameter konnten nicht übergeben werden. Überprüfen Sie ihre Eingaben!");
            System.out.println("Beispielhafte Eingabe:");
            System.out.println("-data frozen_lake.txt -cycles 100 -alpha 0.8 -gamma 0.95");
            System.exit(1);
        }
    }

    // method to set the size of the lake after frozen lake file read
    public void setLakeSize(int height, int width) {
        this.lakeHeight = height;
        this.lakeWidth = width;
        this.states = height * width;
    }

    // method to get the configuration as stringarray
    public String[] getConfiguration() {
        return new String[]{ String.format("%-9s", "Lakefile") + ": " + this.data,
                String.format("%-9s", "Height") + ": " + this.lakeHeight,
                String.format("%-9s", "Width") + ": " + this.lakeWidth,
                String.format("%-9s", "Cycles") + ": " + cycles,
                String.format("%-9s", "Alpha") + ": " + alpha,
                String.format("%-9s", "Gamma") + ": " + gamma };
    }
}
