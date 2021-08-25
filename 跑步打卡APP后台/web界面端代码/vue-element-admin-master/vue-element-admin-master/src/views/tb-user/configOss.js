let OSS = require('ali-oss');
export function client () {
  var client = new OSS({
    region: 'oss-cn-beijing',//地域
    // 		endpoint:"oss-cn-hangzhou.aliyuncs.com",//访问域名
    accessKeyId: 'LTAI5t72Br3FRQTKkUp74z2u',//你自己的key和id
    accessKeySecret: 'Q5jj8h3Wd5KB9KpPxNxl6kDRIZjQPc',
    bucket: 'frja-running',//oss上你的存储空间名称
    secure: true,
  })
  return client;
}