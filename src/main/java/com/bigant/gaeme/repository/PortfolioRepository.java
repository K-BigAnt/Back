package com.bigant.gaeme.repository;

import com.bigant.gaeme.repository.entity.Portfolio;
import com.bigant.gaeme.repository.entity.User;
import java.util.List;
import javax.sound.sampled.Port;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findAllByUser_Id(Long userId);

}
