package com.lautbiru.model;

import java.util.Date;

public class GuestModel {

    private String bookingId;

    private String name;

    private int roomNo;

    private Date date;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "[bookingId='" + bookingId + '\'' +
                ", name='" + name + '\'' +
                ", roomNo='" + roomNo + '\'' +
                ", date='" + date + '\'' +
                ']';
    }
}
