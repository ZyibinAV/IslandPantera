package com.javarush.island.zybin;

import com.javarush.island.zybin.island.Island;
import com.javarush.island.zybin.simulation.Simulation;

public class Main {
    public static void main(String[] args) {
        Island island = new Island(120, 20);
        Simulation simulation = new Simulation(island);
        simulation.start();
    }
}
