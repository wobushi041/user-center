<template>
  <div id="globalHeader">
    <a-row :wrap="false">
      <a-col flex="200px">
        <div class="title-bar">
          <div class="logo-slot"></div>
          <div class="title">用户管理系统</div>
        </div>
      </a-col>
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="current"
          mode="horizontal"
          :items="items"
          @click="doMenuClick"
        />
      </a-col>
      <a-col flex="180px">
        <div class="user-login-status">
          <template v-if="loginUserStore.loginUser.id">
            <span class="nickname">
              {{ loginUserStore.loginUser.username ?? "无名" }}
            </span>
            <a-button
              type="link"
              danger
              :loading="logoutLoading"
              @click="handleLogout"
            >
              退出
            </a-button>
          </template>
          <template v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </template>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";
import { MenuProps, message } from "ant-design-vue";
import { useRouter } from "vue-router";
import { useLoginUserStore } from "@/store/useLoginUserStore";
import { userLogout } from "@/api/user";
import { isSuccessCode } from "@/request";
import { extractErrorMessage } from "@/utils/error";

const loginUserStore = useLoginUserStore();

const router = useRouter();
// 点击菜单后的路由跳转事件
const doMenuClick = ({ key }: { key: string }) => {
  router.push({
    path: key,
  });
};

const current = ref<string[]>([router.currentRoute.value.path]);
// 监听路由变化，更新当前菜单选中状态
router.afterEach((to) => {
  current.value = [to.path];
});

const logoutLoading = ref(false);
const handleLogout = async () => {
  if (logoutLoading.value) {
    return;
  }
  logoutLoading.value = true;
  try {
    const res = await userLogout();
    if (isSuccessCode(res.data.code)) {
      message.success("已退出登录");
      loginUserStore.setLoginUser({
        username: "未登录",
      });
      router.push({
        path: "/user/login",
      });
    } else {
      message.error(res.data.description || "退出失败，请重试");
    }
  } catch (error) {
    message.error(extractErrorMessage(error, "退出失败，请稍后重试"));
  } finally {
    logoutLoading.value = false;
  }
};

const items = computed<MenuProps["items"]>(() => {
  const isLogin = Boolean(loginUserStore.loginUser?.id);
  if (isLogin) {
    return [
      {
        key: "/",
        label: "主页",
        title: "主页",
      },
      {
        key: "/admin/userManage",
        label: "用户管理",
        title: "用户管理",
      },
    ];
  }
  return [
    {
      key: "/user/login",
      label: "用户登录",
      title: "用户登录",
    },
    {
      key: "/user/register",
      label: "用户注册",
      title: "用户注册",
    },
  ];
});
</script>

<style scoped>
.title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: black;
  font-size: 18px;
  margin-left: 16px;
}

.logo-slot {
  width: 48px;
  height: 48px;
  margin-right: 16px;
  background: transparent;
}

.user-login-status {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
}

.nickname {
  font-weight: 500;
  color: #333;
}
</style>
