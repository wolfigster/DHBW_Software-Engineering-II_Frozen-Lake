import base.Lake;
import configuration.Configuration;
import log.LogEngine;
import reinforcement.FrozenLake;

public class Application {

    // args example:
    // -data frozen_lake.txt -cycles 100 -alpha 0.8 -gamma 0.95
    public static void main(final String[] args) {
        Lake lake = new Lake(Configuration.instance.data);
        FrozenLake frozenLake = new FrozenLake(lake);
        LogEngine.instance.init();

        // initialize configuration with args
        if (args.length != 0) {
            Configuration.instance.set(args);
        }
        // start logging show configuration for this run in the log file
        LogEngine.instance.spacer();
        LogEngine.instance.write("Start FrozenLake with the following configuration:");
        for (String config : Configuration.instance.getConfiguration()) {
            LogEngine.instance.write(config);
        }
        LogEngine.instance.spacer();
        LogEngine.instance.write("Layout:");
        for (String field : lake.getLakeAsStringArray()) {
            LogEngine.instance.write(field);
        }
        LogEngine.instance.spacer();

        // start the algorithms
        frozenLake.calculateReward();
        frozenLake.calculateQ();
        frozenLake.writeSelectedStates();
        frozenLake.writeSelectedTable();

        LogEngine.instance.write("Finished\n");
        LogEngine.instance.close();
    }
}
