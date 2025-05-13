package org.example.web.rest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.PatientMedicalRecord;

@Mapper
public interface PatientMedicalRecordMapper extends BaseMapper<PatientMedicalRecord> {

}
