package com.smartcampus;

import java.util.UUID;
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

        Sensor s1 = new Sensor();
        s1.setId(UUID.randomUUID().toString());
        s1.setType("Occupancy");
        //s1.setStatus("ACTIVE");
        s1.setStatus("MAINTENANCE");
        s1.setCurrentValue(0.0);
        s1.setRoomId("SCU_YOGA-03");

        Sensor s2 = new Sensor();
        s2.setId(UUID.randomUUID().toString());
        s2.setType("CO2");
        s2.setStatus("ACTIVE");
        s2.setCurrentValue(400.0);
        s2.setRoomId("SCU_CLIMB-01");

        sensors.put(s1.getId(), s1);
        sensors.put(s2.getId(), s2);

        rooms.get("SCU_YOGA-03").getSensorIds().add(s1.getId());
        rooms.get("SCU_CLIMB-01").getSensorIds().add(s2.getId());
    }
}