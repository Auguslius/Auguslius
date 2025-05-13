<script setup>
import { ref, watch, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { addInstitution } from '@/api/Institution';

const props = defineProps({
  categoryTreeData: Array,
  visible: Boolean,
  selectedCategory: Object,
  tableData: Array
});

// 机构信息管理
const institutionInfo = ref({
  progress: '90%',
  name: '测试医院',
  type: '三级甲等医院',
  address: '北京市朝阳区xxx街道'
});

// 监听表格数据变化，更新机构信息
watch(() => props.tableData, (newData) => {
  if (newData && newData.length > 0) {
    const firstItem = newData[0];
    institutionInfo.value = {
      progress: '90%',
      name: firstItem.institutionName,
      type: getLevelText(firstItem.institutionLevel),
      address: firstItem.address
    };
  }
}, { immediate: true });

// 等级文本转换方法
const getLevelText = (level) => {
  const levelMap = {
    1: '一级',
    2: '二级',
    3: '三级'
  };
  return levelMap[level] || '未知等级';
};

const emit = defineEmits(['update:visible', 'submit-success', 'category-change']);

const formRef = ref(null);

const institutionForm = ref({
  institutionName: '',
  institutionPhone: '',
  address: '',
  institutionCategoryId: null,
  institutionLevel: null
});

const rules = {
  institutionName: [{ required: true, message: '请输入机构名称', trigger: 'blur' }],
  institutionPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  address: [{ required: true, message: '请输入地址', trigger: 'blur' }],
  institutionCategoryId: [{ required: true, message: '请选择机构类别', trigger: 'change' }],
  institutionLevel: [{ required: true, message: '请选择机构等级', trigger: 'change' }]
};

// 计算属性：显示当前选中类别对应的等级
const currentLevelText = computed(() => {
  const categoryId = institutionForm.value.institutionLevel;
  return categoryId ? `等级${categoryId}` : '';
});

const handleSubmit = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const formData = { ...institutionForm.value };
        if (Array.isArray(formData.institutionCategoryId)) {
          formData.institutionCategoryId = formData.institutionCategoryId[formData.institutionCategoryId.length - 1];
        }
        
        await addInstitution(formData);
        ElMessage.success('添加成功');
        emit('update:visible', false);
        emit('submit-success');
        resetForm();
      } catch (error) {
        ElMessage.error('添加失败');
      }
    }
  });
};

const handleClose = () => {
  emit('update:visible', false);
  resetForm();
};

const resetForm = () => {
  institutionForm.value = {
    institutionName: '',
    institutionPhone: '',
    address: '',
    institutionCategoryId: null,
    institutionLevel: null
  };
  formRef.value?.resetFields();
};

// 处理分类选择变化
const handleCategoryChange = (value) => {
  if (Array.isArray(value) && value.length > 0) {
    const parentValue = value.length > 1 ? value[value.length - 2] : value[0];
    institutionForm.value.institutionLevel = parentValue;
    emit('category-change', value);
  } else {
    institutionForm.value.institutionLevel = null;
  }
};

const handleDialogChange = (val) => {
  emit('update:visible', val);
};
</script>

<template>
  <el-dialog
    title="新增机构"
    :modelValue="visible"
    @update:modelValue="handleDialogChange"
    width="500px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="institutionForm"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item label="机构名称" prop="institutionName">
        <el-input v-model="institutionForm.institutionName" />
      </el-form-item>
      <el-form-item label="联系电话" prop="institutionPhone">
        <el-input v-model="institutionForm.institutionPhone" />
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input v-model="institutionForm.address" />
      </el-form-item>
      <el-form-item label="机构类别" prop="institutionCategoryId">
        <el-cascader
          v-model="institutionForm.institutionCategoryId"
          :options="categoryTreeData"
          :props="{ 
            checkStrictly: true,
            emitPath: true,
            value: 'value',
            label: 'label'
          }"
          @change="handleCategoryChange"
          placeholder="请选择机构类别"
        />
      </el-form-item>
      <el-form-item label="机构等级" prop="institutionLevel">
        <el-input
          :model-value="currentLevelText"
          disabled
          placeholder="请先选择机构类别"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>
