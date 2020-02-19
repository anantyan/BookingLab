package com.nursinglab.booking.component;

import java.util.ArrayList;
import java.util.List;

public class ResponseComponent {

    private Integer error;
    private String status;
    private RecordsComponent records;
    private ArrayList<ResultComponent> result;
    private List<BookingIdComponent> booking;

    public ResponseComponent(Integer error, String status, RecordsComponent records, ArrayList<ResultComponent> result, List<BookingIdComponent> booking) {
        this.error = error;
        this.status = status;
        this.records = records;
        this.result = result;
        this.booking = booking;
    }

    public List<BookingIdComponent> getBooking() {
        return booking;
    }

    public void setBooking(List<BookingIdComponent> booking) {
        this.booking = booking;
    }

    public ArrayList<ResultComponent> getResult() {
        return result;
    }

    public void setResult(ArrayList<ResultComponent> result) {
        this.result = result;
    }

    public RecordsComponent getRecords() {
        return records;
    }

    public void setRecords(RecordsComponent records) {
        this.records = records;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
