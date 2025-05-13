package org.example.web.rest.Controller;

import com.google.gson.Gson;
import cn.xfyun.api.LfasrClient;
import cn.xfyun.config.LfasrFailTypeEnum;
import cn.xfyun.config.LfasrOrderStatusEnum;
import cn.xfyun.model.response.lfasr.LfasrOrderResult;
import cn.xfyun.model.response.lfasr.LfasrPredictResult;
import cn.xfyun.model.response.lfasr.LfasrResponse;
import cn.xfyun.model.response.lfasr.LfasrTransResult;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.example.common.enums.CommonEnum;
import org.example.common.result.Result;
import org.example.config.XfyunConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 音频转文字控制器 - 基于科大讯飞长语音转写服务
 */
@RestController
@RequestMapping("/audio")
@Slf4j
public class AudioScoreController {

    @Autowired
    private XfyunConfig xfyunConfig;
    
    private static final Gson GSON = new Gson();
    
    // 定义任务类型
    private static final String TASK_TYPE = "transfer"; // 仅转写，不进行翻译或质检

    /**
     * 服务鉴权参数，使用@PostConstruct初始化
     */
    private String APP_ID;
    private String SECRET_KEY;
    
    @javax.annotation.PostConstruct
    public void init() {
        APP_ID = xfyunConfig.getAppId();
        SECRET_KEY = xfyunConfig.getSecretKey();
        log.info("科大讯飞API参数已初始化，APP_ID: {}", APP_ID);
    }

    /**
     * 通过OSS音频URL转换为文字（长语音转写）
     * @param ossUrl OSS音频文件URL
     * @return 转换后的文字
     */
    @GetMapping("/transform")
    public Result<String> transformByUrl(@RequestParam String ossUrl) {
        try {
            // 验证URL格式
            URL url;
            try {
                url = new URL(ossUrl);
            } catch (MalformedURLException e) {
                log.error("无效的URL格式: {}", ossUrl, e);
                return Result.fail(CommonEnum.FAIL.getCode(), "无效的URL格式，请确保URL以http://或https://开头");
            }
            
            log.info("开始从OSS下载音频文件: {}", ossUrl);
            
            // 将OSS音频文件下载到临时目录
            String tempDir = System.getProperty("java.io.tmpdir");
            String fileExtension = getFileExtensionFromUrl(ossUrl);
            String tempFileName = UUID.randomUUID().toString() + fileExtension;
            Path tempFilePath = Paths.get(tempDir, tempFileName);
            
            // 下载文件
            try {
                downloadFile(url, tempFilePath);
                log.info("音频文件已下载到临时路径: {}", tempFilePath);
            } catch (IOException e) {
                log.error("从OSS下载文件失败: {}", ossUrl, e);
                return Result.fail(CommonEnum.FAIL.getCode(), "从OSS下载文件失败: " + e.getMessage());
            }
            
            // 调用音频转文字服务
            String text = transformAudioToText(tempFilePath.toString());
            
            // 处理完毕后删除临时文件
            try {
                Files.deleteIfExists(tempFilePath);
                log.info("临时文件已删除: {}", tempFilePath);
            } catch (IOException e) {
                log.warn("临时文件删除失败: {}", tempFilePath, e);
            }
            
            // 在控制台输出转写的文字
            System.out.println("============ 音频转写结果(OSS链接) ============");
            System.out.println(text);
            System.out.println("==========================================");
            
            return Result.success(CommonEnum.SUCCESS.getCode(), "音频转文字成功", text);
        } catch (Exception e) {
            log.error("音频转文字失败", e);
            return Result.fail(CommonEnum.FAIL.getCode(), "音频转文字失败: " + e.getMessage());
        }
    }
    
    /**
     * 从URL下载文件到指定路径
     * @param url 文件URL
     * @param targetPath 目标路径
     * @throws IOException 如果下载失败
     */
    private void downloadFile(URL url, Path targetPath) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(30000);
        
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("下载文件失败，HTTP响应码: " + responseCode);
        }
        
        try (InputStream in = connection.getInputStream()) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
    
    /**
     * 从URL获取文件扩展名
     * @param url 文件URL
     * @return 文件扩展名（包含点，如".mp3"）
     */
    private String getFileExtensionFromUrl(String url) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        int queryIndex = fileName.lastIndexOf('?');
        if (queryIndex > 0) {
            fileName = fileName.substring(0, queryIndex);
        }
        
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(dotIndex);
        }
        return ".mp3"; // 默认使用MP3扩展名
    }
    
    /**
     * 音频转文字 - 长语音转写
     * @param audioFilePath 音频文件路径
     * @return 转换后的文本
     */
    private String transformAudioToText(String audioFilePath) throws Exception {
        System.out.println("开始音频转写...");
        
        // 1、创建客户端实例
        LfasrClient lfasrClient = new LfasrClient.Builder(APP_ID, SECRET_KEY)
                .build();

        // 2、上传音频文件
        log.info("音频上传中...");
        LfasrResponse uploadResponse = lfasrClient.uploadFile(audioFilePath);
        
        if (uploadResponse == null) {
            String errorMsg = "上传失败，响应为空";
            log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        
        if (!StringUtils.equals(uploadResponse.getCode(), "000000")) {
            String errorMsg = String.format("上传失败，错误码：%s，错误信息：%s", 
                    uploadResponse.getCode(), uploadResponse.getDescInfo());
            log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        
        String orderId = uploadResponse.getContent().getOrderId();
        log.info("转写任务orderId：{}", orderId);
        System.out.println("音频上传成功，正在转写，订单ID: " + orderId);

        // 3、查询转写结果
        int status = LfasrOrderStatusEnum.CREATED.getKey();
        // 循环直到订单完成或失败
        int maxRetries = 60; // 最多尝试60次，每次20秒，最长等待20分钟
        int retryCount = 0;
        
        while (status != LfasrOrderStatusEnum.COMPLETED.getKey() && 
               status != LfasrOrderStatusEnum.FAILED.getKey() && 
               retryCount < maxRetries) {
            
            retryCount++;
            LfasrResponse resultResponse = lfasrClient.getResult(orderId, TASK_TYPE);
            
            if (!StringUtils.equals(resultResponse.getCode(), "000000")) {
                String errorMsg = String.format("转写任务失败，错误码：%s，错误信息：%s", 
                        resultResponse.getCode(), resultResponse.getDescInfo());
                log.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }

            // 获取订单状态信息
            if (resultResponse.getContent() != null && resultResponse.getContent().getOrderInfo() != null) {
                status = resultResponse.getContent().getOrderInfo().getStatus();
                int failType = resultResponse.getContent().getOrderInfo().getFailType();

                // 根据状态输出日志
                LfasrOrderStatusEnum statusEnum = LfasrOrderStatusEnum.getEnum(status);
                if (statusEnum != null) {
                    log.info("订单状态：{}", statusEnum.getValue());
                    System.out.println("转写进度: " + statusEnum.getValue());

                    // 如果订单失败，输出失败原因
                    if (statusEnum == LfasrOrderStatusEnum.FAILED) {
                        LfasrFailTypeEnum failTypeEnum = LfasrFailTypeEnum.getEnum(failType);
                        String errorMsg = String.format("订单处理失败，失败原因：%s", failTypeEnum.getValue());
                        log.error(errorMsg);
                        throw new RuntimeException(errorMsg);
                    }
                    
                    // 如果订单已完成，解析并返回结果
                    if (statusEnum == LfasrOrderStatusEnum.COMPLETED) {
                        return parseResultResponse(resultResponse);
                    }
                } else {
                    log.error("未知的订单状态：{}", status);
                }
            } else {
                log.error("返回结果中缺少订单信息");
            }

            // 与官方demo保持一致，等待20秒
            TimeUnit.SECONDS.sleep(20);
        }
        
        if (retryCount >= maxRetries) {
            throw new RuntimeException("查询转写结果超时，请稍后尝试查询结果");
        }
        
        return "转写失败，未能获取结果";
    }

    /**
     * 解析转写结果响应
     */
    private String parseResultResponse(LfasrResponse resultResponse) {
        try {
            return parseOrderResult(resultResponse.getContent().getOrderResult());
        } catch (Exception e) {
            log.error("转写结果解析失败", e);
            throw new RuntimeException("转写结果解析失败: " + e.getMessage());
        }
    }

    /**
     * 解析转写结果
     */
    private String parseOrderResult(String orderResultStr) {
        try {
            LfasrOrderResult orderResult = GSON.fromJson(orderResultStr, LfasrOrderResult.class);
            return getLatticeText(orderResult.getLattice());
        } catch (Exception e) {
            log.error("转写结果解析失败", e);
            throw new RuntimeException("转写结果解析失败: " + e.getMessage());
        }
    }

    /**
     * 从转写结果的lattice数组中提取文本
     */
    private String getLatticeText(List<LfasrOrderResult.Lattice> latticeList) {
        StringBuilder resultText = new StringBuilder();
        for (LfasrOrderResult.Lattice lattice : latticeList) {
            LfasrOrderResult.Json1Best json1Best = lattice.getJson1Best();
            if (json1Best == null || json1Best.getSt() == null || json1Best.getSt().getRt() == null) {
                continue;
            }
            // 获取角色信息
            String rl = json1Best.getSt().getRl();
            StringBuilder rlText = getRlText(json1Best);
            // 与官方demo保持一致的输出格式
            resultText.append(rlText);
        }
        return resultText.toString();
    }

    /**
     * 从Json1Best中提取识别结果文本并拼接
     */
    private StringBuilder getRlText(LfasrOrderResult.Json1Best json1Best) {
        StringBuilder rlText = new StringBuilder();
        for (LfasrOrderResult.RecognitionResult rt : json1Best.getSt().getRt()) {
            if (rt.getWs() == null) {
                continue;
            }
            for (LfasrOrderResult.WordResult ws : rt.getWs()) {
                if (ws.getCw() != null && !ws.getCw().isEmpty()) {
                    // 获取每个词的识别结果
                    String word = ws.getCw().get(0).getW();
                    if (word != null && !word.isEmpty()) {
                        rlText.append(word);
                    }
                }
            }
        }
        return rlText;
    }
}
