package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import vn.hoidanit.jobhunter.domain.Company;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.error.IdInValidException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;

    public UserService(UserRepository userRepository,CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }
    
    
    
    //getall
    public ResultPaginationDTO fetchAllUser(Specification<User> spec,Pageable pageable) {

        Page<User> pageUser = userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new  ResultPaginationDTO();
        
        // meta
        ResultPaginationDTO.Meta mt = new  ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResUserDTO> listUsers = pageUser.getContent()
            .stream().map(item -> new ResUserDTO(
                    item.getId(), 
                    item.getName(),
                    item.getAge(),
                    item.getEmail(),
                    item.getGender(),
                    item.getAddress(),    
                    item.getCreatedAt(), 
                    item.getUpdatedAt(),
                    new ResUserDTO.CompanyUser(
                        item.getCompany() != null ? item.getCompany().getId() : 0,
                        item.getCompany() != null ? item.getCompany().getName() : null)))
                .collect(Collectors.toList());

        rs.setResult(listUsers);
        return rs;
    }

    //getid
    public User fetchUserById(long id ){
        Optional<User> userOptional = this.userRepository.findById(id);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        return null;
    }

    //create
    public User handleCreateUser(User user) {
        //check company
        if(user.getCompany() !=null){
            Optional<Company> companyOptional =
                this.companyService.findById(user.getCompany().getId());
            user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
        }
        return this.userRepository.save(user);
    }

    //update
    public User handUpdateUser(User repUser){
        User currentUser=this.fetchUserById(repUser.getId());
        if(currentUser !=null){
            currentUser.setAddress(repUser.getAddress());
            currentUser.setGender(repUser.getGender());
            currentUser.setAge(repUser.getAge());
            currentUser.setName(repUser.getName());

            //check company
            Optional<Company> companyOptional =
                this.companyService.findById(repUser.getCompany().getId());
            repUser.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
            //update
            currentUser=this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    //delete
    public void handleDeleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IdInValidException(
                "User với id = " + id + " không tồn tại"
            );
        }
        userRepository.deleteById(id);
    }

    //check tên theo email
    public User handleGetUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    //cập nhật token
    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    //
    public User getUserByRefreshTokenAndEmail(String token ,String email){
        return userRepository.findByRefreshTokenAndEmail(token ,email);
    }

    //kiểm tra tồn tại email
    public boolean isEmailExist(String email) {
    return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser com =new  ResCreateUserDTO.CompanyUser();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());

        if(user.getCompany()!=null){
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            res.setCompany(com);

        }
        return res;
    }

    public ResUserDTO convertToResUserDTO(User user){
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.CompanyUser com = new  ResUserDTO.CompanyUser();

        if(user.getCompany() !=null){
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            res.setCompany(com);
        }


        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user){
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        ResUpdateUserDTO.CompanyUser com = new  ResUpdateUserDTO.CompanyUser();
        if(user.getCompany() !=null){
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            res.setCompany(com);
        }
        
        
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }


}