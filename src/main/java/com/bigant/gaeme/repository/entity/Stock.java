package com.bigant.gaeme.repository.entity;

import com.bigant.gaeme.dao.dto.StockDto;
import com.bigant.gaeme.repository.enums.StockType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String symbol;

    private boolean isDelisting;

    @Enumerated(value = EnumType.STRING)
    private StockType type;

    public abstract StockDto toDto();

}
