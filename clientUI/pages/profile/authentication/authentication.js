const app = getApp()
const { authenticationApi } = require('../../../api/index')

Page({
  data: {
    uuid: '',
    name: '',
    phone: '',
    email: '',
    idCard: '',
    birthDate: '2000-01-01',
    gender: '男',
    address: '',
    remark: '',
    genderArray: ['男', '女']
  },

  onLoad(options) {
    // 获取用户UUID
    if (app.globalData.userInfo) {
      this.setData({
        uuid: app.globalData.userInfo.uuid || '',
        phone: app.globalData.userInfo.phone || ''
      })
    }
  },

  // 输入框事件处理
  bindNameInput(e) {
    this.setData({
      name: e.detail.value
    })
  },

  bindPhoneInput(e) {
    this.setData({
      phone: e.detail.value
    })
  },

  bindEmailInput(e) {
    this.setData({
      email: e.detail.value
    })
  },

  bindIdCardInput(e) {
    this.setData({
      idCard: e.detail.value
    })
  },

  bindDateChange(e) {
    this.setData({
      birthDate: e.detail.value
    })
  },

  bindGenderChange(e) {
    this.setData({
      gender: this.data.genderArray[e.detail.value]
    })
  },

  bindAddressInput(e) {
    this.setData({
      address: e.detail.value
    })
  },

  bindRemarkInput(e) {
    this.setData({
      remark: e.detail.value
    })
  },

  // 表单验证
  validateForm() {
    if (!this.data.name) {
      wx.showToast({
        title: '请输入真实姓名',
        icon: 'none'
      })
      return false
    }
    if (!this.data.phone) {
      wx.showToast({
        title: '请输入联系方式',
        icon: 'none'
      })
      return false
    }
    if (!this.data.email) {
      wx.showToast({
        title: '请输入邮箱',
        icon: 'none'
      })
      return false
    }
    if (!this.data.idCard) {
      wx.showToast({
        title: '请输入身份证号',
        icon: 'none'
      })
      return false
    }
    if (!this.data.address) {
      wx.showToast({
        title: '请输入住址',
        icon: 'none'
      })
      return false
    }
    return true
  },

  // 提交认证
  submitAuthentication() {
    if (!this.validateForm()) {
      return
    }

    const params = {
      uuid: this.data.uuid,
      name: this.data.name,
      phone: this.data.phone,
      email: this.data.email,
      idCard: this.data.idCard,
      birthDate: this.data.birthDate,
      gender: this.data.gender,
      address: this.data.address,
      remark: this.data.remark
    }

    wx.showLoading({
      title: '提交中...',
    })

    authenticationApi.patientAuthentication(params).then(res => {
      wx.hideLoading()
      if (res.code === 200) {
        wx.showToast({
          title: '认证成功',
          icon: 'success'
        })
        // 更新全局用户信息
        if (app.globalData.userInfo) {
          app.globalData.userInfo.isAuthenticated = true
        }
        // 返回个人中心
        setTimeout(() => {
          wx.navigateBack()
        }, 1500)
      } else {
        wx.showToast({
          title: res.message || '认证失败',
          icon: 'none'
        })
      }
    }).catch(err => {
      wx.hideLoading()
      wx.showToast({
        title: '网络错误，请重试',
        icon: 'none'
      })
      console.error('认证失败:', err)
    })
  }
}) 