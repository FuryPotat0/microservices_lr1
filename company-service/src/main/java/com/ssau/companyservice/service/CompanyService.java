package com.ssau.companyservice.service;

import com.ssau.companyservice.dto.CompanyDto;
import com.ssau.companyservice.entity.Company;
import com.ssau.companyservice.kafka.CompanyKafkaProducer;
import com.ssau.companyservice.repository.CompanyRepository;
import com.ssau.companyservice.service.feignclient.UserFeignServiceClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository repository;
    private final UserFeignServiceClient userFeignServiceClient;
    private final CompanyKafkaProducer kafkaProducer;

    public Boolean existsById(Long companyId) {
        return repository.existsById(companyId);
    }

    @Transactional
    public Long createCompany(CompanyDto companyDto) {
        if (companyDto.getChiefId() != null) {
            Boolean isExistsUser = userFeignServiceClient.existUserById(companyDto.getChiefId());
            if (!isExistsUser){
                throw new EntityNotFoundException(
                        "Пользователь с идентификатором %s не найден!".formatted(companyDto.getChiefId())
                );
            }
        }
        return repository.save(CompanyDto.toEntity(companyDto)).getId();
    }

    public List<CompanyDto> findAll() {
        List<Company> companies = repository.findAll();
        List<CompanyDto> companyDtos = new ArrayList<>(companies.size());
        for (Company company : companies) {
            String chiefName = null;
            if (company.getChiefId() != null){
                chiefName = userFeignServiceClient.getUserNameById(
                        company.getChiefId()
                );
            }
            CompanyDto companyDto = CompanyDto.toDto(company);
            companyDto.setChiefName(chiefName);
            companyDtos.add(companyDto);
        }
        return companyDtos;
    }

    public String getCompanyNameById(Long companyId) {
        Optional<Company> company = repository.findById(companyId);
        return company.orElseThrow(
                () -> new EntityNotFoundException(
                        "Компания с идентификатором %s не найдена".formatted(companyId)
                )
        ).getName();
    }

    public void changeCompanyChief(Long companyId, Long chiefId) {
        Boolean isExistsUser = userFeignServiceClient.existUserById(chiefId);
        if (!isExistsUser){
            throw new EntityNotFoundException(
                    "Пользователь с идентификатором %s не найден!".formatted(chiefId)
            );
        }
        Company company = repository.findById(companyId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Компания с идентификатором %s не найдена".formatted(companyId)
                ));
        company.setChiefId(chiefId);
        repository.save(company);
    }

    public Long deleteCompany(Long companyId) {
        Optional<Company> company = repository.findById(companyId);
        if (company.isEmpty()) {
            throw new EntityNotFoundException(
                    "Компания с идентификатором %s не найдена".formatted(companyId)
            );
        }
        Company companyObj = company.get();
        companyObj.setDeleted(true);

        kafkaProducer.sendDeleteCompanyMessage(String.valueOf(companyId));

        return repository.save(companyObj).getId();
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.topic.company-deleted-user}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void updateUsersFor(String companyId) {
        Optional<Company> company = repository.findById(Long.valueOf(companyId));
        if(company.isPresent())
            repository.delete(company.get());
        else
            throw new EntityNotFoundException("Компания не найдена");
    }
}
