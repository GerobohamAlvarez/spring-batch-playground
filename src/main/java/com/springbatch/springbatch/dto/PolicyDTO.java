package com.springbatch.springbatch.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PolicyDTO {
    
    private Long Policy;

    private Date Expiry;

    private String Region;

    private Long InsuredValue;

    private String Construction;

    private String BusinessType;

    private String Earthquake;

    private String Flood;

}
