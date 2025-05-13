// pages/appointment/appointment.js
const { loginApi, appointmentApi } = require('../../api/index');
const router = require('../../utils/router');
const errorUtil = require('../../utils/error');
const config = require('../../config/index');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    isLogin: false,
    userInfo: null,
    loading: false,
    showTopError: false, // 是否显示顶部错误提示
    topErrorMsg: '', // 顶部错误提示信息
    currentTab: 'hospital', // 当前选中的标签页：hospital-选择医院，doctor-绑定医生
    selectedHospital: '', // 当前选择的医院名称
    
    // 标签页选项
    tabOptions: [
      { text: '选择医院', value: 'hospital' },
      { text: '绑定医生', value: 'doctor' }
    ],

    // 机构分类数据
    categoryTree: [], // 机构种类树
    loadingCategories: false, // 是否正在加载机构分类
    selectedParentIndex: -1, // 选中的父级分类索引
    selectedChildIndex: -1, // 选中的子级分类索引
    currentParentCategory: null, // 当前选中的父级分类
    currentChildCategory: null, // 当前选中的子级分类
    
    // 下拉选择器状态
    showParentPicker: false, // 是否显示父级分类选择器
    showChildPicker: false,  // 是否显示子级分类选择器
    
    // 加载状态
    loadingInstitution: false, // 是否正在加载机构信息
    
    // 医疗机构列表
    institutionList: [], // 医疗机构列表
    
    // 医生列表（静态数据）
    doctorList: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.checkLoginStatus();
    // 加载机构分类数据
    this.loadCategoryTree();
    
    // 根据options.tab参数决定显示哪个标签页
    if (options && options.tab === 'doctor') {
      this.setData({
        currentTab: 'doctor'
      });
    } else {
      // 默认显示"选择医院"标签页
      this.setData({
        currentTab: 'hospital'
      });
    }
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    this.checkLoginStatus();
    
    // 检查全局变量中是否有标签页设置
    const app = getApp();
    if (app.globalData && app.globalData.appointmentTab) {
      this.setData({
        currentTab: app.globalData.appointmentTab
      });
      // 使用后清除，避免影响下次进入
      app.globalData.appointmentTab = null;
    }
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {
    // 重新加载分类数据
    this.loadCategoryTree();
    wx.stopPullDownRefresh();
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },

  // 检查登录状态
  checkLoginStatus() {
    const token = wx.getStorageSync('token');
    if (token) {
      this.setData({
        isLogin: true,
        userInfo: wx.getStorageSync('userInfo') || {}
      });
    } else {
      this.setData({
        isLogin: false,
        userInfo: null,
        loading: false
      });
    }
  },

  // 切换标签页
  switchTab(e) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({
      currentTab: tab
    });
  },

  // 通过下拉菜单切换标签页
  switchTabByDropdown(e) {
    this.setData({
      currentTab: e.detail
    });
  },

  // 加载机构分类树
  loadCategoryTree() {
    this.setData({
      loadingCategories: true
    });
    
    appointmentApi.getCategoryTree()
      .then(res => {
        // 同时支持code为0和200的成功情况
        if ((res.code === 0 || res.code === 200) && res.data) {
          // 处理返回的数据，确保数据结构符合预期
          const categoryData = this.processCategoryData(res.data);
          
          this.setData({
            categoryTree: categoryData,
            loadingCategories: false
          });
          console.log('机构分类数据加载成功:', categoryData);
        } else {
          this.setData({
            loadingCategories: false
          });
          console.error('获取机构分类数据失败:', res);
          
          wx.showToast({
            title: '获取机构分类失败',
            icon: 'none'
          });
        }
      })
      .catch(err => {
        console.error('获取机构分类树失败:', err);
        this.setData({
          loadingCategories: false
        });
        
        // 如果是401未授权错误，则提示重新登录
        if (err && err.code === 401) {
          wx.showModal({
            title: '登录已过期',
            content: '您的登录已过期，需要重新登录',
            showCancel: false,
            success: (res) => {
              if (res.confirm) {
                // 清除登录状态
                wx.removeStorageSync('token');
                wx.removeStorageSync('userInfo');
                this.setData({
                  isLogin: false,
                  userInfo: null
                });
                // 跳转到登录页
                wx.navigateTo({
                  url: '/pages/login/login'
                });
              }
            }
          });
        } else {
          wx.showToast({
            title: '获取机构分类失败',
            icon: 'none'
          });
        }
      });
  },
  
  // 处理分类数据，确保格式一致
  processCategoryData(data) {
    try {
      // 如果数据本身就是对象，可能嵌套在data字段中
      if (data && typeof data === 'object' && !Array.isArray(data) && data.data) {
        data = data.data;
      }
      
      if (!Array.isArray(data)) {
        console.error('机构分类数据不是数组格式:', data);
        return [];
      }
      
      // 检查和转换数据格式，确保每个节点都有label和value字段
      return data.map(category => {
        // 处理后端可能返回的不同键名
        const formattedCategory = {
          label: category.label || category.name || '未命名分类',
          value: category.value || category.id || 0
        };
        
        // 处理子分类，支持children或者其他可能的键名
        const children = category.children || category.childList || category.subCategories || [];
        
        if (Array.isArray(children)) {
          formattedCategory.children = children.map(child => ({
            label: child.label || child.name || '未命名医院',
            value: child.value || child.id || 0
          }));
        } else {
          formattedCategory.children = [];
        }
        
        return formattedCategory;
      });
    } catch (error) {
      console.error('处理分类数据出错:', error);
      return [];
    }
  },

  // 显示父级分类选择器
  showParentCategoryPicker() {
    // 检查是否已加载分类数据
    if (!this.data.categoryTree || this.data.categoryTree.length === 0) {
      wx.showToast({
        title: '正在加载分类数据...',
        icon: 'none'
      });
      
      // 重新加载分类数据
      this.loadCategoryTree();
      return;
    }
    
    this.setData({
      showParentPicker: true
    });
  },
  
  // 隐藏父级分类选择器
  hideParentCategoryPicker() {
    this.setData({
      showParentPicker: false
    });
  },
  
  // 父级分类选择变化
  onParentCategoryChange(e) {
    const index = e.detail.value;
    const parentCategory = this.data.categoryTree[index];
    
    this.setData({
      selectedParentIndex: index,
      currentParentCategory: parentCategory,
      currentChildCategory: null,
      selectedChildIndex: -1,
      institutionList: [] // 当父分类改变时重置机构列表
    });
    
    console.log('选择的父级分类:', parentCategory);
  },
  
  // 确认父级分类选择
  confirmParentCategory() {
    this.setData({
      showParentPicker: false
    });
  },
  
  // 显示子级分类选择器
  showChildCategoryPicker() {
    if (this.data.selectedParentIndex === -1) {
      wx.showToast({
        title: '请先选择父级分类',
        icon: 'none'
      });
      return;
    }
    
    this.setData({
      showChildPicker: true
    });
  },
  
  // 隐藏子级分类选择器
  hideChildCategoryPicker() {
    this.setData({
      showChildPicker: false
    });
  },
  
  // 子级分类选择变化
  onChildCategoryChange(e) {
    const index = e.detail.value;
    // 确保父级分类存在且有子分类
    if (this.data.currentParentCategory && 
        Array.isArray(this.data.currentParentCategory.children) && 
        this.data.currentParentCategory.children.length > index) {
      
      const childCategory = this.data.currentParentCategory.children[index];
      
      this.setData({
        selectedChildIndex: index,
        currentChildCategory: childCategory
      });
      
      console.log('选择的子级分类:', childCategory);
    } else {
      console.error('选择子分类失败: 数据不完整或索引错误');
    }
  },
  
  // 确认子级分类选择
  confirmChildCategory() {
    if (!this.data.currentChildCategory) {
      wx.showToast({
        title: '请选择机构分支类型',
        icon: 'none'
      });
      return;
    }
    
    // 关闭选择器
    this.setData({
      showChildPicker: false
    });
    
    // 直接获取医疗机构详情，不需要再点击按钮
    this.getInstitutionByCategory();
  },
  
  // 根据选择的机构分支类型获取医疗机构列表
  getInstitutionByCategory() {
    if (!this.data.currentChildCategory) {
      wx.showToast({
        title: '请先选择机构分支类型',
        icon: 'none'
      });
      return;
    }
    
    // 确保将value转为数字类型
    const categoryId = parseInt(this.data.currentChildCategory.value);
        
    // 设置加载状态
    this.setData({
      loadingInstitution: true,
      institutionList: [] // 重置机构列表
    });
    
    // 显示加载提示
    wx.showLoading({
      title: '获取机构信息...',
      mask: true
    });
    
    console.log('获取分支类型ID为', categoryId, '的医疗机构信息');
    
    // 获取医疗机构列表
    appointmentApi.getInstitutionList(categoryId)
      .then(res => {
        // 隐藏加载提示
        wx.hideLoading();
        
        console.log('获取医疗机构响应:', res);
        this.setData({
          loadingInstitution: false
        });
        
        if (res.code === 200 && res.data && res.data.length > 0) {
          // 更新UI显示所有医疗机构
          this.setData({
            institutionList: res.data
          });
          
          console.log('获取医疗机构列表成功:', res.data);
          
          // 显示成功提示
          wx.showToast({
            title: '获取成功',
            icon: 'success',
            duration: 1500
          });
        } else {
          wx.showToast({
            title: '未找到医疗机构信息',
            icon: 'none'
          });
        }
      })
      .catch(err => {
        // 隐藏加载提示
        wx.hideLoading();
        
        console.error('获取医院信息失败:', err);
        this.setData({
          loadingInstitution: false
        });
        
        // 如果是401未授权错误，则提示重新登录
        if (err && err.code === 401) {
          wx.showModal({
            title: '登录已过期',
            content: '您的登录已过期，需要重新登录',
            showCancel: false,
            success: (res) => {
              if (res.confirm) {
                // 清除登录状态
                wx.removeStorageSync('token');
                wx.removeStorageSync('userInfo');
                this.setData({
                  isLogin: false,
                  userInfo: null
                });
                // 跳转到登录页
                wx.navigateTo({
                  url: '/pages/login/login'
                });
              }
            }
          });
        } else if (err && err.code === 400) {
          // 处理参数错误
          wx.showToast({
            title: '参数错误: 请重新选择医院',
            icon: 'none',
            duration: 2000
          });
          // 清空选择的医院
          this.setData({
            currentChildCategory: null,
            selectedChildIndex: -1
          });
        } else {
          // 其他错误，显示错误消息
          wx.showToast({
            title: '获取数据失败: ' + (err.message || '未知错误'),
            icon: 'none'
          });
        }
      });
  },

  // 前往登录页
  goToLogin() {
    wx.navigateTo({
      url: '/pages/login/login'
    });
  },

  // 关闭顶部错误提示
  closeTopError() {
    errorUtil.hideTopError(this);
  },

  // 根据医疗机构获取医生列表并跳转到医生绑定页
  getDoctorsByInstitution(e) {
    // 获取医疗机构名称
    const institutionName = e.currentTarget.dataset.name;
    
    if (!institutionName) {
      wx.showToast({
        title: '无法获取医疗机构名称',
        icon: 'none'
      });
      return;
    }
    
    // 显示加载提示
    wx.showLoading({
      title: '获取医生列表...',
      mask: true
    });
    
    // 调用API获取医生列表
    appointmentApi.getDoctorsByInstitution(institutionName)
      .then(res => {
        // 隐藏加载提示
        wx.hideLoading();
        
        console.log('获取医疗机构医生列表响应:', res);
        
        if (res.code === 200 && res.data) {
          // 将医生列表存入全局数据
          const hospitalDoctors = res.data;
          
          // 切换到医生绑定页，并记录已选择的医院
          this.setData({
            currentTab: 'doctor',
            doctorList: hospitalDoctors,
            selectedHospital: institutionName
          });
          
          // 显示成功提示
          wx.showToast({
            title: '获取医生成功',
            icon: 'success',
            duration: 1500
          });
        } else {
          wx.showToast({
            title: '未找到相关医生',
            icon: 'none'
          });
          // 即使没有医生，也切换到医生绑定页并记录已选择的医院
          this.setData({
            currentTab: 'doctor',
            doctorList: [],
            selectedHospital: institutionName
          });
        }
      })
      .catch(err => {
        // 隐藏加载提示
        wx.hideLoading();
        
        console.error('获取医生列表失败:', err);
        
        // 如果是401未授权错误，则提示重新登录
        if (err && err.code === 401) {
          wx.showModal({
            title: '登录已过期',
            content: '您的登录已过期，需要重新登录',
            showCancel: false,
            success: (res) => {
              if (res.confirm) {
                // 清除登录状态
                wx.removeStorageSync('token');
                wx.removeStorageSync('userInfo');
                this.setData({
                  isLogin: false,
                  userInfo: null
                });
                // 跳转到登录页
                wx.navigateTo({
                  url: '/pages/login/login'
                });
              }
            }
          });
        } else {
          // 其他错误，显示错误消息
          wx.showToast({
            title: '获取医生列表失败: ' + (err.message || '未知错误'),
            icon: 'none'
          });
        }
      });
  },

  // 绑定医生
  bindDoctor(e) {
    const index = e.currentTarget.dataset.index;
    const id = e.currentTarget.dataset.id;
    
    // 获取医生信息
    const doctor = this.data.doctorList[index];
    
    if (!doctor) {
      wx.showToast({
        title: '无法获取医生信息',
        icon: 'none'
      });
      return;
    }
    
    // 获取医生工号
    const doctorNumber = doctor.number || id;
    
    if (!doctorNumber) {
      wx.showToast({
        title: '医生工号不能为空',
        icon: 'none'
      });
      return;
    }
    
    wx.showModal({
      title: '绑定确认',
      content: `确定要绑定 ${doctor.nickname || doctor.username || '此医生'} 吗？`,
      success: (res) => {
        if (res.confirm) {
          // 显示加载提示
          wx.showLoading({
            title: '正在绑定...',
            mask: true
          });
          
          // 构建请求参数
          const bindDoctorDto = {
            doctorNumber: parseInt(doctorNumber),  // 确保转换为数字
            uuid: this.data.userInfo.uuid || ''  // 使用当前登录用户的UUID
          };
                    
          // 调用API绑定医生
          try {
            // 检查API方法是否存在
            if (typeof appointmentApi.bindDoctor !== 'function') {
              console.error('绑定医生API方法不存在，使用直接请求');
              
              // 直接发起请求
              const token = wx.getStorageSync('token');
              
              wx.request({
                url: `${config.BASE_URL}/institution-doctor/bindDoctorByDoctorNumber`,
                method: 'PATCH',
                data: bindDoctorDto,
                header: {
                  'Content-Type': 'application/json',
                  'Authorization': token ? `${token}` : ''
                },
                success: (res) => {
                  // 隐藏加载提示
                  wx.hideLoading();
                  
                  if (res.statusCode >= 200 && res.statusCode < 300) {
                    console.log('绑定医生成功:', res.data);
                    
                    // 设置全局医生编号为当前绑定的医生编号
                    const app = getApp();
                    app.globalData.doctorNumber = parseInt(doctorNumber);
                    console.log('已设置全局医生编号:', app.globalData.doctorNumber);
                    
                    // 将医生编号保存到本地存储中，确保持久化
                    try {
                      wx.setStorageSync('doctorNumber', parseInt(doctorNumber));
                    } catch (e) {
                      console.error('保存医生编号到本地存储失败', e);
                    }
                    
                    // 显示成功提示
                    wx.showToast({
                      title: '绑定成功',
                      icon: 'success',
                      duration: 2000
                    });
                    
                    // 标记医生已绑定状态
                    const doctorList = this.data.doctorList;
                    doctorList[index].isBound = true;
                    this.setData({
                      doctorList: doctorList
                    });
                  } else {
                    // 处理错误
                    const errMsg = res.data?.message || '绑定失败，请稍后再试';
                    console.error('绑定医生失败:', res);
                    
                    wx.showToast({
                      title: errMsg,
                      icon: 'none',
                      duration: 2000
                    });
                  }
                },
                fail: (err) => {
                  // 隐藏加载提示
                  wx.hideLoading();
                  
                  console.error('绑定医生请求失败:', err);
                  
                  wx.showToast({
                    title: '网络请求失败，请稍后再试',
                    icon: 'none',
                    duration: 2000
                  });
                }
              });
            } else {
              // 正常调用API
              appointmentApi.bindDoctor(bindDoctorDto)
                .then(res => {
                  // 隐藏加载提示
                  wx.hideLoading();
                  
                  console.log('绑定医生成功:', res);
                  
                  // 设置全局医生编号为当前绑定的医生编号
                  const app = getApp();
                  app.globalData.doctorNumber = parseInt(doctorNumber);
                  console.log('已设置全局医生编号:', app.globalData.doctorNumber);
                  
                  // 将医生编号保存到本地存储中，确保持久化
                  try {
                    wx.setStorageSync('doctorNumber', parseInt(doctorNumber));
                  } catch (e) {
                    console.error('保存医生编号到本地存储失败', e);
                  }
                  
                  // 显示成功提示
                  wx.showToast({
                    title: '绑定成功',
                    icon: 'success',
                    duration: 2000
                  });
                  
                  // 可以在这里标记医生已绑定状态
                  const doctorList = this.data.doctorList;
                  doctorList[index].isBound = true;
                  this.setData({
                    doctorList: doctorList
                  });
                })
                .catch(err => {
                  // 隐藏加载提示
                  wx.hideLoading();
                  
                  console.error('绑定医生失败:', err);
                  
                  // 根据错误码处理不同情况
                  if (err && err.code === 401) {
                    wx.showModal({
                      title: '登录已过期',
                      content: '您的登录已过期，需要重新登录',
                      showCancel: false,
                      success: (res) => {
                        if (res.confirm) {
                          // 清除登录状态
                          wx.removeStorageSync('token');
                          wx.removeStorageSync('userInfo');
                          this.setData({
                            isLogin: false,
                            userInfo: null
                          });
                          // 跳转到登录页
                          wx.navigateTo({
                            url: '/pages/login/login'
                          });
                        }
                      }
                    });
                  } else {
                    // 直接显示后端返回的错误信息
                    wx.showToast({
                      title: err.message || '绑定失败，请稍后再试',
                      icon: 'none',
                      duration: 2000
                    });
                  }
                });
            }
          } catch (error) {
            // 隐藏加载提示
            wx.hideLoading();
            
            console.error('绑定医生过程发生错误:', error);
            
            wx.showToast({
              title: '程序异常，请稍后再试',
              icon: 'none',
              duration: 2000
            });
          }
        }
      }
    });
  },
  
  // 切换医生详情显示
  toggleDoctorDetail(e) {
    const index = e.currentTarget.dataset.index;
    const doctorList = this.data.doctorList;
    
    // 修改指定医生的showDetail属性
    doctorList[index].showDetail = !doctorList[index].showDetail;
    
    this.setData({
      doctorList: doctorList
    });
  },
  
  // 格式化时间
  formatTime(timeStr) {
    if (!timeStr) return '';
    
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
  }
})