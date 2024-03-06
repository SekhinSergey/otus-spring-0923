module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        ws: true,
        changeOrigin: true
      }
    }
  },
  outputDir: 'target/dist',
  assetsDir: 'static'
};
