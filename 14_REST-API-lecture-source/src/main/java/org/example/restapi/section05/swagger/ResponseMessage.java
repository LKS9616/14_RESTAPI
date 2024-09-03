package org.example.restapi.section05.swagger;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseMessage {

    private int HttpStatusCode;
    private String message;
    private Map<String, Object> results;

}
