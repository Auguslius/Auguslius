/**
 * 全局状态管理
 */
const store = {
  // 全局状态
  state: {
    userInfo: null,
    isLogin: false,
    systemInfo: null,
    // 其他全局状态
  },
  
  // 观察者列表
  listeners: {},
  
  /**
   * 获取状态
   * @param {string} key - 状态键名
   * @returns {*} 状态值
   */
  getState(key) {
    return key ? this.state[key] : this.state;
  },
  
  /**
   * 设置状态
   * @param {string} key - 状态键名
   * @param {*} value - 状态值
   */
  setState(key, value) {
    this.state[key] = value;
    
    // 触发监听器
    this._notify(key, value);
    
    return this;
  },
  
  /**
   * 添加状态变化监听器
   * @param {string} key - 监听的状态键
   * @param {Function} callback - 回调函数
   * @param {string} listenerName - 监听器名称，用于移除
   */
  subscribe(key, callback, listenerName) {
    if (!this.listeners[key]) {
      this.listeners[key] = [];
    }
    
    this.listeners[key].push({
      callback,
      name: listenerName || `listener_${Date.now()}`
    });
    
    return this;
  },
  
  /**
   * 移除状态变化监听器
   * @param {string} key - 监听的状态键
   * @param {string} listenerName - 监听器名称
   */
  unsubscribe(key, listenerName) {
    if (!this.listeners[key]) return this;
    
    this.listeners[key] = this.listeners[key].filter(
      listener => listener.name !== listenerName
    );
    
    return this;
  },
  
  /**
   * 清空指定键的所有监听器
   * @param {string} key - 状态键
   */
  clearListeners(key) {
    if (key) {
      this.listeners[key] = [];
    } else {
      this.listeners = {};
    }
    
    return this;
  },
  
  /**
   * 通知状态变化
   * @private
   * @param {string} key - 变化的状态键
   * @param {*} value - 新的状态值
   */
  _notify(key, value) {
    const listeners = this.listeners[key];
    
    if (listeners && listeners.length) {
      listeners.forEach(listener => {
        try {
          listener.callback(value, this.state);
        } catch (error) {
          console.error('状态监听器执行出错:', error);
        }
      });
    }
  }
};

module.exports = store; 