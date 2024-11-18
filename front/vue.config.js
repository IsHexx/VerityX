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
  }

  // configureWebpack: {
  //   resolve: { extensions: [".ts", ".tsx", ".js", ".json"] },
  //   module: {
  //     rules: [
  //       {
  //         test: /\.tsx?$/,
  //         loader: 'ts-loader',
  //         exclude: /node_modules/,
  //         options: {
  //           appendTsSuffixTo: [/\.vue$/],
  //         }
  //       }
  //     ]
  //   }
  // }
})
