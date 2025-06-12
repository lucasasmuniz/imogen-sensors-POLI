package com.poli.embarcados.imogen.controllers;

import com.poli.embarcados.imogen.domain.dtos.LotDTO;
import com.poli.embarcados.imogen.services.LotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/lots")
@RequiredArgsConstructor
public class LotController {
    private final LotService service;

    @PostMapping()
    public ResponseEntity<LotDTO> insert(@Valid @RequestBody LotDTO dto){
        LotDTO newDTO = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(newDTO.id()).toUri();
        return ResponseEntity.created(uri).body(newDTO);
    }

    @GetMapping
    public ResponseEntity<Page<LotDTO>> findAllPaged(@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                     @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                     Pageable pageable){
        return ResponseEntity.ok(service.findAllPaged(startDate, endDate, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LotDTO> findByID(@PathVariable String id){
        return ResponseEntity.ok(service.findById(id));
    }
}
