package parser;

import lombok.Getter;
import tokens.Program;
import tokens.instruction_components.Opcode;
import tokens.instruction_components.Operand;
import tokens.line.*;
import tokens.operands.RegisterOperand;
import tokens.operands.SecondRegisterOperandComponent;
import tokens.operands.ThirdRegisterOperandComponent;
import tokens.simple_components.Identifier;
import tokens.simple_components.Register;
import tokens.simple_components.numbers.HexNumber;
import tools.AssemblyFileReader;

import java.util.*;

public class Compiler {
    private final Tokenizer tokenizer;
    private int[] result;
    private Map<Identifier, Integer> labels;
    @Getter
    private int start_index = 0;
    @Getter
    private int end_index = 0;

    public Compiler() {
        this.tokenizer = new Tokenizer();
        result = new int[65536];
        labels = new HashMap<>();
    }

    private Program parseProgram(ArrayList<String> program) {
        return tokenizer.tokenizeProgram(program);
    }

    public void reset() {
        result = new int[65536];
        labels = new HashMap<>();
    }

    public int[] compile(String filename) {
        reset();
        AssemblyFileReader reader = new AssemblyFileReader();
        ArrayList<String> programArray = reader.readAssemblyFile(filename);
        ArrayList<Line> lines = parseProgram(programArray).getLines();

        int currentAddress = 0;
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);

            if (line.label() != null) {
                Identifier label = line.label().identifier();
                if (labels.containsKey(label)) {
                    throw new RuntimeException("Повторяющаяся метка на строке " + (i+1) + ": " + label);
                }
                labels.put(label, currentAddress);
            }

            if (line.instructionOrDirective() instanceof Directive directive) {
                switch (directive.getName()) {
                    case ORIG -> {
                        HexNumber origin = (HexNumber) directive.getOperand();
                        currentAddress = origin.getExtendedValue();
                        start_index = currentAddress;
                    }
                    case BLKW -> {
                        HexNumber blocks = (HexNumber) directive.getOperand();
                        currentAddress += blocks.getExtendedValue();
                    }
                    case STRINGZ -> {}
                    case END -> {
                        end_index = currentAddress;
                    }
                }
            }
            else if (line.instructionOrDirective() instanceof Instruction) {
                currentAddress++;
            }
        }

        currentAddress = start_index;

        // System.out.println(1);
        for (Line line : lines) {
            // System.out.println(line);
            if (line.instructionOrDirective() instanceof Instruction instruction) {
                System.out.println(codeForInstruction(instruction, currentAddress));
                result[currentAddress++] = codeForInstruction(instruction, currentAddress);
            }
            else if (line.instructionOrDirective() instanceof Directive directive) {
                switch (directive.getName()) {
                    case FILL -> {
                        HexNumber value = (HexNumber) directive.getOperand();
                        result[currentAddress++] = value.getExtendedValue();
                    }
                    case STRINGZ -> {}
                    case HALT -> {
                        result[currentAddress++] = 0xF025; // Код команды HALT
                    }
                    case ORIG -> {
                        currentAddress = ((HexNumber) directive.getOperand()).getExtendedValue();
                    }
                    case BLKW -> {
                        currentAddress += ((HexNumber) directive.getOperand()).getExtendedValue();
                    }
                }
            }
        }

        return Arrays.copyOfRange(result, start_index, end_index);
    }

    private int codeForInstruction(Instruction instruction, int current) {
        Map<String, Integer> registersCodes = new HashMap<>();
        registersCodes.put("Register(\"R0\")", 0);
        registersCodes.put("Register(\"R1\")", 1);
        registersCodes.put("Register(\"R2\")", 2);
        registersCodes.put("Register(\"R3\")", 3);
        registersCodes.put("Register(\"R4\")", 4);
        registersCodes.put("Register(\"R5\")", 5);
        registersCodes.put("Register(\"R6\")", 6);
        registersCodes.put("Register(\"R7\")", 7);

        Operand operand = instruction.operand();

        int code = 0;
        switch (instruction.opcode()) {
            case NOT -> {
                code += 0b111111;

                if (!(operand instanceof RegisterOperand registerOperand)) {
                    throw new RuntimeException("Operand must be a register");
                }

                if (registerOperand.getSize() != 2) {
                    throw new RuntimeException("Must be two registers");
                }

                SecondRegisterOperandComponent secondComponent = registerOperand.getSecondComponent();

                if (!(secondComponent instanceof Register secondRegister)) {
                    throw new RuntimeException("Second operand must be a register");
                }

                code += registersCodes.get(secondRegister.toString()) << 6;
                code += registersCodes.get(registerOperand.getFirstComponent().toString()) << 9;
                code += 0b1001 << 12;

                return code;
            }

            case ADD, AND -> {
                if (!(operand instanceof RegisterOperand registerOperand)) {
                    throw new RuntimeException("Operand must be a register");
                }

                if (registerOperand.getSize() != 3) {
                    throw new RuntimeException("Must be three components");
                }

                SecondRegisterOperandComponent secondComponent = registerOperand.getSecondComponent();

                if (!(secondComponent instanceof Register secondRegister)) {
                    throw new RuntimeException("Second operand must be a register");
                }

                ThirdRegisterOperandComponent thirdComponent = registerOperand.getThirdComponent();

                if (thirdComponent instanceof Register thirdRegister) {
                    code += registersCodes.get(thirdRegister.toString());
                    code += registersCodes.get(secondRegister.toString()) << 6;
                    code += registersCodes.get(registerOperand.getFirstComponent().toString()) << 9;
                } else if (thirdComponent instanceof HexNumber hexNumber) {
                    code += hexNumber.getExtendedValue();
                    code += 0b100000;
                    try {
                        code += registersCodes.get(secondRegister.toString()) << 6;
                    } catch (NullPointerException e) {
                        System.out.println(secondRegister.toString());
                    }

                    code += registersCodes.get(registerOperand.getFirstComponent().toString()) << 9;
                }

                if (instruction.opcode() == Opcode.ADD) {
                    code += 1 << 12;
                } else {
                    code += 5 << 12;
                }

                return code;
            }

            case LD, ST, LDI, STI, LEA -> {
                if (!(operand instanceof RegisterOperand registerOperand)) {
                    throw new RuntimeException("Operand must be a register");
                }

                if (registerOperand.getSize() != 2) {
                    throw new RuntimeException("Must be two components");
                }

                SecondRegisterOperandComponent secondComponent = registerOperand.getSecondComponent();

                if (!(secondComponent instanceof Identifier identifier)) {
                    throw new RuntimeException("Second operand must be a identifier");
                }

                int number = labels.get(identifier);

                if (Math.abs(number - current) >= 255) {
                    throw new RuntimeException("Operand must be in range [-256, 255]");
                }

                code += number;
                code += registersCodes.get(registerOperand.getFirstComponent().toString()) << 9;

                if (instruction.opcode() == Opcode.LD) {
                    return code + (2 << 12);
                } else if (instruction.opcode() == Opcode.LDI) {
                    return code + (10 << 12);
                } else if (instruction.opcode() == Opcode.ST) {
                    return code + (3 << 12);
                } else if (instruction.opcode() == Opcode.STI) {
                    return code + (11 << 12);
                }
                return code + (14 << 12);
            }

            case LDR, STR -> {
                if (!(operand instanceof RegisterOperand registerOperand)) {
                    throw new RuntimeException("Operand must be a register");
                }

                if (registerOperand.getSize() != 3) {
                    throw new RuntimeException("Must be three components");
                }

                SecondRegisterOperandComponent secondComponent = registerOperand.getSecondComponent();

                if (!(secondComponent instanceof Register secondRegister)) {
                    throw new RuntimeException("Second operand must be a register");
                }

                ThirdRegisterOperandComponent thirdComponent = registerOperand.getThirdComponent();

                if (thirdComponent instanceof HexNumber hexNumber) {
                    code += hexNumber.getExtendedForOffset();
                    code += registersCodes.get(secondRegister.toString()) << 6;
                    code += registersCodes.get(registerOperand.getFirstComponent().toString()) << 9;
                } else {
                    throw new RuntimeException("Second operand must be a number");
                }

                if (instruction.opcode() == Opcode.LDR) {
                    return code + (6 << 12);
                }

                return code + (7 << 12);
            }
            case BRn, BRp, BRz -> {
                if (!(operand instanceof Identifier identifier)) {
                    throw new RuntimeException("Operand must be an identifier");
                }

                int targetAddress = labels.get(identifier);
                int offset = targetAddress - current; // Исправлено: убрано +1

                if (Math.abs(offset) > 256) {
                    throw new RuntimeException("Branch offset out of range");
                }

                code = offset & 0x1FF; // Младшие 9 бит смещения

                if (instruction.opcode() == Opcode.BRn) {
                    code |= (1 << 11);
                } else if (instruction.opcode() == Opcode.BRz) {
                    code |= (1 << 10);
                } else if (instruction.opcode() == Opcode.BRp) {
                    code |= (1 << 9);
                }

                return code; // BR имеет opcode 0000
            }

            case JMP -> {
                if (!(operand instanceof RegisterOperand registerOperand)) {
                    throw new RuntimeException("JMP operand must be a register");
                }

                if (registerOperand.getSize() != 1) {
                    throw new RuntimeException("JMP must have exactly one register operand");
                }

                Register baseRegister = registerOperand.getFirstComponent();
                code += registersCodes.get(baseRegister.toString()) << 6;
                return code + (0xC << 12);
            }

            case TRAP -> {
                if (!(operand instanceof HexNumber hexNumber)) {
                    throw new RuntimeException("Operand must be a hex number");
                }

                String number = hexNumber.getHexValue();
                code += 0xf << 12;

                return switch (number) {
                    case "x23" -> code + 0b00100011;
                    case "x21" -> code + 0b0010001;
                    case "x25" -> code + 0b0010101;
                    default -> throw new IllegalStateException("Unexpected value: " + number);
                };
            }
        }

        return code;
    }
}
