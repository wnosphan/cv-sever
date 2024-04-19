package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CvDTO {
    @NotBlank(message = "Full_name is required")
    @Size(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    private String skill;

    private String university;

    @JsonProperty("training_system")
    private String trainingSystem;

    @JsonProperty("gpa")
    private String gpa;

    @JsonProperty("apply_position")
    private String applyPosition;

    @JsonProperty("link_cv")
    private String linkCV;
}
