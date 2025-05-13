package org.example.web.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.entity.PatientMedicalRecord;
import org.example.domain.dto.PageDTO;
import org.example.domain.query.PatientMedicalRecordQuery;
import org.example.domain.vo.PatientMedicalRecordVO;

public interface PatientMedicalRecordService extends IService<PatientMedicalRecord> {

    /**
     * 添加病历记录
     * @param record 病历记录
     * @return 是否成功
     */
    Boolean addMedicalRecord(PatientMedicalRecord record);

    /**
     * 更新病历记录
     * @param record 病历记录
     * @return 是否成功
     */
    Boolean updateMedicalRecord(PatientMedicalRecord record);

    /**
     * 根据UUID删除病历记录
     * @param recordUuid 病历UUID
     * @return 是否成功
     */
    Boolean removeMedicalRecordByUuid(String recordUuid);


    /**
     * 病历分页条件查询
     * @param query 查询条件
     * @return 分页结果
     */
    PageDTO<PatientMedicalRecordVO> queryMedicalRecordPage(PatientMedicalRecordQuery query);
}