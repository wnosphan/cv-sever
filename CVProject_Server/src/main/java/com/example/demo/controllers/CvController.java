package com.example.demo.controllers;

import com.example.demo.dtos.CvDTO;
import com.example.demo.dtos.ListCvIdDTO;
import com.example.demo.dtos.SearchDTO;
import com.example.demo.models.Cv;
import com.example.demo.responses.CvListResponse;
import com.example.demo.responses.CvResponse;
import com.example.demo.services.GetListService;
import com.example.demo.services.ICvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/cv")
@CrossOrigin(origins = "${fe.base-url}",
        allowedHeaders = "*",
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH}
)
@RequiredArgsConstructor
public class CvController {
    private static final String RESPONSE = "Response data: {}";
    private static final String PROCESS = "Processing: {}";
    private final ICvService cvService;
    private final GetListService getListService;
    @Operation(summary = "Create new CV", description = "Require cvDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create new Cv successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User not found")
    })
    @PostMapping("{username}")
    public ResponseEntity<?> postCV(@PathVariable("username") String username, @Valid @RequestBody CvDTO cvDTO, BindingResult result) {
        try {
            log.info("POST method data: {}", cvDTO);
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                log.error(errorMessages.toString());
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Cv cv = cvService.createCv(username,cvDTO);
            log.info(RESPONSE , cv);
            return ResponseEntity.ok(cv);
        } catch (Exception e) {
            log.error(PROCESS, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Get all Cv by User", description = "Require userName, page, limit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all Cv successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User not found")
    })

    @PostMapping("user/{username}")
    public ResponseEntity<?> getAll(@PathVariable(name = "username") String username,
                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "limit", defaultValue = "5") int limit,
                                     @RequestParam(name = "sort_type", defaultValue = "ASC") String sortType,
                                     @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                     @Valid @RequestBody SearchDTO searchDTO) {
            log.info("GET method data: {}, {}, {}, {}, {}, {}", username, page, limit, sortType, sortBy, searchDTO);
            try {
                Page<CvResponse> cvList = cvService.searchCv(page, limit,sortBy,sortType, username,searchDTO);
                int totalPage = cvList.getTotalPages();
                List<CvResponse> cvs = cvList.getContent();
                log.info(RESPONSE+" Cvs", cvs.size());
                return ResponseEntity.ok(CvListResponse.builder().cvResponses(cvs).totalPages(totalPage).build());
            } catch (Exception e) {
                log.error(PROCESS, e.getMessage());
                return ResponseEntity.badRequest().body(e.getMessage());
            }
    }

    @Operation(summary = "Get Cv by ID", description = "Cv ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create new Cv successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getCvById(@Valid @PathVariable("id") Long id) {
        try {
            log.info("GET method data: {}", id);
            Cv cv = cvService.getCvById(id);
            log.info(RESPONSE, cv);
            return ResponseEntity.ok(cv);
        } catch (Exception e) {
            log.error(PROCESS, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Update CV", description = "Require Cv ID, cvDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update Cv successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User not found")
    })
    @PutMapping("{username}/{id}")
    public ResponseEntity<?> updateCv(@PathVariable("username") String username,@PathVariable("id") long id, @Valid @RequestBody CvDTO cvDTO) {
        try {
            log.info("PUT method data: ID: {}, {}", id, cvDTO);
            Cv updateCv = cvService.updateCv(username,id, cvDTO);
            if (updateCv != null){
                log.info(RESPONSE, updateCv);
                return ResponseEntity.ok(updateCv);
            }else {
                log.error("Response data: Cv with Id: {} not found", id);
                return ResponseEntity.badRequest().body("CV with Id: "+id+" not found");
            }
        } catch (Exception e) {
            log.info(PROCESS, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Update CV status", description = "Require Cv ID, status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update status successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User not found")
    })
    @PatchMapping("/status")
    public ResponseEntity<?> updateCvStatus(@RequestBody ListCvIdDTO ids) {
        try {
            log.info("Patch method data: {}", ids);
            cvService.updateCvStatus(ids);
            log.info("Response data: CV status has been updated successfully");
            return ResponseEntity.ok("CV status has been updated successfully");
        } catch (Exception e) {
            log.error(PROCESS, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Delete Cv", description = "Require Cv ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cv deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCv(@PathVariable long id) {
        try {
            log.info("Delete method data: ID: {}", id);
            cvService.deleteCv(id);
            log.info("Response data: CV with id: {} deleted successfully!", id);
            return ResponseEntity.ok("CV with id: " + id + " deleted successfully!");
        } catch (Exception e) {
            log.error("Processing: "+e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Delete list Cv", description = "Require list of Cv ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List CV deleted successfully!"),
            @ApiResponse(responseCode = "400", description = "Invalid body"),
            @ApiResponse(responseCode = "401", description = "User not found")
    })
    @DeleteMapping("")
    public ResponseEntity<?> deleteCvs(@Valid @RequestBody ListCvIdDTO ids) {
        try {
            log.info("Delete method data: {}", ids);
            cvService.deleteCvs(ids);
            log.info("Response data: List CV deleted successfully! {}", ids.getIds());
            return ResponseEntity.ok("List "+ids.ids+" CV deleted successfully!");
        } catch (Exception e) {
            log.error("Processing: "+e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadCv(@RequestParam("file") MultipartFile file,
                                       @RequestHeader("username") String username) throws IllegalAccessException {
        try {
            log.info("Request data: {}, {}", file, username);
            cvService.saveCv(file, username);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            log.error(PROCESS, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/list/skill")
    public ResponseEntity<List<String>> getListSkill(){
        return ResponseEntity.ok(getListService.getListSkill());
    }
    @GetMapping("/list/apply-position")
    public ResponseEntity<List<String>> getListApplyPosition(){
        return ResponseEntity.ok(getListService.getListApplyPosition());
    }
    @GetMapping("/list/university")
    public ResponseEntity<List<String>> getListUniversity(){
        return ResponseEntity.ok(getListService.getListUniversity());
    }
}
