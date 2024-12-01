package org.huydd.bus_ticket_Ecommercial_platform.responseModels;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class PageableResponse implements Serializable {
    private int totalResults;
    private int totalPage;
    private int currentPage;
    private Object results;

}
