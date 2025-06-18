package tokens.operands;

import tokens.instruction_components.Operand;
import tokens.simple_components.numbers.Number;

public interface ImmediateOperand extends Operand, ThirdRegisterOperandComponent {
    static ImmediateOperand parseImmediateOperand(String token) {
        return Number.parseNumber(token);
    }
}
