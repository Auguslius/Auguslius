<template>
  <div class="mmse-questions-container">
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">MMSE问题管理</h2>
        <div class="header-description">管理认知评估问题库</div>
      </div>
      <el-button type="primary" @click="handleAdd" :icon="Plus">新增问题</el-button>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <div class="search-header">
        <span><el-icon><Search /></el-icon> 条件筛选</span>
        <el-button type="primary" link @click="handleQuery">刷新数据</el-button>
      </div>
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="所属大项">
          <el-select v-model="queryParams.section" placeholder="所属大项" clearable>
            <el-option 
              v-for="item in sectionOptions" 
              :key="item.value" 
              :label="item.label" 
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="题目类型">
          <el-select v-model="queryParams.questionType" placeholder="题目类型" clearable>
            <el-option 
              v-for="item in questionTypeOptions" 
              :key="item.value" 
              :label="item.label" 
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" :icon="Search">查询</el-button>
          <el-button @click="resetQuery" :icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据统计区域  -->
    <div class="stats-row">
      <div class="stats-item-flat">
        <div class="stats-icon stats-total">
          <el-icon><Document /></el-icon>
        </div>
        <div class="stats-content">
          <div class="stats-value">{{ total }}</div>
          <div class="stats-label">问题总数</div>
        </div>
      </div>
      
      <div v-for="item in categoryStats" :key="item.section" class="stats-item-flat">
        <div :class="['stats-icon', `stats-${item.section.toLowerCase()}`]">
          <el-icon v-if="item.section === 'Orientation'"><Location /></el-icon>
          <el-icon v-else-if="item.section === 'Memory'"><Monitor /></el-icon>
          <el-icon v-else-if="item.section === 'Calculation'"><OfficeBuilding /></el-icon>
          <el-icon v-else-if="item.section === 'Recall'"><Refresh /></el-icon>
          <el-icon v-else-if="item.section === 'Language'"><ChatLineRound /></el-icon>
          <el-icon v-else><Document /></el-icon>
        </div>
        <div class="stats-content">
          <div class="stats-value">{{ item.count }}</div>
          <div class="stats-label">{{ getSectionLabel(item.section) }}</div>
        </div>
      </div>
    </div>


    <div class="table-container">
      <el-table 
        v-loading="loading" 
        :data="questionList" 
        border 
        style="width: 100%"
        :header-cell-style="{ backgroundColor: '#f8f8f8', color: '#606266' }"
        @row-click="handleRowClick"
        row-key="id"
        stripe
        highlight-current-row
      >
        <el-table-column prop="section" label="所属大项" min-width="100" width="110">
          <template #default="scope">
            <el-tag 
              :type="getSectionTagType(scope.row.section)" 
              effect="plain"
              size="small"
            >
              {{ getSectionLabel(scope.row.section) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="questionText" label="题目内容" min-width="220" show-overflow-tooltip>
          <template #default="scope">
            <div class="question-text">{{ scope.row.questionText }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="questionType" label="题目类型" min-width="100" width="110">
          <template #default="scope">
            <el-tag size="small" effect="dark">{{ getQuestionTypeLabel(scope.row.questionType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="maxScore" label="分值" width="60" align="center">
          <template #default="scope">
            <el-badge :value="scope.row.maxScore" type="primary" />
          </template>
        </el-table-column>
        <el-table-column prop="position" label="题序号" width="80" align="center"></el-table-column>
        <el-table-column prop="validationMethod" label="验证方法" min-width="90" width="110">
          <template #default="scope">
            <el-tag size="small" type="info">{{ getValidationMethodLabel(scope.row.validationMethod) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="urlCategory" label="比对种类" min-width="90" width="90">
          <template #default="scope">
            <el-tag 
              :type="getUrlCategoryTagType(scope.row.urlCategory)" 
              size="small"
            >
              {{ getUrlCategoryLabel(scope.row.urlCategory) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="160" width="170" align="center" fixed="right">
          <template #default="scope">
            <div class="action-buttons">
              <el-button size="small" type="primary" text @click.stop="handleEdit(scope.row)">
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button size="small" type="danger" text @click.stop="handleDelete(scope.row)">
                <el-icon><Delete /></el-icon>
              </el-button>
              <el-button size="small" type="info" text @click.stop="handleDetail(scope.row)">
                <el-icon><View /></el-icon>
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNo"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[8, 16, 24, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
        ></el-pagination>
      </div>
    </div>

    <!-- 新增/编辑抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      :title="drawerTitle"
      :size="460"
      :destroy-on-close="true"
      :close-on-click-modal="false"
      :show-close="true"
      :before-close="handleDrawerClose"
      direction="rtl"
    >
      <div class="drawer-container">
        <el-scrollbar height="calc(100vh - 120px)">
          <el-form ref="questionFormRef" :model="questionForm" :rules="rules" label-width="90px" class="question-form">
            <el-divider content-position="left">基本信息</el-divider>
            
            <el-form-item label="所属大项" prop="section">
              <el-select v-model="questionForm.section" placeholder="请选择所属大项" style="width: 100%">
                <el-option 
                  v-for="item in sectionOptions" 
                  :key="item.value" 
                  :label="item.label" 
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            
            <el-form-item label="题目类型" prop="questionType">
              <el-select v-model="questionForm.questionType" placeholder="请选择题目类型" style="width: 100%">
                <el-option 
                  v-for="item in questionTypeOptions" 
                  :key="item.value" 
                  :label="item.label" 
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            
            <el-form-item label="题目内容" prop="questionText">
              <el-input v-model="questionForm.questionText" type="textarea" :rows="3" placeholder="请输入题目内容"></el-input>
            </el-form-item>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="最高得分" prop="maxScore">
                  <el-input-number v-model="questionForm.maxScore" :min="0" :precision="0" style="width: 100%"></el-input-number>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="顺序号" prop="position">
                  <el-input-number v-model="questionForm.position" :min="1" :precision="0" style="width: 100%"></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item label="验证方法" prop="validationMethod">
              <el-select v-model="questionForm.validationMethod" placeholder="请选择验证方法" style="width: 100%">
                <el-option 
                  v-for="item in validationMethodOptions" 
                  :key="item.value" 
                  :label="item.label" 
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            
            <el-divider content-position="left">比对资料</el-divider>
            
            <el-form-item label="比对种类" prop="urlCategory">
              <el-select v-model="questionForm.urlCategory" placeholder="请选择比对种类" clearable style="width: 100%">
                <el-option 
                  v-for="item in urlCategoryOptions" 
                  :key="item.value" 
                  :label="item.label" 
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            
            <el-form-item label="比对资料" prop="urlIndex">
              <!-- 针对文本类型的处理 -->
              <template v-if="questionForm.urlCategory === 'TEXT'">
                <el-input 
                  v-model="questionForm.urlIndex" 
                  type="textarea" 
                  :rows="3"
                  maxlength="1000"
                  show-word-limit
                  placeholder="请输入文本内容">
                </el-input>
                <div class="form-item-tip">请输入需要比对的文本内容</div>
              </template>
              
              <!-- 针对图片类型的处理 -->
              <template v-else-if="questionForm.urlCategory === 'IMAGE'">
                <div v-if="questionForm.urlIndex && !tempFile" class="preview-container">
                  <div class="preview-media">
                    <img :src="questionForm.urlIndex" class="preview-image" />
                  </div>
                  <div class="preview-info">
                    <div class="preview-url">{{ getFileNameFromUrl(questionForm.urlIndex) }}</div>
                    <div class="preview-actions">
                      <el-button size="small" type="danger" text @click="clearUrlIndex">
                        <el-icon><Delete /></el-icon> 删除
                      </el-button>
                    </div>
                  </div>
                </div>
                <el-upload
                  v-else
                  ref="fileRef"
                  :auto-upload="false"
                  :limit="1"
                  :on-change="handleFileChange"
                  :on-remove="handleRemove"
                  :file-list="fileList"
                  :accept="'image/*'"
                  class="upload-container"
                  list-type="picture-card"
                >
                  <el-icon><Plus /></el-icon>
                </el-upload>
                <div class="form-item-tip" v-if="tempFile">已选择文件: {{ tempFile.name }}</div>
                <div class="form-item-tip" v-if="!tempFile && !questionForm.urlIndex">请点击上传图片，支持JPG/PNG/GIF/WEBP格式，大小不超过5MB</div>
                <el-input v-if="!tempFile && !questionForm.urlIndex" v-model="questionForm.urlIndex" placeholder="或直接输入图片URL" class="mt-10"></el-input>
              </template>
              
              <!-- 针对视频类型的处理 -->
              <template v-else-if="questionForm.urlCategory === 'VIDEO'">
                <div v-if="questionForm.urlIndex && !tempFile" class="preview-container">
                  <div class="preview-media">
                    <video :src="questionForm.urlIndex" controls class="preview-video"></video>
                  </div>
                  <div class="preview-info">
                    <div class="preview-url">{{ getFileNameFromUrl(questionForm.urlIndex) }}</div>
                    <div class="preview-actions">
                      <el-button size="small" type="danger" text @click="clearUrlIndex">
                        <el-icon><Delete /></el-icon> 删除
                      </el-button>
                    </div>
                  </div>
                </div>
                <el-upload
                  v-else
                  ref="fileRef"
                  :auto-upload="false"
                  :limit="1"
                  :on-change="handleFileChange"
                  :on-remove="handleRemove"
                  :file-list="fileList"
                  :accept="'video/*'"
                  class="upload-container"
                >
                  <el-button type="primary" :icon="VideoCamera" :loading="uploadLoading">选择视频</el-button>
                </el-upload>
                <div class="form-item-tip" v-if="tempFile">已选择文件: {{ tempFile.name }}</div>
                <div class="form-item-tip" v-if="!tempFile && !questionForm.urlIndex">请点击选择上传的视频</div>
                <el-input v-if="!tempFile && !questionForm.urlIndex" v-model="questionForm.urlIndex" placeholder="或直接输入视频URL" class="mt-10"></el-input>
              </template>
              
              <!-- 针对音频类型的处理 -->
              <template v-else-if="questionForm.urlCategory === 'AUDIO'">
                <div v-if="questionForm.urlIndex && !tempFile" class="preview-container">
                  <div class="preview-media">
                    <audio :src="questionForm.urlIndex" controls class="preview-audio"></audio>
                  </div>
                  <div class="preview-info">
                    <div class="preview-url">{{ getFileNameFromUrl(questionForm.urlIndex) }}</div>
                    <div class="preview-actions">
                      <el-button size="small" type="danger" text @click="clearUrlIndex">
                        <el-icon><Delete /></el-icon> 删除
                      </el-button>
                    </div>
                  </div>
                </div>
                <el-upload
                  v-else
                  ref="fileRef"
                  :auto-upload="false"
                  :limit="1"
                  :on-change="handleFileChange"
                  :on-remove="handleRemove"
                  :file-list="fileList"
                  :accept="'audio/*'"
                  class="upload-container"
                >
                  <el-button type="primary" :icon="Headset" :loading="uploadLoading">选择音频</el-button>
                </el-upload>
                <div class="form-item-tip" v-if="tempFile">已选择文件: {{ tempFile.name }}</div>
                <div class="form-item-tip" v-if="!tempFile && !questionForm.urlIndex">请点击选择上传的音频</div>
                <el-input v-if="!tempFile && !questionForm.urlIndex" v-model="questionForm.urlIndex" placeholder="或直接输入音频URL" class="mt-10"></el-input>
              </template>
              
              <!-- 针对未选择比对种类的情况 -->
              <template v-else>
                <el-input v-model="questionForm.urlIndex" placeholder="请先选择比对种类"></el-input>
              </template>
            </el-form-item>
            
            <el-divider content-position="left">答案信息</el-divider>
            
            <el-form-item label="期望答案" prop="expectedAnswer">
              <el-input v-model="questionForm.expectedAnswer" placeholder="请输入期望的答案"></el-input>
            </el-form-item>
          </el-form>
        </el-scrollbar>
        
        <div class="drawer-footer">
          <el-button @click="handleDrawerClose" :icon="Close">取 消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading" :icon="Check">确 定</el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 查看详情抽屉 -->
    <el-drawer
      v-model="detailDrawerVisible"
      title="问题详情"
      :size="460"
      :destroy-on-close="true"
      direction="rtl"
    >
      <el-scrollbar height="calc(100vh - 120px)">
        <el-descriptions 
          :column="1" 
          size="large"
          class="detail-descriptions"
          border
        >
          <el-descriptions-item label="问题ID">
            <el-tag type="info">{{ detailForm.id }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="所属大项">
            <el-tag 
              :type="getSectionTagType(detailForm.section)" 
              effect="plain"
            >
              {{ getSectionLabel(detailForm.section) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="题目内容">
            <div class="detail-text">{{ detailForm.questionText }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="题目类型">
            <el-tag type="success">{{ getQuestionTypeLabel(detailForm.questionType) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="最高得分">
            <el-badge :value="detailForm.maxScore" type="primary" />
          </el-descriptions-item>
          <el-descriptions-item label="顺序号">{{ detailForm.position }}</el-descriptions-item>
          <el-descriptions-item label="验证方法">
            <el-tag type="info">{{ getValidationMethodLabel(detailForm.validationMethod) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item v-if="detailForm.urlCategory" label="比对种类">
            <el-tag :type="getUrlCategoryTagType(detailForm.urlCategory)">
              {{ getUrlCategoryLabel(detailForm.urlCategory) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item v-if="detailForm.urlIndex" label="比对资料">
            <template v-if="detailForm.urlCategory === 'IMAGE'">
              <div class="detail-media">
                <img :src="detailForm.urlIndex" class="detail-image" />
                <div class="detail-url">{{ detailForm.urlIndex }}</div>
              </div>
            </template>
            <template v-else-if="detailForm.urlCategory === 'VIDEO'">
              <div class="detail-media">
                <video :src="detailForm.urlIndex" controls class="detail-video"></video>
                <div class="detail-url">{{ detailForm.urlIndex }}</div>
              </div>
            </template>
            <template v-else-if="detailForm.urlCategory === 'AUDIO'">
              <div class="detail-media">
                <audio :src="detailForm.urlIndex" controls class="detail-audio"></audio>
                <div class="detail-url">{{ detailForm.urlIndex }}</div>
              </div>
            </template>
            <template v-else>
              <div class="detail-text">{{ detailForm.urlIndex }}</div>
            </template>
          </el-descriptions-item>
          <el-descriptions-item v-if="detailForm.expectedAnswer" label="期望答案">
            <div class="detail-text">{{ detailForm.expectedAnswer }}</div>
          </el-descriptions-item>
          <el-descriptions-item v-if="detailForm.createTime" label="创建时间">
            <el-tag type="info" effect="plain">{{ detailForm.createTime }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item v-if="detailForm.updateTime" label="更新时间">
            <el-tag type="info" effect="plain">{{ detailForm.updateTime }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </el-scrollbar>
      
      <template #footer>
        <div class="drawer-footer">
          <el-button @click="detailDrawerVisible = false" :icon="Close">关 闭</el-button>
          <el-button type="primary" @click="handleEditFromDetail" :icon="Edit">编 辑</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, watch, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { 
  Search, Refresh, Edit, Delete, View, Close, Check, Plus, VideoCamera, Headset,
  Document, Location, Monitor, OfficeBuilding, ChatLineRound
} from '@element-plus/icons-vue';
import { addMMSEQuestion, updateMMSEQuestion, deleteMMSEQuestion, getMMSEQuestionById, getMMSEQuestionsPage, getMMSEQuestionCategory } from '@/api/mmse';
import { uploadMedia } from '@/api/media';

// 所属大项选项
const sectionOptions = [
  { label: '定向力', value: 'Orientation' },
  { label: '记忆力', value: 'Memory' },
  { label: '计算力', value: 'Calculation' },
  { label: '回忆能力', value: 'Recall' },
  { label: '语言能力', value: 'Language' }
];

// 问题类型选项
const questionTypeOptions = [
  { label: '时间定向', value: 'TIME' },
  { label: '地点定向', value: 'LOCATION' },
  { label: '即刻记忆', value: 'IMMEDIATE_MEMORY' },
  { label: '连续计算', value: 'SERIAL_CALCULATION' },
  { label: '延迟记忆', value: 'DELAYED_MEMORY' },
  { label: '视觉命名', value: 'VISUAL_NAMING' },
  { label: '语句重复', value: 'REPETITION' },
  { label: '命令执行', value: 'COMMAND_EXECUTION' },
  { label: '书写能力', value: 'WRITING' },
  { label: '图形复制', value: 'FIGURE_COPYING' }
];

// 比对种类选项（原URL种类）
const urlCategoryOptions = [
  { label: '图片', value: 'IMAGE' },
  { label: '视频', value: 'VIDEO' },
  { label: '音频', value: 'AUDIO' },
  { label: '文本', value: 'TEXT' }
];

// 验证方法选项
const validationMethodOptions = [
  { label: '代码验证', value: 'BY_CODE' },
  { label: '数据验证', value: 'BY_DATA' }
];

// 数据加载状态
const loading = ref(false);
// 提交加载状态
const submitLoading = ref(false);
// 问题列表数据
const questionList = ref([]);
// 总数据条数
const total = ref(0);
// 分类统计数据
const categoryStats = ref([]);
// 查询参数
const queryParams = reactive({
  section: '',
  questionType: '',
  pageNo: 1,
  pageSize: 8,
  sortField: 'position',  // 添加排序字段
  sortOrder: 'desc'       // 添加排序方向
});

// 抽屉状态
const drawerVisible = ref(false);
const drawerTitle = ref('');
const detailDrawerVisible = ref(false);

// 表单ref
const questionFormRef = ref(null);

// 表单对象
const questionForm = reactive({
  id: undefined,
  section: '',
  questionText: '',
  questionType: '',
  maxScore: 1,
  position: 1,
  validationMethod: '',
  urlCategory: '',
  urlIndex: '',
  expectedAnswer: ''
});

// 详情表单
const detailForm = reactive({});

// 文件上传相关
const fileRef = ref(null);
const fileList = ref([]);
const uploadLoading = ref(false);
// 临时保存选择的文件，但不立即上传
const tempFile = ref(null);

// 上传文件类型限制
const fileTypeMap = {
  'IMAGE': ['image/jpeg', 'image/png', 'image/gif', 'image/webp'],
  'VIDEO': ['video/mp4', 'video/webm', 'video/avi'],
  'AUDIO': ['audio/mp3', 'audio/wav', 'audio/ogg', 'audio/mpeg'],
  'TEXT': ['text/plain', 'application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document']
};

// 文件类型映射到后端期望的类型
const contentTypeToFileType = {
  'image': 'IMAGE',
  'audio': 'AUDIO',
  'video': 'VIDEO'
};

// 文件类型错误提示信息
const fileTypeErrorMessage = {
  'IMAGE': '请上传jpg、png、gif、webp格式的图片',
  'VIDEO': '请上传mp4、webm、avi格式的视频',
  'AUDIO': '请上传mp3、wav、ogg、mpeg格式的音频',
  'TEXT': '请上传txt、pdf、doc、docx格式的文档'
};

// 文件大小限制（单位：MB）
const fileSizeLimit = {
  'IMAGE': 5,
  'VIDEO': 50,
  'AUDIO': 20,
  'TEXT': 10
};

// 表单校验规则
const rules = {
  section: [{ required: true, message: '请选择所属大项', trigger: 'change' }],
  questionText: [
    { required: true, message: '请输入题目内容', trigger: 'blur' },
    { max: 100, message: '题目内容不能超过100个字符', trigger: 'blur' }
  ],
  questionType: [{ required: true, message: '请选择题目类型', trigger: 'change' }],
  maxScore: [{ required: true, message: '请输入最高得分', trigger: 'blur' }],
  position: [{ required: true, message: '请输入顺序号', trigger: 'blur' }],
  validationMethod: [{ required: true, message: '请选择验证方法', trigger: 'change' }]
};

// 监听比对种类变化，重置相关字段
watch(() => questionForm.urlCategory, (newValue, oldValue) => {
  if (newValue !== oldValue) {
    // 重置文件列表
    fileList.value = [];
    // 重置临时文件
    tempFile.value = null;
    
    // 如果切换类别且有URL，则清空URL
    // 但如果是编辑模式，保留原URL直到用户选择新文件
    if (!questionForm.id) {
      questionForm.urlIndex = '';
    }
  }
});

// 文件上传前验证
const beforeUpload = (file) => {
  const selectedType = questionForm.urlCategory;
  if (!selectedType) {
    ElMessage.error('请先选择比对种类');
    return false;
  }
  
  // 验证文件类型
  const isValidType = fileTypeMap[selectedType].includes(file.type);
  if (!isValidType) {
    ElMessage.error(fileTypeErrorMessage[selectedType]);
    return false;
  }
  
  // 验证文件大小
  const isLessThanLimit = file.size / 1024 / 1024 < fileSizeLimit[selectedType];
  if (!isLessThanLimit) {
    ElMessage.error(`文件大小不能超过${fileSizeLimit[selectedType]}MB!`);
    return false;
  }
  
  return true;
};

// 处理文件选择（不立即上传）
const handleFileChange = (file) => {
  console.log('选择的文件信息:', {
    fileName: file.name,
    fileRawName: file.raw ? file.raw.name : '未获取原始文件名',
    fileSize: file.size,
    fileType: file.raw ? file.raw.type : '未获取原始文件类型'
  });
  
  if (!beforeUpload(file.raw)) {
    fileList.value = [];
    tempFile.value = null;
    return;
  }
  
  // 保存临时文件以供稍后上传
  tempFile.value = file.raw;
  console.log('已保存临时文件:', tempFile.value.name);
  // 清空手动输入的URL
  questionForm.urlIndex = '';
};

// 移除文件
const handleRemove = () => {
  fileList.value = [];
  tempFile.value = null;
  questionForm.urlIndex = '';
};

// 实际执行文件上传
const uploadFile = async () => {
  if (!tempFile.value) {
    // 没有文件需要上传，直接返回true继续后续流程
    console.log('没有需要上传的文件');
    return true;
  }
  
  try {
    uploadLoading.value = true;
    
    // 检查文件类型是否符合要求
    const file = tempFile.value;
    const contentType = file.type;
    const mainType = contentType.split('/')[0]; // 获取主类型：image, audio, video
    
    console.log('准备上传文件详情:', {
      fileName: file.name,
      fileSize: file.size,
      contentType: contentType,
      mainType: mainType,
      lastModified: new Date(file.lastModified).toLocaleString()
    });
    
    // 通过检查content-type的前缀判断文件类型
    let isValidType = false;
    let fileType = null;
    
    if (questionForm.urlCategory === 'IMAGE' && contentType.startsWith('image/')) {
      isValidType = true;
      fileType = 'IMAGE';
    } else if (questionForm.urlCategory === 'VIDEO' && contentType.startsWith('video/')) {
      isValidType = true;
      fileType = 'VIDEO';
    } else if (questionForm.urlCategory === 'AUDIO' && contentType.startsWith('audio/')) {
      isValidType = true;
      fileType = 'AUDIO';
    } else if (questionForm.urlCategory === 'TEXT') {
      isValidType = fileTypeMap['TEXT'].includes(contentType);
      fileType = 'TEXT';
    }
    
    if (!isValidType) {
      console.error('文件类型不匹配:', {
        expectedCategory: questionForm.urlCategory,
        actualContentType: contentType
      });
      ElMessage.error(fileTypeErrorMessage[questionForm.urlCategory] || '文件类型不符合要求');
      return false;
    }
    
    console.log(`准备上传文件，类型: ${fileType}, 内容类型: ${contentType}`);
    
    const formData = new FormData();
    formData.append('file', file);
    
    // 打印FormData中的文件信息
    console.log('FormData中的文件信息:', {
      hasFile: formData.has('file'),
      fileName: file.name,
      fileType: file.type,
      fileSize: `${(file.size / 1024).toFixed(2)} KB`
    });
    
    console.log(`正在上传文件，文件名: ${file.name}, 类型: ${contentType}, 大小: ${file.size}字节`);
    
    const response = await uploadMedia(formData);
    console.log('文件上传响应:', response);
    
    // 简化的URL提取逻辑
    if (response && response.code === 0 && response.data) {
      if (response.data.url) {
        // 从响应中直接获取URL
        questionForm.urlIndex = response.data.url;
        console.log('成功获取文件URL:', questionForm.urlIndex);
        return true;
      }
    }
    
    console.error('无法获取文件URL:', response);
    ElMessage.error('文件上传失败或无法获取URL');
    return false;
  } catch (error) {
    console.error('文件上传失败', error);
    ElMessage.error('文件上传失败: ' + (error.message || '未知错误'));
    return false;
  } finally {
    uploadLoading.value = false;
  }
};

// 生命周期钩子
onMounted(() => {
  queryParams.pageSize = 8; // 默认设置为8条/页
  getList();
  getCategoryStats(); // 获取分类统计数据
});

// 获取问题列表
const getList = () => {
  loading.value = true;
  getMMSEQuestionsPage(queryParams)
    .then(response => {
      questionList.value = response.data.list;
      total.value = response.data.total;
    })
    .catch(error => {
      console.error('获取问题列表失败', error);
      ElMessage.error('获取问题列表失败');
    })
    .finally(() => {
      loading.value = false;
    });
};

// 获取分类统计数据
const getCategoryStats = () => {
  getMMSEQuestionCategory()
    .then(response => {
      if (response && response.data) {
        categoryStats.value = response.data;
        console.log('获取分类统计成功:', categoryStats.value);
      }
    })
    .catch(error => {
      console.error('获取分类统计数据失败', error);
    });
};

// 查询按钮点击事件
const handleQuery = () => {
  queryParams.pageNo = 1;
  getList();
  getCategoryStats(); // 刷新统计数据
};

// 重置查询条件
const resetQuery = () => {
  queryParams.section = '';
  queryParams.questionType = '';
  queryParams.pageNo = 1;
  // 保持排序设置不变
  getList();
  getCategoryStats(); // 刷新统计数据
};

// 处理页码变化
const handleCurrentChange = (val) => {
  queryParams.pageNo = val;
  getList();
};

// 处理每页数量变化
const handleSizeChange = (val) => {
  queryParams.pageSize = val;
  queryParams.pageNo = 1;
  getList();
};

// 添加按钮点击事件
const handleAdd = () => {
  resetForm();
  drawerTitle.value = '新增MMSE问题';
  drawerVisible.value = true;
};

// 编辑按钮点击事件
const handleEdit = (row) => {
  resetForm();
  drawerTitle.value = '编辑MMSE问题';
  const id = row.id;
  
  getMMSEQuestionById(id)
    .then(response => {
      Object.assign(questionForm, response.data);
      drawerVisible.value = true;
    })
    .catch(error => {
      console.error('获取问题详情失败', error);
      ElMessage.error('获取问题详情失败');
    });
};

// 从详情抽屉编辑
const handleEditFromDetail = () => {
  resetForm();
  Object.assign(questionForm, detailForm);
  detailDrawerVisible.value = false;
  drawerTitle.value = '编辑MMSE问题';
  drawerVisible.value = true;
};

// 查看详情按钮点击事件
const handleDetail = (row) => {
  const id = row.id;
  
  getMMSEQuestionById(id)
    .then(response => {
      Object.assign(detailForm, response.data);
      detailDrawerVisible.value = true;
    })
    .catch(error => {
      console.error('获取问题详情失败', error);
      ElMessage.error('获取问题详情失败');
    });
};

// 删除按钮点击事件
const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除该MMSE问题吗？`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      deleteMMSEQuestion(row.id)
        .then(response => {
          ElMessage.success('删除成功');
          getList();
          getCategoryStats(); // 刷新统计数据
        })
        .catch(error => {
          console.error('删除失败', error);
          ElMessage.error('删除失败');
        });
    })
    .catch(() => {
      // 用户取消删除操作
    });
};

// 重置表单
const resetForm = () => {
  if (questionFormRef.value) {
    questionFormRef.value.resetFields();
  }
  
  questionForm.id = undefined;
  questionForm.section = '';
  questionForm.questionText = '';
  questionForm.questionType = '';
  questionForm.maxScore = 1;
  questionForm.position = 1;
  questionForm.validationMethod = '';
  questionForm.urlCategory = '';
  questionForm.urlIndex = '';
  questionForm.expectedAnswer = '';
  
  // 重置文件相关
  fileList.value = [];
  tempFile.value = null;
};

// 关闭抽屉
const handleDrawerClose = () => {
  drawerVisible.value = false;
  resetForm();
};

// 提交表单
const submitForm = async () => {
  if (!questionFormRef.value) return;
  
  questionFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true;
      
      try {
        // 1. 如果有临时文件，先上传文件
        let fileUploadSuccess = true;
        if (tempFile.value) {
          fileUploadSuccess = await uploadFile();
          if (!fileUploadSuccess) {
            // 如果是更新操作，且文件上传失败，可以使用原来的URL继续更新其他信息
            if (questionForm.id) {
              console.log('文件上传失败，但这是更新操作，将保留原有URL继续更新其他字段');
              // 获取原始URL，避免上传失败导致URL被清空
              if (!questionForm.urlIndex) {
                const originalQuestion = await getMMSEQuestionById(questionForm.id);
                if (originalQuestion && originalQuestion.data && originalQuestion.data.urlIndex) {
                  questionForm.urlIndex = originalQuestion.data.urlIndex;
                  console.log('已恢复原有URL:', questionForm.urlIndex);
                  fileUploadSuccess = true; // 允许继续更新
                }
              }
            } else {
              // 如果是新增，文件上传失败且未手动输入URL，则无法继续
              if (!questionForm.urlIndex) {
                submitLoading.value = false;
                ElMessageBox.confirm(
                  '文件上传失败，是否继续提交表单？（将不包含媒体文件）',
                  '上传失败',
                  {
                    confirmButtonText: '继续提交',
                    cancelButtonText: '取消',
                    type: 'warning'
                  }
                ).then(() => {
                  // 用户选择继续，再次调用提交（但不再尝试上传文件）
                  tempFile.value = null; // 清空临时文件，避免再次尝试上传
                  submitForm();
                }).catch(() => {
                  // 用户选择取消
                  console.log('用户取消了提交');
                });
                return;
              }
            }
          }
        }
        
        // 如果文件上传失败且无法继续，则中止表单提交
        if (!fileUploadSuccess && !questionForm.urlIndex) {
          submitLoading.value = false;
          return;
        }
        
        // 2. 提交表单数据
        const formData = { ...questionForm };
        
        // 确保urlIndex是字符串类型
        if (formData.urlIndex && typeof formData.urlIndex === 'object') {
          formData.urlIndex = String(formData.urlIndex);
        }
        
        if (formData.id) {
          // 更新
          try {
            const response = await updateMMSEQuestion(formData);
            ElMessage.success('更新成功');
            drawerVisible.value = false;
            getList();
            getCategoryStats(); // 刷新统计数据
          } catch (updateError) {
            console.error('更新失败', updateError);
            // 如果是因为URL问题导致的更新失败，提供特别提示
            if (updateError.message && updateError.message.includes('urlIndex')) {
              ElMessage.error('更新失败: URL字段格式不正确，请检查文件URL格式');
            } else {
              ElMessage.error('更新失败: ' + (updateError.message || '未知错误'));
            }
          }
        } else {
          // 新增
          try {
            const response = await addMMSEQuestion(formData);
            ElMessage.success('新增成功');
            drawerVisible.value = false;
            getList();
            getCategoryStats(); // 刷新统计数据
          } catch (addError) {
            console.error('新增失败', addError);
            // 如果是因为URL问题导致的新增失败，提供特别提示
            if (addError.message && addError.message.includes('urlIndex')) {
              ElMessage.error('新增失败: URL字段格式不正确，请检查文件URL格式');
            } else {
              ElMessage.error('新增失败: ' + (addError.message || '未知错误'));
            }
          }
        }
      } catch (error) {
        console.error('提交失败', error);
        ElMessage.error('提交失败: ' + (error.message || '未知错误'));
      } finally {
        submitLoading.value = false;
      }
    } else {
      return false;
    }
  });
};

// 获取所属大项显示标签
const getSectionLabel = (section) => {
  const option = sectionOptions.find(item => item.value === section);
  return option ? option.label : section;
};

// 获取所属大项标签类型
const getSectionTagType = (section) => {
  const typeMap = {
    'Orientation': 'success',
    'Memory': 'warning',
    'Calculation': 'danger',
    'Recall': 'info',
    'Language': 'primary'
  };
  return typeMap[section] || 'info';
};

// 获取问题类型显示标签
const getQuestionTypeLabel = (type) => {
  const option = questionTypeOptions.find(item => item.value === type);
  return option ? option.label : type;
};

// 获取验证方法显示标签
const getValidationMethodLabel = (method) => {
  const option = validationMethodOptions.find(item => item.value === method);
  return option ? option.label : method;
};

// 获取URL类别显示标签
const getUrlCategoryLabel = (category) => {
  const option = urlCategoryOptions.find(item => item.value === category);
  return option ? option.label : category;
};

// 获取URL类别标签类型
const getUrlCategoryTagType = (category) => {
  const typeMap = {
    'IMAGE': 'success',
    'VIDEO': 'warning',
    'AUDIO': 'info',
    'TEXT': 'primary'
  };
  return typeMap[category] || 'info';
};

// 处理表格行点击
const handleRowClick = (row) => {
  handleDetail(row);
};

// 从URL中提取文件名
const getFileNameFromUrl = (url) => {
  if (!url) return '';
  try {
    // 尝试从URL中提取文件名
    const urlParts = url.split('/');
    return urlParts[urlParts.length - 1] || url;
  } catch (e) {
    return url;
  }
};

// 清除URL索引
const clearUrlIndex = () => {
  questionForm.urlIndex = '';
};

// 编辑时加载现有文件
const loadExistingFile = () => {
  if (questionForm.urlIndex && questionForm.urlCategory) {
    // 如果有URL但没有临时文件，说明是已经上传的文件
    console.log(`加载已有${questionForm.urlCategory}文件: ${questionForm.urlIndex}`);
  }
};

// 监听编辑数据变化，处理已有文件
watch(() => questionForm.id, (newValue) => {
  if (newValue) {
    // 如果是编辑模式
    nextTick(() => {
      loadExistingFile();
    });
  }
});
</script>

<style scoped>
.mmse-questions-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-left {
  display: flex;
  flex-direction: column;
}

.page-title {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.header-description {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}

/* 扁平化搜索区域 */
.search-area {
  margin-bottom: 20px;
  background-color: #f8f8f8;
  padding: 16px;
  border-radius: 2px;
  border: 1px solid #ebeef5;
}

.search-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

/* 扁平化统计区域 */
.stats-row {
  display: flex;
  margin-bottom: 20px;
  gap: 12px;
  flex-wrap: wrap;
}

.stats-item-flat {
  flex: 1;
  min-width: 180px;
  max-width: calc(16.66% - 10px);
  display: flex;
  align-items: center;
  background-color: #f8f8f8;
  padding: 12px;
  border-radius: 2px;
  border: 1px solid #ebeef5;
}

.stats-icon {
  width: 48px;
  height: 48px;
  border-radius: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.stats-icon .el-icon {
  font-size: 24px;
  color: white;
}

.stats-total {
  background-color: #409EFF;
}

.stats-orientation {
  background-color: #67C23A;
}

.stats-memory {
  background-color: #E6A23C;
}

.stats-calculation {
  background-color: #F56C6C;
}

.stats-recall {
  background-color: #909399;
}

.stats-language {
  background-color: #9370DB;
}

.stats-content {
  display: flex;
  flex-direction: column;
}

.stats-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.stats-label {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}

/* 扁平化表格区域 */
.table-container {
  background-color: #ffffff;
  border: 1px solid #ebeef5;
  border-radius: 2px;
  padding: 16px;
  margin-bottom: 20px;
}

.question-text {
  line-height: 1.5;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

/* 解决表单溢出问题 */
.question-form {
  padding: 0 16px 16px 0;
}

.drawer-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.drawer-footer {
  margin-top: auto;
  padding: 16px;
  text-align: right;
  border-top: 1px solid #ebeef5;
}

.el-descriptions {
  width: 100%;
}

.detail-descriptions {
  padding: 16px;
}

.detail-text {
  white-space: pre-wrap;
  line-height: 1.5;
}

.detail-media {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.detail-image {
  max-width: 100%;
  max-height: 200px;
  object-fit: contain;
  border-radius: 2px;
  border: 1px solid #e0e0e0;
}

.detail-video, .detail-audio {
  width: 100%;
  max-width: 100%;
  border-radius: 2px;
  border: 1px solid #e0e0e0;
}

.detail-url {
  font-size: 12px;
  color: #909399;
  word-break: break-all;
}

.upload-container {
  width: 100%;
}

.form-item-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.el-upload__tip {
  line-height: 1.4;
}

.mt-10 {
  margin-top: 10px;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 8px;
}

.action-buttons .el-button {
  margin: 0;
  padding: 4px;
}

.action-buttons .el-icon {
  font-size: 16px;
}

/* 预览容器样式 */
.preview-container {
  margin-bottom: 16px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}

.preview-media {
  width: 100%;
  background-color: #f8f8f8;
  display: flex;
  justify-content: center;
  padding: 8px;
}

.preview-image {
  max-width: 100%;
  max-height: 160px;
  object-fit: contain;
}

.preview-video, .preview-audio {
  width: 100%;
  max-height: 160px;
}

.preview-info {
  padding: 8px 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f8f8f8;
  border-top: 1px solid #ebeef5;
}

.preview-url {
  font-size: 12px;
  color: #606266;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 70%;
}

.preview-actions {
  display: flex;
  gap: 8px;
}

/* 提升el-drawer的滚动性能 */
:deep(.el-drawer__body) {
  overflow: hidden;
  padding: 0;
}

.drawer-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.question-form {
  padding: 16px 24px 32px 16px;
}
</style>
