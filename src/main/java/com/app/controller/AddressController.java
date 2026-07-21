package com.app.controller;

import com.app.model.dto.request.AddressRequestDto;
import com.app.model.dto.response.AddressResponseDto;
import com.app.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<AddressResponseDto> createAddress(@RequestBody AddressRequestDto request,
                                                            @AuthenticationPrincipal String userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.createAddress(request, Long.valueOf(userId)));
    }

    @PutMapping
    public ResponseEntity<AddressResponseDto> updateAddress(@RequestBody AddressRequestDto addressDto,
                                                            @AuthenticationPrincipal String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.updateAddress(addressDto, Long.valueOf(userId)));
    }

    @GetMapping
    public ResponseEntity<AddressResponseDto> getAddressByUserId(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(addressService.getAddressByUserId(Long.valueOf(userId)));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAddressByUserId(@AuthenticationPrincipal String userId) {
        addressService.deleteAddressByUserId(Long.valueOf(userId));
        return ResponseEntity.noContent().build();
    }
}
