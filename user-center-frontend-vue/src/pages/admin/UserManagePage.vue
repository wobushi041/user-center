<template>
  <div id="userManagePage">
    <a-input-search
      style="max-width: 320px; margin-bottom: 20px"
      v-model:value="searchValue"
      placeholder="输入用户名搜索"
      enter-button="搜索"
      size="large"
      @search="onSearch"
    />
    <a-table :columns="columns" :data-source="data">
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'avatarUrl'">
          <a-image :src="record.avatarUrl" :width="120" />
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <div v-if="record.userRole === 1">
            <a-tag color="green">管理员</a-tag>
          </div>
          <div v-else>
            <a-tag color="blue">普通用户</a-tag>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-button danger @click="doDelete(record.id)">删除</a-button>
        </template>
      </template>
    </a-table>
  </div>
</template>
<script lang="ts" setup>
import { deleteUser, searchUsers, type UserProfile } from "@/api/user";
import { ref } from "vue";
import { message } from "ant-design-vue";
import dayjs from "dayjs";
import { isSuccessCode } from "@/request";
import { extractErrorMessage } from "@/utils/error";

const searchValue = ref("");
// 获取数据
const onSearch = () => {
  fetchData(searchValue.value);
};

// 删除数据
const doDelete = async (id: string) => {
  if (!id) {
    return;
  }
  try {
    const res = await deleteUser(id);
    const deleteSuccess =
      typeof res.data === "boolean"
        ? res.data
        : isSuccessCode(res.data.code) && res.data.data;
    if (deleteSuccess) {
      message.success("删除成功");
      await fetchData(searchValue.value);
    } else {
      message.error(res.data?.description || "删除失败");
    }
  } catch (error) {
    message.error(extractErrorMessage(error, "删除失败，请稍后重试"));
  }
};

const columns = [
  {
    title: "id",
    dataIndex: "id",
  },
  {
    title: "用户名",
    dataIndex: "username",
  },
  {
    title: "账号",
    dataIndex: "userAccount",
  },
  {
    title: "头像",
    dataIndex: "avatarUrl",
  },
  {
    title: "性别",
    dataIndex: "gender",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
  },
  {
    title: "用户角色",
    dataIndex: "userRole",
  },
  {
    title: "操作",
    key: "action",
  },
];

// 数据
const data = ref<UserProfile[]>([]);

// 获取数据
const fetchData = async (username = "") => {
  try {
    const res = await searchUsers(username);
    if (Array.isArray(res.data)) {
      data.value = res.data;
      return;
    }
    if (isSuccessCode(res.data.code) && res.data.data) {
      data.value = res.data.data;
      return;
    }
    message.error(res.data?.description || "获取数据失败");
  } catch (error) {
    message.error(extractErrorMessage(error, "获取数据失败，请稍后重试"));
  }
};

fetchData();
</script>
