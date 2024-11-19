const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    port: 8080,
    proxy: {
      '/api': {
        target: 'http://localhost:8089', // 后端接口地址
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    },
    client: {
      overlay: false
    }
  },

  configureWebpack: (config) => {
    if (process.env.NODE_ENV === 'production') {
      config.optimization.minimizer[0].options.terserOptions.compress.drop_console = true
    }
  }
})
