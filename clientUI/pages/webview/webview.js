// pages/webview/webview.js
Page({
  data: {
    url: '',
    title: '医保服务平台'
  },

  onLoad: function (options) {
    if (options.url) {
      const decodedUrl = decodeURIComponent(options.url);
      this.setData({
        url: decodedUrl
      });
      
      // 如果有标题参数，也设置标题
      if (options.title) {
        this.setData({
          title: decodeURIComponent(options.title)
        });
        wx.setNavigationBarTitle({
          title: this.data.title
        });
      }
    } else {
      // 没有传入URL参数，返回上一页
      wx.showToast({
        title: '加载页面失败',
        icon: 'none'
      });
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    }
  }
}); 