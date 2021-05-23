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
    public int cycles = 10000;
    public double alpha = 0.8;
    public double gamma = 0.95;

    public final MersenneTwisterFast randomNumberGenerator = new MersenneTwisterFast(System.currentTimeMillis());

    public void set(String... args) {
        HashMap<String, String> values = new HashMap<>();
        if (args.length % 2 != 0) {
            System.out.println("Bitte Eingabewerte überprüfen");
            System.exit(1);
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-') {
                values.put(args[i].substring(1), args[i+1]);
                i++;
            }
        }
        // try to get all the inputs from the hashmap and parse them to the correct datatype
        try {
            this.data = values.get("data") == null ? this.data : dataDirectory + values.get("data");
            this.cycles = values.get("cycles") == null ? this.cycles : Integer.parseInt(values.get("cycles"));
            this.alpha = values.get("alpha") == null ? this.alpha : Double.parseDouble(values.get("alpha"));
            this.gamma = values.get("gamma") == null ? this.gamma : Double.parseDouble(values.get("gamma"));
        } catch (NumberFormatException nfe) {
            System.out.println("Ein oder mehrere Parameter konnten nicht übergeben werden. Überprüfen Sie ihre Eingaben!");
            System.out.println("Beispielhafte Eingabe:");
            System.out.println("-data frozen_lake.txt -cycles 10000 -alpha 0.8 -gamma 0.95");
            System.exit(1);
        }
    }

    public void setLakeSize(int height, int width) {
        this.lakeHeight = height;
        this.lakeWidth = width;
        this.states = height * width;
    }

    public String[] getConfiguration() {
        return new String[]{ "Lakefile: " + this.data, "Height: " + this.lakeHeight, "Width: " + this.lakeWidth, "Cycles: " + cycles, "Alpha: " + alpha, "Gamma: " + gamma };
    }
}
