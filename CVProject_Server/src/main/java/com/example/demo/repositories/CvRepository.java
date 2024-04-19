package com.example.demo.repositories;

import com.example.demo.models.Cv;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface CvRepository extends JpaRepository<Cv, Long> {
    Page<Cv> findAll(Pageable pageable);

    @Query("SELECT c FROM Cv c WHERE c.createdBy.userName = :username" +
            " AND (:fullname IS NULL OR c.fullName LIKE CONCAT('%', :fullname, '%'))" +
            " AND (:skill IS NULL OR c.skill LIKE CONCAT('%', :skill, '%'))" +
            " AND (:status IS NULL OR c.status IN :status)" +
            " AND (:dateOfBirth IS NULL OR c.dateOfBirth = :dateOfBirth)" +
            " AND (:university IS NULL OR c.university IN :university)" +
            " AND (:trainingSystem IS NULL OR c.trainingSystem IN :trainingSystem)" +
            " AND (:gpa IS NULL OR c.gpa LIKE CONCAT('%', :gpa, '%'))" +
            " AND (:applyPosition IS NULL OR c.applyPosition IN :applyPosition)")




    Page<Cv> searchCv(Pageable pageable, @Param("username") String userName, @Param("fullname") String fullName, @Param("skill") String skill, @Param("status") String status, @Param("dateOfBirth") LocalDate dateOfBirth, @Param("university") List<String> university, @Param("trainingSystem") String trainingSystem, @Param("gpa") String gpa, @Param("applyPosition") List<String> applyPosition);

}
