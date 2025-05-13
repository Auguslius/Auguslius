<script setup>
import { ref, watch, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { updateInstitution, getInstitutionDetail } from '@/api/Institution';

const props = defineProps({
  categoryTreeData: Array,
  visible: Boolean,
  editId: [String, Number]
});

const emit = defineEmits(['update:visible', 'submit-success']);

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
  institutionCategoryId: [{ required: true, message: '请选择机构类别', trigger: 'change' }]
};

// 计算属性：显示当前选中类别对应的等级
const currentLevelText = computed(() => {
  if (!institutionForm.value.institutionLevel) return '';
  return `等级${institutionForm.value.institutionLevel}`;
});

// 加载编辑数据
const loadEditData = async () => {
  console.log('开始加载编辑数据, editId:', props.editId);
  if (!props.editId) {
    console.warn('未获取到editId');
    return;
  }

  try {
    const res = await getInstitutionDetail(props.editId);
    console.log('获取到的详情数据:', res);
    
    if (res.code === 0 && res.data) {
      const { uuid, institutionName, institutionPhone, address, institutionCategoryId, institutionLevel } = res.data;
      
      // 设置表单数据
      institutionForm.value = {
        uuid, // 使用uuid代替id
        institutionName,
        institutionPhone,
        address,
        institutionCategoryId,
        institutionLevel
      };

      console.log('设置后的表单数据:', institutionForm.value);
    }
  } catch (error) {
    console.error('加载详情失败:', error);
    ElMessage.error('获取机构详情失败');
  }
};

// 处理分类选择变化
const handleCategoryChange = (value) => {
  institutionForm.value.institutionLevel = null;
  if (Array.isArray(value) && value.length > 0) {
    const parentValue = value.length > 1 ? value[value.length - 2] : value[0];
    institutionForm.value.institutionLevel = parentValue;
  }
};

const handleSubmit = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const formData = { ...institutionForm.value };
        
        if (Array.isArray(formData.institutionCategoryId)) {
          formData.institutionCategoryId = formData.institutionCategoryId[formData.institutionCategoryId.length - 1];
        }
        await updateInstitution(formData);
        ElMessage.success('更新成功');
        emit('update:visible', false);
        emit('submit-success');
      } catch (error) {
        ElMessage.error('更新失败');
      }
    }
  });
};

const handleClose = () => {
  emit('update:visible', false);
};

// 修改watch逻辑
watch(
  () => props.editId,
  (newId) => {
    console.log('编辑ID变化:', newId);
    if (newId && props.visible) {
      loadEditData();
    }
  }
);

watch(
  () => props.visible,
  (newVisible) => {
    console.log('对话框显示状态:', newVisible);
    if (newVisible && props.editId) {
      loadEditData();
    }
  }
);
</script>

<template>
  <el-dialog
    title="编辑机构"
    :modelValue="visible"
    @update:modelValue="$emit('update:visible', $event)"
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
