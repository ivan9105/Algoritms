package com.patterns.structural.flyweight;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.patterns.structural.flyweight.FlyWeightUsage.ROW_MAX_SIZE;
import static java.lang.String.format;
import static java.util.stream.Stream.iterate;

public class FlyWeightUsage {
    private static final String[] COLORS = {
            "#000", "#fff", "#333",
            "#666", "#999", "#0c3",
            "#0c8", "#777", "#555"
    };

    private static final int CELL_MAX_SIZE = 100000;
    static final int ROW_MAX_SIZE = 1000;

    public static void main(String[] args) {
        FlyWeightUsage executor = new FlyWeightUsage();
        executor.execute(true);
    }

    /**
     * if useFlyWeight == true, busy memory size is KB: 25970
     * else busy memory size is KB: 135760.03125
     *
     * @param useFlyWeight
     */
    private void execute(boolean useFlyWeight) {
        memoryLog();

        Random random = new Random();
        Grid grid = new Grid();
        CellTypeFactory cellTypeFactory = new CellTypeFactory();

        for (int i = 0; i < CELL_MAX_SIZE; i++) {
            System.out.println(format("Iteration %s from %s", i, CELL_MAX_SIZE));

            String text = UUID.randomUUID().toString();
            String color = COLORS[random.nextInt(COLORS.length - 1)];
            String textColor = COLORS[random.nextInt(COLORS.length - 1)];
            int width = random.nextInt(10);
            int height = random.nextInt(10);
            grid.put(
                    random.nextInt(ROW_MAX_SIZE - 1),
                    Cell.builder()
                            .text(text)
                            .type(useFlyWeight ?
                                    cellTypeFactory.getType(color, textColor, width, height) :
                                    CellType.builder()
                                            .textColor(textColor)
                                            .color(color)
                                            .width(width)
                                            .height(height)
                                            .build()
                            )
                            .build()
            );
        }

        memoryLog();
    }

    private void memoryLog() {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        System.out.println("KB: " + (double) (totalMemory - freeMemory) / 1024);
    }
}

class Grid {
    private List<List<Cell>> data = new ArrayList<>();

    Grid() {
        iterate(0, n -> n)
                .limit(ROW_MAX_SIZE)
                .forEach(x -> data.add(new ArrayList<>()));
    }

    void put(int rowIndex, Cell cell) {
        List<Cell> row = data.get(rowIndex);
        if (row == null) {
            row = new ArrayList<>();
            data.add(rowIndex, row);
        }

        row.add(cell);
    }
}

@Builder
@Data
class Cell {
    private String text;
    private CellType type;
}

@Builder
@Data
class CellType {
    private String color;
    private String textColor;
    private Integer width;
    private Integer height;
}

class CellTypeFactory {
    List<CellType> types = new ArrayList<>();

    CellType getType(String color, String textColor, Integer width, Integer height) {
        CellType existsType = types.stream()
                .filter(type ->
                        type.getColor().equals(color) &&
                                type.getTextColor().equals(textColor) &&
                                type.getWidth().equals(width) &&
                                type.getHeight().equals(height)).findFirst().orElse(null);

        if (existsType != null) {
            return existsType;
        }

        CellType newType = CellType.builder().
                color(color)
                .textColor(textColor)
                .height(height)
                .width(width)
                .build();

        types.add(newType);

        return newType;
    }
}