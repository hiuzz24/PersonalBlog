package com.he181180.personalblog.mapper;

import com.he181180.personalblog.dto.UsersDTO;
import com.he181180.personalblog.entity.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UsersDTO toDTO(Users users);
    Users toEntity(UsersDTO usersDTO);
}

