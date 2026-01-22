package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInValidException;


@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public UserController(UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }





    //getAll có filter,page,sort
    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
        @Filter Specification<User> spec ,
        Pageable pageable) {

    return ResponseEntity.status(HttpStatus.OK).body(
                this.userService.fetchAllUser(spec,pageable));
    }

    //getById
    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") Long id)
        throws IdInValidException {
            User fetchUser  = this.userService.fetchUserById(id);
        if(fetchUser == null){
            throw new IdInValidException("User với id = "+id+" không tồn tại");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.convertToResUserDTO(fetchUser));
    }

    //PostMapping("/users")
    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User postManUser) 
        throws IdInValidException {

        boolean isEmailExist =this.userService.isEmailExist(postManUser.getEmail());

        if (isEmailExist) {
            throw new IdInValidException("Email " + postManUser.getEmail()+ " đã tồn tại, vui lòng sử dụng email khác"
            );
        }
        String hashPassword =this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);

        User ericUser =this.userService.handleCreateUser(postManUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(ericUser));
    }

    
    //UpdateMapping("/users")
    @PutMapping("/users")
    @ApiMessage("update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@Valid @RequestBody User user) {
        User ericUser = userService.handUpdateUser(user);
        if(ericUser ==null ){
            throw new IdInValidException("User với id "+user.getId() +" không tồn tại");
        }
        return ResponseEntity.ok(this.userService.convertToResUpdateUserDTO(ericUser));
    }

    //delete
    @DeleteMapping("/users/{id}")
    @ApiMessage("delete a user")
    public ResponseEntity<Void>  deleteUser(@PathVariable("id") Long id)
            throws IdInValidException {
        User currentUser=  userService.fetchUserById(id);
        if(currentUser == null){
            throw new IdInValidException("User với id "+id+" không tồn tại");
        }
        userService.handleDeleteUser(id);      
        return ResponseEntity.ok(null);
    }

    

    
}
