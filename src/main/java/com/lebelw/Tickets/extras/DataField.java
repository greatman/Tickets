package com.lebelw.Tickets.extras;

/**
 *
 * @author brenda
 */
public class DataField {

    public enum DataFieldType {

        INT, LONG, DOUBLE, STRING, TEXT, BOOL
    }
    public String Name;
    public String DefaultValue;
    public int MaxLength;
    public int Decimals;
    public boolean AllowNull;
    public DataFieldType Type;

    public DataField(String name, String defaultValue, int maxLength,
            int decimals, boolean allowNull, DataFieldType type) {
        Name = name;
        DefaultValue = defaultValue;
        MaxLength = maxLength;
        Decimals = decimals;
        AllowNull = allowNull;
        Type = type;
    }
}
