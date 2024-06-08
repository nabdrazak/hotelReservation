package com.lautbiru.view;

import com.lautbiru.model.GuestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuestView {

    private static final Logger logger = LoggerFactory.getLogger(GuestView.class);

    public void printGuestData(GuestModel guestModel) {
        logger.info("Print Guest Details");
        logger.info("Booking Id  -->  "+ guestModel.getBookingId());
        logger.info("Guest Name  -->  "+ guestModel.getName());
        logger.info("Booking Date  -->  "+ guestModel.getDate());
        logger.info("Room Number  -->  "+ guestModel.getRoomNo());
    }
}
