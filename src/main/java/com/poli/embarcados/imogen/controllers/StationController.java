package com.poli.embarcados.imogen.controllers;

import com.poli.embarcados.imogen.domain.dtos.StationDTO;
import com.poli.embarcados.imogen.services.StationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService service;

    @GetMapping()
    public ResponseEntity<List<StationDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping()
    public ResponseEntity<StationDTO> insert(@Valid @RequestBody StationDTO dto){
        StationDTO newDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(newDto.id()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationDTO> findById(@PathVariable String id){
        return ResponseEntity.ok(service.findById(id));
    }
}
