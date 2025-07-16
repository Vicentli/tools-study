# Tools Study Frontend

这是一个基于 Vue 3 的前端项目，展示了现代化的 Vue 开发实践。

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **Vite** - 下一代前端构建工具
- **Vue Router** - Vue.js 官方路由管理器
- **Pinia** - Vue 的状态管理库
- **Axios** - HTTP 客户端
- **CSS Variables** - 现代 CSS 变量系统

## 项目结构

```
frontend/
├── src/
│   ├── api/           # API 接口
│   ├── components/    # 公共组件
│   ├── router/        # 路由配置
│   ├── stores/        # Pinia 状态管理
│   ├── views/         # 页面组件
│   ├── App.vue        # 根组件
│   ├── main.js        # 入口文件
│   └── style.css      # 全局样式
├── index.html         # HTML 模板
├── package.json       # 项目配置
├── vite.config.js     # Vite 配置
└── README.md          # 项目说明
```

## 开发指南

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

### 构建生产版本

```bash
npm run build
```

### 代码检查

```bash
npm run lint
```

## Vue 3 最佳实践

### 1. 组合式 API (Composition API)

使用 `<script setup>` 语法糖，这是 Vue 3 推荐的写法：

```vue
<script setup>
import { ref, reactive, computed, onMounted } from 'vue'

// 响应式数据
const count = ref(0)
const user = reactive({ name: '', email: '' })

// 计算属性
const doubleCount = computed(() => count.value * 2)

// 方法
const increment = () => count.value++

// 生命周期
onMounted(() => {
  console.log('组件已挂载')
})
</script>
```

### 2. 状态管理 (Pinia)

使用 Pinia 进行状态管理：

```javascript
// stores/counter.js
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useCounterStore = defineStore('counter', () => {
  const count = ref(0)
  const doubleCount = computed(() => count.value * 2)
  
  const increment = () => count.value++
  
  return { count, doubleCount, increment }
})
```

### 3. 路由管理

使用 Vue Router 4：

```javascript
// router/index.js
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/views/Home.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})
```

### 4. API 调用

使用 Axios 进行 HTTP 请求：

```javascript
// api/user.js
import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

export const userApi = {
  login: (credentials) => api.post('/auth/login', credentials),
  getUserInfo: () => api.get('/user/info')
}
```

### 5. 样式管理

使用 CSS 变量和现代 CSS 特性：

```css
:root {
  --primary-color: #3b82f6;
  --text-primary: #1f2937;
  --shadow-sm: 0 1px 2px 0 rgb(0 0 0 / 0.05);
}

.btn {
  background: var(--primary-color);
  box-shadow: var(--shadow-sm);
}
```

## 特性

- ✅ 响应式设计
- ✅ 表单验证
- ✅ 路由守卫
- ✅ 状态管理
- ✅ API 集成
- ✅ 错误处理
- ✅ 加载状态
- ✅ 现代化 UI

## 学习要点

1. **组合式 API**: 了解 `ref`, `reactive`, `computed`, `watch` 等
2. **生命周期**: 掌握 `onMounted`, `onUnmounted` 等钩子
3. **响应式系统**: 理解 Vue 3 的响应式原理
4. **状态管理**: 学习 Pinia 的使用方法
5. **路由管理**: 掌握 Vue Router 4 的配置
6. **组件通信**: 了解 props, emit, provide/inject
7. **样式管理**: 学习现代 CSS 的最佳实践

## 扩展建议

1. 添加 TypeScript 支持
2. 集成 UI 组件库 (如 Element Plus)
3. 添加单元测试
4. 配置 ESLint 和 Prettier
5. 添加国际化支持
6. 实现 PWA 功能 