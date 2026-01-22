package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.error.IdInValidException;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    
    
    //getall
    public ResultPaginationDTO fetchAllUser(Specification<User> spec,Pageable pageable) {

        Page<User> pageUser = userRepository.findAll(spec, pageable);

        // meta
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        // map User -> ResponseDTO
        List<ResUserDTO> users = pageUser.getContent()
            .stream()
            .map(user -> {
                ResUserDTO dto = new ResUserDTO();
                dto.setId(user.getId());
                dto.setName(user.getName());
                dto.setEmail(user.getEmail());
                String gender = user.getGender() != null
                    ? user.getGender().toString()
                    : null;
                dto.setAge(user.getAge());
                dto.setAddress(user.getAddress());
                dto.setCreatedAt(user.getCreatedAt());
                return dto;
            })
            .toList();

        // result
        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setMeta(meta);
        result.setResult(users);

        return result;
    }

    //getid
    public ResUserDTO fetchUserById(Long id) {
        User user = userRepository.findById(id)
        .orElseThrow(() ->
            new IdInValidException(
                "User với id = " + id + " không tồn tại"
            )
        );
        ResUserDTO rpUser = new ResUserDTO();
        rpUser.setId(user.getId());
        rpUser.setEmail(user.getEmail());
        rpUser.setName(user.getName());
        rpUser.setGender(user.getGender().toString());
        rpUser.setAge(user.getAge());
        rpUser.setCreatedAt(user.getCreatedAt());
        rpUser.setUpdatedAt(user.getUpdatedAt());

        return rpUser;
    }
    
    //create
    public ResCreateUserDTO handleCreateUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(
                "Email " + user.getEmail() + " đã tồn tại, vui lòng sử dụng email khác");
            }
            User newUser = userRepository.save(user);
            
            ResCreateUserDTO rpUser = new ResCreateUserDTO();
            rpUser.setId(newUser.getId());
            rpUser.setName(newUser.getName());
            rpUser.setEmail(newUser.getEmail());
            rpUser.setGender(newUser.getGender().toString());
            rpUser.setAddress(newUser.getAddress());
            rpUser.setAge(newUser.getAge());
            rpUser.setCreatedAt(newUser.getCreatedAt());

            return rpUser;
    }

    //update
    public ResUpdateUserDTO handleUpdateUser(User repUser){
        User currentUser = userRepository.findById(repUser.getId())
        .orElseThrow(() ->
            new IdInValidException(
                "User với id = " + repUser.getId() + " không tồn tại để cập nhật"
            )
        );

        //set dữ liệu update
        currentUser.setId(repUser.getId());
        currentUser.setName(repUser.getName());
        currentUser.setGender(repUser.getGender());
        currentUser.setAge(repUser.getAge());
        currentUser.setAddress(repUser.getAddress());
        //save updating user
        User updatedUser = userRepository.save(currentUser);

        //convert sang UpdateDTO
        ResUpdateUserDTO rpUser = new ResUpdateUserDTO();
        rpUser.setId(updatedUser.getId());
        rpUser.setName(updatedUser.getName());
        rpUser.setGender(updatedUser.getGender().toString());
        rpUser.setAddress(updatedUser.getAddress());
        rpUser.setAge(updatedUser.getAge());
        rpUser.setUpdatedAt(updatedUser.getUpdatedAt());
        
        return  rpUser;
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



    public User handleGetUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token ,String email){
        return userRepository.findByRefreshTokenAndEmail(token ,email);
    }



}
