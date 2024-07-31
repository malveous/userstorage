package com.globallogic.userstorage.util;

import com.globallogic.userstorage.model.PhoneDto;
import com.globallogic.userstorage.model.request.UserInput;
import com.globallogic.userstorage.model.response.UserAuthenticationResponse;
import com.globallogic.userstorage.model.response.UserRegistrationResponse;
import com.globallogic.userstorage.persistence.entity.Phone;
import com.globallogic.userstorage.persistence.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserStorageModelDataUtility {

    public static User mapUserInputToUserEntity(UserInput userInput) {
        return new User(userInput.getName(), userInput.getEmail(), userInput.getPassword());
    }

    public static List<Phone> mapPhoneDtoListToPhoneEntityList(List<PhoneDto> phoneDtoList, User ownerUserEntity) {
        return phoneDtoList.stream().map(phoneDto -> new Phone(phoneDto.getNumber(), phoneDto.getCityCode(),
                phoneDto.getCountryCode(), ownerUserEntity)).collect(Collectors.toList());
    }

    public static List<PhoneDto> mapPhoneEntityListToPhoneDtoList(List<Phone> phonesEntityList) {
        return phonesEntityList.stream().map(phoneEntity -> new PhoneDto(phoneEntity.getNumber(),
                phoneEntity.getCityCode(), phoneEntity.getCountryCode())).collect(Collectors.toList());
    }

    public static UserRegistrationResponse mapUserEntityToUserRegistrationResponse(User userEntity) {
        return new UserRegistrationResponse(userEntity.getUserId(), userEntity.getCreatedDate(),
                userEntity.getLastLoginDate(), userEntity.getToken(), userEntity.isActive());
    }

    public static UUID generateRandomUUID() {
        return UUID.randomUUID();
    }

    public static String generateRandomUUIDAsString() {
        return generateRandomUUID().toString();
    }

    public static UserAuthenticationResponse mapUserEntityToUserAuthenticationResponse(User authenticatedUser) {
        var userAuthenticationResponse = UserAuthenticationResponse.builder().userId(authenticatedUser.getUserId())
                .created(authenticatedUser.getCreatedDate()).lastLogin(authenticatedUser.getLastLoginDate())
                .token(authenticatedUser.getToken()).active(authenticatedUser.isActive())
                .name(authenticatedUser.getName()).email(authenticatedUser.getEmail())
                .password(authenticatedUser.getPassword())
                .build();
        var authenticatedUserPhones = authenticatedUser.getPhoneList();

        if (!CollectionUtils.isEmpty(authenticatedUserPhones)) {
            userAuthenticationResponse.setPhones(mapPhoneEntityListToPhoneDtoList(authenticatedUserPhones));
        }

        return userAuthenticationResponse;
    }
}
