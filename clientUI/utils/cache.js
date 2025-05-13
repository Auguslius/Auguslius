const cache = {
  data: {},
  
  set(key, value, ttl) {
    this.data[key] = {
      value,
      expire: ttl ? Date.now() + ttl : null
    };
  },
  
  get(key) {
    const item = this.data[key];
    if (!item) return null;
    if (item.expire && item.expire < Date.now()) {
      delete this.data[key];
      return null;
    }
    return item.value;
  },
  
  remove(key) {
    delete this.data[key];
  },
  
  clear() {
    this.data = {};
  }
};

module.exports = cache; 