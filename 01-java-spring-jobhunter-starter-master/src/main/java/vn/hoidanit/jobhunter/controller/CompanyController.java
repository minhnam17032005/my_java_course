package vn.hoidanit.jobhunter.controller;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;



@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;
    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @ApiMessage("create a company")
    public ResponseEntity<Company> createNewCompany(@Valid @RequestBody Company company){
        Company newCompany = this.companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }



    @GetMapping("/companies")
    @ApiMessage("fetch all companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
        @Filter Specification<Company> spec ,
        Pageable pageable) {
        
            return ResponseEntity
                .status(HttpStatus.OK).body(this.companyService.fetchAllCompanies(spec,pageable));
    }

    @GetMapping("/companies/{id}")
    @ApiMessage("fetch company by id")
    public ResponseEntity<Company> fetchCompanyById(@PathVariable("id") Long id) {
        Optional<Company> cOptional = this.companyService.findById(id);
        return ResponseEntity.ok().body(cOptional.get());
    }

    //UpdateMapping("/companies")
    @PutMapping("/companies")
    @ApiMessage("update a company")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company putManCompany) {
        Company company = this.companyService.handleUpdateCompany(putManCompany);
        return ResponseEntity.ok(company);
    }
    
    
    @DeleteMapping("/companies/{id}")
    @ApiMessage("delete company by id")
    public ResponseEntity<Void>  deleteCompany(@PathVariable("id") Long id) {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);
    }

}