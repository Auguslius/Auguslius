package org.example.web.rest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.domain.entity.Patient;

@Mapper
public interface PatientMapper extends BaseMapper<Patient> {
    @Select("SELECT COUNT(*) FROM health_system.patient " +
            "WHERE doctor_number = #{doctorNumber} " +
            "AND create_time >= #{date} " +
            "AND create_time < #{nextDate}")
    Integer countNewPatientsByDate(@Param("doctorNumber") Integer doctorNumber,
                                   @Param("date") String date,
                                   @Param("nextDate") String nextDate);
}
