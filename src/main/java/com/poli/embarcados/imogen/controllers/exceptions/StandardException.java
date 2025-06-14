package com.poli.embarcados.imogen.controllers.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class StandardException {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
