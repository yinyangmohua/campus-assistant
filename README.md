# Campus Assistant（校园智能助手）

一个基于 Spring Boot 与 Spring AI Alibaba Agent Framework 的校园智能助手服务，集成达摩院通义千问（DashScope）模型，提供天气查询、课程信息、图书馆座位等校园场景能力，并支持对话记忆。

**仓库位置**：`d:\工程文件\IDEA\campus-assistant`

## 项目简介
- 面向大学生的对话式校园服务，昵称“**小椰**”。
- 通过工具调用实现多种校园场景能力：`getWeather`、`getCourseInfo`、`getLibrarySeat`、`getCampusInfo`。
- 提供无记忆的简单接口与带记忆的高级接口两套 API。
- 采用 Maven Wrapper，开箱即用，无需本地安装 Maven。

## 技术栈
- Java `17`
- Spring Boot `3.5.7`
- Spring AI Alibaba Agent Framework
- DashScope（通义千问）对话模型：`qwen-max`

## 快速开始
1) 准备环境
- 安装 JDK 17，并确保 `java -version` 可用
- Windows 使用 PowerShell 设置环境变量：
  ```powershell
  $env:AI_DASHSCOPE_API_KEY="你的DashScope密钥"
  ```

2) 启动服务（Windows）
- 开发模式运行：
  ```powershell
  .\mvnw.cmd spring-boot:run
  ```
- 打包运行：
  ```powershell
  .\mvnw.cmd clean package
  java -jar target\campus-assistant-1.0-SNAPSHOT.jar
  ```

3) 默认端口
- 应用端口：`8080`（见 `src/main/resources/application.yml:1`）

## 配置说明
- DashScope API 密钥从环境变量注入：`application.yml:7`
- 模型与参数：`application.yml:10-12`
  - `model: qwen-max`
  - `temperature: 0.7`
  - `max-tokens: 2000`

## 核心代码入口
- 启动类：`src/main/java/top/seven/assistant/CampusAssistantApplication.java:8`
- Agent 配置：`src/main/java/top/seven/assistant/config/AgentConfig.java:104`
  - 系统提示词：`AgentConfig.java:29-50`
  - 工具注册：`AgentConfig.java:68-102`

## API 说明

### 简单接口（无记忆）
- 路径：`GET /api/v1/chat`
- 代码位置：`src/main/java/top/seven/assistant/controller/SimpleAgentController.java:22`
- 请求参数：`question`（字符串）
- 返回：纯文本答案
- 示例：
  ```bash
  curl "http://localhost:8080/api/v1/chat?question=图书馆在哪里"
  ```

### 高级接口（带记忆）
- 路径：`POST /api/v2/chat`
- 代码位置：`src/main/java/top/seven/assistant/controller/AdvancedAgentController.java:36`
- 请求体：`RequestDTO`（`src/main/java/top/seven/assistant/model/RequestDTO.java:8-10`）
  ```json
  {
    "userId": "u-1001",
    "message": "帮我查一下今天的课程"
  }
  ```
- 返回体：`AssistantResponse`（`src/main/java/top/seven/assistant/model/AssistantResponse.java:12-24`）
  ```json
  {
    "answer": "…",
    "type": "general",
    "suggestion": "请根据你的需求，给出一个建议",
    "needsFurtherHelp": false
  }
  ```
- 示例：
  ```bash
  curl -X POST "http://localhost:8080/api/v2/chat" \
    -H "Content-Type: application/json" \
    -d '{"userId":"u-1001","message":"北京天气怎么样"}'
  ```

### 对话历史（示例占位）
- 路径：`GET /api/v2/history/{userId}`
- 代码位置：`src/main/java/top/seven/assistant/controller/AdvancedAgentController.java:63`
- 说明：当前返回占位信息，历史能力需要结合 `MemorySaver` 做进一步实现。

## 工具能力（Agent Tools）
- 天气查询：`WeatherTool`（`src/main/java/top/seven/assistant/tool/WeatherTool.java:16-25`）
- 课程查询：`CourseQueryTool`（`src/main/java/top/seven/assistant/tool/CourseQueryTool.java:22-41`）
- 图书馆座位：`LibrarySeatTool`（`src/main/java/top/seven/assistant/tool/LibrarySeatTool.java:19-31`）
- 校园信息：`CampusInfoTool`（`src/main/java/top/seven/assistant/tool/CampusInfoTool.java:18-33`）

## 开发提示
- 修改系统提示词与工具规则：`AgentConfig.java:29-50`
- 自定义模型参数：`application.yml:10-12`
- 新增/修改工具后记得在 `AgentConfig` 中注册对应 `ToolCallback`（`AgentConfig.java:68-102`）。

## 常见问题
- 403/401：检查 `AI_DASHSCOPE_API_KEY` 是否正确与生效。
- 连接失败：确认本机网络可访问 DashScope 与端口 `8080` 未被占用。
- 中文乱码：确保终端编码为 UTF-8，Maven/项目编码已设置为 UTF-8（`pom.xml:17`）。

## 许可证
- 默认为闭源或课程示例项目。若需开源，请补充许可证声明。