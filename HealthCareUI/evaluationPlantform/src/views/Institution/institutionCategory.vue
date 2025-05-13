<template>
  <el-container :style="{ height: containerHeight, display: 'flex', flexDirection: 'column', gap: '10px' }">
    <el-header style="background-color: #fff; padding: 20px; border-radius: 8px; ">
      <div style="display: flex; align-items: center; justify-content: space-between;">
        <el-radio-group v-model="radioValue">
          <el-radio-button 
            v-for="option in radioOptions" 
            :key="option.level" 
            :label="option.level">
            {{ option.levelName }}
          </el-radio-button>
        </el-radio-group>
        <el-button type="primary" @click="openAddDialog">新增</el-button>
      </div>
    </el-header>
    <el-main style="flex: 1; background-color: #fff; padding: 20px; ">
      <div class="data-cards-container" style="display: flex; gap: 10px;height: 100%; width: 100%;">
        <el-card
          v-for="option in radioOptions" 
          :key="option.level"
          :class="{ 'active-card': radioValue === option.level }"
          style="width: 300px; height: 100%; flex-shrink: 0;"
          shadow="hover"
          @click="radioValue = option.level">
          <template #header>{{ option.levelName }}</template>
          <div style="height: 600px;">
            <el-table :data="filteredCategories(option.level)" :border="parentBorder" style="width: 100%">
              <el-table-column type="expand">
                <template #default="props">
                  <div style="padding-left: 20px">
                    <p>创建人: {{ props.row.createUser }}</p>
                    <p>创建时间: {{ props.row.createTime }}</p>
                    <p>别名: {{ props.row.categoryAlias }}</p>
                    <p>更新时间: {{ props.row.updateTime }}</p>
                    <p>备注: {{ props.row.remark }}</p>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="categoryName" label="名称" width="100px"></el-table-column>
              <el-table-column label="操作" width="100px">
                <template #default="scope">
                  <el-button type="text" @click="openDrawer(scope.row)">详情</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <template #footer>
              <el-button type="primary" size="small">新增</el-button>
          </template>
        </el-card>
      </div>
    </el-main>
  </el-container>

  <!-- 抽屉组件 -->
  <el-drawer v-model="drawerVisible" title="详情" direction="rtl" size="30%" style="padding: 20px;">
    <p>名称: {{ drawerData.categoryName }}</p>
    <p>别名: {{ drawerData.categoryAlias }}</p>
    <p>创建时间: {{ drawerData.createTime }}</p>
    <p>更新时间: {{ drawerData.updateTime }}</p>
    <p>创建人: {{ drawerData.createUser }}</p>
    <p>备注: {{ drawerData.remark }}</p>
    <div style="display: flex; gap: 10px; justify-content: flex-end; margin-top: 20px;">
      <el-button type="primary" size="small" @click="openEditDialog">编辑</el-button>
      <el-button type="danger" size="small" @click="deleteCategory">删除</el-button>
    </div>
  </el-drawer>

  <!-- 编辑对话框 -->
  <el-dialog v-model="editDialogVisible" title="编辑机构种类">
    <el-form :model="editFormData" label-width="100px">
      <el-form-item label="名称">
        <el-input v-model="editFormData.categoryName" style="width: 100%;"></el-input>
      </el-form-item>
      <el-form-item label="别名">
        <el-input v-model="editFormData.categoryAlias" style="width: 100%;"></el-input>
      </el-form-item>
      <el-form-item label="创建人">
        <el-input v-model="editFormData.createUser" style="width: 100%;" disabled></el-input>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-input v-model="editFormData.createTime" style="width: 100%;" disabled></el-input>
      </el-form-item>
      <el-form-item label="更新时间">
        <el-input v-model="editFormData.updateTime" style="width: 100%;" disabled></el-input>
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="editFormData.remark" style="width: 100%;"></el-input>
      </el-form-item>
      <!-- 添加其他需要编辑的字段 -->
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="editDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="saveEdit">保存</el-button>
    </div>
  </el-dialog>

  <!-- 新增对话框 -->
  <el-dialog v-model="addDialogVisible" title="新增机构种类">
    <el-form :model="addFormData" label-width="100px">
      <el-form-item label="名称">
        <el-input v-model="addFormData.categoryName" style="width: 100%;"></el-input>
      </el-form-item>
      <el-form-item label="别名">
        <el-input v-model="addFormData.categoryAlias" style="width: 100%;"></el-input>
      </el-form-item>
      <el-form-item label="创建人">
        <el-input v-model="addFormData.createUser" style="width: 100%;"></el-input>
      </el-form-item>
      <el-form-item label="种类等级">
        <el-input v-model="addFormData.level" style="width: 100%;"></el-input>
      </el-form-item>
      <el-form-item label="等级名称">
        <el-input v-model="addFormData.levelName" style="width: 100%;"></el-input>
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="addFormData.remark" style="width: 100%;"></el-input>
      </el-form-item>
      <!-- 添加其他需要编辑的字段 -->
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="addDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="saveAdd">保存</el-button>
    </div>
  </el-dialog>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { institutionCategoryLevelCountService, institutionCategoryAllService, deleteInstitutionCategoryService, updateInstitutionCategoryService, institutionCategoryService } from '@/api/InstitutionCategory'
import { ElContainer, ElHeader, ElMain, ElRadioGroup, ElRadioButton, ElCard, ElButton, ElTable, ElTableColumn, ElDrawer, ElDialog, ElForm, ElFormItem, ElInput } from 'element-plus'

const headerHeight = 80 // LayoutHeader高度(px)
const containerHeight = computed(() => `calc(100vh - ${headerHeight}px)`)

const radioValue = ref(null)
const radioOptions = ref([])
const cardCount = ref(0)
const categories = ref([])

// 抽屉相关状态
const drawerVisible = ref(false)
const drawerData = ref({})

// 编辑对话框相关状态
const editDialogVisible = ref(false)
const editFormData = ref({})

// 新增对话框相关状态
const addDialogVisible = ref(false)
const addFormData = ref({
  categoryName: '',
  categoryAlias: '',
  createUser: '',
  level: '',
  levelName: '',
  remark: ''
})

// 打开抽屉并设置数据
function openDrawer(row) {
  console.log("openDrawer:", row)
  drawerData.value = row
  drawerVisible.value = true
}

// 打开编辑对话框
function openEditDialog() {
  editFormData.value = { ...drawerData.value }
  editDialogVisible.value = true
}

// 打开新增对话框
function openAddDialog() {
  addFormData.value = {
    categoryName: '',
    categoryAlias: '',
    createUser: '',
    level: '',
    levelName: '',
    remark: ''
  }
  addDialogVisible.value = true
}

// 保存编辑
function saveEdit() {
  updateInstitutionCategoryService(editFormData.value.id, editFormData.value).then(() => {
    console.log('更新成功')
    editDialogVisible.value = false
    drawerVisible.value = false
    // 重新获取数据
    institutionCategoryAllService().then(res => {
      categories.value = res.data
    })
  }).catch(err => console.error('更新失败:', err))
}

// 保存新增
function saveAdd() {
  institutionCategoryService(addFormData.value).then(() => {
    console.log('新增成功')
    addDialogVisible.value = false
    // 重新获取数据
    institutionCategoryAllService().then(res => {
      categories.value = res.data
    })
  }).catch(err => console.error('新增失败:', err))
}

// 删除机构种类
function deleteCategory() {
  deleteInstitutionCategoryService(drawerData.value.id).then(() => {
    console.log('删除成功')
    drawerVisible.value = false
    // 重新获取数据
    institutionCategoryAllService().then(res => {
      categories.value = res.data
    })
  }).catch(err => console.error('删除失败:', err))
}

// 新增：根据level过滤符合数据列表，增加日志以调试过滤结果
function filteredCategories(level) {
  const filtered = categories.value.filter(cat => cat.level == level)
  console.log("filteredCategories for level", level, ":", filtered)
  return filtered
}

onMounted(() => {
  institutionCategoryLevelCountService().then(res => {
    console.log("请求级别数量成功:", res.data)
    
    const { count, levelList } = res.data
    radioOptions.value = levelList // 更新单选按钮选项
    cardCount.value = count // 更新卡片数量

    if (levelList.length > 0) {
      radioValue.value = levelList[0].level // 默认选中第一个选项
    }
  }).catch(err => console.error("请求级别数量异常:", err))

  // 新增：获取所有机构种类
  institutionCategoryAllService().then(res => {
    console.log("获取所有机构种类成功:", res.data)
    categories.value = res.data
  }).catch(err => console.error("获取所有机构种类异常:", err))
})
</script>

<style scoped>
.active-card {
  /* 添加选中卡片的 hover 效果 */
  border: 2px solid #008E65;
  transition: border 0.3s, box-shadow 0.3s;
}
.active-card:hover {
  box-shadow: 0 0 10px #008E65;
}
</style>