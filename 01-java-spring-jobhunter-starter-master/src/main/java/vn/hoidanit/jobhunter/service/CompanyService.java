package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository ;
    public CompanyService( CompanyRepository companyRepository){
        this.companyRepository=companyRepository;
    }

    //tạo công ty mới
    public Company handleCreateCompany (Company c ){
        return this.companyRepository.save(c);
    }

    //Lấy danh sách công ty
    public ResultPaginationDTO fetchAllCompanies (Specification<Company> spec, Pageable pageable){
        Page<Company> pageCompany = this.companyRepository.findAll(spec,pageable);

        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new  ResultPaginationDTO.Meta();
        //lấy tại phía front end
        meta.setPage(pageable.getPageNumber()+1);
        meta.setPageSize(pageable.getPageSize());

        //query và lấy tại database
        meta.setPages(pageCompany.getTotalPages());
        meta.setTotal(pageCompany.getTotalElements());

        //gán meta và kết quả vào ResultPaginationDTO
        result.setMeta(meta);
        result.setResult(pageCompany.getContent());

        return result;
    }

    //lấy công ty theo id 
    public Company fetchCompanyById(Long id) {
        Optional<Company> optionalCompany = this.companyRepository.findById(id);
        if(optionalCompany.isPresent()) {
            return optionalCompany.get();
        }
        return null;
    }   
    //Cập nhật công ty
    public Company handleUpdateCompany(Company cop){
        Company currentCompany = this.fetchCompanyById(cop.getId());
        if(currentCompany != null) {
            currentCompany.setName(cop.getName());
            currentCompany.setDescription(cop.getDescription());
            currentCompany.setAddress(cop.getAddress());
            currentCompany.setLogo(cop.getLogo());

            //update    
            currentCompany = this.companyRepository.save(currentCompany);
        }
        return currentCompany;
    }

    //xóa công ty 
    public void handleDeleteCompany(Long id) {
        this.companyRepository.deleteById(id);
    }

}
