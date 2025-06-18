package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AssemblyFileReader {
    public AssemblyFileReader() {}

    public ArrayList<String> readAssemblyFile(String filename) {
        ArrayList<String> assemblies = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = br.readLine()) != null) {
                assemblies.add(line.strip());
            }

        } catch (IOException e) {
            System.err.println("Error while reading a file: " + e.getMessage());
        }

        return assemblies;
    }
}
