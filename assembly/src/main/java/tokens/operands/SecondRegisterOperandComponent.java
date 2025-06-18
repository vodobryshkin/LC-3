package tokens.operands;

import tokens.simple_components.Identifier;
import tokens.simple_components.Register;

public interface SecondRegisterOperandComponent {
    static SecondRegisterOperandComponent parseSecondRegisterOperandComponent(String token) {
        Register registerOperand = Register.parseRegister(token);

        if (registerOperand != null) {
            return registerOperand;
        }

        return Identifier.parseIdentifier(token);
    }
}
