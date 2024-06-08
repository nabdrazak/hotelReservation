package com.lautbiru.controller;

import com.lautbiru.microservices.FindAvailableRooms;
import com.lautbiru.microservices.GuestRegistration;
import com.lautbiru.microservices.RetrieveAllGuests;
import com.lautbiru.microservices.RetrieveGuestDetailsById;
import com.lautbiru.model.GuestModel;
import com.lautbiru.view.GuestView;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GuestController {

    private GuestModel guestModel;

    private GuestView guestView;

    public static Map<String, GuestModel> guestModels;

    static {
        guestModels = new HashMap();
    }

    public GuestController(GuestModel guestModel, GuestView guestView) {
        this.guestModel = guestModel;
        this.guestView = guestView;
    }

    public void setBookingId(String bookingId) {
        guestModel.setBookingId(bookingId);
    }

    public void setGuestName(String guestName) {
        guestModel.setName(guestName);
    }

    public void setRoomNumber(int roomNumber) {
        guestModel.setRoomNo(roomNumber);
    }

    public void setBookingDate(Date bookingDate) {
        guestModel.setDate(bookingDate);
    }

    public String getBookingId() {
        return guestModel.getBookingId();
    }

    public String getGuestName() {
        return guestModel.getName();
    }

    public int getRoomNumber() {
        return guestModel.getRoomNo();
    }

    public Date getBookingDate() {
        return guestModel.getDate();
    }

    public void viewBooking() {
        guestView.printGuestData(guestModel);
    }

    public void addGuest(HttpExchange httpExchange) throws IOException {
        GuestRegistration guestRegistration = new GuestRegistration();
        guestRegistration.copyHandle(httpExchange);
    }

    public void viewGuests(HttpExchange httpExchange) throws IOException {
        RetrieveAllGuests retrieveAllGuests = new RetrieveAllGuests();
        retrieveAllGuests.copyHandle(httpExchange);
    }

    public void viewAvailableRooms(HttpExchange httpExchange) throws IOException {
        FindAvailableRooms findAvailableRooms = new FindAvailableRooms();
        findAvailableRooms.copyHandle(httpExchange);
    }

    public void retrieveGuest(HttpExchange httpExchange) throws IOException {
        RetrieveGuestDetailsById retrieveGuestDetailsById = new RetrieveGuestDetailsById();
        retrieveGuestDetailsById.copyHandle(httpExchange);
    }

    @Override
    public String toString() {
        return guestModel.toString();
    }
}
