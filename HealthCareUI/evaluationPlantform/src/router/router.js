import { createRouter, createWebHistory } from "vue-router";

// 路由懒加载
const Login = () => import("@/views/Login.vue");
const Layout = () => import("@/views/Layout.vue");
const Home = () => import("@/views/Home.vue");

const PatientInfo = () => import("@/views/patient/PatientInfo.vue");
const PatientCase = () => import("@/views/patient/PatientCase.vue");
const PatientData = () => import("@/views/patient/PatientData.vue");
const InstitutionManager = () => import("@/views/Institution/institutionManager.vue");
const InstitutionCategory = () => import("@/views/Institution/institutionCategory.vue");
const UserInfo = () => import("@/views/user/UserInfo.vue"); // 基本资料
const UserAvatar = () => import("@/views/user/UserAvatar.vue"); // 更换头像
const UserManager = () => import("@/views/UserManager/UserManager.vue");
// 添加MMSE组件导入
const MMSEQuestions = () => import("@/views/mmse/MMSEQuestions.vue");
const MMSEReview = () => import("@/views/mmse/MMSEReview.vue");

const routes = [
  {
    path: "/login",
    component: Login,
  },
  {
    path: "/",
    component: Layout,
    redirect: "/home", // 默认重定向到首页
    children: [
      {
        path:"home",
        component: Home,
      },
      // 修改MMSE管理路由
      {
        path: "mmse-management",
        redirect: "/mmse-management/scale",
        children: [
          {
            path: "scale",
            component: MMSEQuestions,
            meta: { title: "MMSE量表" }
          },
          {
            path: "review",
            component: MMSEReview,
            meta: { title: "量表审查" }
          }
        ],
      },
      {
        path: "patient",
        redirect: "/patient/info",
        children: [
          {
            path: "info",
            component: PatientInfo,
          },
          {
            path: "case",
            component: PatientCase,
          },
        ],
      },
      {
        path: "institutionManager",
        redirect: "/institutionManager/institutionManager", // 默认重定向到用户信息页面
        children: [
          {
            path: "institutionManager",
            component: InstitutionManager,
          },
          {
            path: "institutionCategory",
            component: InstitutionCategory,
          },
        ],
      },
      {
        path: "userManager",
        component: UserManager,
      },
      {
        path: "user",
        redirect: "/user/info", // 默认重定向到用户信息页面
        children: [
          {
            path: "info",
            component: UserInfo,
          },
          {
            path: "avatar",
            component: UserAvatar,
          },
        ],
      },

    ],
  },
  // 404 页面处理
  {
    path: "/:pathMatch(.*)*",
    redirect: "/login", // 所有未匹配的路由重定向到登录页面
  },
];

// 创建路由器
const router = createRouter({
  history: createWebHistory(),
  routes,
});


export default router;
