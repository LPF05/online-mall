<template>
  <footer class="app-footer">
    <div class="footer-container">
      <div class="footer-content">
        <div class="footer-section">
          <h3>关于我们</h3>
          <p>在线商城 - 优质商品，放心购物</p>
        </div>
        <div class="footer-section">
          <h3>客户服务</h3>
          <ul>
            <li>购物指南</li>
            <li>配送方式</li>
            <li>支付方式</li>
            <li>售后服务</li>
          </ul>
        </div>
        <div class="footer-section">
          <h3>联系方式</h3>
          <ul>
            <li>客服电话：400-123-4567</li>
            <li>工作时间：9:00-18:00</li>
            <li>邮箱：support@example.com</li>
          </ul>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2024 在线商城. All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>

<style lang="scss" scoped>
@import '@/assets/styles/variables.scss';

.app-footer {
  background: $secondary-color;
  color: white;
  padding: $spacing-xl 0 $spacing-lg;

  .footer-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 $spacing-md;
  }

  .footer-content {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: $spacing-xl;
    margin-bottom: $spacing-xl;
    padding-bottom: $spacing-xl;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);

    @media (max-width: $breakpoint-md) {
      grid-template-columns: 1fr;
      gap: $spacing-lg;
    }
  }

  .footer-section {
    h3 {
      font-size: $font-size-lg;
      font-weight: 600;
      margin-bottom: $spacing-md;
      color: $primary-color;
    }

    p,
    li {
      font-size: $font-size-sm;
      color: rgba(255, 255, 255, 0.8);
      line-height: 2;
    }

    ul {
      list-style: none;
    }
  }

  .footer-bottom {
    text-align: center;
    padding-top: $spacing-lg;

    p {
      font-size: $font-size-sm;
      color: rgba(255, 255, 255, 0.6);
    }
  }
}
</style>
