package org.example.rest.controller;

import org.example.common.result.Result;
import org.example.domain.dto.BindDoctorDto;
import org.example.domain.entity.Institution;
import org.example.domain.entity.User;

import org.example.domain.vo.UserVO;
import org.example.rest.service.ClientService;
import org.example.rest.service.DoctorService;
import org.example.rest.service.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/institution-doctor")
public class InstitutionDoctorController {

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/listInstitution/{institutionCategoryId}")
    public Result<List<Institution>> getInstitutionList(@PathVariable int institutionCategoryId) {
        List<Institution> institutionList = institutionService.getInstitutionByInstitutionCategoryId(institutionCategoryId);
        return Result.success(institutionList);
    }

    @GetMapping("/listDoctor")
    public Result<List<User>> getDoctorList(@RequestParam String institution) {
        List<User> doctorList = doctorService.getDoctorByInstitutionName(institution);
        return Result.success(doctorList);
    }

    @PatchMapping("/bindDoctorByDoctorNumber")
    public Result<Boolean> bindDoctorByDoctorNumber(@RequestBody BindDoctorDto bindDoctorDto){
        System.out.println(bindDoctorDto);
        boolean result = clientService.bindDoctorByDoctorNumber(bindDoctorDto);
        return Result.success(result);
    }

    @PatchMapping("/unbindDoctorByDoctorNumber")
    public Result<Boolean> unbindDoctorByDoctorNumber(@RequestBody BindDoctorDto bindDoctorDto){
        boolean result = clientService.unbindDoctorByDoctorNumber(bindDoctorDto);
        return Result.success(result);
    }

    @GetMapping("/getDoctorMsg/{doctorNumber}")
    public Result<UserVO> getDoctorMsg(@PathVariable int doctorNumber){
        UserVO doctorMsg = doctorService.getDoctorMsg(doctorNumber);
        return Result.success(doctorMsg);
    }

}
