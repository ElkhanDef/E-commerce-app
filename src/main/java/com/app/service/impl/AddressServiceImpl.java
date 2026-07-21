package com.app.service.impl;

import com.app.exception.ApplicationException;
import com.app.exception.data.ErrorCode;
import com.app.mapper.AddressMapper;
import com.app.model.dto.request.AddressRequestDto;
import com.app.model.dto.response.AddressResponseDto;
import com.app.model.entity.AddressEntity;
import com.app.model.entity.UserEntity;
import com.app.repository.AddressRepository;
import com.app.repository.UserRepository;
import com.app.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public AddressServiceImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }


    @Override
    @Transactional
    public AddressResponseDto createAddress(AddressRequestDto request, Long userId) {
        log.info("ActionLog.createAddress.start");
        AddressEntity newAddress = AddressMapper.INSTANCE.toEntity(request);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        AddressEntity savedAddress = addressRepository.save(newAddress);
        user.setAddress(savedAddress);
        log.info("ActionLog.createAddress.end");
        return AddressMapper.INSTANCE.toDto(savedAddress);
    }

    @Override
    @Transactional
    public AddressResponseDto updateAddress(AddressRequestDto request, Long userId) {
        log.info("ActionLog.updateAddress.start");
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        AddressEntity existingAddress = user.getAddress();
        if (existingAddress == null) {
            throw new ApplicationException(ErrorCode.ADDRESS_NOT_FOUND);
        }
        AddressEntity address = AddressMapper.INSTANCE.updateEntityFromDto(request, existingAddress);
        AddressEntity updatedAddress = addressRepository.save(address);
        log.info("ActionLog.updateAddress.end");
        return AddressMapper.INSTANCE.toDto(updatedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponseDto getAddressByUserId(Long userId) {
        log.info("ActionLog.getAddressByUserId.start");
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        AddressEntity address = user.getAddress();
        if (address == null) {
            throw new ApplicationException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        log.info("ActionLog.getAddressByUserId.end");
        return AddressMapper.INSTANCE.toDto(address);
    }

    @Override
    @Transactional
    public void deleteAddressByUserId(Long userId) {
        log.info("ActionLog.deleteAddressByUserId.start");
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        AddressEntity address = user.getAddress();
        if (address == null) {
            throw new ApplicationException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        user.setAddress(null);
        addressRepository.delete(address);

        log.info("ActionLog.deleteAddressByUserId.end");
    }
}
