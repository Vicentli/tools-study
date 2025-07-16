<template>
  <div class="home-container">
    <header class="header">
      <div class="header-content">
        <h1 class="logo">Tools Study</h1>
        <nav class="nav">
          <a href="#" class="nav-link">首页</a>
          <a href="#" class="nav-link">工具</a>
          <a href="#" class="nav-link">文档</a>
          <a href="#" class="nav-link">关于</a>
        </nav>
        <div class="user-menu">
          <span class="username">{{ authStore.userInfo?.username || '用户' }}</span>
          <button @click="handleLogout" class="logout-btn">退出</button>
        </div>
      </div>
    </header>

    <main class="main-content">
      <div class="welcome-section">
        <h2 class="welcome-title">欢迎使用 Tools Study</h2>
        <p class="welcome-subtitle">这是一个学习各种工具和技术的平台</p>
      </div>

      <div class="tools-grid">
        <div class="tool-card" v-for="tool in tools" :key="tool.id">
          <div class="tool-icon">{{ tool.icon }}</div>
          <h3 class="tool-title">{{ tool.title }}</h3>
          <p class="tool-description">{{ tool.description }}</p>
          <button class="btn btn-primary tool-btn">开始使用</button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

// 工具列表数据
const tools = ref([
  {
    id: 1,
    icon: '🔧',
    title: 'Redis 工具',
    description: 'Redis 数据库操作和监控工具'
  },
  {
    id: 2,
    icon: '🔐',
    title: 'JWT 工具',
    description: 'JWT 令牌生成和验证工具'
  },
  {
    id: 3,
    icon: '📊',
    title: '数据统计',
    description: '数据分析和统计功能'
  },
  {
    id: 4,
    icon: '⚙️',
    title: '系统配置',
    description: '系统配置和管理工具'
  }
])

// 退出登录
const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}

onMounted(() => {
  console.log('首页已加载')
})
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: var(--bg-secondary);
}

.header {
  background: white;
  box-shadow: var(--shadow-sm);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem 2rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--primary-color);
}

.nav {
  display: flex;
  gap: 2rem;
}

.nav-link {
  color: var(--text-secondary);
  text-decoration: none;
  font-weight: 500;
  transition: var(--transition);
}

.nav-link:hover {
  color: var(--primary-color);
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.username {
  color: var(--text-primary);
  font-weight: 500;
}

.logout-btn {
  background: none;
  border: 1px solid var(--border-color);
  padding: 0.5rem 1rem;
  border-radius: var(--radius);
  color: var(--text-secondary);
  cursor: pointer;
  transition: var(--transition);
}

.logout-btn:hover {
  background: var(--error-color);
  color: white;
  border-color: var(--error-color);
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.welcome-section {
  text-align: center;
  margin-bottom: 3rem;
}

.welcome-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 1rem;
}

.welcome-subtitle {
  font-size: 1.125rem;
  color: var(--text-secondary);
  max-width: 600px;
  margin: 0 auto;
}

.tools-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 2rem;
}

.tool-card {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: var(--shadow-sm);
  transition: var(--transition);
  text-align: center;
}

.tool-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.tool-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.tool-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 0.5rem;
}

.tool-description {
  color: var(--text-secondary);
  margin-bottom: 1.5rem;
  line-height: 1.6;
}

.tool-btn {
  width: 100%;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    padding: 1rem;
    flex-direction: column;
    gap: 1rem;
  }
  
  .nav {
    gap: 1rem;
  }
  
  .main-content {
    padding: 1rem;
  }
  
  .welcome-title {
    font-size: 2rem;
  }
  
  .tools-grid {
    grid-template-columns: 1fr;
  }
}
</style> 