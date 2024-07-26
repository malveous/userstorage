package com.globallogic.userstorage.util;

import com.globallogic.userstorage.model.request.PhoneInput;
import com.globallogic.userstorage.model.request.UserInput;
import com.globallogic.userstorage.model.response.UserRegistrationResponse;
import com.globallogic.userstorage.persistence.entity.Phone;
import com.globallogic.userstorage.persistence.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserStorageModelDataMapper {

    public static User mapUserInputToUserEntity(UserInput userInput) {
        return new User(userInput.getName(), userInput.getEmail(), userInput.getPassword());
    }

    public static List<Phone> mapPhoneInputToPhoneEntity(List<PhoneInput> phoneInputList, User ownerUserEntity) {
        return phoneInputList.stream().map(phoneInput -> new Phone(phoneInput.getNumber(), phoneInput.getCityCode(),
                phoneInput.getCountryCode(), ownerUserEntity)).collect(Collectors.toList());
    }

    public static UserRegistrationResponse mapUserEntityToUserResponse(User userEntity) {
        return new UserRegistrationResponse(userEntity.getUserId(), userEntity.getCreatedDate(),
                userEntity.getUpdatedDate(), userEntity.getLastLoginDate(), userEntity.getToken(),
                userEntity.isActive());
    }

}
