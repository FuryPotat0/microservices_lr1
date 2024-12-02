package com.ssau.userservice.dto;

import com.ssau.userservice.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;

    private String name;

    private String email;

    private String login;

    private String companyName;

    private Long companyId;

    public UserDto(Long id, String name, String email, String login, Long companyId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.companyId = companyId;
    }

    public static User toEntity(UserDto dto) {
        return new User(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getLogin(),
                dto.getCompanyId()
        );
    }

    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getCompanyId()
        );
    }
}
