package com.nerdery.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError implements Serializable {

    private String backendMessage;
    private String message;
    private String url;
    private String method;
    private LocalDateTime timestamp;


}
