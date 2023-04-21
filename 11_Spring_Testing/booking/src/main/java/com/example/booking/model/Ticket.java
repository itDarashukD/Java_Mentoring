package com.example.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    private long id;
    private long eventId;
    private long userId;
    private int place;
    private Category category;

}
