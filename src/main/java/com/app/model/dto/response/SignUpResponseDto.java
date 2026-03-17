package com.app.model.dto.response;

public class SignUpResponseDto {

    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;

    public SignUpResponseDto(String name,
                             String phoneNumber,
                             String email,
                             String lastName) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.lastName = lastName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String lastName;
        private String phoneNumber;
        private String email;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public SignUpResponseDto build() {
            return new SignUpResponseDto(this.name, this.phoneNumber, this.email, this.lastName);
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
