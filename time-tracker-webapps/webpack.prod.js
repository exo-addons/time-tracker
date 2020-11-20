const path = require('path');
const merge = require('webpack-merge');
const webpackCommonConfig = require('./webpack.common.js');

const config = merge(webpackCommonConfig, {
    mode: 'development',
    module: {
        rules: [{
            test: /.(ttf|otf|eot|svg|woff(2)?)(\?[a-z0-9]+)?$/,
            use: {
                loader: "file-loader",
                options: {
                    name: "/time-tracker/fonts/[name].[ext]",
                    emitFile: false
                }
            }
        }]
    },
    entry: {
        activityManagement: './src/main/webapp/vue-app/activityManagement.js',
        timeTracking: './src/main/webapp/vue-app/timeTracking.js',
        timeSheet: './src/main/webapp/vue-app/timeSheet.js'
    },
    output: {
        path: path.join(__dirname, 'target/time-tracker/'),
        filename: 'js/[name].bundle.js'
    },
    externals: {
        jquery: '$',
        vuetify: 'Vuetify'
    }
});

module.exports = config;