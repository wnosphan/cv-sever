package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CvStatusDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("status")
    private String status;
}
