package com.ssau.companyservice.controller;

import com.ssau.companyservice.dto.CompanyDto;
import com.ssau.companyservice.service.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService service;

    @GetMapping("/exist-by-id/{companyId}")
    public Boolean existById(@PathVariable("companyId") Long companyId) {
        return service.existsById(companyId);
    }

    @GetMapping("/all")
    public List<CompanyDto> getAllCompanies() {
        return service.findAll();
    }

    @PostMapping("/create-company")
    public ResponseEntity<?> createCompany(@RequestBody CompanyDto dto) {
        try {
            return new ResponseEntity<>(service.createCompany(dto), HttpStatus.CREATED);
        }
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{companyId}/name")
    public ResponseEntity<?> getCompanyNameById(@PathVariable("companyId") Long companyId) {
        try {
            return new ResponseEntity<>(service.getCompanyNameById(companyId), HttpStatus.OK);
        }
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{companyId}/change-chief")
    public ResponseEntity<?> changeCompanyChief(@PathVariable("companyId") Long companyId, @RequestBody Long chiefId) {
        try {
            service.changeCompanyChief(companyId, chiefId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
