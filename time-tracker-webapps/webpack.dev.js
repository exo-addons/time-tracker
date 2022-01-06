const path = require('path');
const merge = require('webpack-merge');

const webpackCommonConfig = require('./webpack.common.js');

// add the server path to your server location path
const exoServerPath = "D:\\EXO\\binaries\\platform-6.3.x-SNAPSHOT-24112021\\webapps";

module.exports = merge(webpackCommonConfig, {
    module: {
        rules: [{
            test: /.(ttf|otf|eot|svg|woff(2)?)(\?[a-z0-9]+)?$/,
            use: {
                loader: "file-loader",
                options: {
                    name: "[name].[ext]",
                    context: 'css',
                    outputPath: "fonts/"
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
        path: path.join(exoServerPath, '/time-tracker/'),
        filename: 'js/[name].bundle.js'
    },
    devServer: {
        contentBase: path.join(exoServerPath, 'src/main/webapp'),
        port: 3000
    },
    devtool: 'inline-source-map'
});