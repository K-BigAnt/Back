package com.bigant.gaeme.repository.entity;

import com.bigant.gaeme.dao.dto.KrStockDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class KrStock extends Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isinCode;

    @Override
    public KrStockDto toDto() {
        return KrStockDto.builder()
                .name(this.getName())
                .symbol(this.getSymbol())
                .isinCode(this.isinCode)
                .build();
    }

}
