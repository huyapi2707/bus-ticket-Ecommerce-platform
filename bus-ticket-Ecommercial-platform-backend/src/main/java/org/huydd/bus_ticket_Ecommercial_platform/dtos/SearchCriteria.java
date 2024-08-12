package org.huydd.bus_ticket_Ecommercial_platform.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;


}
