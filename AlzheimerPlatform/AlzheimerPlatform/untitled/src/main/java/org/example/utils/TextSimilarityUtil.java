package org.example.utils;

import java.util.*;

/**
 * 文本相似度计算工具类
 * 实现了多种常用的文本相似度算法
 */
public class TextSimilarityUtil {
    
    /**
     * 使用余弦相似度计算两段文本的相似度
     * 
     * @param text1 第一段文本
     * @param text2 第二段文本
     * @return 相似度值，范围0-1，值越大表示越相似
     */
    public static double cosineSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return 0.0;
        }
        
        if (text1.equals(text2)) {
            return 1.0;
        }
        
        // 将文本分词并计算词频
        Map<Character, Integer> vector1 = getTermFrequencyMap(text1);
        Map<Character, Integer> vector2 = getTermFrequencyMap(text2);
        
        // 计算余弦相似度
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        
        Set<Character> allTerms = new HashSet<>(vector1.keySet());
        allTerms.addAll(vector2.keySet());
        
        for (Character term : allTerms) {
            int freq1 = vector1.getOrDefault(term, 0);
            int freq2 = vector2.getOrDefault(term, 0);
            
            dotProduct += freq1 * freq2;
            norm1 += freq1 * freq1;
            norm2 += freq2 * freq2;
        }
        
        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }
        
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
    
    /**
     * 使用Jaccard相似度计算两段文本的相似度
     * 
     * @param text1 第一段文本
     * @param text2 第二段文本
     * @return 相似度值，范围0-1，值越大表示越相似
     */
    public static double jaccardSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return 0.0;
        }
        
        if (text1.equals(text2)) {
            return 1.0;
        }
        
        // 字符级别的Jaccard相似度
        Set<Character> set1 = new HashSet<>();
        Set<Character> set2 = new HashSet<>();
        
        for (char c : text1.toCharArray()) {
            set1.add(c);
        }
        
        for (char c : text2.toCharArray()) {
            set2.add(c);
        }
        
        // 计算交集大小
        Set<Character> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        
        // 计算并集大小
        Set<Character> union = new HashSet<>(set1);
        union.addAll(set2);
        
        // Jaccard相似度 = 交集大小 / 并集大小
        return (double) intersection.size() / union.size();
    }
    
    /**
     * 使用编辑距离(Levenshtein)计算两段文本的相似度
     * 
     * @param text1 第一段文本
     * @param text2 第二段文本
     * @return 相似度值，范围0-1，值越大表示越相似
     */
    public static double levenshteinSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return 0.0;
        }
        
        if (text1.equals(text2)) {
            return 1.0;
        }
        
        // 计算编辑距离
        int distance = levenshteinDistance(text1, text2);
        
        // 归一化为相似度
        int maxLength = Math.max(text1.length(), text2.length());
        if (maxLength == 0) {
            return 1.0;
        }
        
        return 1.0 - (double) distance / maxLength;
    }
    
    /**
     * 计算编辑距离(Levenshtein)
     * 
     * @param text1 第一段文本
     * @param text2 第二段文本
     * @return 编辑距离值
     */
    private static int levenshteinDistance(String text1, String text2) {
        int len1 = text1.length();
        int len2 = text2.length();
        
        // 创建距离矩阵
        int[][] distance = new int[len1 + 1][len2 + 1];
        
        // 初始化第一行和第一列
        for (int i = 0; i <= len1; i++) {
            distance[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            distance[0][j] = j;
        }
        
        // 填充距离矩阵
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (text1.charAt(i - 1) == text2.charAt(j - 1)) ? 0 : 1;
                distance[i][j] = Math.min(
                        Math.min(distance[i - 1][j] + 1, distance[i][j - 1] + 1),
                        distance[i - 1][j - 1] + cost);
            }
        }
        
        return distance[len1][len2];
    }
    
    /**
     * 使用汉明距离计算两段文本的相似度(需要等长字符串)
     * 
     * @param text1 第一段文本
     * @param text2 第二段文本
     * @return 相似度值，范围0-1，值越大表示越相似
     */
    public static double hammingSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return 0.0;
        }
        
        if (text1.equals(text2)) {
            return 1.0;
        }
        
        // 汉明距离需要等长字符串
        int minLength = Math.min(text1.length(), text2.length());
        int maxLength = Math.max(text1.length(), text2.length());
        
        // 计算汉明距离
        int distance = 0;
        for (int i = 0; i < minLength; i++) {
            if (text1.charAt(i) != text2.charAt(i)) {
                distance++;
            }
        }
        
        // 加上长度差异
        distance += (maxLength - minLength);
        
        // 归一化为相似度
        return 1.0 - (double) distance / maxLength;
    }
    
    /**
     * 获取文本的词频映射
     * 
     * @param text 输入文本
     * @return 词频映射
     */
    private static Map<Character, Integer> getTermFrequencyMap(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        
        return frequencyMap;
    }
} 