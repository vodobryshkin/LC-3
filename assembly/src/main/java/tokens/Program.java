package tokens;

import tokens.line.Line;
import tools.AssemblyFileReader;

import java.util.ArrayList;

public record Program(ArrayList<Line> lines) {

    public static Program parseProgram(ArrayList<String> program) {
        ArrayList<Line> lines = new ArrayList<>();

        for (String string : program) {
            Line line = Line.parseLine(string.strip());

            if (line != null) {
                lines.add(line);
            }
        }

        return new Program(lines);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Program(\n");

        for (Line line : lines) {
            sb.append(line.toString());
            sb.append("\n");
        }

        sb.append(")");

        return sb.toString();
    }

    public static void main(String[] args) {
        AssemblyFileReader reader = new AssemblyFileReader();
        Program.parseProgram(reader.readAssemblyFile("/home/vodobryshkin/progs/proj/IdeaProjects/6502/text.asm")).toString();
    }

}
