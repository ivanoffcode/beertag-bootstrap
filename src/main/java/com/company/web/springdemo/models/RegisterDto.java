package com.company.web.springdemo.models;

import jakarta.validation.constraints.NotEmpty;

public class RegisterDto extends LoginDto {

    @NotEmpty(message = "Password confirmation can't be empty")
    private String passwordConfirm;

    @NotEmpty(message = "First name can't be empty")
    private String firstName;

    @NotEmpty(message = "Last name can't be empty")
    private String lastName;

    @NotEmpty(message = "Email can't be empty")
    private String email;

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
