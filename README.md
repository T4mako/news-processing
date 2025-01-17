1. 创建 **news** 数据库，将 **news.sql** 导入数据库中
2. 打开 cmd，进入 Redis 文件夹下，输入 **redis-server.exe  redis.windows.conf** 开启 redis 服务
3. 打开 ruoyi-Vue3，输入 **npm instal**l 和 **npm install --registry=https://registry.npmmirror.com**下载依赖，输入 **npm run dev** 启动前端
4. 更改 RuoYi-SpringBoot\ruoyi-admin\src\main\resources\application-druid.yml 文件的 username 和 password 为本地数据库用户名密码

系统管理员

- 账号：admin
- 密码：admin123

普通用户

- 账号：ry
- 密码：123456
