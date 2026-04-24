package com.app.model.dto.response;

public class UserResponseDto {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private boolean isActive;
    private boolean isVerified;

    public UserResponseDto(Long id, String name,
                           String lastName, String email,
                           String phoneNumber, boolean isActive,
                           boolean isVerified) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
        this.isVerified = isVerified;
    }

    public UserResponseDto() {}

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    public boolean isVerified() {
        return isVerified;
    }
    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {
        private Long id;
        private String name;
        private String lastName;
        private String email;
        private String phoneNumber;
        private boolean isActive;
        private boolean isVerified;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }
        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }
        public Builder isVerified(boolean isVerified) {
            this.isVerified = isVerified;
            return this;
        }
        public UserResponseDto build() {
            return new UserResponseDto(id, name, lastName, email, phoneNumber, isActive, isVerified);
        }
    }
}
