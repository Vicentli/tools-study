import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'

export const useAuthStore = defineStore('auth', () => {
  // 状态
  const user = ref(null)
  const token = ref(localStorage.getItem('token') || null)
  const loading = ref(false)
  const error = ref(null)

  // 计算属性
  const isAuthenticated = computed(() => !!token.value)
  const userInfo = computed(() => user.value)

  // 方法
  const login = async (credentials) => {
    loading.value = true
    error.value = null
    
    try {
      const response = await authApi.login(credentials)
      const { token: newToken, user: userData } = response.data
      
      // 保存token和用户信息
      token.value = newToken
      user.value = userData
      localStorage.setItem('token', newToken)
      
      return { success: true }
    } catch (err) {
      error.value = err.response?.data?.message || '登录失败，请重试'
      return { success: false, error: error.value }
    } finally {
      loading.value = false
    }
  }

  const logout = () => {
    token.value = null
    user.value = null
    error.value = null
    localStorage.removeItem('token')
  }

  const clearError = () => {
    error.value = null
  }

  return {
    // 状态
    user,
    token,
    loading,
    error,
    
    // 计算属性
    isAuthenticated,
    userInfo,
    
    // 方法
    login,
    logout,
    clearError
  }
}) 