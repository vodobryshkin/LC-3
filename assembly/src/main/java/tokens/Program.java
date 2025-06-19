package tokens;

import lombok.Getter;
import tokens.line.Line;

import java.util.ArrayList;
import java.util.Objects;

@Getter
public final class Program {
    private final ArrayList<Line> lines;

    public Program(ArrayList<Line> lines) {
        this.lines = lines;
    }

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
    public ArrayList<Line> lines() {
        return lines;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Program) obj;
        return Objects.equals(this.lines, that.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines);
    }
}
