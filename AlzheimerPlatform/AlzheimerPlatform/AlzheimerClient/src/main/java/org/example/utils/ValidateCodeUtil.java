package org.example.utils;


import org.apache.commons.lang3.RandomUtils;
import org.example.domain.dto.ValidateDto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * 验证码生成工具类
 *
 * @author admin
 */
public class ValidateCodeUtil {

    private static ValidateDto validateDto = null; // 验证码类，用于最后返回此对象，包含验证码图片base64和真值

    private static Random random = new Random(); // 随机类，用于生成随机参数

    private static String randString = "0123456789abcdefghijkmnpqrtyABCDEFGHIJLMNQRTY";// 随机生成字符串的取值范围

    private static int width = 80; // 图片宽度

    private static int height = 34; // 图片高度

    private static int stringNum = 4; // 字符的数量

    private static int lineSize = 40; // 干扰线数量

    // 将构造函数私有化 禁止new创建
    private ValidateCodeUtil() {
        super();
    }

    /**
     * 获取随机字符,并返回字符的String格式
     *
     * @param index (指定位置)
     * @return 随机字符
     */
    private static String getRandomChar(int index) {
        // 获取指定位置index的字符，并转换成字符串表示形式
        return String.valueOf(randString.charAt(index));
    }

    /**
     * 获取随机指定区间的随机数
     *
     * @param min (指定最小数)
     * @param max (指定最大数)
     * @return 随机数
     */
    private static int getRandomNum(int min, int max) {
        return RandomUtils.nextInt(min, max);
    }

    /**
     * 获得字体
     *
     * @return 字体
     */
    private static Font getFont() {
        return new Font("Fixedsys", Font.CENTER_BASELINE, 25); // 名称、样式、磅值
    }

    /**
     * 获得颜色
     *
     * @param fc 前景色
     * @param bc 背景色
     * @return 颜色
     */
    private static Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }

        int red = fc + random.nextInt(bc - fc - 16);
        int green = fc + random.nextInt(bc - fc - 14);
        int blue = fc + random.nextInt(bc - fc - 18);
        return new Color(red, green, blue);
    }

    /**
     * 绘制字符串,返回绘制的字符串
     *
     * @param g            Graphics
     * @param randomString 随机字符串
     * @param i            字符索引
     * @return 绘制后的随机字符串
     */
    private static String drawString(Graphics g, String randomString, int i) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(getFont()); // 设置字体
        g2d.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));// 设置颜色
        String randChar = String.valueOf(getRandomChar(random.nextInt(randString.length())));
        randomString += randChar; // 组装
        int rot = getRandomNum(5, 10);
        g2d.translate(random.nextInt(3), random.nextInt(3));
        g2d.rotate(rot * Math.PI / 180);
        g2d.drawString(randChar, 13 * i, 20);
        g2d.rotate(-rot * Math.PI / 180);
        return randomString;
    }

    /**
     * 绘制干扰线
     *
     * @param g Graphics
     */
    private static void drawLine(Graphics g) {
        // 起点(x,y) 偏移量x1、y1
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
        g.drawLine(x, y, x + xl, y + yl);
    }

    /**
     * 生成Base64图片验证码
     *
     * @return 验证码DTO
     */
    public static ValidateDto getRandomCode() {
        validateDto = validateDto == null ? new ValidateDto() : validateDto;

        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();// 获得BufferedImage对象的Graphics对象
        g.fillRect(0, 0, width, height);// 填充矩形
        g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));// 设置字体
        g.setColor(getRandColor(110, 133));// 设置颜色
        // 绘制干扰线
        for (int i = 0; i <= lineSize; i++) {
            drawLine(g);
        }
        // 绘制字符
        String randomString = "";
        for (int i = 1; i <= stringNum; i++) {
            randomString = drawString(g, randomString, i);
            validateDto.setValue(randomString);
        }

        g.dispose();// 释放绘图资源
        ByteArrayOutputStream bs = null;
        try {
            bs = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bs);// 将绘制得图片输出到流
            String imgsrc = Base64.getEncoder().encodeToString(bs.toByteArray());
            validateDto.setBase64Str(imgsrc);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bs != null) {
                    bs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return validateDto;
    }
}