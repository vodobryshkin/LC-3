package components;

import parser.Compiler;

import java.io.IOException;
import java.util.Arrays;

public class Cpu {
    private final int[] memory;
    private final int[] registers;
    private int pc;
    private boolean running;
    private ConditionFlag conditionFlag;

    private enum ConditionFlag {
        POSITIVE, ZERO, NEGATIVE
    }

    public Cpu(int[] program) {
        this.memory = new int[65536];
        System.arraycopy(program, 0, this.memory, 0x3000, program.length);
        this.registers = new int[8];
        this.pc = 0x3000;
        this.running = true;
        this.conditionFlag = ConditionFlag.ZERO;
    }

    public void run() {
        while (running && pc < memory.length) {
            int instruction = memory[pc++];
            executeInstruction(instruction);
        }
    }

    private void executeInstruction(int instruction) {
        int opcode = (instruction >> 12) & 0xF;

        switch (opcode) {
            case 0x0: // BR
                branch(instruction);
                break;
            case 0x1: // ADD
                add(instruction);
                break;
            case 0x5: // AND
                and(instruction);
                break;
            case 0x9: // NOT
                not(instruction);
                break;
            case 0x2: // LD
                load(instruction);
                break;
            case 0xA: // LDI
                loadIndirect(instruction);
                break;
            case 0x6: // LDR
                loadBaseOffset(instruction);
                break;
            case 0xE: // LEA
                loadEffectiveAddress(instruction);
                break;
            case 0x3: // ST
                store(instruction);
                break;
            case 0xB: // STI
                storeIndirect(instruction);
                break;
            case 0x7: // STR
                storeBaseOffset(instruction);
                break;
            case 0xC: // JMP/RET
                jump(instruction);
                break;
            case 0xF: // TRAP
                trap(instruction);
                break;
            default:
                throw new RuntimeException("Неизвестный опкод: " + Integer.toHexString(opcode));
        }
    }

    private void add(int instruction) {
        int dr = (instruction >> 9) & 0x7;
        int sr1 = (instruction >> 6) & 0x7;
        boolean immFlag = ((instruction >> 5) & 0x1) == 1;

        if (immFlag) {
            int imm5 = signExtend(instruction & 0x1F, 5);
            registers[dr] = registers[sr1] + imm5;
        } else {
            int sr2 = instruction & 0x7;
            registers[dr] = registers[sr1] + registers[sr2];
        }

        updateFlags(dr);
    }

    private void and(int instruction) {
        int dr = (instruction >> 9) & 0x7;
        int sr1 = (instruction >> 6) & 0x7;
        boolean immFlag = ((instruction >> 5) & 0x1) == 1;

        if (immFlag) {
            int imm5 = signExtend(instruction & 0x1F, 5);
            registers[dr] = registers[sr1] & imm5;
        } else {
            int sr2 = instruction & 0x7;
            registers[dr] = registers[sr1] & registers[sr2];
        }

        updateFlags(dr);
    }

    private void not(int instruction) {
        int dr = (instruction >> 9) & 0x7;
        int sr = (instruction >> 6) & 0x7;

        registers[dr] = ~registers[sr];
        updateFlags(dr);
    }

    private void load(int instruction) {
        int dr = (instruction >> 9) & 0x7;
        int pcOffset = signExtend(instruction & 0x1FF, 9);

        registers[dr] = memory[pc + pcOffset];
        updateFlags(dr);
    }

    private void loadIndirect(int instruction) {
        int dr = (instruction >> 9) & 0x7;
        int pcOffset = signExtend(instruction & 0x1FF, 9);

        int address = memory[pc + pcOffset];
        registers[dr] = memory[address];
        updateFlags(dr);
    }

    private void loadBaseOffset(int instruction) {
        int dr = (instruction >> 9) & 0x7;
        int baseR = (instruction >> 6) & 0x7;
        int offset = signExtend(instruction & 0x3F, 6);

        registers[dr] = memory[registers[baseR] + offset];
        updateFlags(dr);
    }

    private void loadEffectiveAddress(int instruction) {
        int dr = (instruction >> 9) & 0x7;
        int pcOffset = signExtend(instruction & 0x1FF, 9);

        registers[dr] = pc + pcOffset;
        updateFlags(dr);
    }

    private void store(int instruction) {
        int sr = (instruction >> 9) & 0x7;
        int pcOffset = signExtend(instruction & 0x1FF, 9);

        memory[pc + pcOffset] = registers[sr];
    }

    private void storeIndirect(int instruction) {
        int sr = (instruction >> 9) & 0x7;
        int pcOffset = signExtend(instruction & 0x1FF, 9);

        int address = memory[pc + pcOffset];
        memory[address] = registers[sr];
    }

    private void storeBaseOffset(int instruction) {
        int sr = (instruction >> 9) & 0x7;
        int baseR = (instruction >> 6) & 0x7;
        int offset = signExtend(instruction & 0x3F, 6);

        memory[registers[baseR] + offset] = registers[sr];
    }

    private void branch(int instruction) {
        int n = (instruction >> 11) & 0x1;
        int z = (instruction >> 10) & 0x1;
        int p = (instruction >> 9) & 0x1;
        int pcOffset = signExtend(instruction & 0x1FF, 9);

        boolean shouldBranch =
                (n == 1 && conditionFlag == ConditionFlag.NEGATIVE) ||
                        (z == 1 && conditionFlag == ConditionFlag.ZERO) ||
                        (p == 1 && conditionFlag == ConditionFlag.POSITIVE);

        if (shouldBranch) {
            pc += pcOffset; // PC уже указывает на следующую инструкцию
        }
    }

    private void jump(int instruction) {
        int baseR = (instruction >> 6) & 0x7;
        pc = registers[baseR];
    }

    private void trap(int instruction) {
        int trapVector = instruction & 0xFF;

        switch (trapVector) {
            case 0x21: // TRAP_OUT - вывод символа
                System.out.print((char) (registers[0] & 0xFF));
                break;
            case 0x23: // TRAP_IN - ввод символа
                try {
                    registers[0] = System.in.read();
                } catch (IOException e) {
                    registers[0] = 0;
                }
                break;
            case 0x25: // TRAP_HALT
                running = false;
                break;
            default:
                throw new RuntimeException("Неизвестный TRAP вектор: " + Integer.toHexString(trapVector));
        }
    }

    private void updateFlags(int register) {
        int value = registers[register];
        if (value == 0) {
            conditionFlag = ConditionFlag.ZERO;
        } else if ((value & 0x8000) != 0) {
            conditionFlag = ConditionFlag.NEGATIVE;
        } else {
            conditionFlag = ConditionFlag.POSITIVE;
        }
    }

    private int signExtend(int value, int bitCount) {
        if (((value >> (bitCount - 1)) & 1) == 1) {
            value |= (0xFFFF << bitCount);
        }
        return value;
    }

    public void printRegisters() {
        System.out.println("Состояние регистров:");
        for (int i = 0; i < registers.length; i++) {
            System.out.printf("R%d: 0x%04X (%d)\n", i, registers[i] & 0xFFFF, registers[i] & 0xFFFF);
        }
        System.out.println("PC: 0x" + Integer.toHexString(pc & 0xFFFF));
        System.out.println("Флаг: " + conditionFlag);
    }

    public void printMemory(int start, int end) {
        System.out.println("Дамп памяти:");
        for (int i = start; i <= end; i++) {
            System.out.printf("0x%04X: 0x%04X\n", i, memory[i] & 0xFFFF);
        }
    }

    public static void main(String[] args) {
        Compiler compiler = new Compiler();
        int[] program = compiler.compile("/home/vodobryshkin/progs/proj/IdeaProjects/6502/assembly/src/test/resources/test3.asm");
        System.out.println(Arrays.toString(program));
        Cpu cpu = new Cpu(program);
        cpu.run();
        cpu.printRegisters();
    }
}