package com.poli.embarcados.imogen.controllers;

import com.poli.embarcados.imogen.domain.dtos.LotDTO;
import com.poli.embarcados.imogen.services.LotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lots")
@RequiredArgsConstructor
public class LotController {
    private final LotService service;

    @PostMapping()
    public ResponseEntity<Void> insert(@RequestBody LotDTO dto){
        service.insert(dto);
        return ResponseEntity.noContent().build();
    }
}
