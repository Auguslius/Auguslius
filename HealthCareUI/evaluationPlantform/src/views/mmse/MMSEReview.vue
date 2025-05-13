<template>
  <div class="mmse-review-container">
    <el-container>
      <el-header class="page-header">
        <span class="header-title">MMSE量表审查</span>
      </el-header>

      <el-main class="main-content">
        <el-card class="review-content" shadow="hover">
          <template #header>
            <div class="card-header">
              <div class="left-section">
                <span class="title-text">患者MMSE量表结果</span>
                <el-tag type="info" effect="plain" class="data-count">共 {{ filteredTableData.length }} 条记录</el-tag>
              </div>
              <div class="header-actions">
                <el-input
                  v-model="searchKeyword"
                  placeholder="搜索患者编号"
                  clearable
                  @clear="handleSearch"
                  @keyup.enter="handleSearch"
                  style="width: 250px"
                  class="search-input"
                >
                  <template #prefix>
                    <el-icon><Search /></el-icon>
                  </template>
                </el-input>
                <el-button type="primary" @click="handleSearch" class="search-btn">
                  搜索
                </el-button>
                <el-button @click="refreshData" class="refresh-btn">
                  <el-icon><Refresh /></el-icon>
                </el-button>
              </div>
            </div>
          </template>
          
          <el-table
            :data="filteredTableData"
            border
            stripe
            v-loading="loading"
            element-loading-text="正在加载患者数据..."
            element-loading-background="rgba(255, 255, 255, 0.8)"
            style="width: 100%; margin-top: 15px;"
            :header-cell-style="{
              background: '#f5f7fa',
              color: '#303133',
              fontWeight: 'bold',
              textAlign: 'center',
              height: '50px',
              fontSize: '14px'
            }"
            :cell-style="{
              textAlign: 'center',
              padding: '10px 0'
            }"
            highlight-current-row
            @row-click="handleRowClick"
            class="mmse-table"
          >
            <el-table-column prop="patientUuid" label="患者编号" min-width="180" align="center" fixed>
              <template #default="scope">
                <span class="patient-id">{{ scope.row.patientUuid }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="mmseAnswerUuid" label="答案编号" min-width="180" align="center" show-overflow-tooltip />
            <el-table-column prop="answerTime" label="答题时间" min-width="160" align="center" sortable />
            <el-table-column prop="totalScore" label="总分" min-width="100" align="center" sortable>
              <template #default="scope">
                <el-tag 
                  :type="getScoreTagType(scope.row.totalScore)" 
                  effect="light"
                  class="score-tag"
                >
                  {{ scope.row.totalScore === 0 ? '未批改' : scope.row.totalScore + ' 分' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="认知状态" min-width="140" align="center">
              <template #default="scope">
                <el-tag 
                  :type="getCognitiveStatusType(scope.row.totalScore)"
                  class="status-tag"
                >
                  {{ scope.row.totalScore === 0 ? '未评估' : getCognitiveStatus(scope.row.totalScore) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="120" align="center" fixed="right">
              <template #default="scope">
                <el-button 
                  type="primary" 
                  size="small" 
                  @click.stop="viewDetails(scope.row)"
                  :icon="View"
                  class="view-btn"
                >
                  查看详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <el-pagination
            v-if="filteredTableData.length > 0"
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="filteredTableData.length"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            class="pagination"
            background
          />
          
          <el-empty v-if="filteredTableData.length === 0" description="暂无数据" :image-size="120" />
        </el-card>
      </el-main>
    </el-container>
    
    <!-- 详情抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      title="MMSE答案详情"
      direction="rtl"
      size="80%"
      :before-close="handleDrawerClose"
      class="details-drawer"
    >
      <template #header>
        <div class="drawer-header">
          <span class="drawer-title">MMSE答案详情</span>
        </div>
      </template>
      
      <div class="drawer-content">
        <el-descriptions 
          title="患者基本信息" 
          :column="2" 
          border 
          class="patient-info"
        >
          <el-descriptions-item label="患者编号" label-class-name="info-label" content-class-name="info-content">
            <el-tag size="medium" effect="plain" type="info">{{ currentPatient.patientUuid }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="答案编号" label-class-name="info-label" content-class-name="info-content">
            <el-tag size="medium" effect="plain" type="info">{{ currentPatient.mmseAnswerUuid }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="答题时间" label-class-name="info-label" content-class-name="info-content">
            {{ currentPatient.answerTime }}
          </el-descriptions-item>
          <el-descriptions-item label="总分" label-class-name="info-label" content-class-name="info-content">
            <el-tag :type="getScoreTagType(currentPatient.totalScore)" size="medium">
              {{ currentPatient.totalScore === 0 ? '未批改' : currentPatient.totalScore + ' 分' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="认知状态" :span="2" label-class-name="info-label" content-class-name="info-content">
            <el-tag :type="getCognitiveStatusType(currentPatient.totalScore)" effect="dark" size="medium">
              {{ currentPatient.totalScore === 0 ? '未评估' : getCognitiveStatus(currentPatient.totalScore) }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        
        <div class="answer-details-section">
          <div class="section-header">
            <span class="section-title">答题详情</span>
            <div class="section-actions">
              <el-button size="small" type="success" :icon="Check" @click="submitScores" :loading="submittingScores">
                提交打分
              </el-button>
              <el-button size="small" type="primary" :icon="Printer" @click="printDetails">
                打印详情
              </el-button>
              <el-button size="small" type="warning" :icon="Download" @click="exportToExcel" :loading="exporting">
                导出Excel
              </el-button>
            </div>
          </div>
          
          <el-alert
            v-if="showScoreAlert"
            :type="scoreAlertType"
            :title="scoreAlertTitle"
            :description="scoreAlertMessage"
            show-icon
            :closable="true"
            @close="showScoreAlert = false"
            class="score-alert"
          />
          
          <el-table
            v-if="currentPatient.answerDetails"
            :data="formatAnswerDetails(currentPatient.answerDetails)"
            border
            stripe
            style="width: 100%; margin-top: 20px;"
            :header-cell-style="{
              background: '#f5f7fa',
              color: '#303133',
              fontWeight: 'bold',
              textAlign: 'center'
            }"
            :cell-style="{
              textAlign: 'center',
              padding: '8px 0'
            }"
            class="details-table"
          >
            <el-table-column prop="questionNumber" label="题号" min-width="80" align="center" />
            <el-table-column prop="questionText" label="题目内容" min-width="220" align="center" show-overflow-tooltip>
              <template #default="scope">
                <el-tooltip
                  effect="dark"
                  :content="scope.row.questionText"
                  placement="top"
                  :enterable="false"
                >
                  <span class="question-text">{{ scope.row.questionText }}</span>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="section" label="所属大项" min-width="120" align="center">
              <template #default="scope">
                <el-tag size="small" type="info" effect="plain">{{ scope.row.section }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="questionType" label="题目类型" min-width="120" align="center">
              <template #default="scope">
                <el-tag size="small" type="success" effect="plain">{{ scope.row.questionType }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="value" label="患者答案" min-width="160" align="center" show-overflow-tooltip>
              <template #default="scope">
                <div v-if="isMediaAnswer(scope.row.questionType)" class="audio-container">
                  <template v-if="scope.row.value">
                    <div v-if="isAudioAnswer(scope.row.questionType)" class="audio-wrapper">
                      <audio 
                        ref="audioPlayer"
                        controls 
                        class="audio-player"
                        controlsList="nodownload"
                        @error="handleAudioError(scope.row)"
                        @loadstart="audioLoading[scope.row.questionNumber] = true"
                        @canplay="audioLoading[scope.row.questionNumber] = false"
                      >
                        <source :src="getAudioUrl(scope.row.value)" type="audio/mpeg">
                        您的浏览器不支持音频播放
                      </audio>
                      <el-icon 
                        v-if="audioLoading[scope.row.questionNumber]" 
                        class="loading-icon is-loading"
                      >
                        <Loading />
                      </el-icon>
                    </div>
                    <div v-if="audioError[scope.row.questionNumber]" class="audio-error">
                      <el-tooltip content="音频加载失败，请检查链接是否有效" placement="top">
                        <el-tag type="danger" size="small">加载失败</el-tag>
                      </el-tooltip>
                      <el-link 
                        type="primary" 
                        :href="getAudioUrl(scope.row.value)" 
                        target="_blank" 
                        class="audio-link"
                        :underline="false"
                      >
                        查看链接
                      </el-link>
                    </div>
                    
                    <div v-if="isImageAnswer(scope.row.questionType)" class="image-wrapper">
                      <el-image 
                        :src="getImageUrl(scope.row.value)"
                        fit="contain"
                        class="answer-image"
                        :preview-src-list="[getImageUrl(scope.row.value)]"
                        :initial-index="0"
                        @error="handleImageError(scope.row)"
                        @load="imageLoading[scope.row.questionNumber] = false"
                        v-loading="imageLoading[scope.row.questionNumber]"
                      >
                        <template #error>
                          <div class="image-error">
                            <el-icon><PictureFilled /></el-icon>
                            <span>加载失败</span>
                          </div>
                        </template>
                      </el-image>
                    </div>
                    <div v-if="imageError[scope.row.questionNumber]" class="image-error-msg">
                      <el-tooltip content="图片加载失败，请检查链接是否有效" placement="top">
                        <el-tag type="danger" size="small">加载失败</el-tag>
                      </el-tooltip>
                      <el-link 
                        type="primary" 
                        :href="getImageUrl(scope.row.value)" 
                        target="_blank" 
                        class="image-link"
                        :underline="false"
                      >
                        查看链接
                      </el-link>
                    </div>
                  </template>
                  <span v-else class="no-answer">未上传</span>
                </div>
                <span v-else class="patient-answer">{{ scope.row.value || '未作答' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="转换文字" min-width="180" align="center" v-if="hasMediaAnswers">
              <template #default="scope">
                <div v-if="isAudioAnswer(scope.row.questionType) && scope.row.value">
                  <div v-if="transcriptLoading[scope.row.questionNumber]" class="loading-text">
                    <el-icon class="is-loading"><Loading /></el-icon> 转换中...
                  </div>
                  <div v-else-if="transcriptError[scope.row.questionNumber]" class="error-text">
                    <el-tooltip content="转换失败，点击重试" placement="top">
                      <el-button 
                        type="danger" 
                        size="small" 
                        @click="convertAudioToText(scope.row)"
                        plain
                        circle
                        class="retry-btn"
                      >
                        <el-icon><RefreshRight /></el-icon>
                      </el-button>
                    </el-tooltip>
                  </div>
                  <div v-else-if="transcriptMap[scope.row.questionNumber]" class="transcript-text">
                    <el-tooltip 
                      :content="transcriptMap[scope.row.questionNumber]" 
                      placement="top" 
                      :enterable="false"
                      :show-after="500"
                    >
                      <span>{{ transcriptMap[scope.row.questionNumber] }}</span>
                    </el-tooltip>
                    
                    <!-- 添加相似度比较区域 -->
                    <div class="similarity-actions">
                      <!-- 输入期望答案 -->
                      <el-popover
                        placement="bottom"
                        :width="300"
                        trigger="click"
                        popper-class="expect-answer-popover"
                      >
                        <template #reference>
                          <el-button 
                            type="primary" 
                            size="small" 
                            circle
                          >
                            <el-icon><DataAnalysis /></el-icon>
                          </el-button>
                        </template>
                        <template #default>
                          <div class="expect-answer-container">
                            <div class="expect-answer-title">输入期望答案进行相似度比较</div>
                            <el-input
                              v-model="expectedAnswerInputs[scope.row.questionNumber]"
                              type="textarea"
                              :rows="3"
                              placeholder="请输入期望答案..."
                              class="expect-answer-input"
                            />
                            <div class="expect-answer-actions">
                              <el-button 
                                type="primary" 
                                @click="compareWithExpected(scope.row, expectedAnswerInputs[scope.row.questionNumber])"
                                :loading="similarityLoading[scope.row.questionNumber]"
                                :disabled="!transcriptMap[scope.row.questionNumber]"
                              >
                                开始比较
                              </el-button>
                            </div>
                          </div>
                        </template>
                      </el-popover>
                      
                      <!-- 显示相似度分析结果的浮窗 -->
                      <el-popover
                        v-if="similarityResults[scope.row.questionNumber]"
                        placement="right"
                        :width="400"
                        trigger="click"
                        popper-class="similarity-results-popover"
                      >
                        <template #reference>
                          <el-button 
                            type="success" 
                            size="small" 
                            circle
                            class="result-btn"
                          >
                            <el-icon><DataLine /></el-icon>
                          </el-button>
                        </template>
                        <template #default>
                          <div class="similarity-results">
                            <div class="similarity-title">相似度分析结果:</div>
                            <pre class="similarity-data">{{ JSON.stringify(similarityResults[scope.row.questionNumber], null, 2) }}</pre>
                            <div class="compared-texts">
                              <div class="compared-text-title">比较的文本:</div>
                              <div class="compared-text-item">
                                <div class="compared-text-label">转换文字:</div>
                                <div class="compared-text-content">{{ transcriptMap[scope.row.questionNumber] }}</div>
                              </div>
                              <div class="compared-text-item">
                                <div class="compared-text-label">期望答案:</div>
                                <div class="compared-text-content">{{ comparedTexts[scope.row.questionNumber] }}</div>
                              </div>
                            </div>
                          </div>
                        </template>
                      </el-popover>
                    </div>
                  </div>
                  <el-button 
                    v-else 
                    type="primary" 
                    size="small" 
                    @click="convertAudioToText(scope.row)"
                    plain
                    class="convert-btn"
                  >
                    <el-icon><Connection /></el-icon> 转换文字
                  </el-button>
                </div>
                <span v-else-if="isImageAnswer(scope.row.questionType)">-</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="分数" min-width="120" align="center">
              <template #default="scope">
                <el-input-number 
                  v-model="scoreMap[scope.row.questionNumber]" 
                  :min="0" 
                  :max="getMaxScore(scope.row.questionType)"
                  size="small"
                  controls-position="right"
                  @change="handleScoreChange"
                />
              </template>
            </el-table-column>
          </el-table>
          
          <el-empty v-else description="暂无答题详情数据" :image-size="100" />
        </div>
      </div>
      
      <template #footer>
        <div class="drawer-footer">
          <el-button type="primary" @click="drawerVisible = false">关闭</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, reactive } from 'vue';
import { getAllMMSEAnswers, listAllMMSEQuestions, scoreMMSEAnswer, transformAudioToText, compareTextSimilarity } from '@/api/mmse';
import { ElMessage } from 'element-plus';
import { Search, Refresh, View, Printer, Check, Download, Loading, RefreshRight, Connection, DataAnalysis, DataLine, PictureFilled } from '@element-plus/icons-vue';
import * as XLSX from 'xlsx';
import FileSaver from 'file-saver';

// 数据加载状态
const loading = ref(false);
// 表格数据
const tableData = ref([]);
// MMSE问题列表
const mmseQuestions = ref([]);
// 分数映射
const scoreMap = reactive({});
// 提交打分状态
const submittingScores = ref(false);
// 打分提示
const showScoreAlert = ref(false);
const scoreAlertType = ref('info');
const scoreAlertTitle = ref('');
const scoreAlertMessage = ref('');

// 导出状态
const exporting = ref(false);

// 音频加载状态
const audioLoading = reactive({});
// 音频错误状态
const audioError = reactive({});
// 图片加载状态
const imageLoading = reactive({});
// 图片错误状态
const imageError = reactive({});
// 转写结果
const transcriptMap = reactive({});
// 转写加载状态
const transcriptLoading = reactive({});
// 转写错误状态
const transcriptError = reactive({});
// 相似度比较结果
const similarityResults = reactive({});
// 相似度比较加载状态
const similarityLoading = reactive({});

// 期望答案输入存储
const expectedAnswerInputs = reactive({});
// 存储比较时使用的文本
const comparedTexts = reactive({});

// 判断是否存在记忆类答案
const hasAudioAnswers = computed(() => {
  if (!currentPatient.value || !currentPatient.value.answerDetails) return false;
  
  const details = currentPatient.value.answerDetails;
  return Object.values(details).some(item => 
    item.questionType === 'IMMEDIATE_MEMORY' || item.questionType === 'DELAYED_MEMORY'
  );
});

// 判断是否存在多媒体答案（音频或图片）
const hasMediaAnswers = computed(() => {
  if (!currentPatient.value || !currentPatient.value.answerDetails) return false;
  
  const details = currentPatient.value.answerDetails;
  return Object.values(details).some(item => 
    isMediaAnswer(item.questionType)
  );
});

// 过滤后的表格数据
const filteredTableData = computed(() => {
  if (!searchKeyword.value) {
    return tableData.value;
  }
  return tableData.value.filter(item => 
    item.patientUuid.toLowerCase().includes(searchKeyword.value.toLowerCase())
  );
});

// 分页参数
const currentPage = ref(1);
const pageSize = ref(10);
// 搜索关键字
const searchKeyword = ref('');

// 抽屉相关
const drawerVisible = ref(false);
const currentPatient = ref({});

// 获取所有MMSE问题列表
const fetchMMSEQuestions = async () => {
  try {
    const res = await listAllMMSEQuestions();
    if (res.code === 0) {
      mmseQuestions.value = res.data || [];
      console.log('MMSE问题列表:', mmseQuestions.value);
    } else {
      console.error('获取MMSE问题列表失败:', res);
    }
  } catch (error) {
    console.error('获取MMSE问题列表错误:', error);
  }
};

// 根据position获取题目内容
const getQuestionTextByPosition = (position) => {
  if (!mmseQuestions.value || mmseQuestions.value.length === 0) {
    return '未知题目';
  }
  
  const question = mmseQuestions.value.find(q => q.position === parseInt(position, 10));
  return question ? question.questionText : '未知题目';
};

// 获取所有患者的MMSE答案数据
const fetchAllAnswers = async () => {
  loading.value = true;
  try {
    const res = await getAllMMSEAnswers();
    
    // 判断成功的条件兼容多种状态码
    if (res.code === 0 || res.code === 200) {
      // 将对象转换为数组格式方便表格展示
      tableData.value = transformData(res.data);
      
      if (tableData.value.length === 0) {
        // 如果res.data为null或undefined，但res本身可能包含数据
        if (!res.data && typeof res === 'object') {
          const fallbackData = [];
          // 尝试其他可能的数据位置
          if (res.mmseAnswerUuid) {
            // 可能是单个答案数据
            fallbackData.push({
              patientUuid: res.patientUuid || '未知患者',
              mmseAnswerUuid: res.mmseAnswerUuid,
              answerTime: formatDateTime(res.answerTime),
              totalScore: res.totalScore || 0,
              answerDetails: res.answerDetails
            });
            tableData.value = fallbackData;
          }
        }
      }
    } else {
      // 优先显示后端返回的错误消息
      ElMessage.error(res.msg || res.message || '获取数据失败');
    }
  } catch (error) {
    // 尝试从错误响应中提取有效数据
    if (error && error.code === 200 && error.data) {
      tableData.value = transformData(error.data);
    } else {
      // 优先显示后端返回的错误消息
      const backendMessage = error.response?.data?.msg || error.response?.data?.message || error.msg || error.message;
      ElMessage.error(backendMessage || '获取数据失败');
    }
  } finally {
    loading.value = false;
  }
};

// 将接口返回的对象数据转换为数组
const transformData = (data) => {
  if (!data) return [];
  
  // 检查并打印数据结构
  console.log('API返回数据:', data);
  
  // 处理不同的数据格式
  if (Array.isArray(data)) {
    return data;
  }
  
  // 处理嵌套对象格式
  const result = [];
  
  try {
    // 如果data是一个对象，其中包含患者ID作为键
    if (typeof data === 'object' && !Array.isArray(data)) {
      Object.keys(data).forEach(patientUuid => {
        const info = data[patientUuid];
        // 添加调试信息
        console.log('处理患者数据:', patientUuid, info);
        
        result.push({
           patientUuid,
           mmseAnswerUuid: info.mmseAnswerUuid,
           answerTime: formatDateTime(info.answerTime),
           totalScore: info.totalScore || 0,
           answerDetails: info.answerDetails
        });
      });
    } 
    // 如果是单个患者的数据
    else if (data.mmseAnswerUuid) {
      result.push({
        patientUuid: data.patientUuid || '未知患者',
        mmseAnswerUuid: data.mmseAnswerUuid,
        answerTime: formatDateTime(data.answerTime),
        totalScore: data.totalScore || 0,
        answerDetails: data.answerDetails
      });
    }
  } catch (error) {
    console.error('数据转换错误:', error);
    ElMessage.error('数据格式错误，请检查接口返回');
  }
  
  console.log('转换后的数据:', result);
  return result;
};

// 格式化日期时间
const formatDateTime = (dateTimeStr) => {
  if (!dateTimeStr) return '';
  
  try {
    const date = new Date(dateTimeStr);
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`;
  } catch (error) {
    console.error('日期格式化错误:', error);
    return dateTimeStr;
  }
};

// 根据分数获取认知状态
const getCognitiveStatus = (score) => {
  if (score >= 27 && score <= 30) return '正常';
  if (score >= 21 && score <= 26) return '轻度认知障碍';
  if (score >= 10 && score <= 20) return '中度认知障碍';
  if (score >= 0 && score <= 9) return '重度认知障碍';
  return '未知';
};

// 根据认知状态获取标签类型
const getCognitiveStatusType = (score) => {
  if (score === 0) return 'info';
  if (score >= 27 && score <= 30) return 'success';
  if (score >= 21 && score <= 26) return 'warning';
  if (score >= 10 && score <= 20) return 'danger';
  if (score >= 1 && score <= 9) return 'danger';
  return 'info';
};

// 根据分数获取标签类型
const getScoreTagType = (score) => {
  if (score === 0) return 'info';
  if (score >= 27 && score <= 30) return 'success';
  if (score >= 21 && score <= 26) return 'warning';
  if (score >= 10 && score <= 20) return 'danger';
  if (score >= 1 && score <= 9) return 'danger';
  return 'info';
};

// 处理分页大小变化
const handleSizeChange = (val) => {
  pageSize.value = val;
};

// 处理页码变化
const handleCurrentChange = (val) => {
  currentPage.value = val;
};

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1;
};

// 刷新数据
const refreshData = () => {
  searchKeyword.value = '';
  currentPage.value = 1;
  fetchAllAnswers();
};

// 行点击事件
const handleRowClick = (row) => {
  viewDetails(row);
};

// 关闭抽屉前的回调
const handleDrawerClose = (done) => {
  done();
};

// 打印详情
const printDetails = () => {
  ElMessage.success('打印功能开发中...');
  // 实际打印功能可以在这里实现
};

// 查看详情
const viewDetails = (row) => {
  console.log('查看详情:', row);
  currentPatient.value = row;
  drawerVisible.value = true;
  
  // 重置各种状态
  Object.keys(audioLoading).forEach(key => {
    audioLoading[key] = false;
  });
  Object.keys(audioError).forEach(key => {
    audioError[key] = false;
  });
  Object.keys(imageLoading).forEach(key => {
    imageLoading[key] = false;
  });
  Object.keys(imageError).forEach(key => {
    imageError[key] = false;
  });
  Object.keys(transcriptLoading).forEach(key => {
    transcriptLoading[key] = false;
  });
  Object.keys(transcriptError).forEach(key => {
    transcriptError[key] = false;
  });
  // 重置相似度比较状态
  Object.keys(similarityLoading).forEach(key => {
    similarityLoading[key] = false;
  });
  
  // 初始化分数映射
  initScoreMap(row.answerDetails);
  
  // 预填充输入框数据
  if (row.answerDetails) {
    Object.keys(row.answerDetails).forEach(key => {
      const detail = row.answerDetails[key];
      if (detail && detail.expectedAnswer) {
        expectedAnswerInputs[key] = detail.expectedAnswer;
      }
    });
  }
};

// 格式化答案详情为表格数据
const formatAnswerDetails = (answerDetails) => {
  if (!answerDetails) return [];
  
  console.log('格式化答案详情:', answerDetails);
  
  try {
    // 处理不同格式的answerDetails
    if (typeof answerDetails === 'object' && !Array.isArray(answerDetails)) {
      return Object.entries(answerDetails).map(([questionNumber, details]) => {
        // 确保details是对象
        if (typeof details !== 'object' || details === null) {
          return {
            questionNumber,
            value: details,
            questionText: getQuestionTextByPosition(questionNumber)
          };
        }
        
        // 确保对于延迟记忆题型，value字段至少为空字符串而不是undefined
        let value = details.value;
        if (details.questionType === 'DELAYED_MEMORY' && value === undefined) {
          value = '';
        }
        
        return {
          questionNumber,
          section: details.section || '',
          questionType: details.questionType || '',
          expectedAnswer: details.expectedAnswer || '',
          value: value, // 使用上面处理过的value
          timeSpent: details.timeSpent || 0,
          questionText: getQuestionTextByPosition(questionNumber)
        };
      });
    } else if (Array.isArray(answerDetails)) {
      return answerDetails.map((item, index) => ({
        questionNumber: (index + 1).toString(),
        ...item,
        questionText: getQuestionTextByPosition((index + 1).toString())
      }));
    }
  } catch (error) {
    console.error('格式化答案详情出错:', error);
    return [];
  }
  
  return [];
};

// 获取题目类型对应的最大分值
const getMaxScore = (questionType) => {
  if (!questionType) return 1;
  
  // 根据题目类型返回对应的最大分值
  switch(questionType) {
    case 'IMMEDIATE_MEMORY':
    case 'DELAYED_MEMORY':
      return 3;
    case 'COMMAND_EXECUTION':
      return 3;
    case 'VISUAL_NAMING':
      return 2;
    default:
      return 1;
  }
};

// 初始化分数映射
const initScoreMap = (answerDetails) => {
  if (!answerDetails) return;
  
  // 清空现有分数
  Object.keys(scoreMap).forEach(key => delete scoreMap[key]);
  
  // 设置所有题目的初始分数
  Object.keys(answerDetails).forEach(questionNumber => {
    const details = answerDetails[questionNumber];
    const questionType = details.questionType || '';
    // 默认设置为该题型的最大分值（假设全对），医生可以根据实际情况修改
    scoreMap[questionNumber] = getMaxScore(questionType);
  });
  
  console.log('初始化分数映射:', scoreMap);
};

// 分数变更处理
const handleScoreChange = () => {
  calculateTotalScore();
};

// 计算总分
const calculateTotalScore = () => {
  let total = 0;
  Object.values(scoreMap).forEach(score => {
    total += Number(score) || 0;
  });
  console.log('当前总分:', total);
  return total;
};

// 提交打分
const submitScores = async () => {
  if (!currentPatient.value.patientUuid || !currentPatient.value.mmseAnswerUuid) {
    showScoreAlert.value = true;
    scoreAlertType.value = 'error';
    scoreAlertTitle.value = '提交失败';
    scoreAlertMessage.value = '患者信息不完整，无法提交打分';
    return;
  }
  
  if (Object.keys(scoreMap).length === 0) {
    showScoreAlert.value = true;
    scoreAlertType.value = 'warning';
    scoreAlertTitle.value = '提交失败';
    scoreAlertMessage.value = '请先完成打分';
    return;
  }
  
  submittingScores.value = true;
  
  try {
    const scoreData = {
      patientUuid: currentPatient.value.patientUuid,
      mmseAnswerUuid: currentPatient.value.mmseAnswerUuid,
      scoreMap: { ...scoreMap }
    };
    
    console.log('提交打分数据:', scoreData);
    
    const res = await scoreMMSEAnswer(scoreData);
    
    if (res.code === 0 || res.code === 200) {
      showScoreAlert.value = true;
      scoreAlertType.value = 'success';
      scoreAlertTitle.value = '打分成功';
      scoreAlertMessage.value = '患者MMSE量表评分已保存';
      
      // 刷新数据
      fetchAllAnswers();
      
      // 更新当前显示的总分
      currentPatient.value.totalScore = calculateTotalScore();
    } else {
      throw new Error(res.message || '提交失败');
    }
  } catch (error) {
    console.error('提交打分失败:', error);
    showScoreAlert.value = true;
    scoreAlertType.value = 'error';
    scoreAlertTitle.value = '提交失败';
    scoreAlertMessage.value = error.message || '提交分数时发生错误';
  } finally {
    submittingScores.value = false;
  }
};

// 导出到Excel
const exportToExcel = () => {
  if (!currentPatient.value || !currentPatient.value.answerDetails) {
    ElMessage.warning('没有可导出的数据');
    return;
  }
  
  exporting.value = true;
  
  try {
    // 准备导出数据
    const patientInfo = [
      ['患者信息'],
      ['患者编号', currentPatient.value.patientUuid],
      ['答案编号', currentPatient.value.mmseAnswerUuid],
      ['答题时间', currentPatient.value.answerTime],
      ['总分', currentPatient.value.totalScore === 0 ? '未批改' : currentPatient.value.totalScore],
      ['认知状态', currentPatient.value.totalScore === 0 ? '未评估' : getCognitiveStatus(currentPatient.value.totalScore)],
      ['']  // 空行分隔
    ];
    
    // 答题详情表头
    const headers = ['题号', '题目内容', '所属大项', '题目类型', '患者答案', '转换文字', '分数'];
    const detailsData = [headers];
    
    // 答题详情数据
    const details = formatAnswerDetails(currentPatient.value.answerDetails);
    details.forEach(item => {
      detailsData.push([
        item.questionNumber,
        item.questionText || '未知题目',
        item.section || '',
        item.questionType || '',
        item.value || '未作答',
        isAudioAnswer(item.questionType) ? (transcriptMap[item.questionNumber] || '-') : '-',
        scoreMap[item.questionNumber] || 0,
      ]);
    });
    
    // 合并数据
    const exportData = [...patientInfo, ...detailsData];
    
    // 创建工作簿
    const wb = XLSX.utils.book_new();
    const ws = XLSX.utils.aoa_to_sheet(exportData);
    
    // 设置列宽
    const wscols = [
      { wch: 10 },  // 题号
      { wch: 40 },  // 题目内容
      { wch: 15 },  // 所属大项
      { wch: 15 },  // 题目类型
      { wch: 20 },  // 患者答案
      { wch: 30 },  // 转换文字
      { wch: 10 },  // 分数
    ];
    ws['!cols'] = wscols;
    
    // 添加工作表到工作簿
    XLSX.utils.book_append_sheet(wb, ws, 'MMSE答题详情');
    
    // 生成Excel文件并下载
    const excelBuffer = XLSX.write(wb, { bookType: 'xlsx', type: 'array' });
    const blob = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    
    // 使用患者ID和日期作为文件名
    const fileName = `MMSE评估_${currentPatient.value.patientUuid}_${formatDate(new Date())}.xlsx`;
    FileSaver.saveAs(blob, fileName);
    
    ElMessage.success('Excel文件导出成功');
  } catch (error) {
    console.error('导出Excel失败:', error);
    ElMessage.error('导出Excel失败: ' + (error.message || '未知错误'));
  } finally {
    exporting.value = false;
  }
};

// 格式化日期为YYYYMMDD格式
const formatDate = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}${month}${day}`;
};

// 判断是否为需要显示音频的题目类型
const isAudioAnswer = (questionType) => {
  return questionType === 'IMMEDIATE_MEMORY' || questionType === 'REPETITION';
};

// 判断是否为需要显示媒体内容的题目类型
const isMediaAnswer = (questionType) => {
  return isAudioAnswer(questionType) || isImageAnswer(questionType);
};

// 判断是否为图片类型题目
const isImageAnswer = (questionType) => {
  return questionType === 'VISUAL_NAMING' || questionType === 'WRITING' || questionType === 'FIGURE_COPYING';
};

// 处理音频加载错误
const handleAudioError = (row) => {
  audioError[row.questionNumber] = true;
  audioLoading[row.questionNumber] = false;
  console.error('音频加载失败:', row.value);
};

// 处理图片加载错误
const handleImageError = (row) => {
  imageError[row.questionNumber] = true;
  imageLoading[row.questionNumber] = false;
  console.error('图片加载失败:', row.value);
};

// 获取音频URL
const getAudioUrl = (url) => {
  if (!url) {
    return '';
  }
  
  console.log('处理音频URL:', url);
  
  // 如果是相对路径，添加基础URL
  if (url.startsWith('/') && !url.startsWith('//')) {
    const fullUrl = import.meta.env.VITE_API_BASE_URL + url;
    console.log('转换为完整URL:', fullUrl);
    return fullUrl;
  }
  
  return url;
};

// 获取图片URL
const getImageUrl = (url) => {
  if (!url) {
    return '';
  }
  
  // 如果是相对路径，添加基础URL
  if (url.startsWith('/') && !url.startsWith('//')) {
    return import.meta.env.VITE_API_BASE_URL + url;
  }
  
  return url;
};

// 转换音频为文字
const convertAudioToText = async (row) => {
  if (!row.value) {
    ElMessage.warning('无音频链接可转换');
    return;
  }
  
  // 重置状态
  transcriptError[row.questionNumber] = false;
  transcriptLoading[row.questionNumber] = true;
  
  try {
    const ossUrl = getAudioUrl(row.value);
    console.log('转换音频URL:', ossUrl);
    
    const response = await transformAudioToText(ossUrl);
    console.log('音频转文字响应:', response);
    
    if (response.code === 0) {
      transcriptMap[row.questionNumber] = response.data || '无识别结果';
      ElMessage.success('音频转文字成功');
    } else {
      throw new Error(response.message || '转换失败');
    }
  } catch (error) {
    console.error('音频转文字错误:', error);
    transcriptError[row.questionNumber] = true;
    ElMessage.error('音频转文字失败: ' + (error.message || '未知错误'));
  } finally {
    transcriptLoading[row.questionNumber] = false;
  }
};

// 比较转写文本与期望答案的相似度
const compareWithExpected = async (row, inputExpectedAnswer) => {
  if (!transcriptMap[row.questionNumber]) {
    ElMessage.warning('需要先转换音频为文字');
    return;
  }
  
  similarityLoading[row.questionNumber] = true;
  
  try {
    // 使用转写文本作为第一个参数
    const textA = transcriptMap[row.questionNumber];
    // 使用用户输入的期望答案
    const textB = inputExpectedAnswer || '';
    
    // 保存比较的文本
    comparedTexts[row.questionNumber] = textB;
    
    console.log('比较文本:', { textA, textB });
    
    const response = await compareTextSimilarity(textA, textB);
    console.log('相似度比较响应:', response);
    
    if (response.code === 0) {
      // 直接存储API返回的原始数据
      similarityResults[row.questionNumber] = response.data;
      ElMessage.success('相似度比较完成');
    } else {
      throw new Error(response.message || '比较失败');
    }
  } catch (error) {
    console.error('相似度比较错误:', error);
    ElMessage.error('相似度比较失败: ' + (error.message || '未知错误'));
  } finally {
    similarityLoading[row.questionNumber] = false;
  }
};

onMounted(() => {
  fetchMMSEQuestions();
  fetchAllAnswers();
});
</script>

<style lang="scss" scoped>
.mmse-review-container {
  height: 100%;
  
  .page-header {
    background-color: #fff;
    border-bottom: 1px solid #ebeef5;
    padding: 0 20px;
    display: flex;
    align-items: center;
    height: 60px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.03);
    
    .header-title {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      position: relative;
      padding-left: 12px;
      
      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 4px;
        height: 18px;
        background-color: #008E65;
        border-radius: 2px;
      }
    }
  }
  
  .main-content {
    padding: 20px;
    height: calc(100% - 60px);
    background-color: #f5f7fa;
    
    .review-content {
      height: 100%;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      border-radius: 4px;
      transition: all 0.3s;
      
      &:hover {
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }
      
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10px;
        padding: 10px 0;
        border-bottom: 1px solid #ebeef5;
        
        .left-section {
          display: flex;
          align-items: center;
          
          .title-text {
            font-size: 16px;
            font-weight: 600;
            color: #303133;
            margin-right: 10px;
          }
          
          .data-count {
            font-size: 12px;
          }
        }
        
        .header-actions {
          display: flex;
          align-items: center;
          
          .search-input {
            margin-right: 10px;
            
            :deep(.el-input__wrapper) {
              box-shadow: 0 0 0 1px #dcdfe6 inset;
              border-radius: 4px;
              
              &:hover {
                box-shadow: 0 0 0 1px #c0c4cc inset;
              }
              
              &.is-focus {
                box-shadow: 0 0 0 1px #008E65 inset !important;
              }
            }
          }
          
          .search-btn {
            background-color: #008E65;
            border-color: #008E65;
            
            &:hover, &:focus {
              background-color: #00a472;
              border-color: #00a472;
            }
          }
          
          .refresh-btn {
            margin-left: 8px;
          }
        }
      }
      
      .mmse-table {
        flex: 1;
        overflow: auto;
        margin-bottom: 15px;
        
        :deep(.el-table__row) {
          cursor: pointer;
          transition: background-color 0.2s;
          
          &:hover {
            background-color: #f0f9eb;
          }
        }
        
        .patient-id {
          font-weight: 500;
          color: #409eff;
        }
        
        .score-tag {
          font-weight: 500;
          padding: 4px 8px;
        }
        
        .status-tag {
          font-weight: 500;
          padding: 4px 10px;
        }
        
        .view-btn {
          padding: 6px 12px;
          transition: all 0.3s;
          color: #ffffff !important;
          
          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
          }
        }
      }
      
      .pagination {
        margin-top: 20px;
        text-align: right;
        padding-bottom: 10px;
        
        :deep(.el-pagination .el-pager li.is-active) {
          background-color: #008E65;
          border-color: #008E65;
        }
      }
    }
  }
  
  .details-drawer {
    :deep(.el-drawer__header) {
      margin-bottom: 0;
      padding: 16px 20px;
      border-bottom: 1px solid #ebeef5;
    }
    
    .drawer-header {
      .drawer-title {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
      }
    }
    
    .drawer-content {
      padding: 20px;
      
      .patient-info {
        margin-bottom: 30px;
        
        :deep(.el-descriptions__label) {
          font-weight: bold;
          background-color: #fafafa;
        }
        
        :deep(.el-descriptions__content) {
          padding: 12px 15px;
        }
      }
      
      .answer-details-section {
        margin-top: 20px;
        
        .section-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 15px;
          
          .section-title {
            font-size: 16px;
            font-weight: 600;
            color: #303133;
            position: relative;
            padding-left: 10px;
            
            &::before {
              content: '';
              position: absolute;
              left: 0;
              top: 50%;
              transform: translateY(-50%);
              width: 3px;
              height: 16px;
              background-color: #008E65;
              border-radius: 1.5px;
            }
          }
        }
        
        .details-table {
          margin-bottom: 20px;
          
          .patient-answer {
            color: #606266;
            font-weight: 500;
          }
          
          .time-spent {
            color: #409eff;
            font-weight: 500;
          }
          
          .question-text {
            display: inline-block;
            max-width: 200px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            color: #303133;
            font-weight: 500;
          }
        }
      }
    }
    
    .drawer-footer {
      padding: 10px 20px;
      text-align: right;
      border-top: 1px solid #ebeef5;
    }
  }
  
  @media screen and (max-width: 768px) {
    .main-content {
      padding: 10px;
    }
    
    .card-header {
      flex-direction: column;
      align-items: flex-start !important;
      
      .header-actions {
        margin-top: 10px;
        width: 100%;
        
        .search-input {
          flex: 1;
        }
      }
    }
  }
}

.section-actions {
  display: flex;
  gap: 10px;
  
  :deep(.el-button) {
    color: #ffffff;
  }
}

.score-alert {
  margin: 15px 0;
}

.details-table {
  :deep(.el-input-number) {
    width: 90px;
  }
  
  .question-text {
    display: inline-block;
    max-width: 200px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    color: #303133;
    font-weight: 500;
  }
}

.audio-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
}

.audio-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.audio-player {
  width: 140px;
  height: 32px;
  border-radius: 16px;
  background-color: #f5f7fa;
}

.loading-icon {
  position: absolute;
  font-size: 18px;
  color: #008E65;
  margin-left: 4px;
}

.audio-error {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 12px;
}

.audio-link {
  font-size: 12px;
}

.no-answer {
  color: #909399;
  font-style: italic;
}

.transcript-text {
  max-width: 160px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding: 4px 8px;
  background-color: #f0f9eb;
  border-radius: 4px;
  color: #606266;
  font-size: 13px;
  text-align: left;
  position: relative;
}

.loading-text {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  font-size: 13px;
  color: #909399;
}

.error-text {
  color: #f56c6c;
  font-size: 13px;
}

.convert-btn {
  font-size: 12px;
  color:rgb(255, 255, 255) !important;
  
  :deep(.el-icon) {
    color:rgb(253, 254, 255);
  }
  
  &:hover {
    color: #66b1ff !important;
  }
}

.retry-btn {
  padding: 4px;
}

.similarity-actions {
  display: flex;
  justify-content: center;
  margin-top: 5px;
  gap: 8px; /* 添加间距 */
}

.result-btn {
  background-color: #67c23a;
  color: white;
  
  &:hover {
    background-color: #85ce61;
  }
}

.similarity-results {
  padding: 15px;
  max-height: 500px;
  overflow-y: auto;
}

.similarity-results-popover {
  max-width: 500px;
}

.similarity-title {
  font-weight: bold;
  margin-bottom: 8px;
  color: #303133;
  font-size: 13px;
}

.similarity-data {
  margin: 0;
  background-color: #fff;
  padding: 8px;
  border-radius: 4px;
  overflow-x: auto;
  font-family: monospace;
  font-size: 12px;
  white-space: pre-wrap;
  color: #606266;
}

.expect-answer-container {
  padding: 10px;
}

.expect-answer-title {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #303133;
}

.expect-answer-input {
  margin-bottom: 15px;
}

.expect-answer-actions {
  display: flex;
  justify-content: flex-end;
}

.compared-texts {
  margin-top: 15px;
  padding-top: 10px;
  border-top: 1px dashed #dcdfe6;
}

.compared-text-title {
  font-weight: bold;
  margin-bottom: 8px;
  color: #303133;
}

.compared-text-item {
  margin-bottom: 8px;
}

.compared-text-label {
  font-weight: 500;
  color: #606266;
  margin-bottom: 2px;
}

.compared-text-content {
  padding: 5px;
  background-color: #fff;
  border-radius: 3px;
  font-size: 12px;
  word-break: break-all;
}

.image-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.answer-image {
  width: 140px;
  height: 140px;
  border-radius: 4px;
  background-color: #f5f7fa;
}

.image-error {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 12px;
}

.image-error-msg {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 12px;
}

.image-link {
  font-size: 12px;
}
</style> 