<template>
  <div class="profile-page">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>个人中心</span>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <!-- 我的订单 -->
        <el-tab-pane label="我的订单" name="orders">
          <el-card class="order-filter">
            <div class="filter-tabs">
              <button 
                v-for="tab in orderTabs" 
                :key="tab.label"
                :class="['filter-tab', { active: currentOrderStatus === tab.value }]"
                @click="currentOrderStatus = tab.value"
              >
                <span>{{ tab.label }}</span>
                <span v-if="tab.count > 0" class="badge">{{ tab.count }}</span>
              </button>
            </div>
          </el-card>
          
          <div v-if="orders.length === 0" class="empty-state">
            <el-empty description="暂无订单">
              <router-link to="/products" class="btn btn-primary">去购物</router-link>
            </el-empty>
          </div>
          
          <el-card v-else v-for="order in orders" :key="order.orderNo" class="order-card">
            <div class="order-header">
              <span class="order-no">订单号：{{ order.orderNo }}</span>
              <span :class="['order-status', 'status-' + order.status]">{{ getOrderStatusText(order.status) }}</span>
            </div>
            <div class="order-items">
              <div v-for="item in getOrderItems(order)" :key="item.productId" class="order-item">
                <img :src="item.imageUrl" :alt="item.productName" class="item-image" />
                <div class="item-info">
                  <h4>{{ item.productName }}</h4>
                  <p class="item-price">¥{{ item.price.toFixed(2) }} x {{ item.quantity }}</p>
                </div>
                <span class="item-subtotal">¥{{ (item.price * item.quantity).toFixed(2) }}</span>
              </div>
            </div>
            <div class="order-footer">
              <span class="total">合计：¥{{ order.totalAmount.toFixed(2) }}</span>
              <div class="order-actions">
                <el-button v-if="order.status === 0" type="primary" @click="handlePay(order.orderNo)">去支付</el-button>
                <el-button v-if="order.status === 0" type="danger" @click="handleCancelOrder(order.orderNo)">取消订单</el-button>
                <el-button v-if="order.status === 2" type="primary" @click="handleConfirm(order.orderNo)">确认收货</el-button>
                <el-button v-if="order.status === 3" type="default">去评价</el-button>
              </div>
            </div>
          </el-card>
        </el-tab-pane>

        <!-- 浏览记录 -->
        <el-tab-pane label="浏览记录" name="history">
          <div v-if="browseHistory.length === 0" class="empty-state">
            <el-empty description="暂无浏览记录">
              <router-link to="/products" class="btn btn-primary">去购物</router-link>
            </el-empty>
          </div>
          <div v-else class="history-grid">
            <el-card v-for="item in browseHistory" :key="item.productId" class="history-item">
              <router-link :to="'/products/' + item.productId">
                <img :src="item.imageUrl" :alt="item.productName" class="history-image" />
                <h4 class="history-name">{{ item.productName }}</h4>
                <p class="history-price">¥{{ item.price.toFixed(2) }}</p>
              </router-link>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- 收藏 -->
        <el-tab-pane label="收藏" name="favorites">
          <div v-if="favoriteStore.isLoading" class="empty-state">
            <el-spin :size="40" />
          </div>
          <div v-else-if="favoriteStore.favorites.length === 0" class="empty-state">
            <el-empty description="暂无收藏商品">
              <router-link to="/products" class="btn btn-primary">去购物</router-link>
            </el-empty>
          </div>
          <div v-else class="history-grid">
            <el-card v-for="item in favoriteStore.favorites" :key="item.id" class="history-item">
              <router-link :to="'/products/' + item.productId">
                <img :src="item.imageUrl" :alt="item.productName" class="history-image" />
                <h4 class="history-name">{{ item.productName }}</h4>
                <p class="history-price">¥{{ item.price.toFixed(2) }}</p>
              </router-link>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- 收货地址 -->
        <el-tab-pane label="收货地址" name="address">
          <div class="address-header">
            <el-button type="primary" @click="showAddAddressDialog">添加地址</el-button>
          </div>
          <div v-if="addressStore.isLoading" class="empty-state">
            <el-spin :size="40" />
          </div>
          <div v-else-if="addressStore.addresses.length === 0" class="empty-state">
            <el-empty description="暂无收货地址" />
          </div>
          <div v-else class="address-list">
            <el-card v-for="addr in addressStore.addresses" :key="addr.id" class="address-card">
              <div class="address-info">
                <div class="address-top">
                  <span class="address-name">{{ addr.receiverName }}</span>
                  <span class="address-phone">{{ addr.phone }}</span>
                  <el-tag v-if="addr.isDefault === 1" type="danger" size="small" class="default-tag">默认</el-tag>
                </div>
                <p class="address-detail">{{ addr.province }}{{ addr.city }}{{ addr.district }}{{ addr.detailAddress }}</p>
              </div>
              <div class="address-actions">
                <el-button link type="primary" @click="showEditAddressDialog(addr)">编辑</el-button>
                <el-button v-if="addr.isDefault !== 1" link type="primary" @click="handleSetDefault(addr.id)">设为默认</el-button>
                <el-button link type="danger" @click="handleDeleteAddress(addr.id)">删除</el-button>
              </div>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- 账户设置 -->
        <el-tab-pane label="账户设置" name="settings">
          <el-tabs v-model="settingsTab">
            <!-- 个人信息 -->
            <el-tab-pane label="个人信息" name="info">
              <el-form :model="profileForm" label-width="100px">
                <el-form-item label="用户名">
                  <el-input v-model="profileForm.username" placeholder="请输入用户名" />
                </el-form-item>
                <el-form-item label="邮箱">
                  <el-input v-model="profileForm.email" disabled />
                </el-form-item>
                <el-form-item label="手机号">
                  <el-input v-model="profileForm.phone" placeholder="请输入手机号" />
                </el-form-item>
                <el-form-item label="余额">
                  <el-input v-model="profileForm.balance" disabled>
                    <template #prepend>¥</template>
                  </el-input>
                </el-form-item>
                <el-form-item label="积分">
                  <el-input v-model="profileForm.points" disabled>
                    <template #prepend>⭐</template>
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="saveProfile">保存</el-button>
                  <el-button @click="showRechargeDialog">充值</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <!-- 修改密码 -->
            <el-tab-pane label="修改密码" name="password">
              <el-form :model="passwordForm" label-width="100px">
                <el-form-item label="原密码">
                  <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码" show-password />
                </el-form-item>
                <el-form-item label="新密码">
                  <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码（至少6位）" show-password />
                </el-form-item>
                <el-form-item label="确认密码">
                  <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="changePassword">修改密码</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <!-- 消息通知 -->
            <el-tab-pane label="消息通知" name="notifications">
              <div class="settings-section">
                <el-divider>推送设置</el-divider>
                <el-form label-width="150px">
                  <el-form-item label="订单状态通知">
                    <el-switch v-model="notificationSettings.orderStatus" />
                  </el-form-item>
                  <el-form-item label="库存预警通知">
                    <el-switch v-model="notificationSettings.stockAlert" />
                  </el-form-item>
                  <el-form-item label="促销活动通知">
                    <el-switch v-model="notificationSettings.promotion" />
                  </el-form-item>
                </el-form>
                <el-button type="primary" @click="saveNotifications">保存设置</el-button>
              </div>
            </el-tab-pane>

            <!-- 系统设置 -->
            <el-tab-pane label="系统设置" name="system">
              <div class="settings-section">
                <el-divider>界面设置</el-divider>
                <el-form label-width="150px">
                  <el-form-item label="主题模式">
                    <el-radio-group v-model="systemSettings.theme">
                      <el-radio label="light">浅色</el-radio>
                      <el-radio label="dark">深色</el-radio>
                      <el-radio label="auto">跟随系统</el-radio>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="语言">
                    <el-select v-model="systemSettings.language" placeholder="请选择语言">
                      <el-option label="简体中文" value="zh-CN" />
                      <el-option label="English" value="en-US" />
                    </el-select>
                  </el-form-item>
                </el-form>
                <el-button type="primary" @click="saveSystemSettings">保存设置</el-button>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 充值对话框 -->
    <el-dialog v-model="rechargeDialogVisible" title="账户充值" width="400px">
      <el-form :model="rechargeForm" label-width="80px">
        <el-form-item label="充值金额">
          <el-input-number v-model="rechargeForm.amount" :min="1" :max="10000" :precision="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rechargeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmRecharge">确认充值</el-button>
      </template>
    </el-dialog>

    <!-- 地址对话框 -->
    <el-dialog v-model="addressDialogVisible" :title="isEditingAddress ? '编辑地址' : '添加地址'" width="500px">
      <el-form :model="addressForm" label-width="80px">
        <el-form-item label="收货人">
          <el-input v-model="addressForm.receiverName" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="addressForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="省份">
          <el-input v-model="addressForm.province" placeholder="请输入省份" />
        </el-form-item>
        <el-form-item label="城市">
          <el-input v-model="addressForm.city" placeholder="请输入城市" />
        </el-form-item>
        <el-form-item label="区/县">
          <el-input v-model="addressForm.district" placeholder="请输入区/县" />
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input v-model="addressForm.detailAddress" type="textarea" :rows="2" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="默认地址">
          <el-switch v-model="addressForm.isDefault" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addressDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAddress">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import userApi from '@/api/user'
import { orderAPI, type Order } from '@/api/order'
import { useBrowseHistoryStore, type BrowseHistoryItem } from '@/stores/browseHistory'
import { useFavoriteStore } from '@/stores/favorite'
import { useAddressStore } from '@/stores/address'
import type { Address } from '@/api/address'

const userStore = useUserStore()
const activeTab = ref('orders')
const settingsTab = ref('info')
const favoriteStore = useFavoriteStore()
const addressStore = useAddressStore()

// 地址相关
const addressDialogVisible = ref(false)
const isEditingAddress = ref(false)
const addressForm = ref({
  id: 0,
  receiverName: '',
  phone: '',
  province: '',
  city: '',
  district: '',
  detailAddress: '',
  isDefault: 0
})

// 订单相关
const orders = ref<Order[]>([])
const currentOrderStatus = ref<number | null>(null)
const orderTabs = ref([
  { label: '全部', value: null as number | null, count: 0 },
  { label: '待支付', value: 0 as number | null, count: 0 },
  { label: '待发货', value: 1 as number | null, count: 0 },
  { label: '待收货', value: 2 as number | null, count: 0 },
  { label: '待评价', value: 3 as number | null, count: 0 }
])

// 浏览记录
const browseHistory = ref<BrowseHistoryItem[]>([])

// 个人信息表单
const profileForm = ref({
  username: '',
  email: '',
  phone: '',
  balance: 0,
  points: 0
})

// 密码修改表单
const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 通知设置
const notificationSettings = ref({
  orderStatus: true,
  stockAlert: true,
  promotion: false
})

// 系统设置
const systemSettings = ref({
  theme: 'light',
  language: 'zh-CN'
})

// 充值相关
const rechargeDialogVisible = ref(false)
const rechargeForm = ref({
  amount: 100
})

const getOrderStatusText = (status: number) => {
  const statusMap: Record<number, string> = {
    0: '待支付',
    1: '待发货',
    2: '待收货',
    3: '已完成',
    4: '已取消'
  }
  return statusMap[status] || '未知'
}

const getOrderItems = (order: Order) => {
  if (!order.items) return []
  if (typeof order.items === 'string') {
    return JSON.parse(order.items as string)
  }
  return order.items
}

// 加载订单
const loadOrders = async () => {
  try {
    const data = await orderAPI.getOrderList(currentOrderStatus.value ?? undefined)
    orders.value = data
    updateOrderCounts(data)
  } catch (error) {
    console.error('加载订单失败:', error)
  }
}

const updateOrderCounts = (allOrders: Order[]) => {
  orderTabs.value = orderTabs.value.map(tab => ({
    ...tab,
    count: tab.value === null 
      ? allOrders.length 
      : allOrders.filter(o => o.status === tab.value).length
  }))
}

// 加载浏览记录
const browseHistoryStore = useBrowseHistoryStore()
const loadBrowseHistory = async () => {
  await browseHistoryStore.loadHistory()
  browseHistory.value = browseHistoryStore.history
}

// 加载用户信息
const loadProfile = async () => {
  try {
    const user = await userApi.getProfile()
    profileForm.value = {
      username: user.username,
      email: user.email,
      phone: user.phone || '',
      balance: user.balance,
      points: user.points || 0
    }
  } catch (error) {
    ElMessage.error('加载用户信息失败')
    console.error(error)
  }
}

// 保存个人信息
const saveProfile = async () => {
  try {
    await userApi.updateProfile({
      username: profileForm.value.username,
      phone: profileForm.value.phone
    })
    ElMessage.success('保存成功')
    await loadProfile()
    await userStore.fetchUser()
  } catch (error) {
    ElMessage.error('保存失败')
    console.error(error)
  }
}

// 修改密码
const changePassword = async () => {
  if (!passwordForm.value.oldPassword) {
    ElMessage.warning('请输入原密码')
    return
  }
  if (!passwordForm.value.newPassword || passwordForm.value.newPassword.length < 6) {
    ElMessage.warning('新密码至少6位')
    return
  }
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }

  try {
    await userApi.changePassword({
      oldPassword: passwordForm.value.oldPassword,
      newPassword: passwordForm.value.newPassword
    })
    ElMessage.success('密码修改成功，请重新登录')
    passwordForm.value = {
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    }
    await userStore.logout()
  } catch (error) {
    ElMessage.error('密码修改失败，请检查原密码是否正确')
    console.error(error)
  }
}

// 支付订单
const handlePay = async (orderNo: string) => {
  try {
    await orderAPI.payOrder(orderNo)
    ElMessage.success('支付成功')
    await loadOrders()
  } catch (error) {
    ElMessage.error('支付失败')
    console.error(error)
  }
}

// 确认收货
const handleConfirm = async (orderNo: string) => {
  try {
    await orderAPI.confirmReceipt(orderNo)
    ElMessage.success('确认收货成功')
    await loadOrders()
  } catch (error) {
    ElMessage.error('确认收货失败')
    console.error(error)
  }
}

// 取消订单
const handleCancelOrder = async (orderNo: string) => {
  try {
    await orderAPI.cancelOrder(orderNo)
    ElMessage.success('订单已取消')
    await loadOrders()
  } catch (error) {
    ElMessage.error('取消订单失败')
    console.error(error)
  }
}

// 显示充值对话框
const showRechargeDialog = () => {
  rechargeForm.value.amount = 100
  rechargeDialogVisible.value = true
}

// 确认充值
const confirmRecharge = async () => {
  try {
    await userApi.recharge(rechargeForm.value.amount)
    ElMessage.success('充值成功')
    rechargeDialogVisible.value = false
    await loadProfile()
    await userStore.fetchUser()
  } catch (error) {
    ElMessage.error('充值失败')
    console.error(error)
  }
}

// 保存通知设置
const saveNotifications = () => {
  ElMessage.success('通知设置已保存')
  localStorage.setItem('notificationSettings', JSON.stringify(notificationSettings.value))
}

// 保存系统设置
const saveSystemSettings = () => {
  ElMessage.success('系统设置已保存')
  localStorage.setItem('systemSettings', JSON.stringify(systemSettings.value))
}

// 地址管理
const resetAddressForm = () => {
  addressForm.value = {
    id: 0,
    receiverName: '',
    phone: '',
    province: '',
    city: '',
    district: '',
    detailAddress: '',
    isDefault: 0
  }
}

const showAddAddressDialog = () => {
  resetAddressForm()
  isEditingAddress.value = false
  addressDialogVisible.value = true
}

const showEditAddressDialog = (addr: Address) => {
  addressForm.value = {
    id: addr.id,
    receiverName: addr.receiverName,
    phone: addr.phone,
    province: addr.province,
    city: addr.city,
    district: addr.district,
    detailAddress: addr.detailAddress,
    isDefault: addr.isDefault
  }
  isEditingAddress.value = true
  addressDialogVisible.value = true
}

const saveAddress = async () => {
  if (!addressForm.value.receiverName) {
    ElMessage.warning('请输入收货人姓名')
    return
  }
  if (!addressForm.value.phone) {
    ElMessage.warning('请输入手机号')
    return
  }
  if (!addressForm.value.province || !addressForm.value.city || !addressForm.value.district) {
    ElMessage.warning('请完整填写省/市/区')
    return
  }
  if (!addressForm.value.detailAddress) {
    ElMessage.warning('请输入详细地址')
    return
  }

  try {
    const data = {
      receiverName: addressForm.value.receiverName,
      phone: addressForm.value.phone,
      province: addressForm.value.province,
      city: addressForm.value.city,
      district: addressForm.value.district,
      detailAddress: addressForm.value.detailAddress,
      isDefault: addressForm.value.isDefault
    }
    if (isEditingAddress.value) {
      await addressStore.updateAddress(addressForm.value.id, data)
      ElMessage.success('地址修改成功')
    } else {
      await addressStore.addAddress(data)
      ElMessage.success('地址添加成功')
    }
    addressDialogVisible.value = false
  } catch (error) {
    ElMessage.error(isEditingAddress.value ? '修改地址失败' : '添加地址失败')
    console.error(error)
  }
}

const handleDeleteAddress = async (id: number) => {
  try {
    await addressStore.deleteAddress(id)
    ElMessage.success('地址已删除')
  } catch (error) {
    ElMessage.error('删除地址失败')
    console.error(error)
  }
}

const handleSetDefault = async (id: number) => {
  try {
    await addressStore.setDefault(id)
    ElMessage.success('已设为默认地址')
  } catch (error) {
    ElMessage.error('设置默认地址失败')
    console.error(error)
  }
}

// 加载本地保存的设置
const loadSettings = () => {
  const savedNotifications = localStorage.getItem('notificationSettings')
  const savedSystemSettings = localStorage.getItem('systemSettings')
  
  if (savedNotifications) {
    notificationSettings.value = JSON.parse(savedNotifications)
  }
  if (savedSystemSettings) {
    systemSettings.value = JSON.parse(savedSystemSettings)
  }
}

// 监听订单状态变化
watch(currentOrderStatus, () => {
  loadOrders()
})

// 监听标签页变化
watch(activeTab, (newTab) => {
  if (newTab === 'orders') {
    loadOrders()
  } else if (newTab === 'history') {
    loadBrowseHistory()
  } else if (newTab === 'favorites') {
    favoriteStore.loadFavorites()
  } else if (newTab === 'address') {
    addressStore.loadAddresses()
  } else if (newTab === 'settings') {
    loadProfile()
  }
})

onMounted(() => {
  loadOrders()
  loadSettings()
})
</script>

<style scoped>
.profile-page {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.profile-card {
  :deep(.el-tabs__nav-wrap::after) {
    height: 1px;
  }

  :deep(.el-tabs__item) {
    font-size: 15px;
    font-weight: 500;
    transition: all 0.25s ease;
  }

  :deep(.el-tabs__item.is-active) {
    font-weight: 700;
  }

  :deep(.el-tabs__active-bar) {
    transition: transform 0.3s ease;
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
}

.order-filter {
  margin-bottom: 20px;
}

.filter-tabs {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.filter-tab {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 8px 16px;
  border: 1px solid #e4e7ed;
  border-radius: 20px;
  background: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.filter-tab:hover {
  border-color: #409eff;
  color: #409eff;
  transform: translateY(-1px);
}

.filter-tab.active {
  background: #409eff;
  color: white;
  border-color: #409eff;
}

.badge {
  background: #f56c6c;
  color: white;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
}

.order-card {
  margin-bottom: 20px;
  transition: box-shadow 0.3s ease;
}

.order-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 15px;
  border-bottom: 1px dashed #e4e7ed;
  margin-bottom: 15px;
}

.order-no {
  color: #666;
}

.order-status {
  font-weight: 600;
}

.status-0 { color: #f56c6c; }
.status-1 { color: #e6a23c; }
.status-2 { color: #409eff; }
.status-3 { color: #67c23a; }
.status-4 { color: #909399; }

.order-items {
  margin-bottom: 15px;
}

.order-item {
  display: flex;
  gap: 15px;
  padding: 10px 0;
}

.item-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
}

.item-info {
  flex: 1;
}

.item-info h4 {
  margin: 0 0 5px 0;
  font-size: 14px;
}

.item-price {
  margin: 0;
  color: #999;
  font-size: 13px;
}

.item-subtotal {
  font-weight: 600;
  color: #f56c6c;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 15px;
  border-top: 1px dashed #e4e7ed;
}

.total {
  font-size: 16px;
  font-weight: 700;
  color: #f56c6c;
}

.order-actions {
  display: flex;
  gap: 10px;
}

.history-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.history-item {
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #f0f0f0;
}

.history-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
}

.history-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 8px;
}

.history-name {
  margin: 10px 0 5px 0;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.history-price {
  margin: 0;
  color: #f56c6c;
  font-weight: 600;
}

.settings-section {
  padding: 20px 0;
}

.settings-section :deep(.el-form-item) {
  margin-bottom: 22px;
}

.address-header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.address-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.address-card {
  border: 1px solid #f0f0f0;
  transition: all 0.3s ease;

  :deep(.el-card__body) {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.address-card:hover {
  border-color: #409eff;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.1);
}

.address-info {
  flex: 1;
}

.address-top {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 6px;
}

.address-name {
  font-weight: 600;
  font-size: 15px;
}

.address-phone {
  color: #666;
  font-size: 14px;
}

.default-tag {
  margin-left: 4px;
}

.address-detail {
  margin: 0;
  color: #999;
  font-size: 13px;
  line-height: 1.5;
}

.address-actions {
  display: flex;
  gap: 4px;
  flex-shrink: 0;
  margin-left: 16px;
}
</style>
