package com.simform.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {
    private String hotelId;
    private String name;
    private String location;
    private String about;
    private String userId;

    @Transient
    private List<Rating> ratings = new ArrayList<>();
}
