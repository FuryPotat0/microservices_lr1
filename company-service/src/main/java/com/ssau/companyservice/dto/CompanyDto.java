package com.ssau.companyservice.dto;

import com.ssau.companyservice.entity.Company;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {
    private Long id;

    private String name;

    private String ogrn;

    private String activityDescription;

    private String chiefName;
    
    private boolean isDeleted;

    private Long chiefId;

    public CompanyDto(Long id, String name, String ogrn, String activityDescription, boolean isDeleted, Long chiefId) {
        this.id = id;
        this.name = name;
        this.ogrn = ogrn;
        this.activityDescription = activityDescription;
        this.chiefId = chiefId;
    }

    public static Company toEntity(CompanyDto dto) {
        return new Company(
                dto.id,
                dto.name,
                dto.ogrn,
                dto.activityDescription,
                dto.isDeleted,
                dto.chiefId
        );
    }

    public static CompanyDto toDto(Company company) {
        return new CompanyDto(
                company.getId(),
                company.getName(),
                company.getOgrn(),
                company.getActivityDescription(),
                company.isDeleted(),
                company.getChiefId()
        );
    }
}
