const { appointmentApi } = require('../../../api/index');
const router = require('../../../utils/router');
const errorUtil = require('../../../utils/error');
const config = require('../../../config/index');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    isLogin: false,
    userInfo: null,
    loading: true,
    doctorList: [],
    total: 0,
    showTopError: false,
    topErrorMsg: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.checkLoginStatus();
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    this.checkLoginStatus();
    if (this.data.isLogin) {
      // 清除缓存，确保每次都获取最新数据
      const app = getApp();
      console.log('页面显示时的医生编号:', app.globalData.doctorNumber);
      this.loadMyDoctors();
    }
  },

  /**
   * 生命周期函数--监听页面下拉刷新
   */
  onPullDownRefresh() {
    if (this.data.isLogin) {
      this.loadMyDoctors();
    } else {
      wx.stopPullDownRefresh();
    }
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
    
    this.setData({
      isLogin: loginStatus,
      userInfo: loginStatus ? userInfo : null
    });
    
    return loginStatus;
  },

  // 加载我的医生列表
  loadMyDoctors() {
    this.setData({ loading: true });
    
    // 每次都重新获取全局配置中的医生工号，确保获取的是最新值
    const app = getApp();
    
    // 优先从本地存储获取医生编号
    let doctorNumber;
    try {
      const storedDoctorNumber = wx.getStorageSync('doctorNumber');
      if (storedDoctorNumber) {
        doctorNumber = storedDoctorNumber;
        // 同步更新全局变量
        app.globalData.doctorNumber = storedDoctorNumber;
        console.log('从本地存储获取的医生编号:', doctorNumber);
      } else {
        // 如果本地存储没有，则使用全局变量中的值
        doctorNumber = app.globalData.doctorNumber;
      }
    } catch (e) {
      console.error('读取本地存储医生编号失败', e);
      doctorNumber = app.globalData.doctorNumber;
    }
    
    // 打印医生编号
    console.log('当前医生编号:', doctorNumber);
    
    wx.showLoading({
      title: '加载中...',
      mask: true
    });
    
    appointmentApi.getMyDoctors(doctorNumber)
      .then(res => {
        wx.hideLoading();
        wx.stopPullDownRefresh();
        
        if (res.code === 200 && res.data) {
          // API返回的是单个医生对象而不是列表
          const doctorData = res.data;
          
          // 将单个医生对象放入数组中
          const doctorList = [doctorData];
          
          this.setData({
            doctorList: doctorList,
            total: 1,
            loading: false
          });
        } else {
          this.setData({ 
            doctorList: [],
            total: 0,
            loading: false 
          });
          errorUtil.handleError(this, '获取医生数据失败');
        }
      })
      .catch(err => {
        wx.hideLoading();
        wx.stopPullDownRefresh();
        this.setData({ loading: false });
        
        if (err && err.code === 401) {
          // 登录状态失效，重新检查登录状态
          this.checkLoginStatus();
          
          wx.showModal({
            title: '登录已过期',
            content: '您的登录已过期，需要重新登录',
            showCancel: false,
            success: (res) => {
              if (res.confirm) {
                // 跳转到登录页
                router.navigateTo('/pages/login/login');
              }
            }
          });
        } else {
          errorUtil.handleError(this, err.message || '获取医生列表失败');
        }
      });
  },

  // 查看医生详情
  viewDoctorDetail(e) {
    const index = e.currentTarget.dataset.index;
    const doctor = this.data.doctorList[index];
    
    if (!doctor) {
      wx.showToast({
        title: '医生信息不完整',
        icon: 'none'
      });
      return;
    }
    
    // 显示医生详细信息
    wx.showModal({
      title: '医生详情',
      content: `姓名：${doctor.nickname || doctor.username || '未知'}\n` +
              `职位：${doctor.position || '未知'}\n` +
              `年龄：${doctor.age || '未知'}\n` +
              `医院：${doctor.institution || '未知'}\n` +
              `诊室：${doctor.room || '未知'}\n` +
              `电话：${doctor.phone || '未知'}\n` +
              `邮箱：${doctor.email || '未知'}\n` +
              `地址：${doctor.address || '未知'}\n` +
              `状态：${doctor.status === 'active' ? '在职' : '离职'}\n` +
              `更新时间：${this.formatTime(doctor.updateTime)}`,
      showCancel: false
    });
  },
  
  // 格式化时间
  formatTime(timeStr) {
    if (!timeStr) return '未知';
    
    try {
      const date = new Date(timeStr);
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      
      return `${year}-${month}-${day}`;
    } catch (error) {
      console.error('时间格式化错误:', error);
      return timeStr;
    }
  },

  // 解绑医生
  unbindDoctor(e) {
    const index = e.currentTarget.dataset.index;
    const doctor = this.data.doctorList[index];
    
    if (!doctor || !doctor.number) {
      wx.showToast({
        title: '医生工号不存在',
        icon: 'none'
      });
      return;
    }
    
    wx.showModal({
      title: '解绑医生',
      content: `确定要解绑 ${doctor.nickname || doctor.username || '此医生'} 吗？`,
      success: (res) => {
        if (res.confirm) {
          // 显示加载提示
          wx.showLoading({
            title: '解绑中...',
            mask: true
          });
          
          // 调用API解绑医生
          appointmentApi.unbindDoctor(doctor.number)
            .then(res => {
              wx.hideLoading();
              
              if (res.code === 200 && res.data === true) {
                // 立即更新全局医生工号为默认医生
                const app = getApp();
                app.globalData.doctorNumber = 887375; // 设置为默认医生工号
                
                // 将医生编号保存到本地存储中，确保持久化
                try {
                  wx.setStorageSync('doctorNumber', 887375);
                } catch (e) {
                  console.error('保存医生编号到本地存储失败', e);
                }
                
                console.log('解绑后更新医生编号为:', app.globalData.doctorNumber);
                
                wx.showToast({
                  title: '解绑成功',
                  icon: 'success'
                });
                
                // 延迟一段时间后重新加载医生列表
                setTimeout(() => {
                  this.loadMyDoctors();
                }, 1000);
              } else {
                wx.showToast({
                  title: res.message || '解绑失败',
                  icon: 'none'
                });
              }
            })
            .catch(err => {
              wx.hideLoading();
              
              let errorMsg = '解绑失败';
              if (err.code === 4033) {
                errorMsg = '您未绑定此医生';
              } else if (err.code === 4035) {
                errorMsg = '绑定错误，请稍后再试';
              } else {
                errorMsg = err.message || '解绑失败';
              }
              
              this.showTopError(errorMsg);
            });
        }
      }
    });
  },

  // 联系医生
  contactDoctor(e) {
    const index = e.currentTarget.dataset.index;
    const doctor = this.data.doctorList[index];
    
    if (doctor && doctor.phone) {
      this.makePhoneCall(doctor.phone);
    } else {
      wx.showToast({
        title: '医生电话不存在',
        icon: 'none'
      });
    }
  },

  // 拨打电话
  makePhoneCall: function (e) {
    let phoneNumber;
    
    // 支持两种调用方式：从事件中获取电话号码或直接传入电话号码
    if (typeof e === 'string') {
      // 直接传入的电话号码
      phoneNumber = e;
    } else if (e.currentTarget && e.currentTarget.dataset) {
      // 从点击事件获取电话号码
      phoneNumber = e.currentTarget.dataset.phone;
    } else if (e.detail && e.detail.phone) {
      // 其他可能的方式
      phoneNumber = e.detail.phone;
    }

    if (!phoneNumber) {
      this.showTopError('无法获取医生联系方式');
      return;
    }

    wx.makePhoneCall({
      phoneNumber: phoneNumber,
      success: () => {
        console.log('拨打电话成功');
      },
      fail: (err) => {
        console.error('拨打电话失败', err);
        if (err.errMsg !== 'makePhoneCall:fail cancel') {
          this.showTopError('拨打电话失败，请稍后再试');
        }
      }
    });
  },

  // 前往绑定医生页面
  goToBindDoctor() {
    const app = getApp();
    app.globalData.appointmentTab = 'doctor';
    router.switchTab(router.pages.appointment);
  },

  // 前往登录页面
  goToLogin() {
    router.navigateTo('/pages/login/login');
  },

  // 关闭顶部错误提示
  closeTopError() {
    errorUtil.hideTopError(this);
  },

  // 显示顶部错误提示
  showTopError(msg) {
    this.setData({
      showTopError: true,
      topErrorMsg: msg
    });
    setTimeout(() => {
      this.closeTopError();
    }, 3000);
  }
}); 