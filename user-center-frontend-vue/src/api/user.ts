import myAxios from "@/request";

export interface UserRegisterParams {
  userAccount: string;
  userPassword: string;
  checkPassword: string;
  planetCode: string;
}

export interface UserLoginParams {
  userAccount: string;
  userPassword: string;
}

export interface UserProfile {
  id?: number;
  username?: string;
  userAccount?: string;
  userRole?: number;
  avatarUrl?: string;
  gender?: number;
  createTime?: string;
  [key: string]: unknown;
}

/**
 * 用户注册
 */
export const userRegister = async (params: UserRegisterParams) => {
  return myAxios.request({
    url: "/api/user/register",
    method: "POST",
    data: params,
  });
};

/**
 * 用户登录
 */
export const userLogin = async (params: UserLoginParams) => {
  return myAxios.request({
    url: "/api/user/login",
    method: "POST",
    data: params,
  });
};

/**
 * 用户注销
 */
export const userLogout = async () => {
  return myAxios.request({
    url: "/api/user/logout",
    method: "POST",
  });
};

/**
 * 获取当前用户
 */
export const getCurrentUser = async () => {
  return myAxios.request({
    url: "/api/user/current",
    method: "GET",
  });
};

/**
 * 获取用户列表
 */
export const searchUsers = async (username?: string) => {
  return myAxios.request({
    url: "/api/user/search",
    method: "GET",
    params: {
      username,
    },
  });
};

/**
 * 删除用户
 */
export const deleteUser = async (id: string | number) => {
  return myAxios.request({
    url: "/api/user/delete",
    method: "GET",
    params: {
      id,
    },
  });
};
