<script setup lang="js">
import { ref, onMounted, computed, nextTick } from 'vue';
import { getInstitutionPage, getCategoryTree } from '@/api/Institution';
import { ElMessage } from 'element-plus';

import AddInstitution from './component/AddInstitution.vue';
import EditInstitution from './component/EditInstitution.vue';

// 机构信息
const institutionInfo = ref({
  progress: '90%',
  name: '测试医院',
  type: '三级甲等医院',
  address: '北京市朝阳区xxx街道'
});

// 查询表单数据
const queryForm = ref({
  pageNo: 1,
  pageSize: 8,
  institutionName: '',
  institutionCategoryId: null,
  InstitutionLevel: null,
});

// 表格数据
const tableData = ref([]);
const loading = ref(false);
const categoryTreeData = ref([]);

// 分页配置
const pagination = ref({
  currentPage: 1,
  pageSize: 8,
  total: 100
});

// 新增机构对话框显示状态
const dialogVisible = ref(false);
const selectedCategory = ref(null);

// 编辑相关状态
const editDialogVisible = ref(false);
const editId = ref(null);

// 加载表格数据
const loadTableData = async () => {
  loading.value = true;
  try {
    console.log('查询参数:', JSON.stringify(queryForm.value));
    const res = await getInstitutionPage(queryForm.value);
    console.log('接口返回数据:', res);
    if (res.code === 0) {
      tableData.value = res.data.list;
      pagination.value = {
        currentPage: queryForm.value.pageNo,
        pageSize: queryForm.value.pageSize,
        total: res.data.total || 0
      };
      console.log('更新后的分页信息:', pagination.value);
    }
  } catch (error) {
    ElMessage.error('查询失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
};

// 等级文本转换
const getLevelText = (level) => {
  const levelMap = {
    1: '一级',
    2: '二级',
    3: '三级',
    4: '四级',
    5: '五级',
  };
  return levelMap[level] || '未知等级';
};

// 基础方法
const handleCurrentChange = (val) => {
  console.log('分页改变:', val);
  queryForm.value.pageNo = val;
  loadTableData();
};

const handleView = (row) => {
  ElMessage.info(`查看机构: ${row.institutionName}`);
};

const handleEdit = (row) => {
  console.log('编辑行数据:', row);
  // 使用uuid作为编辑ID
  if (row && row.uuid) {
    editId.value = row.uuid;
    console.log('设置编辑ID:', editId.value);
    nextTick(() => {
      editDialogVisible.value = true;
    });
  } else {
    ElMessage.warning('无效的机构ID');
  }
};

const handleEditSuccess = () => {
  editId.value = null;
  editDialogVisible.value = false;
  loadTableData();
};

const resetQueryForm = () => {
  queryForm.value = {
    pageNo: 1,
    pageSize: 8,
    institutionName: '',
    institutionCategoryId: null,
    InstitutionLevel: null,
  };
  loadTableData();
};

const handleSearch = () => {
  queryForm.value.pageNo = 1;
  loadTableData();
};

const handleAddSuccess = () => {
  handleSearch();
};

const openAddDialog = () => {
  dialogVisible.value = true;
};

// 加载机构类别树
const loadCategoryTree = async () => {
  try {
    const res = await getCategoryTree();
    console.log('机构类别:', res);
    if (res.code === 0 && res.data) {
      categoryTreeData.value = res.data;
      console.log('机构类别:', categoryTreeData.value);
    }
  } catch (error) {
    ElMessage.error(res.message ? res.message : '加载机构类别失败');
  }
};

// 获取当前选中的等级显示文本
const currentLevelText = computed(() => {
  if (!queryForm.value.InstitutionLevel) {
    return '';
  }
  return `等级${queryForm.value.InstitutionLevel}`;
});

// 监听机构类别选择变化
const handleCategoryChange = (value) => {
  queryForm.value.InstitutionLevel = null; // 重置机构等级
  if (Array.isArray(value) && value.length > 0) {
    const parentValue = value.length > 1 ? value[value.length - 2] : value[0];
    selectedCategory.value = value[value.length - 1]; // 选中的子类 ID
    queryForm.value.institutionCategoryId = selectedCategory.value; // 只取子类 ID
    queryForm.value.InstitutionLevel = parentValue; // 设置父级类别
  } else {
    selectedCategory.value = null;
    queryForm.value.institutionCategoryId = null; // 清空类别 ID
  }
};

// 初始化加载数据
onMounted(() => {
  loadTableData();
  loadCategoryTree();
});
</script>

<template>
  <div :style="{ flex: 1, display: 'flex', flexDirection: 'column', backgroundColor: '#fff', borderLeft: '1px solid #eee', padding: '20px' }">
    <!-- 下部查询和列表区 -->
    <div :style="{ height: '75%', display: 'flex', flexDirection: 'column' }">
      <div :style="{ marginBottom: '20px' }">
        <el-form :model="queryForm" :inline="true" size="default">
          <el-form-item label="机构名称" :style="{ marginRight: '10px' }">
            <el-input v-model="queryForm.institutionName" placeholder="请输入机构名称" :style="{ width: '150px' }" clearable />
          </el-form-item>
          <el-form-item label="机构类别" :style="{ marginRight: '10px' }">
            <el-cascader
              v-model="queryForm.institutionCategoryId"
              :options="categoryTreeData"
              :props="{
                checkStrictly: true,
                label: 'label',
                value: 'value',
                children: 'children'
              }"
              clearable
              :style="{ width: '150px' }"
              placeholder="请选择类别"
              @change="handleCategoryChange"
            />
          </el-form-item>
          <el-form-item label="机构等级" :style="{ marginRight: '10px' }">
            <el-input
              :model-value="currentLevelText"
              disabled
              placeholder="请先选择机构类别"
              :style="{ width: '150px' }"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetQueryForm">重置</el-button>
          </el-form-item>
          <el-button type="primary" @click="openAddDialog">新增机构</el-button>
        </el-form>
      </div>

      <div :style="{ flex: 1 }">
        <el-table 
          :data="tableData" 
          :loading="loading"
          :style="{ width: '100%', height: 'calc(100% - 50px)', maxHeight: '390px' }" 
          border 
          stripe
        >
          <el-table-column prop="institutionName" label="机构名称" />
          <el-table-column prop="address" label="地址" />
          <el-table-column prop="institutionPhone" label="联系电话" />
          <el-table-column prop="institutionLevel" label="机构等级">
            <template #default="{ row }">
              {{ getLevelText(row.institutionLevel) }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'info'">
                {{ row.status === 1 ? '正常' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button link type="primary" @click="handleView(row)">查看</el-button>
              <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div :style="{ marginTop: '10px', textAlign: 'right' }">
          <el-pagination
            v-model:current-page="queryForm.pageNo"
            v-model:page-size="queryForm.pageSize"
            :total="pagination.total"
            :page-sizes="[5, 8, 10, 20]"
            @current-change="handleCurrentChange"
            @size-change="(size) => { queryForm.pageSize = size; queryForm.pageNo = 1; loadTableData(); }"
            layout="total, sizes, prev, pager, next, jumper"
            background
          />
        </div>
      </div>
    </div>
  </div>

  <!-- 新增机构对话框 -->
  <AddInstitution
    v-model:visible="dialogVisible"
    :category-tree-data="categoryTreeData"
    :selected-category="selectedCategory"
    @submit-success="handleAddSuccess"
    @category-change="handleCategoryChange"
  />

  <!-- 编辑机构对话框 -->
  <EditInstitution
    v-model:visible="editDialogVisible"
    :category-tree-data="categoryTreeData"
    :edit-id="editId"
    @submit-success="handleEditSuccess"
  />
</template>
