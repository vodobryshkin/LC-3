package tokens.simple_components;

import tokens.operands.SecondRegisterOperandComponent;
import tokens.operands.ThirdRegisterOperandComponent;

public enum Register implements SecondRegisterOperandComponent, ThirdRegisterOperandComponent {
    R0,
    R1,
    R2,
    R3,
    R4,
    R5,
    R6,
    R7;

    public static Register parseRegister(String directiveName) {
        return switch (directiveName) {
            case "R0" -> R0;
            case "R1" -> R1;
            case "R2" -> R2;
            case "R3" -> R3;
            case "R4" -> R4;
            case "R5" -> R5;
            case "R6" -> R6;
            case "R7" -> R7;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case R0 -> "Register(\"R0\")";
            case R1 -> "Register(\"R1\")";
            case R2 -> "Register(\"R2\")";
            case R3 -> "Register(\"R3\")";
            case R4 -> "Register(\"R4\")";
            case R5 -> "Register(\"R5\")";
            case R6 -> "Register(\"R6\")";
            case R7 -> "Register(\"R7\")";
        };
    }
}
