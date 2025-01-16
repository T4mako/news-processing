1.在ruoyi-SpringBoot文件里有个sql文件夹，将里面的两个sql文件导入数据库。在ruoyi-admin/src/main/resources/application-druid.yml里面更改数据库的名称和密码
2.打开cmd，进入Redis文件夹下，输入redis-server.exe  redis.windows.conf开启redis服务
3.打开ruoyi-Vue3，输入npm install和npm install --registry=https://registry.npmmirror.com下载依赖，输入npm run dev启动前端
