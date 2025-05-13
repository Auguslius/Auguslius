<template>
    <div class="patient-data-container">
        <el-container>
            <el-header class="page-header">
                <el-page-header>
                    <template #content>
                        <span class="text-large font-600 mr-3">患者数据</span>
                    </template>
                </el-page-header>
            </el-header>

            <el-main class="main-content">
                <el-card class="box-card">
                    <!-- 这里添加数据展示的具体内容 -->
                </el-card>
            </el-main>
        </el-container>
    </div>
</template>

<script setup>
import { ref } from 'vue'
</script>

<style lang="scss" scoped>
.patient-data-container {
    height: 100%;
    width: 100%;

    .page-header {
        background-color: #fff;
        border-bottom: 1px solid #dcdfe6;
        padding: 0 20px;
        display: flex;
        align-items: center;
        height: 50px;
    }

    .main-content {
        padding: 20px;
        height: calc(100% - 50px);
        overflow: hidden;
    }

    .box-card {
        height: 100%;
        box-shadow: none;
        border: 1px solid #dcdfe6;
    }
}
</style>
