package tokens.instruction_components;

import tokens.operands.*;

public interface Operand {
    static Operand parseOperand(String token) {
        RegisterOperand registerOperand = RegisterOperand.parseRegisterOperand(token);
        if (registerOperand != null) {
            return registerOperand;
        }

        ImmediateOperand immediateOperand = ImmediateOperand.parseImmediateOperand(token);
        if (immediateOperand != null) {
            return immediateOperand;
        }

        MemoryOperand memoryOperand = MemoryOperand.parseMemoryOperand(token);
        if (memoryOperand != null) {
            return memoryOperand;
        }

        StringOperand stringOperand = StringOperand.parseStringOperand(token);
        if (stringOperand != null) {
            return stringOperand;
        }

        return InterruptVectorOperand.parseInterruptVectorOperand(token);
    }
}
