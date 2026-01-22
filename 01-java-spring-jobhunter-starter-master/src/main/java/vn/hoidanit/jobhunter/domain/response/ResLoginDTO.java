package vn.hoidanit.jobhunter.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import vn.hoidanit.jobhunter.domain.response.ResLoginDTO.UserLogin;

public class ResLoginDTO {

    @JsonProperty("access_token")
    private String accessToken;
    
    private UserLogin user;

    //lồng class trong class : non-static inner class
    public static class UserLogin{
        private Long id;
        private String email;
        private String name;

        public UserLogin() {

        }
        public UserLogin(Long id, String email ,String name) {
            this.id = id;
            this.email = email;
            this.name = name;
            
        }

        //getter setter UserLogin
        public Long getId() {
        return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    //lồng class trong class : non-static inner class
    public static class UserGetAccount{
        private UserLogin user;

        //getter setter UserGetAccount
        public UserGetAccount() {
        }

        public UserGetAccount(UserLogin user) {
            this.user = user;
        }

        public UserLogin getUser() {
            return user;
        }

        public void setUser(UserLogin user) {
            this.user = user;
        }
    }


    //getter setter ResLoginDTO
    public UserLogin getUser() {
        return user;
    }

    public void setUser(UserLogin user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
