package tokens.line;

import tokens.instruction_components.Opcode;
import tokens.instruction_components.Operand;

public record Instruction(Opcode opcode, Operand operand) {

    public static Instruction parseInstruction(String instruction) {
        StringBuilder part1 = new StringBuilder();

        for (int i = 0; i < instruction.length(); i++) {
            if (instruction.charAt(i) == ' ') {
                break;
            }
            part1.append(instruction.charAt(i));
        }

        String part2 = instruction.substring(part1.length() + 1);

        Opcode opcode = Opcode.parseOpcode(part1.toString());
        Operand operand = Operand.parseOperand(part2);

        if (opcode == null || operand == null) {
            return null;
        }

        return new Instruction(opcode, operand);
    }

    @Override
    public String toString() {
        return "Instruction(" + opcode.toString() + ", " + operand.toString() + ")";
    }
}
