package tokens.directive_components;

public enum DirectiveName {
    ORIG,
    FILL,
    BLKW,
    STRINGZ,
    END,
    HALT;

    public static DirectiveName parseDirectiveName(String directiveName) {
        return switch (directiveName) {
            case "ORIG" -> ORIG;
            case "FILL" -> FILL;
            case "BLKW" -> BLKW;
            case "STRINGZ" -> STRINGZ;
            case "END" -> END;
            case "HALT" -> HALT;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case ORIG -> "DirectiveName(\"ORIG\")";
            case FILL -> "DirectiveName(\"FILL\")";
            case BLKW -> "DirectiveName(\"BLKW\")";
            case STRINGZ -> "DirectiveName(\"STRINGZ\")";
            case END -> "DirectiveName(\"END\")";
            case HALT -> "DirectiveName(\"HALT\")";
        };
    }
}
