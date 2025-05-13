package org.example.web.rest.Controller;

import lombok.extern.slf4j.Slf4j;
import org.example.common.enums.CommonEnum;
import org.example.common.result.Result;
import org.example.utils.TextSimilarityUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 文本相似度比对控制器
 */
@RestController
@RequestMapping("/text")
@Slf4j
public class TextSimilarityController {

    /**
     * 使用多种算法计算两段文本的相似度
     * @param textA 第一段文本
     * @param textB 第二段文本
     * @return 多种算法的相似度结果
     */
    @GetMapping("/similarity")
    public Result<Map<String, Double>> compareTextSimilarity(
            @RequestParam String textA,
            @RequestParam String textB) {
        try {
            log.info("开始比较文本相似度: textA={}, textB={}", textA, textB);
            
            long startTime = System.currentTimeMillis();
            
            // 使用多种算法计算相似度
            Map<String, Double> similarities = new HashMap<>();
            
            // 1. 余弦相似度
            double cosineSim = TextSimilarityUtil.cosineSimilarity(textA, textB);
            similarities.put("cosine", cosineSim);
            
            // 2. Jaccard相似度
            double jaccardSim = TextSimilarityUtil.jaccardSimilarity(textA, textB);
            similarities.put("jaccard", jaccardSim);
            
            // 3. 编辑距离相似度
            double levenshteinSim = TextSimilarityUtil.levenshteinSimilarity(textA, textB);
            similarities.put("levenshtein", levenshteinSim);
            
            // 4. 汉明距离相似度
            double hammingSim = TextSimilarityUtil.hammingSimilarity(textA, textB);
            similarities.put("hamming", hammingSim);
            
            // 5. 平均相似度
            double avgSimilarity = (cosineSim + jaccardSim + levenshteinSim + hammingSim) / 4.0;
            similarities.put("average", avgSimilarity);
            
            long endTime = System.currentTimeMillis();
            log.info("文本相似度计算完成，耗时: {}ms", (endTime - startTime));
            
            // 在控制台输出结果
            System.out.println("文本A: " + textA);
            System.out.println("文本B: " + textB);
            System.out.println("余弦相似度: " + cosineSim);
            System.out.println("Jaccard相似度: " + jaccardSim);
            System.out.println("编辑距离相似度: " + levenshteinSim);
            System.out.println("汉明距离相似度: " + hammingSim);
            System.out.println("平均相似度: " + avgSimilarity);
            
            return Result.success(CommonEnum.SUCCESS.getCode(), "相似度计算成功", similarities);
            
        } catch (Exception e) {
            log.error("文本相似度计算失败", e);
            return Result.fail(CommonEnum.FAIL.getCode(), "文本相似度计算失败: " + e.getMessage());
        }
    }

} 