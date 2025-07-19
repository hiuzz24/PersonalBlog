package com.he181180.personalblog.Mapper;

import com.he181180.personalblog.DTO.UserUpdateDTO;
import com.he181180.personalblog.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toUser(Users user);


//    @Mapping(target = "isDeleted", ignore = true)
    void updateUser( @MappingTarget Users user, UserUpdateDTO userUpdateDTO);

}
