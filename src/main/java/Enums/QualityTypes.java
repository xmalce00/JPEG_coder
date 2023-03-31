package Enums;

public enum QualityTypes {

    RED("RED"),
    GREEN("GREEN"),
    BLUE("BLUE"),
    R_G_B("RGB"),
    Y("Y"),
    cR("Cr"),
    cB("Cb"),
    YcRcB("YcRcB");

    String name;

    QualityTypes(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name;
    }

}
