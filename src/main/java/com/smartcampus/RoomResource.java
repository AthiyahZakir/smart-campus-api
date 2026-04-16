package com.smartcampus;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    // GET all rooms
    @GET
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(DataStore.rooms.values());
        return Response.ok(roomList).build();
    }

    // POST - create a room
    @POST
    public Response createRoom(Room room) {
        room.setId(UUID.randomUUID().toString());
        DataStore.rooms.put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    // GET single room by ID
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Room not found\"}")
                    .build();
        }
        return Response.ok(room).build();
    }

    // DELETE a room
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Room not found\"}")
                    .build();
        }

        boolean hasSensors = DataStore.sensors.values()
                .stream()
                .anyMatch(s -> s.getRoomId().equals(roomId));

        if (hasSensors) {
            throw new RoomNotEmptyException(roomId);
        }

        DataStore.rooms.remove(roomId);
        return Response.noContent().build();
    }
}