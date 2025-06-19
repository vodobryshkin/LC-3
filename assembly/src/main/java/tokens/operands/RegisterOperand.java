package tokens.operands;

import lombok.Getter;
import tokens.instruction_components.Operand;
import tokens.simple_components.Register;

import java.util.Arrays;

@Getter
public class RegisterOperand implements Operand {
    private final Register firstComponent;
    private SecondRegisterOperandComponent secondComponent = null;
    private ThirdRegisterOperandComponent thirdComponent = null;

    public RegisterOperand(Register firstComponent) {
        this.firstComponent = firstComponent;
    }

    public RegisterOperand(Register firstComponent, SecondRegisterOperandComponent secondComponent) {
        this.firstComponent = firstComponent;
        this.secondComponent = secondComponent;
    }

    public RegisterOperand(Register firstComponent, SecondRegisterOperandComponent secondComponent, ThirdRegisterOperandComponent thirdComponent) {
        this.firstComponent = firstComponent;
        this.secondComponent = secondComponent;
        this.thirdComponent = thirdComponent;
    }

    public static RegisterOperand parseRegisterOperand(String token) {
        String[] tokens = Arrays.stream(token.split("\\s*,\\s*")).toArray(String[]::new);

        Register firstComponent = Register.parseRegister(tokens[0]);

        switch (tokens.length) {
            case 1 -> {
                if (firstComponent != null) {
                    return new RegisterOperand(firstComponent);
                }
            }

            case 2 -> {
                SecondRegisterOperandComponent secondComponent = SecondRegisterOperandComponent.parseSecondRegisterOperandComponent(tokens[1]);

                if (firstComponent != null && secondComponent != null) {
                    return new RegisterOperand(firstComponent, secondComponent);
                }
            }

            case 3 -> {
                Register secondComponent = Register.parseRegister(tokens[1]);
                ThirdRegisterOperandComponent thirdComponent = ThirdRegisterOperandComponent.parseThirdRegisterOperandComponent(tokens[2]);

                if (firstComponent != null && secondComponent != null && thirdComponent != null) {
                    return new RegisterOperand(firstComponent, secondComponent, thirdComponent);
                }
            }
        }

        return null;
    }

    @Override
    public String toString() {
        if (secondComponent == null) {
            return "RegisterOperand(" + firstComponent.toString() + ")";
        }

        if (thirdComponent == null) {
            return "RegisterOperand(" + firstComponent.toString() + ", " + secondComponent.toString() + ")";
        }

        return "RegisterOperand(" + firstComponent.toString() + ", " + secondComponent.toString() + ", " + thirdComponent.toString() + ")";
    }

    public int getSize() {
        if (secondComponent == null && thirdComponent == null) {
            return 1;
        }
        if (thirdComponent == null) {
            return 2;
        }

        return 3;
    }
}
