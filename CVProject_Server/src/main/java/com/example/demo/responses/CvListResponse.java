package com.example.demo.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class CvListResponse {
    @JsonProperty("cvs_list")
    public List<CvResponse> cvResponses;
    @JsonProperty("total")
    public int totalPages;
}
