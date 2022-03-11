# IACAA3.0-auth2.0-new
#### 基于微服务框架SpringCloud搭建，使用nacos注册中心，业务服务持久层使用Mybatis，权限服务使用JPA，共分为四个服务：权限、用户、网关、业务服务以及前台UI；数据库使用Mysql关系型数据库以及Redis非关系型数据（用于缓存分布式Tocken）,权限框架基于Auth2搭建，涉及限流、熔断、分布式事务配置中心等，前台使用VUE+ElementUI搭建，Echarts图表构建，项目是我的毕业设计，该项目旨在通过数据收集、数据分析等手段统计专业毕业生毕业要求达成度，及一些其他功能

### 最近很多小伙伴再咨询项目相关的问题，大多集中在部署问题，这个版本简化了部署难度，上传了数据库sql脚本，路径为iacaa-server/iacaa2_0-admin/src/main/resources/iacaa-20210407.sql

## 部署基本步骤如下：
## 1.使用以上SQL脚本创建数据库
## 2.安装Srping-Alibaba Nacos 安装方法参照Nacos官方网站 https://nacos.io/zh-cn/docs/what-is-nacos.html
## 3.将auth,user,iacaa,gateway四个服务中appliction.yml的Nacos配置为你的安装机器IP:端口（云上服务器主要打开防火墙，开放端口）
## 4.将auth,user,iacaa 服务中appliction.yml的数据库配置改为你的数据库地址
## 5.安装Redis （具体安装方法寻问度娘）
## 6.将auth,gateway服务中appliction.yml的Redis配置改为你的Redis地址
## 7.按照gateway, auth, user, iacaa的顺序启动服务
## 8.将iacaa-admin 前端代码配置文件中 src/utils/HttpUtils.js 的 devServer 配置改为你的地址
## 9.在iacaa-admin 文件夹下执行命令npm install后执行npm run dev 启动前端UI
## 10.超级管理员root 密码666666 使用超管创建不同角色用户（权限采用RBAC模型设计）
