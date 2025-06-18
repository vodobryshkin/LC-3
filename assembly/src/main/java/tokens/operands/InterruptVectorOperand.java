package tokens.operands;

import tokens.instruction_components.Operand;

public enum InterruptVectorOperand implements Operand {
    x23,
    x21,
    x25;

    public static InterruptVectorOperand parseInterruptVectorOperand(String interruptVectorOperand) {
        return switch (interruptVectorOperand) {
            case "x23" -> x23;
            case "x21" -> x21;
            case "x25" -> x25;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case x23 -> "InterruptVectorOperand(x23)";
            case x21 -> "InterruptVectorOperand(x21)";
            case x25 -> "InterruptVectorOperand(x25)";
        };
    }
}

