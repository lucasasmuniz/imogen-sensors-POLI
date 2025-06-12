package com.poli.embarcados.imogen.controllers.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationError extends StandardException{

    private List<FieldMessage> errors = new ArrayList<>();
}
