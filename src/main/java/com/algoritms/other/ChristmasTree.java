package com.algoritms.other;

public class ChristmasTree {
    public static void main(String[] args) {
        int layers = 100;

        for (int layerIndex = 0; layerIndex < layers; layerIndex++) {
            for (int spaceIndex = 0; spaceIndex < layers - layerIndex; spaceIndex++) {
                System.out.print(" ");
            }

            for (int asteriskIndex = 0; asteriskIndex < (layerIndex * 2 + 1); asteriskIndex++) {
                System.out.print("*");
            }

            System.out.println();
        }


    }
}
