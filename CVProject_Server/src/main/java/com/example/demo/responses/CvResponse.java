package com.example.demo.responses;

import com.example.demo.models.Cv;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CvResponse {
    private Long id;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;
    private String skill;
    private String university;
    @JsonProperty("training_system")
    private String trainingSystem;
    @JsonProperty("create_by")
    private String createBy;
    private String gpa;
    @JsonProperty("apply_position")
    private String applyPosition;
    @JsonProperty("link_cv")
    private String linkCv;
    private String status;

    public static CvResponse fromCv(Cv cv){
        return CvResponse.builder()
                .id(cv.getId())
                .fullName(cv.getFullName())
                .dateOfBirth(cv.getDateOfBirth())
                .skill(cv.getSkill())
                .university(cv.getUniversity())
                .trainingSystem(cv.getTrainingSystem())
                .createBy(cv.getCreatedBy().getUserName())
                .gpa(cv.getGpa())
                .applyPosition(cv.getApplyPosition())
                .linkCv(cv.getLinkCV())
                .status(cv.getStatus())
                .build();
    }
}
