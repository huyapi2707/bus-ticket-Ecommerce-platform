package org.huydd.bus_ticket_Ecommercial_platform.responseObjects;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class PageableResponse implements Serializable {
    private int totalResults;
    private int totalPage;
    private int currentPage;
    private Object results;

}
