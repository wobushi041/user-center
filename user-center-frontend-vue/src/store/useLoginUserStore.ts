import { defineStore } from "pinia";
import { ref } from "vue";
import { getCurrentUser, type UserProfile } from "@/api/user";
import { isSuccessCode } from "@/request";

const defaultUser: UserProfile = {
  username: "未登录",
};

export const useLoginUserStore = defineStore("loginUser", () => {
  const loginUser = ref<UserProfile>(defaultUser);

  // 远程获取登录用户信息
  async function fetchLoginUser() {
    const res = await getCurrentUser();
    if (isSuccessCode(res.data.code) && res.data.data) {
      loginUser.value = res.data.data;
    }
  }

  // 单独设置信息
  function setLoginUser(newLoginUser?: UserProfile | null) {
    loginUser.value = newLoginUser ?? defaultUser;
  }

  return { loginUser, fetchLoginUser, setLoginUser };
});
