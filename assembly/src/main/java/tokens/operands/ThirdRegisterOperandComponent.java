package tokens.operands;

import tokens.simple_components.Register;

public interface ThirdRegisterOperandComponent {
    static ThirdRegisterOperandComponent parseThirdRegisterOperandComponent(String token) {
        Register registerOperand = Register.parseRegister(token);

        if (registerOperand != null) {
            return registerOperand;
        }

        return ImmediateOperand.parseImmediateOperand(token);
    }
}
