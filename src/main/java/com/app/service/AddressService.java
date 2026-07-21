package com.app.service;

import com.app.model.dto.request.AddressRequestDto;
import com.app.model.dto.response.AddressResponseDto;

public interface AddressService {

    AddressResponseDto createAddress(AddressRequestDto request, Long userId);

    AddressResponseDto updateAddress(AddressRequestDto request, Long userId);

    AddressResponseDto getAddressByUserId(Long userId);

    void deleteAddressByUserId(Long userId);
}
