package com.example.demo.services;

import com.example.demo.dtos.CvDTO;
import com.example.demo.dtos.ListCvIdDTO;
import com.example.demo.dtos.SearchDTO;
import com.example.demo.models.Cv;
import com.example.demo.models.CvStatus;
import com.example.demo.models.User;
import com.example.demo.repositories.CvRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.responses.CvResponse;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CVService implements ICvService {
    private final CvRepository cvRepository;
    private final UserRepository userRepository;

    @Override
    public Page<CvResponse> searchCv(int page, int size,String sortBy,String sortType, String userName, SearchDTO seacrhDTO) throws Exception {
            User user = userRepository.findByUserName(userName);
            if (user == null) {
                log.error("Processing: User with username: {} not found", userName);
                throw new IllegalArgumentException("User not found");
            }
        // Kiểm tra và set giá trị cho các trường
        if (StringUtils.isBlank(seacrhDTO.getFullName())) seacrhDTO.setFullName(null);
        if (StringUtils.isBlank(seacrhDTO.getSkill())) seacrhDTO.setSkill(null);
        if (CollectionUtils.isEmpty(seacrhDTO.getUniversity())) seacrhDTO.setUniversity(null);
        if (StringUtils.isEmpty(seacrhDTO.getTrainingSystem())) seacrhDTO.setTrainingSystem(null);
        if (StringUtils.isBlank(seacrhDTO.getGpa())) seacrhDTO.setGpa(null);
        if (CollectionUtils.isEmpty(seacrhDTO.getApplyPosition())) seacrhDTO.setApplyPosition(null);
        if (StringUtils.isBlank(seacrhDTO.getStatus())) seacrhDTO.setStatus(null);

        Sort.Direction direction = Sort.Direction.fromString(sortType);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Cv> cvPage = cvRepository.searchCv(pageable, userName, seacrhDTO.getFullName(), seacrhDTO.getSkill(), seacrhDTO.getStatus(), seacrhDTO.getDateOfBirth(), seacrhDTO.getUniversity(), seacrhDTO.getTrainingSystem(), seacrhDTO.getGpa(), seacrhDTO.getApplyPosition());
        log.info("Processing: {} found", cvPage.map(CvResponse::fromCv));
        return cvPage.map(CvResponse::fromCv);
    }


    public Cv getCvById(Long id) throws Exception {
        Cv cv = cvRepository.findById(id).orElseThrow(() -> new Exception("Cannot find CV with id =" + id));
        log.info("Processing: {} found", cv);
        return cv;
    }

    public Cv createCv(String username,CvDTO cvDTO) throws Exception {
        try {
            User user = userRepository.findByUserName(username);
            if (user != null){
                Cv newCv = Cv.builder()
                        .fullName(cvDTO.getFullName())
                        .dateOfBirth(cvDTO.getDateOfBirth())
                        .skill(cvDTO.getSkill())
                        .university(cvDTO.getUniversity())
                        .applyPosition(cvDTO.getApplyPosition())
                        .trainingSystem(cvDTO.getTrainingSystem())
                        .createdBy(user)
                        .gpa(cvDTO.getGpa())
                        .status(CvStatus.INPROGRESS)
                        .linkCV(cvDTO.getLinkCV())
                        .build();
                cvRepository.save(newCv);
                log.info("Processing: create CV: {}", newCv);
                return newCv;
            }
            return null;
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Cv updateCv(String username,Long id, CvDTO cvDTO) throws Exception {
        Cv existingCv = getCvById(id);
        if (existingCv != null) {
            User user = userRepository.findByUserName(username);
            if(user != null){
                Cv oldCv = existingCv;
                existingCv.setFullName(cvDTO.getFullName());
                existingCv.setDateOfBirth(cvDTO.getDateOfBirth());
                existingCv.setSkill(cvDTO.getSkill());
                existingCv.setUniversity(cvDTO.getUniversity());
                existingCv.setTrainingSystem(cvDTO.getTrainingSystem());
                existingCv.setCreatedBy(user);
                existingCv.setGpa(cvDTO.getGpa());
                existingCv.setApplyPosition(cvDTO.getApplyPosition());
                existingCv.setLinkCV(cvDTO.getLinkCV());
                Cv newCv = cvRepository.save(existingCv);
                log.info("Processing: Updated CV with id: {} from {} to {}", id, oldCv, existingCv);
                return newCv;
            }else {
                log.error("Processing: username not found: {}", username);
                throw new IllegalArgumentException("Username not found: "+username);
            }
        }else {
            log.error("Processing: CV with id: {} not existing", id);
            return null;
        }
    }

    @Override
    public void updateCvStatus(ListCvIdDTO id) throws Exception {
        List<Long> idList = id.ids;
        for (Long cvId : idList) {
            Optional<Cv> cvOptional = cvRepository.findById(cvId);
            if (cvOptional.isPresent()) {
                Cv cv = cvOptional.get();
                if (id.getStatus().equals("pass")) {
                    cv.setStatus(CvStatus.PASS);
                    cvRepository.save(cv);
                    log.info("Processing: update Cv status with Id: {} into {}", id, CvStatus.PASS);
                } else if (id.getStatus().equals("not_pass")) {
                    cv.setStatus(CvStatus.NOTPASS);
                    cvRepository.save(cv);
                    log.info("Processing: update Cv status with Id: {} into {}", id, CvStatus.NOTPASS);
                } else {
                    log.error("Processing: status value {} of Cv Id: {} is invalid", id.getStatus(), cvId);
                    throw new IllegalArgumentException("Status value is invalid");
                }
            } else {
                log.error("Processing: Cv with Id: {} not found", cvId);
            }
        }

    }
    
    @Override
    public void deleteCv(Long id) throws Exception {
        Cv cv = getCvById(id);
        if (cv != null) {
            cvRepository.delete(cv);
            log.info("Processing: Cv with Id: {} deleted successfully", id);
        } else log.error("Processing: Cv with Id: {} not found", id);
    }

    @Override
    public void deleteCvs(ListCvIdDTO ids) {
        List<Long> idList = ids.ids;
        cvRepository.deleteAllById(idList);
        log.info("Processing: Deleted successfully {} CVs", idList.size());
    }

    @Override
    public void saveCv(MultipartFile file, String username) throws IllegalAccessException {
        if (ExcelUploadService.isValidExcelFile(file)) {
            List<Cv> cvList = null;
            try {
                cvList = ExcelUploadService.getCvsFromExcel(file.getInputStream());

                // Get the user by username
                User user = userRepository.findByUserName(username);

                if (user == null) {
                    throw new IllegalAccessException("User with username " + username + " not found!");
                }
                // Set createdBy field for each Cv
                for (Cv cv : cvList) {
                    cv.setCreatedBy(user);
                    cv.setStatus(CvStatus.INPROGRESS);
                }
                cvRepository.saveAll(cvList);
                log.info("Import CV successfully!");
            } catch (IOException e) {
                throw new IllegalAccessException("The file is not valid!");
            }

        }
    }

}
