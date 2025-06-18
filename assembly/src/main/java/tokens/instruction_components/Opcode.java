package tokens.instruction_components;

public enum Opcode {
    ADD,
    AND,
    NOT,
    LD,
    LDR,
    LDI,
    ST,
    STR,
    STI,
    LEA,
    BRn,
    BRz,
    BRp,
    JMP,
    TRAP,
    HALT;

    public static Opcode parseOpcode(String token) {
        return switch (token) {
            case "ADD" -> ADD;
            case "AND" -> AND;
            case "NOT" -> NOT;
            case "LD" -> LD;
            case "LDR" -> LDR;
            case "LDI" -> LDI;
            case "ST" -> ST;
            case "STR" -> STR;
            case "STI" -> STI;
            case "LEA" -> LEA;
            case "BRn" -> BRn;
            case "BRz" -> BRz;
            case "BRp" -> BRp;
            case "JMP" -> JMP;
            case "TRAP" -> TRAP;
            case "HALT" -> HALT;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case ADD -> "Opcode(\"ADD\")";
            case AND -> "Opcode(\"AND\")";
            case NOT -> "Opcode(\"NOT\")";
            case LD -> "Opcode(\"LD\")";
            case LDR -> "Opcode(\"LDR\")";
            case LDI -> "Opcode(\"LDI\")";
            case ST -> "Opcode(\"ST\")";
            case STR -> "Opcode(\"STR\")";
            case STI -> "Opcode(\"STI\")";
            case LEA -> "Opcode(\"LEA\")";
            case BRn -> "Opcode(\"BRn\")";
            case BRz -> "Opcode(\"BRz\")";
            case BRp -> "Opcode(\"BRp\")";
            case JMP -> "Opcode(\"JMP\")";
            case TRAP -> "Opcode(\"TRAP\")";
            case HALT -> "Opcode(\"HALT\")";
        };
    }
}
