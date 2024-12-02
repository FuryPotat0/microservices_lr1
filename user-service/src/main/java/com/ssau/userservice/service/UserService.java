package com.ssau.userservice.service;

import com.ssau.userservice.dto.UserDto;
import com.ssau.userservice.entity.User;
import com.ssau.userservice.repository.UserRepository;
import com.ssau.userservice.service.feign.CompanyServiceFeignClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final CompanyServiceFeignClient feignClient;

    @Transactional
    public Long createUser(UserDto dto) {
        Boolean existById = feignClient.existById(dto.getCompanyId());
        if (!existById) {
            throw new EntityNotFoundException(
                    "Компании с идентификатором %s не существует".formatted(dto.getCompanyId())
            );
        }
        return repository.save(UserDto.toEntity(dto)).getId();
    }

    public Boolean existById(Long userId) {
        boolean isExists = false;
        Optional<User> user = repository.findById(userId);
        if (user.isPresent()) {
            isExists = user.get().isEnabled();
        }
        return isExists;
    }

    public String getUserNameById(Long userId) {
        Optional<User> user = repository.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(
                    "Пользователя с идентификатором %s не существует".formatted(userId)
            );
        }
        return user.get().getName();
    }

    public Long setUserState(Long userId, boolean isEnabled) {
        Optional<User> optionalUser = repository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException(
                    "Пользователя с идентификатором %s не существует".formatted(userId)
            );
        }
        User user = optionalUser.get();
        user.setEnabled(isEnabled);
        return repository.save(user).getId();
    }

    public List<UserDto> getAllUsers() {
        List<User> users = repository.findAll();
        ArrayList<UserDto> userDtos = new ArrayList<>(users.size());
        for (User user : users) {
            UserDto dto = UserDto.toDto(user);
            dto.setCompanyName(
                    feignClient.getCompanyNameById(dto.getCompanyId())
            );
            userDtos.add(dto);
        }
        return userDtos;
    }

    public Long updateUser(UserDto dto) {
        User user = repository.findById(dto.getId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Пользователя с идентификатором %s не существует".formatted(dto.getId())
                ));
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            user.setName(dto.getEmail());
        }
        if (dto.getCompanyId() != null && !dto.getCompanyId().equals(user.getCompanyId())) {
            Boolean existById = feignClient.existById(dto.getCompanyId());
            if (!existById) {
                throw new EntityNotFoundException(
                        "Компании с идентификатором %s не существует".formatted(dto.getCompanyId())
                );
            }
            user.setName(dto.getName());
        }
        return user.getId();
    }
}
