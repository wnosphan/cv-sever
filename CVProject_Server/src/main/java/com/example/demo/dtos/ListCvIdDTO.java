package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListCvIdDTO {
    @JsonProperty("ids")
    public List<Long> ids;
    @JsonProperty("status")
    private String status;
}
