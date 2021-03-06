package reinforcement;

import base.Field;
import base.Lake;
import configuration.Configuration;
import log.LogEngine;

import java.util.*;

public class FrozenLake {
    private final Field[][] LAKE;
    private final int[][] R = new int[Configuration.instance.states][Configuration.instance.states];
    private final double[][] Q = new double[Configuration.instance.states][Configuration.instance.states];

    public FrozenLake(Lake lake) {
        this.LAKE = lake.getLake();
    }

    // method to fill the reward table
    public void calculateReward() {
        for (int i = 0; i < Configuration.instance.states; i++) {
            int x = i / Configuration.instance.lakeWidth;
            int y = i - x * Configuration.instance.lakeWidth;

            // set complete row to the worst reward
            for (int k = 0; k < Configuration.instance.states; k++) R[i][k] = Field.HOLE.getReward();

            // try to move in each direction
            if (LAKE[x][y] != Field.GOAL) {
                // check the field on the left
                int checkLeft = y - 1;
                if (checkLeft > -1) {
                    // get the current column and set the reward
                    int t = x * Configuration.instance.lakeWidth + checkLeft;
                    R[i][t] = LAKE[x][checkLeft].getReward();
                }
                // check the field on the right
                int checkRight = y + 1;
                if (checkRight < Configuration.instance.lakeWidth) {
                    // get the current column and set the reward
                    int t = x * Configuration.instance.lakeWidth + checkRight;
                    R[i][t] = LAKE[x][checkRight].getReward();
                }
                // check the field above
                int checkUp = x - 1;
                if (checkUp > -1) {
                    // get the current row and set the reward
                    int t = checkUp * Configuration.instance.lakeWidth + y;
                    R[i][t] = LAKE[checkUp][y].getReward();
                }
                // check the field below
                int checkDown = x + 1;
                if (checkDown < Configuration.instance.lakeHeight) {
                    // get the current row and set the reward
                    int t = checkDown * Configuration.instance.lakeWidth + y;
                    R[i][t] = LAKE[checkDown][y].getReward();
                }
            }
        }
        writeR();
    }

    // method to calculate q values and fill the q-table
    public void calculateQ() {
        for (int i = 0; i < Configuration.instance.cycles; i++) {
            // get an random starting state
            int state = Configuration.instance.randomNumberGenerator.nextInt(Configuration.instance.states);

            // get the column and row
            int x = state / Configuration.instance.lakeWidth;
            int y = state - x * Configuration.instance.lakeWidth;
            while (!LAKE[x][y].equals(Field.GOAL)) {
                // get all available directions / actions for current state and select a random action for the calculation
                int[] possibleActions = getActionsByState(state);
                int action = possibleActions.length == 0 ? 0 : Configuration.instance.randomNumberGenerator.nextInt(possibleActions.length);
                int nextState = possibleActions[action];

                // calculate actual q value
                Q[state][nextState] = Q[state][nextState] + Configuration.instance.alpha * (R[state][nextState] + Configuration.instance.gamma * calculateMaxQ(nextState) - Q[state][nextState]);

                writeQ(state, nextState, possibleActions);

                // set new current state and set column and row
                state = nextState;
                x = state / Configuration.instance.lakeWidth;
                y = state - x * Configuration.instance.lakeWidth;
            }
        }
        writeQTable();
    }

    // calculate the maximum q of the next state
    private double calculateMaxQ(int state) {
        int[] possibleActions = getActionsByState(state);
        double max = Double.MIN_VALUE;
        // check each possible action for there q-value and set the new max value if the q-value is higher than the current max
        for (int nextAction : possibleActions) {
            double value = Q[state][nextAction];
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    // get possible actions for a state -> check if reward fields which are not equal to the hole reward
    private int[] getActionsByState(int state) {
        ArrayList<Integer> possibleActions = new ArrayList<>();
        for (int i = 0; i < Configuration.instance.states; i++) {
            // check if the reward of the field is not the reward of a hole and add it as possible action
            if (R[state][i] != Field.HOLE.getReward()) {
                possibleActions.add(i);
            }
        }
        return possibleActions.stream().mapToInt(i -> i).toArray();
    }

    // method which returns best action for a state
    private int getNextMovement(int state) {
        int[] possibleActions = getActionsByState(state);
        int nextMovement = state;
        double max = Double.MIN_VALUE;

        // get for each possible State the q-value and compare it to the current best value (max)
        for (int possibleState : possibleActions) {
            double value = Q[state][possibleState];
            if (value > max) {
                max = value;
                nextMovement = possibleState;
            }
        }

        return nextMovement;
    }

    // method to log the reward table
    private void writeR() {
        LogEngine.instance.write("Reward-Table:");
        StringBuilder rS = new StringBuilder(String.format("%8s", "Actions: "));
        for (int i = 0; i < Configuration.instance.states; i++) {
            rS.append(String.format("%4s", i));
        }
        LogEngine.instance.write(rS.toString());

        for (int i = 0; i < Configuration.instance.states; i++) {
            rS = new StringBuilder();
            rS.append(String.format("R %4s :[", i));
            for (int j = 0; j < Configuration.instance.states; j++) {
                rS.append(String.format("%4s", R[i][j]));
            }
            rS.append(" ]");
            LogEngine.instance.write(rS.toString());
        }
        LogEngine.instance.spacer();
        LogEngine.instance.write("Q-Steps:");
        StringBuilder qS = new StringBuilder(String.format("%18s", "States    /    Actions: "));
        for (int i = 0; i < Configuration.instance.states; i++) {
            qS.append(String.format("| %5s ", i));
        }
        LogEngine.instance.write(qS.toString());
    }

    // method to log the current calculated q value
    private void writeQ(int state, int nextState, int[] possibleActions) {
        StringBuilder qS = new StringBuilder(String.format("Current %3s, Next %3s :[", state, nextState));
        for (int i = 0; i < Configuration.instance.states; i++) {
            int finalI = i;
            if (Arrays.stream(possibleActions).anyMatch(x -> x == finalI)) qS.append(String.format("| %,.3f ", Q[state][i]));
            else qS.append(String.format("|%7s", ""));
        }
        qS.append("|]");
        LogEngine.instance.write(qS.toString());
    }

    // method to log the q-table
    private void writeQTable() {
        LogEngine.instance.spacer();
        LogEngine.instance.write("Q-Table:");
        StringBuilder qS = new StringBuilder(String.format("%18s", "States    /    Actions: "));
        for (int i = 0; i < Configuration.instance.states; i++) {
            qS.append(String.format("| %5s ", i));
        }
        LogEngine.instance.write(qS.toString());
        for (int i = 0; i < Configuration.instance.states; i++) {
            qS = new StringBuilder(String.format("State %15s :[", i));
            for (int j = 0; j < Configuration.instance.states; j++) {
                if(Q[i][j] == 0) qS.append(String.format("| %5s ", 0));
                else qS.append(String.format("| %,.3f ", Q[i][j]));
            }
            qS.append("|]");
            LogEngine.instance.write(qS.toString());
        }
        LogEngine.instance.spacer();
    }

    // method to log next state of all states
    public void writeSelectedTable() {
        LogEngine.instance.write("State-Table:");
        LogEngine.instance.write("State -> Next");
        for (int state = 0; state < Configuration.instance.states; state++) {
            int nextState = getNextMovement(state);
            LogEngine.instance.write(String.format("%5s -> %4s", state, nextState));
        }
    }

    // method to log the way across the lake
    public void writeSelectedWay() {
        LogEngine.instance.spacer();
        int state = 0;
        StringBuilder way = new StringBuilder("Way across the frozen lake: " + String.format(" %3s ", state));
        Field[][]lake = LAKE;
        state = getNextMovement(state);
        do {
            int x = state / Configuration.instance.lakeWidth;
            int y = state - x * Configuration.instance.lakeWidth;
            lake[x][y] = Field.WAY;

            way.append(String.format(" -> %3s ", state));
            state = getNextMovement(state);
        } while (state != Configuration.instance.states-1);
        way.append(String.format(" -> %3s ", state));
        String[] wayString = new String[Configuration.instance.lakeHeight];

        LogEngine.instance.write(way.toString());

        for (int y = 0; y < Configuration.instance.lakeHeight; y++) {
            wayString[y] = "";
            for (int x = 0; x < Configuration.instance.lakeWidth; x++) {
                wayString[y] += lake[y][x].getLabel() + " ";
            }
            LogEngine.instance.write(wayString[y]);
        }
        LogEngine.instance.spacer();
    }
}