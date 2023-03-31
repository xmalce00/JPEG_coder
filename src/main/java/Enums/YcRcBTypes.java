package Enums;

public enum YcRcBTypes {

    Y("Y"),
    cR("Cr"),
    cB("Cb");

    String name;

    YcRcBTypes(String s) {
        name = s;
    }

    @Override
    public String toString() { return name; }

}
