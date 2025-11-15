package com.javarush.island.zybin;

import com.javarush.island.zybin.entity.island.Island;

public class Runner {
    public static void main(String[] args) {
        Island island = new Island(120, 20);
        Simulation simulation = new Simulation(island);
        simulation.start();
    }
}
