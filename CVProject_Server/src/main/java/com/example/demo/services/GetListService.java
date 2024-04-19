package com.example.demo.services;

import com.example.demo.models.ApplyPosition;
import com.example.demo.models.Skill;
import com.example.demo.models.University;
import com.example.demo.repositories.ApplyPositionRepository;
import com.example.demo.repositories.SkillRepository;
import com.example.demo.repositories.UniversityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetListService {
    private final ApplyPositionRepository applyPositionRepository;
    private final SkillRepository skillRepository;
    private final UniversityRepository universityRepository;
    public List<String> getListApplyPosition(){
        return applyPositionRepository.findAll().stream().map(ApplyPosition::getName).toList();
    }
    public List<String> getListSkill(){
        return skillRepository.findAll().stream().map(Skill::getSkillItem).toList();
    }
    public List<String> getListUniversity(){
        return universityRepository.findAll().stream().map(University::getName).toList();
    }
}
