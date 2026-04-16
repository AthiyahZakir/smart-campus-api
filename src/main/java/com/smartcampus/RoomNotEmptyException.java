package com.smartcampus;

public class RoomNotEmptyException extends RuntimeException {
    public RoomNotEmptyException(String roomId) {
        super("Room " + roomId + " still has sensors. Remove them first.");
    }
}