package components;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Memory {
    private int[] memoryArray;
    private int current;

    public Memory() {
        memoryArray = new int[65536];
        current = 0;
    }

    public void add(int value) {
        memoryArray[current] = value;
        current = (current + 1) % 65536;
    }

    public int getCurrent() {
        return memoryArray[current];
    }

    public int getByIndex(int index) {
        return memoryArray[index];
    }
}
