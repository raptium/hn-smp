module.exports = {
  env: {
    NODE_ENV: '"production"'
  },
  defineConstants: {},
  mini: {
    webpackChain(chain) {
      chain.merge({
        optimization: {
          splitChunks: {
            cacheGroups: {
              kotlin: {
                name: 'kotlin',
                test: module => {
                  return /[\\/]build[\\/]js[\\/](packages|packages_imported|node_modules)[\\/]/.test(module.resource)
                },
                priority: 300
              }
            }
          }
        }
      })
    },
    commonChunks(commonChunks) {
      commonChunks.push('kotlin');
      return commonChunks
    }
  },
  h5: {}
}
