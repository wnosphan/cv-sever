package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "cv")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String skill;

    private String university;

    @Column(name = "training_system")
    private String trainingSystem;

    @ManyToOne
    @JoinColumn(name = "create_by")
    private User createdBy;

    @Column(name = "gpa")
    private String gpa;

    @Column(name = "apply_position")
    private String applyPosition;

    @Column(name = "link_cv")
    private String linkCV;

    private String status;

    @Override
    public String toString() {
        return "Cv{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", skill='" + skill + '\'' +
                ", university='" + university + '\'' +
                ", trainingSystem='" + trainingSystem + '\'' +
                ", createdBy=" + createdBy +
                ", gpa='" + gpa + '\'' +
                ", applyPosition='" + applyPosition + '\'' +
                ", linkCV='" + linkCV + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
