const path = require('path');
const HtmlWebpackPlugin = require("html-webpack-plugin");
module.exports = {
    entry: './src/main/js/app.js',
    devtool: 'sourcemaps',
    cache: true,
    mode: 'development',
    output: {
        filename: './src/main/resources/static/built/bundle.js',
        path: __dirname,
    },
    devServer: {
        contentBase: path.join(__dirname, 'dist'),
        hot: true,
        open: true,
        proxy: {
            "/": {
                target: "http://localhost:8080"
            }
        },
        port: 8081,
    	publicPath: "/",
    	writeToDisk: true,
    	historyApiFallback: { index: "./dist/index.html" },
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                    }
                }]
            },
            {
                test: /\.css$/,
                loader: "style-loader!css-loader",
            },
            {
                test: /\.html$/,
                use: [
                    {
                        loader: "html-loader",
                    },
                ],
            },

        ]
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: "./src/main/resources/templates/index.html"
            
        })
    ]
};