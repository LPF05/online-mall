<template>
  <div class="admin-page">
    <el-card class="header-card">
      <h1>管理员后台</h1>
    </el-card>

    <el-tabs v-model="activeTab" class="admin-tabs">
      <!-- 订单管理 -->
      <el-tab-pane label="订单管理" name="orders">
        <el-card class="content-card">
          <div class="toolbar" style="margin-bottom: 20px;">
            <el-radio-group v-model="orderStatusFilter" @change="filterOrders">
              <el-radio-button :value="null">全部</el-radio-button>
              <el-radio-button :value="0">待付款</el-radio-button>
              <el-radio-button :value="1">待发货</el-radio-button>
              <el-radio-button :value="2">已发货</el-radio-button>
              <el-radio-button :value="3">已完成</el-radio-button>
            </el-radio-group>
          </div>
          <el-table :data="orders" stripe style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="orderNo" label="订单号" width="180" />
            <el-table-column prop="userId" label="用户ID" width="100" />
            <el-table-column prop="totalAmount" label="总金额" width="120">
              <template #default="{ row }">¥{{ row.totalAmount.toFixed(2) }}</template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="120">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" fixed="right" width="200">
              <template #default="{ row }">
                <el-button v-if="row.status === 1" type="primary" size="small" @click="shipOrder(row)">
                  发货
                </el-button>
                <el-button type="default" size="small" @click="viewOrderDetail(row)">
                  详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <!-- 商品管理 -->
      <el-tab-pane label="商品管理" name="products">
        <el-card class="content-card">
          <div class="toolbar">
            <el-button type="primary" @click="showProductDialog()">添加商品</el-button>
          </div>
          <el-table :data="products" stripe style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="商品名称" min-width="150" />
            <el-table-column prop="price" label="价格" width="120">
              <template #default="{ row }">¥{{ row.price.toFixed(2) }}</template>
            </el-table-column>
            <el-table-column prop="stock" label="库存" width="100" />
            <el-table-column prop="category" label="分类" width="120" />
            <el-table-column prop="viewCount" label="浏览量" width="100" />
            <el-table-column prop="salesCount" label="销量" width="100" />
            <el-table-column label="操作" fixed="right" width="200">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="showProductDialog(row)">编辑</el-button>
                <el-button type="danger" size="small" @click="deleteProduct(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <!-- 用户管理 -->
      <el-tab-pane label="用户管理" name="users">
        <el-card class="content-card">
          <el-table :data="users" stripe style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="username" label="用户名" width="150" />
            <el-table-column prop="email" label="邮箱" min-width="200" />
            <el-table-column prop="phone" label="手机号" width="130" />
            <el-table-column prop="balance" label="余额" width="120">
              <template #default="{ row }">¥{{ row.balance.toFixed(2) }}</template>
            </el-table-column>
            <el-table-column prop="role" label="角色" width="100">
              <template #default="{ row }">
                <el-tag :type="row.role === 'ADMIN' ? 'danger' : 'primary'">{{ row.role }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="注册时间" width="180" />
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <!-- 商品编辑/添加对话框 -->
    <el-dialog
      v-model="productDialogVisible"
      :title="editingProduct ? '编辑商品' : '添加商品'"
      width="500px"
    >
      <el-form :model="productForm" label-width="80px">
        <el-form-item label="商品名称">
          <el-input v-model="productForm.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="productForm.description" type="textarea" :rows="3" placeholder="请输入商品描述" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="productForm.price" :min="0" :precision="2" :step="0.01" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="productForm.stock" :min="0" />
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="productForm.category" placeholder="请输入分类" />
        </el-form-item>
        <el-form-item label="图片URL">
          <el-input v-model="productForm.imageUrl" placeholder="请输入图片URL" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="productDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveProduct">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import orderApi from '@/api/order'
import productApi from '@/api/product'
import userApi from '@/api/user'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('orders')
const orders = ref([])
const allOrders = ref([])
const orderStatusFilter = ref(null)
const products = ref([])
const users = ref([])

const productDialogVisible = ref(false)
const editingProduct = ref(null)
const productForm = ref({
  name: '',
  description: '',
  price: 0,
  stock: 0,
  category: '',
  imageUrl: ''
})

const getStatusText = (status) => {
  const statusMap = {
    0: '待付款',
    1: '已付款',
    2: '已发货',
    3: '已完成',
    4: '已取消'
  }
  return statusMap[status] || '未知'
}

const getStatusType = (status) => {
  const typeMap = {
    0: 'warning',
    1: 'info',
    2: 'primary',
    3: 'success',
    4: 'danger'
  }
  return typeMap[status] || ''
}

const filterOrders = () => {
  if (orderStatusFilter.value === null) {
    orders.value = allOrders.value
  } else {
    orders.value = allOrders.value.filter(o => o.status === orderStatusFilter.value)
  }
}

const loadOrders = async () => {
  try {
    const res = await orderApi.getAllOrders()
    allOrders.value = res
    filterOrders()
  } catch (error) {
    ElMessage.error('加载订单失败')
    console.error(error)
  }
}

const loadProducts = async () => {
  try {
    const res = await productApi.getAllProducts()
    products.value = res
  } catch (error) {
    ElMessage.error('加载商品失败')
    console.error(error)
  }
}

const loadUsers = async () => {
  try {
    const res = await userApi.getAllUsers()
    users.value = res
  } catch (error) {
    ElMessage.error('加载用户失败')
    console.error(error)
  }
}

const shipOrder = async (order) => {
  try {
    await ElMessageBox.confirm('确定要发货吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await orderApi.shipOrder(order.orderNo)
    ElMessage.success('发货成功')
    await loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('发货失败')
      console.error(error)
    }
  }
}

const viewOrderDetail = (order) => {
  router.push(`/orders/${order.orderNo}`)
}

const showProductDialog = (product = null) => {
  editingProduct.value = product
  if (product) {
    productForm.value = { ...product }
  } else {
    productForm.value = {
      name: '',
      description: '',
      price: 0,
      stock: 0,
      category: '',
      imageUrl: ''
    }
  }
  productDialogVisible.value = true
}

const saveProduct = async () => {
  try {
    if (editingProduct.value) {
      await productApi.updateProduct(editingProduct.value.id, productForm.value)
      ElMessage.success('商品更新成功')
    } else {
      await productApi.createProduct(productForm.value)
      ElMessage.success('商品添加成功')
    }
    productDialogVisible.value = false
    await loadProducts()
  } catch (error) {
    ElMessage.error('保存商品失败')
    console.error(error)
  }
}

const deleteProduct = async (product) => {
  try {
    await ElMessageBox.confirm('确定要删除这个商品吗？', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'danger'
    })
    await productApi.deleteProduct(product.id)
    ElMessage.success('商品删除成功')
    await loadProducts()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除商品失败')
      console.error(error)
    }
  }
}

onMounted(() => {
  loadOrders()
  loadProducts()
  loadUsers()
})
</script>

<style scoped>
.admin-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.header-card {
  margin-bottom: 20px;
}

.header-card h1 {
  margin: 0;
  font-size: 24px;
}

.content-card {
  min-height: 400px;
}

.toolbar {
  margin-bottom: 20px;
}
</style>
