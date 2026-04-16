package com.smartcampus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    public static final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Sensor> sensors = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, List<SensorReading>> readings = new ConcurrentHashMap<>();

    static {
        Room r1 = new Room("SCU_DANCE-21", "Dance Hall", 15);
        Room r2 = new Room("SCU_YOGA-03", "Yoga Studio", 60);
        Room r3 = new Room("SCU_CLIMB-01", "Climbing Room", 200);

        rooms.put(r1.getId(), r1);
        rooms.put(r2.getId(), r2);
        rooms.put(r3.getId(), r3);
    }
}