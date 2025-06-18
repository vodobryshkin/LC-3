package parser;

import tokens.Program;
import tools.AssemblyFileReader;

import java.util.ArrayList;

public class Tokenizer {
    public Tokenizer() {}

    public Program tokenizeProgram(ArrayList<String> program) {
        return Program.parseProgram(program);
    }

    public static void main(String[] args) {
        Tokenizer tokenizer = new Tokenizer();
        AssemblyFileReader reader = new AssemblyFileReader();
        Program program = tokenizer.tokenizeProgram(reader.readAssemblyFile("/home/vodobryshkin/progs/proj/IdeaProjects/6502/text.asm"));
        System.out.println(program);
    }
}
