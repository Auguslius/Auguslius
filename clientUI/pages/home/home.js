const { homeApi } = require('../../api/index');
const router = require('../../utils/router');

Page({
  data: {
    isLogin: false,
    userInfo: null,
    banners: [
      { title: '阿尔茨海默症防治知识', image: '../../static/images/elderly1.jpg' },
      { title: '专业医生一对一咨询', image: '../../static/images/elderly2.jpg' },
      { title: '居家照护技能培训', image: '../../static/images/elderly3.jpg' }
    ],
    services: [
      { name: '医院信息', icon: 'shop-o' },
      { name: '绑定医生', icon: 'friends-o' },
      { name: '健康评估', icon: 'chart-trending-o' },
      { name: '我的医生', icon: 'manager' },
      { name: '健康档案', icon: 'records' },
      { name: '量表评估', icon: 'chart-trending-o' },
      { name: '个人信息', icon: 'contact' },
      { name: '医保服务', icon: 'shield-o' }
    ],
    doctors: [],
    articles: [
      {
        id: 1,
        title: '阿尔茨海默症的早期症状及预防方法',
        views: 1205,
        image: '../../static/images/elderly1.jpg'
      },
      {
        id: 2,
        title: '老年痴呆患者家庭护理指南',
        views: 986,
        image: '../../static/images/elderly2.jpg'
      },
      {
        id: 3,
        title: '认知障碍的最新治疗进展',
        views: 752,
        image: '../../static/images/elderly3.jpg'
      }
    ],
    loading: false
  },

  onLoad() {
    // 获取医生数据
    this.getDoctors();
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    // 每次显示页面时检查登录状态
    this.checkLoginStatus();
    console.log('首页显示 - 检查登录状态:', getApp().globalData.isLogin ? '已登录' : '未登录');
  },

  // 检查登录状态
  checkLoginStatus() {
    const app = getApp();
    
    // 从全局数据获取最新的登录状态
    const isLogin = app.globalData.isLogin;
    const userInfo = app.globalData.userInfo || {};
    
    // 检查本地存储中的token (双重验证)
    const token = wx.getStorageSync('token');
    const loginStatus = isLogin && token;
    
    // 打印调试信息
    console.log('首页 - 更新登录状态:', 
      loginStatus ? '已登录' : '未登录', 
      '全局状态:', isLogin ? '已登录' : '未登录',
      '本地token:', token ? '存在' : '不存在'
    );
    
    // 更新页面数据
    this.setData({
      isLogin: loginStatus,
      userInfo: loginStatus ? userInfo : null
    });
  },

  // 获取医生数据
  getDoctors() {
    this.setData({ loading: true });
    
    // 构建查询参数
    const params = {
      pageNo: 1,
      pageSize: 4, // 只获取4个医生展示
      // 可根据需要添加其他查询参数
    };

    // 调用API获取医生列表
    homeApi.getDoctors(params).then(res => {
      // 处理返回结果，同时支持code为0和200的情况
      if (res && (res.code === 0 || res.code === 200) && res.data) {
        // 获取医生列表，适配两种可能的数据结构
        const records = res.data.records || res.data.list || [];
        
        // 格式化医生数据
        const formattedDoctors = records.map(doctor => ({
          id: doctor.id,
          name: doctor.nickname || '未知医生',
          number: doctor.number || '无编号',
          institution: doctor.institution || '暂无机构',
          position: doctor.position || '暂无职位',
          avatar: doctor.userPic || '../../static/images/default-avatar.png'
        }));

        this.setData({
          doctors: formattedDoctors,
          loading: false
        });
      } else {
        this.setData({ loading: false });
        console.error('获取医生数据格式不正确:', res);
      }
    }).catch(err => {
      this.setData({ loading: false });
      console.error('获取医生数据失败:', err);
    });
  },

  // 跳转到登录页
  goToLogin() {
    router.navigateTo(router.pages.login);
  },

  // 服务点击
  serviceClick(e) {
    const index = e.currentTarget.dataset.index;
    const service = this.data.services[index];
    const app = getApp();
    
    // 根据不同服务跳转到不同页面
    switch(service.name) {
      case '绑定医生':
        // 导航到绑定医生页面，使用全局变量传递参数
        app.globalData.appointmentTab = 'doctor';
        router.switchTab(router.pages.appointment);
        break;
      case '医院信息':
        // 导航到医院信息页面，使用全局变量传递参数
        app.globalData.appointmentTab = 'hospital';
        router.switchTab(router.pages.appointment);
        break;
      case '我的医生':
        // 导航到我的医生页面前设置医生工号
        // 这里使用一个默认的医生工号，实际应该根据需求确定
        const doctorNumber = 110110; // 默认医生工号
        app.globalData.doctorNumber = doctorNumber;
        
        // 导航到我的医生页面
        router.navigateTo('/pages/profile/my-doctors/my-doctors');
        break;
      case '个人信息':
        // 导航到个人资料详情页面
        const userInfo = app.globalData.userInfo || {};
        const isAuthenticated = userInfo.isAuthenticated ? 'true' : 'false';
        
        // 导航到用户信息页面，传递认证状态
        router.navigateTo('/pages/profile/userInfo/userInfo', { 
          isAuthenticated: isAuthenticated
        });
        break;
      case '健康档案':
        // 导航到健康档案页面
        router.navigateTo('/pages/health-record/health-record');
        break;
      case '健康评估':
        router.navigateTo('/pages/assessment/assessment');
        break;
      case '量表评估':
        router.navigateTo('/pages/mmse-assessment/mmse-assessment');
        break;
      case '医保服务':
        // 使用微信内置浏览器打开国家医保服务平台
        wx.showModal({
          title: '打开医保服务平台',
          content: '即将打开国家医保服务平台网站',
          success (res) {
            if (res.confirm) {
              wx.navigateTo({
                url: '/pages/webview/webview?url=' + encodeURIComponent('https://fuwu.nhsa.gov.cn/nationalHallSt/')
              });
            }
          }
        });
        break;
      default:
        wx.showToast({
          title: `${service.name}功能开发中`,
          icon: 'none'
        });
    }
  },

  // 查看医生详情
  viewDoctor(e) {
    const id = e.currentTarget.dataset.id;
    router.navigateTo(`/pages/doctor/doctor`, { id });
  },

  // 查看文章详情
  viewArticle(e) {
    const id = e.currentTarget.dataset.id;
    router.navigateTo(`/pages/article/article`, { id });
  }
}); 