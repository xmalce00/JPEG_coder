package Enums;

public enum TransformTypes {

    DCT("DCT"),
    WHT("WHT");

    String name;

    TransformTypes(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return "Transform " + name;
    }
}
