const env = {
  development: {
    // 本地开发环境
    BASE_URL: 'http://localhost:8081',
    API_TIMEOUT: 10000
  },
  production: {
    // 生产环境
    BASE_URL: 'https://api.example.com',
    API_TIMEOUT: 10000
  },
  // 测试环境 - 如果后端已经部署在其他测试服务器上，可以配置该环境
  testing: {
    BASE_URL: 'http://test-api.example.com',
    API_TIMEOUT: 15000
  }
};

// 根据环境变量或自定义配置选择环境
// 这里可以根据实际情况修改环境
const currentEnv = 'development';

// 添加特定端口的URL地址
const HOST_URL = 'http://localhost:8080';

console.log(`当前API环境: ${currentEnv}, 基础URL: ${env[currentEnv].BASE_URL}`);
console.log(`HOST_URL: ${HOST_URL}`);

module.exports = {
  ...env[currentEnv],
  HOST_URL,
  TOKEN_KEY: 'token',
  USER_INFO_KEY: 'userInfo'
};