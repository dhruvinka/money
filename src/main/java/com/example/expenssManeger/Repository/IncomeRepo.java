package com.example.expenssManeger.Repository;
import com.example.expenssManeger.entity.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepo extends JpaRepository<IncomeEntity,Long> {


    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileId);

    List<IncomeEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);


    @Query("SELECT SUM(e.amount) FROM IncomeEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);

    //select * from expense_entity where profile_id=1 and date between '2023-01-01' and '2023-12-31' and name like '%keyword%' order by date desc
    List<IncomeEntity>  findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId, LocalDate startDate, LocalDate endDate, String keyword, Sort sort
    );

    //select * from expense_entity where profile_id=1 and date between '2023-01-01' and '2023-12-31' order by date desc
    List<IncomeEntity> findByProfileIdAndDateBetween(
            Long profileId, LocalDate startDate, LocalDate endDate
    );


}
