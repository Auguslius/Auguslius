package org.example.web.rest.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.entity.MMSEQuestion;
import org.example.domain.dto.PageDTO;
import org.example.domain.query.MMSEQuestionQuery;
import org.example.domain.vo.MMSEQuestionCategoryVO;
import org.example.domain.vo.MMSEQuestionShowVO;
import org.example.domain.vo.MMSEQuestionVO;

import java.util.List;

public interface MMSEQuestionService extends IService<MMSEQuestion> {
    PageDTO<MMSEQuestionVO> queryMMSEQuestionsPage(MMSEQuestionQuery query);
    
    List<MMSEQuestionShowVO> listAllMMSEQuestions();
    
    List<MMSEQuestionCategoryVO> listMMSEQuestionCategory();
}
