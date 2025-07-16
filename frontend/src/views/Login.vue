<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>

    <!-- 登录卡片 -->
    <div class="login-card">
      <div class="login-header">
        <h1 class="login-title">欢迎回来</h1>
        <p class="login-subtitle">请登录您的账户</p>
      </div>

      <!-- 登录表单 -->
      <form @submit.prevent="handleLogin" class="login-form">
        <!-- 用户名输入 -->
        <div class="form-group">
          <label for="username" class="form-label">用户名</label>
          <input
            id="username"
            v-model="form.username"
            type="text"
            class="form-input"
            :class="{ error: errors.username }"
            placeholder="请输入用户名"
            @blur="validateField('username')"
            @input="clearFieldError('username')"
          />
          <span v-if="errors.username" class="error-message">
            {{ errors.username }}
          </span>
        </div>

        <!-- 密码输入 -->
        <div class="form-group">
          <label for="password" class="form-label">密码</label>
          <div class="password-input-wrapper">
            <input
              id="password"
              v-model="form.password"
              :type="showPassword ? 'text' : 'password'"
              class="form-input"
              :class="{ error: errors.password }"
              placeholder="请输入密码"
              @blur="validateField('password')"
              @input="clearFieldError('password')"
            />
            <button
              type="button"
              class="password-toggle"
              @click="togglePassword"
              :aria-label="showPassword ? '隐藏密码' : '显示密码'"
            >
              <svg v-if="showPassword" class="eye-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.878 9.878L3 3m6.878 6.878L21 21"></path>
              </svg>
              <svg v-else class="eye-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path>
              </svg>
            </button>
          </div>
          <span v-if="errors.password" class="error-message">
            {{ errors.password }}
          </span>
        </div>

        <!-- 记住我选项 -->
        <div class="form-options">
          <label class="checkbox-wrapper">
            <input
              v-model="form.rememberMe"
              type="checkbox"
              class="checkbox"
            />
            <span class="checkbox-label">记住我</span>
          </label>
          <a href="#" class="forgot-password">忘记密码？</a>
        </div>

        <!-- 登录按钮 -->
        <button
          type="submit"
          class="btn btn-primary login-btn"
          :disabled="loading || !isFormValid"
        >
          <span v-if="loading" class="loading-spinner"></span>
          {{ loading ? '登录中...' : '登录' }}
        </button>

        <!-- 错误消息 -->
        <div v-if="authStore.error" class="error-message global-error">
          {{ authStore.error }}
        </div>

        <!-- 注册链接 -->
        <div class="register-link">
          <span>还没有账户？</span>
          <a href="#" @click.prevent="showRegister = true">立即注册</a>
        </div>
      </form>
    </div>

    <!-- 注册模态框 -->
    <div v-if="showRegister" class="modal-overlay" @click="showRegister = false">
      <div class="modal" @click.stop>
        <div class="modal-header">
          <h2>注册新账户</h2>
          <button class="modal-close" @click="showRegister = false">&times;</button>
        </div>
        <form @submit.prevent="handleRegister" class="register-form">
          <div class="form-group">
            <label for="reg-username" class="form-label">用户名</label>
            <input
              id="reg-username"
              v-model="registerForm.username"
              type="text"
              class="form-input"
              placeholder="请输入用户名"
              required
            />
          </div>
          <div class="form-group">
            <label for="reg-email" class="form-label">邮箱</label>
            <input
              id="reg-email"
              v-model="registerForm.email"
              type="email"
              class="form-input"
              placeholder="请输入邮箱"
              required
            />
          </div>
          <div class="form-group">
            <label for="reg-password" class="form-label">密码</label>
            <input
              id="reg-password"
              v-model="registerForm.password"
              type="password"
              class="form-input"
              placeholder="请输入密码"
              required
            />
          </div>
          <div class="form-group">
            <label for="reg-confirm-password" class="form-label">确认密码</label>
            <input
              id="reg-confirm-password"
              v-model="registerForm.confirmPassword"
              type="password"
              class="form-input"
              placeholder="请再次输入密码"
              required
            />
          </div>
          <button type="submit" class="btn btn-primary">注册</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

// 使用组合式API - Vue 3的推荐写法
const router = useRouter()
const authStore = useAuthStore()

// 响应式数据
const showPassword = ref(false)
const showRegister = ref(false)
const loading = ref(false)

// 表单数据
const form = reactive({
  username: '',
  password: '',
  rememberMe: false
})

const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

// 表单验证错误
const errors = reactive({
  username: '',
  password: ''
})

// 计算属性
const isFormValid = computed(() => {
  return form.username.trim() && form.password.trim() && !errors.username && !errors.password
})

// 方法
const validateField = (field) => {
  errors[field] = ''
  
  if (field === 'username') {
    if (!form.username.trim()) {
      errors.username = '用户名不能为空'
    } else if (form.username.length < 3) {
      errors.username = '用户名至少3个字符'
    }
  }
  
  if (field === 'password') {
    if (!form.password) {
      errors.password = '密码不能为空'
    } else if (form.password.length < 6) {
      errors.password = '密码至少6个字符'
    }
  }
}

const clearFieldError = (field) => {
  if (errors[field]) {
    errors[field] = ''
  }
}

const togglePassword = () => {
  showPassword.value = !showPassword.value
}

const handleLogin = async () => {
  // 验证所有字段
  validateField('username')
  validateField('password')
  
  if (!isFormValid.value) {
    return
  }
  
  loading.value = true
  
  try {
    const result = await authStore.login({
      username: form.username,
      password: form.password
    })
    
    if (result.success) {
      // 登录成功，跳转到首页
      router.push('/home')
    }
  } catch (error) {
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}

const handleRegister = async () => {
  if (registerForm.password !== registerForm.confirmPassword) {
    alert('两次输入的密码不一致')
    return
  }
  
  try {
    // 这里调用注册API
    console.log('注册信息:', registerForm)
    showRegister.value = false
    // 清空注册表单
    Object.assign(registerForm, {
      username: '',
      email: '',
      password: '',
      confirmPassword: ''
    })
  } catch (error) {
    console.error('注册失败:', error)
  }
}

// 生命周期钩子
onMounted(() => {
  // 组件挂载后的初始化逻辑
  console.log('登录页面已加载')
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.circle-1 {
  width: 200px;
  height: 200px;
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.circle-2 {
  width: 150px;
  height: 150px;
  top: 60%;
  right: 10%;
  animation-delay: 2s;
}

.circle-3 {
  width: 100px;
  height: 100px;
  bottom: 20%;
  left: 20%;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-20px); }
}

.login-card {
  background: white;
  border-radius: 16px;
  box-shadow: var(--shadow-lg);
  padding: 2.5rem;
  width: 100%;
  max-width: 400px;
  position: relative;
  z-index: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 2rem;
}

.login-title {
  font-size: 1.875rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 0.5rem;
}

.login-subtitle {
  color: var(--text-secondary);
  font-size: 0.875rem;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.password-input-wrapper {
  position: relative;
}

.password-toggle {
  position: absolute;
  right: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  color: var(--text-secondary);
  padding: 0.25rem;
}

.password-toggle:hover {
  color: var(--text-primary);
}

.eye-icon {
  width: 1.25rem;
  height: 1.25rem;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.875rem;
}

.checkbox-wrapper {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.checkbox {
  margin-right: 0.5rem;
  width: 1rem;
  height: 1rem;
}

.checkbox-label {
  color: var(--text-secondary);
}

.forgot-password {
  color: var(--primary-color);
  text-decoration: none;
}

.forgot-password:hover {
  text-decoration: underline;
}

.login-btn {
  width: 100%;
  height: 3rem;
  font-size: 1rem;
  font-weight: 600;
  position: relative;
}

.loading-spinner {
  width: 1rem;
  height: 1rem;
  border: 2px solid transparent;
  border-top: 2px solid currentColor;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-right: 0.5rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.global-error {
  text-align: center;
  padding: 0.75rem;
  background: #fef2f2;
  border: 1px solid #fecaca;
  border-radius: var(--radius);
  font-size: 0.875rem;
}

.register-link {
  text-align: center;
  font-size: 0.875rem;
  color: var(--text-secondary);
}

.register-link a {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: 500;
}

.register-link a:hover {
  text-decoration: underline;
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  width: 90%;
  max-width: 400px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.modal-header h2 {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-primary);
}

.modal-close {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: var(--text-secondary);
  padding: 0.25rem;
}

.modal-close:hover {
  color: var(--text-primary);
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-card {
    margin: 1rem;
    padding: 1.5rem;
  }
  
  .login-title {
    font-size: 1.5rem;
  }
  
  .form-options {
    flex-direction: column;
    gap: 0.5rem;
    align-items: flex-start;
  }
}
</style> 