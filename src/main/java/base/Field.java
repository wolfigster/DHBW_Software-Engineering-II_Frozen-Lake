package base;

import java.util.HashMap;

public enum Field {
    START("Start", 'S', 0),
    HOLE("Hole", 'H', -1),
    FIELD("Field", 'F', 0),
    GOAL("Goal", 'G', 1),
    WAY("Way", 'X', -1);

    private static final HashMap<Character, Field> FIELDS = new HashMap<>();
    private final String name;
    private final char label;
    private final int reward;

    static {
        for (Field field : values()) {
            FIELDS.put(field.label, field);
        }
    }

    Field(String name, Character label, int reward) {
        this.name = name;
        this.label = label;
        this.reward = reward;
    }

    public String getName() {
        return name;
    }

    public char getLabel() {
        return label;
    }

    public int getReward() {
        return reward;
    }

    // method to get a field by it's label
    public static Field getFieldByLabel(char label) {
        return FIELDS.get(label);
    }
}
