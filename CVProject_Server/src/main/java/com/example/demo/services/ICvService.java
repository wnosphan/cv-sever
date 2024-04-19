package com.example.demo.services;

import com.example.demo.dtos.CvDTO;
import com.example.demo.dtos.ListCvIdDTO;
import com.example.demo.dtos.SearchDTO;
import com.example.demo.models.Cv;
import com.example.demo.responses.CvResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ICvService {

    Page<CvResponse> searchCv(int page, int size, String sortBy, String sortType, String userName, SearchDTO seacrhDTO) throws Exception;

    public Cv getCvById(Long id) throws Exception;

    public Cv createCv(String username, CvDTO cvDTO) throws Exception;

    public Cv updateCv(String username, Long id, CvDTO cvDTO) throws Exception;

    public void updateCvStatus(ListCvIdDTO id) throws Exception;

    public void deleteCv(Long id) throws Exception;

    public void deleteCvs(ListCvIdDTO id) throws Exception;

    void saveCv(MultipartFile file, String username) throws IllegalAccessException;


}
