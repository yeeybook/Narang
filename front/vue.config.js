// Vue3 관련 설정 파일
module.exports = {
  devServer: {
    https: false,
    port: 8083,
    open: true,
    proxy: {
      '/api/v1': {
        target: 'https://0.0.0.0:8080/'
      },
      '/narang':{
        target: 'https://0.0.0.0:8080/'
<<<<<<< HEAD
=======
      },
      '/call': {
        target: 'https://0.0.0.0:8080/'
>>>>>>> d502a27633c4c49ed2754a0c6d7feac5110795e7
      }
    },
    historyApiFallback: true,
    hot: true
  },
  css: {
    requireModuleExtension: false // import 시에 경로에 .module 포함 안해도 됨.
  },
  transpileDependencies: [
    'element-plus'
  ],
  lintOnSave: false,
  outputDir: '../back/src/main/resources/dist',
}
