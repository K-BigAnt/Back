package com.bigant.gaeme.repository.entity;

import com.bigant.gaeme.dao.dto.UsStockDto.UsStockItem;
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
public class UsStock extends Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;

    public UsStockItem toDto() {
        return UsStockItem.builder()
                .name(this.getName())
                .symbol(this.getSymbol())
                .country(this.getCountry())
                .build();
    }

}
