package com.poli.embarcados.imogen.controllers;

import com.poli.embarcados.imogen.domain.dtos.LotDTO;
import com.poli.embarcados.imogen.services.LotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lots")
@RequiredArgsConstructor
public class LotController {
    private final LotService service;

    @PostMapping()
    public ResponseEntity<LotDTO> insert(@RequestBody LotDTO dto){
        LotDTO newDTO = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(newDTO.id()).toUri();
        return ResponseEntity.created(uri).body(newDTO);
    }

    @GetMapping
    public ResponseEntity<List<LotDTO>> findAll(){
        return ResponseEntity.ok(service.findALl());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LotDTO> findByID(@PathVariable String id){
        return ResponseEntity.ok(service.findById(id));
    }
}
