package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchDTO {
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    private String skill;

    private List<String> university;

    @JsonProperty("training_system")
    private String trainingSystem;


    private String gpa;

    @JsonProperty("apply_position")
    private List<String> applyPosition;

    private String status;
}
