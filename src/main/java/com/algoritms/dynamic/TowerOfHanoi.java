package com.algoritms.dynamic;

import static java.lang.String.format;

import java.util.Stack;
import java.util.stream.IntStream;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * В классической задаче о ханойских башнях имеются 3 башни и N дисков раз ного
 * размера, которые могут перекладываться между башнями. В начале го ловоломки
 * диски отсортированы по возрастанию размера сверху вниз (то есть
 * каждый диск лежит на диске большего размера). Установлены следующие
 * ограничения:
 * (1) За один раз можно переместить только один диск.
 * (2) Диск кладется на вершину другой башни.
 * (3) Диск яельзя положить на диск меньшего размера.
 * Напишите программу для перемещения дисков с первой башни на последнюю
 * с использованием стеков.
 */
public class TowerOfHanoi {
    public static void main(String[] args) {
        var towersSize = 3;
        var towers = new Tower[towersSize];
        IntStream.range(0, towersSize).forEach(index -> towers[index] = new Tower(index));

        var sourceTower = towers[0];
        var disksSize = 3;

        IntStream.range(0, disksSize).forEach(index -> sourceTower.add(disksSize - index));

        var bufferTower = towers[1];
        var destinationTower = towers[2];
        sourceTower.moveDisks(disksSize, destinationTower, bufferTower);

        System.out.println("Source tower: " + sourceTower);
        System.out.println("Buffer tower: " + bufferTower);
        System.out.println("Destination tower: " + destinationTower);
    }

    @Data
    @RequiredArgsConstructor
    private static class Tower {
        private Stack<Integer> disks = new Stack<>();
        private final int index;

        //Положить элемент на башню
        public void add(int disk) {
            //Если текущий диск меньше чем новый
            if (!disks.isEmpty() && disks.peek() <= disk) {
                throw new RuntimeException(format("Couldn't add bigger disk %d to tower, current top %d", disk, disks.peek()));
            }
            disks.push(disk);
        }

        public void transferTo(Tower another) {
            System.out.printf("Transfer to disk: %d from one tower: %d to another: %d %n", disks.peek(), this.getIndex(), another.getIndex());
            another.add(disks.pop());
        }

        public void moveDisks(int size, Tower dest, Tower buffer) {
            if (size <= 0) {
                return;
            }

            System.out.printf("Move disks size: %d from one tower: %d to another: %d %n", size - 1, buffer.getIndex(), dest.getIndex());
            moveDisks(size - 1, buffer, dest);
            transferTo(dest);
            System.out.printf("Move disks after transfer size: %d from one tower: %d to another: %d %n", size - 1, dest.getIndex(), this.getIndex());
            buffer.moveDisks(size - 1, dest, this);
        }
    }
}
