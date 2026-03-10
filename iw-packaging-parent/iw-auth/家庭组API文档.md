# 家庭组功能 API 文档

## 概述

家庭组功能允许用户创建或加入家庭组，实现多用户协作。用户可以在个人模式和家庭组模式之间切换。

**核心特性：**
- 临时邀请码机制（带有效期）
- 组长权限管理
- 成员管理
- 模式切换

## API 接口列表

### 1. 创建家庭组

**接口：** `POST /auth-service/family-group/create`

**描述：** 用户创建一个新的家庭组，创建者自动成为组长。

**请求参数：**
```json
{
  "groupName": "我的家庭",
  "groupAvatar": "https://example.com/avatar.jpg",
  "groupDesc": "温馨的家",
  "maxMember": 10
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupName | String | 是 | 家庭组名称，最长64字符 |
| groupAvatar | String | 否 | 家庭组头像URL |
| groupDesc | String | 否 | 家庭组描述，最长255字符 |
| maxMember | Integer | 否 | 最大成员数，默认10 |

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "groupId": 1,
    "groupName": "我的家庭",
    "groupAvatar": "https://example.com/avatar.jpg",
    "ownerUserId": 1,
    "ownerName": "张三",
    "memberCount": 1,
    "maxMember": 10,
    "groupDesc": "温馨的家",
    "status": 1,
    "createTime": "2024-03-09 14:30:00",
    "memberRole": 1,
    "memberNickname": "张三"
  }
}
```

**业务规则：**
- 每个用户只能创建一个家庭组
- 创建后自动切换到该家庭组模式

---

### 2. 生成邀请码

**接口：** `POST /auth-service/family-group/generate-invite`

**描述：** 为家庭组生成临时邀请码，用于邀请其他用户加入。

**请求参数：**
```json
{
  "groupId": 1,
  "validHours": 24
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | Integer | 是 | 家庭组ID |
| validHours | Integer | 否 | 有效期（小时），默认24小时，最少1小时，最多168小时（7天） |

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "inviteCode": "ABC12345",
    "groupId": 1,
    "groupName": "我的家庭",
    "inviteUserId": 1,
    "inviteUserName": "张三",
    "expireTime": "2024-03-10 14:30:00",
    "expired": false,
    "createTime": "2024-03-09 14:30:00"
  }
}
```

**业务规则：**
- 只有家庭组成员才能生成邀请码
- 邀请码为8位大写字母+数字组合，排除易混淆字符（0,O,1,I,L）
- 邀请码过期后自动失效
- 家庭组满员时不能生成邀请码

---

### 3. 验证邀请码

**接口：** `GET /auth-service/family-group/validate-invite`

**描述：** 验证邀请码是否有效，并返回家庭组信息。

**请求参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| inviteCode | String | 是 | 邀请码 |

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "inviteCode": "ABC12345",
    "groupId": 1,
    "groupName": "我的家庭",
    "inviteUserId": 1,
    "inviteUserName": "张三",
    "expireTime": "2024-03-10 14:30:00",
    "expired": false,
    "createTime": "2024-03-09 14:30:00"
  }
}
```

**业务规则：**
- 邀请码格式必须正确（8位大写字母+数字）
- 邀请码必须在有效期内
- 家庭组必须处于启用状态

---

### 4. 加入家庭组

**接口：** `POST /auth-service/family-group/join`

**描述：** 通过邀请码加入家庭组。

**请求参数：**
```json
{
  "inviteCode": "ABC12345"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| inviteCode | String | 是 | 邀请码，8位大写字母+数字 |

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "groupId": 1,
    "groupName": "我的家庭",
    "groupAvatar": "https://example.com/avatar.jpg",
    "ownerUserId": 1,
    "ownerName": "张三",
    "memberCount": 2,
    "maxMember": 10,
    "groupDesc": "温馨的家",
    "status": 1,
    "createTime": "2024-03-09 14:30:00",
    "memberRole": 2,
    "memberNickname": "李四"
  }
}
```

**业务规则：**
- 邀请码必须有效且未过期
- 用户不能重复加入同一家庭组
- 家庭组不能已满员
- 加入后自动切换到该家庭组模式
- 邀请记录状态更新为"已接受"

---

### 5. 切换家庭组

**接口：** `POST /auth-service/family-group/switch`

**描述：** 在个人模式和家庭组模式之间切换，或在多个家庭组之间切换。

**请求参数：**
```json
{
  "groupId": 1
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | Integer | 是 | 家庭组ID，0表示切换到个人模式 |

**响应示例：**

切换到家庭组：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "groupId": 1,
    "groupName": "我的家庭",
    "memberRole": 2,
    "memberNickname": "李四"
  }
}
```

切换到个人模式：
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

**业务规则：**
- 只能切换到自己加入的家庭组
- 家庭组必须处于启用状态
- 切换到个人模式时返回 null

---

### 6. 退出家庭组

**接口：** `POST /auth-service/family-group/quit`

**描述：** 退出家庭组（仅成员可用，组长不能退出）。

**请求参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | Integer | 是 | 家庭组ID |

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

**业务规则：**
- 组长不能退出，只能解散或转让后退出
- 退出后成员状态更新为"已退出"
- 家庭组成员数量减1
- 如果当前激活的是该家庭组，自动切换到个人模式

---

### 7. 解散家庭组

**接口：** `POST /auth-service/family-group/dissolve`

**描述：** 解散家庭组（仅组长可用）。

**请求参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | Integer | 是 | 家庭组ID |

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

**业务规则：**
- 只有组长才能解散家庭组
- 所有成员状态更新为"已被移除"
- 家庭组状态更新为"禁用"
- 所有成员自动切换到个人模式

---

### 8. 移除成员

**接口：** `POST /auth-service/family-group/remove-member`

**描述：** 移除家庭组成员（仅组长可用）。

**请求参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | Integer | 是 | 家庭组ID |
| userId | Integer | 是 | 要移除的用户ID |

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

**业务规则：**
- 只有组长才能移除成员
- 不能移除自己
- 被移除成员状态更新为"已被移除"
- 家庭组成员数量减1
- 被移除用户自动切换到个人模式

---

### 9. 转让组长

**接口：** `POST /auth-service/family-group/transfer-owner`

**描述：** 将组长权限转让给其他成员（仅组长可用）。

**请求参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | Integer | 是 | 家庭组ID |
| userId | Integer | 是 | 新组长用户ID |

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

**业务规则：**
- 只有组长才能转让组长权限
- 不能转让给自己
- 新组长必须是该家庭组成员
- 原组长角色变为普通成员

---

### 10. 更新家庭组信息

**接口：** `POST /auth-service/family-group/update`

**描述：** 更新家庭组信息（仅组长可用）。

**请求参数：**
```json
{
  "groupId": 1,
  "groupName": "新的家庭名称",
  "groupAvatar": "https://example.com/new-avatar.jpg",
  "groupDesc": "新的描述",
  "maxMember": 15
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | Integer | 是 | 家庭组ID |
| groupName | String | 否 | 家庭组名称 |
| groupAvatar | String | 否 | 家庭组头像URL |
| groupDesc | String | 否 | 家庭组描述 |
| maxMember | Integer | 否 | 最大成员数 |

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

**业务规则：**
- 只有组长才能更新家庭组信息
- 最大成员数不能小于当前成员数
- 只更新提供的字段

---

### 11. 获取我的家庭组列表

**接口：** `GET /auth-service/family-group/my-groups`

**描述：** 获取当前用户加入的所有家庭组列表。

**请求参数：** 无

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "groupId": 1,
      "groupName": "我的家庭",
      "groupAvatar": "https://example.com/avatar.jpg",
      "ownerUserId": 1,
      "ownerName": "张三",
      "memberCount": 3,
      "maxMember": 10,
      "groupDesc": "温馨的家",
      "status": 1,
      "createTime": "2024-03-09 14:30:00",
      "memberRole": 1,
      "memberNickname": "张三"
    },
    {
      "groupId": 2,
      "groupName": "朋友圈",
      "groupAvatar": "https://example.com/avatar2.jpg",
      "ownerUserId": 2,
      "ownerName": "李四",
      "memberCount": 5,
      "maxMember": 10,
      "groupDesc": "朋友们的家",
      "status": 1,
      "createTime": "2024-03-08 10:00:00",
      "memberRole": 2,
      "memberNickname": "张三"
    }
  ]
}
```

---

### 12. 获取家庭组详情

**接口：** `GET /auth-service/family-group/detail`

**描述：** 获取家庭组详细信息。

**请求参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | Integer | 是 | 家庭组ID |

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "groupId": 1,
    "groupName": "我的家庭",
    "groupAvatar": "https://example.com/avatar.jpg",
    "ownerUserId": 1,
    "ownerName": "张三",
    "memberCount": 3,
    "maxMember": 10,
    "groupDesc": "温馨的家",
    "status": 1,
    "createTime": "2024-03-09 14:30:00",
    "memberRole": 2,
    "memberNickname": "李四"
  }
}
```

**业务规则：**
- 只能查看自己加入的家庭组

---

### 13. 获取家庭组成员列表

**接口：** `GET /auth-service/family-group/members`

**描述：** 获取家庭组的所有成员列表。

**请求参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | Integer | 是 | 家庭组ID |

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "username": "zhangsan",
      "name": "张三",
      "avatar": "https://example.com/avatar1.jpg",
      "memberRole": 1,
      "memberRoleName": "组长",
      "memberNickname": "张三",
      "joinTime": "2024-03-09 14:30:00",
      "status": 1,
      "statusName": "正常"
    },
    {
      "id": 2,
      "userId": 2,
      "username": "lisi",
      "name": "李四",
      "avatar": "https://example.com/avatar2.jpg",
      "memberRole": 2,
      "memberRoleName": "成员",
      "memberNickname": "李四",
      "joinTime": "2024-03-09 15:00:00",
      "status": 1,
      "statusName": "正常"
    }
  ]
}
```

**业务规则：**
- 只能查看自己加入的家庭组成员
- 成员按角色和加入时间排序（组长在前）

---

### 14. 更新我的昵称

**接口：** `POST /auth-service/family-group/update-nickname`

**描述：** 更新当前用户在家庭组内的昵称。

**请求参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | Integer | 是 | 家庭组ID |
| nickname | String | 是 | 新昵称 |

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

**业务规则：**
- 只能修改自己在家庭组内的昵称
- 昵称只在该家庭组内生效

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未登录或登录已过期 |
| 403 | 无权限 |
| 500 | 服务器内部错误 |

**常见业务错误：**
- "您已经创建了家庭组，不能重复创建"
- "邀请码无效或已过期"
- "家庭组已满员"
- "您不是该家庭组成员"
- "只有组长才能XXX"
- "组长不能退出家庭组，请先转让组长或解散家庭组"

---

## 使用流程示例

### 场景1：创建家庭组并邀请成员

1. **用户A创建家庭组**
   ```
   POST /family-group/create
   {
     "groupName": "我的家庭",
     "maxMember": 10
   }
   ```

2. **用户A生成邀请码**
   ```
   POST /family-group/generate-invite
   {
     "groupId": 1,
     "validHours": 24
   }
   ```
   得到邀请码：`ABC12345`

3. **用户A分享邀请码给用户B**

4. **用户B验证邀请码**
   ```
   GET /family-group/validate-invite?inviteCode=ABC12345
   ```

5. **用户B加入家庭组**
   ```
   POST /family-group/join
   {
     "inviteCode": "ABC12345"
   }
   ```

### 场景2：在多个家庭组之间切换

1. **查看我的家庭组列表**
   ```
   GET /family-group/my-groups
   ```

2. **切换到家庭组1**
   ```
   POST /family-group/switch
   {
     "groupId": 1
   }
   ```

3. **切换到个人模式**
   ```
   POST /family-group/switch
   {
     "groupId": 0
   }
   ```

### 场景3：组长管理家庭组

1. **查看成员列表**
   ```
   GET /family-group/members?groupId=1
   ```

2. **移除某个成员**
   ```
   POST /family-group/remove-member?groupId=1&userId=3
   ```

3. **转让组长**
   ```
   POST /family-group/transfer-owner?groupId=1&userId=2
   ```

4. **解散家庭组**
   ```
   POST /family-group/dissolve?groupId=1
   ```

---

## 前端集成建议

### 1. 家庭组切换器组件

建议在顶部导航栏添加家庭组切换器：

```vue
<template>
  <el-dropdown @command="handleSwitch">
    <span class="el-dropdown-link">
      {{ currentMode }}
      <el-icon><arrow-down /></el-icon>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item command="0">个人模式</el-dropdown-item>
        <el-dropdown-item
          v-for="group in myGroups"
          :key="group.groupId"
          :command="group.groupId">
          {{ group.groupName }}
        </el-dropdown-item>
        <el-dropdown-item divided command="create">创建家庭组</el-dropdown-item>
        <el-dropdown-item command="join">加入家庭组</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>
```

### 2. 邀请码分享

生成邀请码后，提供多种分享方式：
- 复制邀请码
- 生成二维码
- 分享链接

### 3. 数据标识

在列表页显示数据来源：
- 个人数据：无标识
- 家庭组数据：显示"共享"标签 + 创建人昵称

---

## 数据库脚本

执行以下脚本创建家庭组相关表：

```bash
mysql -u root -p iw_mixes < iw-mixes/iw-packaging-parent/iw-auth/scripts/family-group-init.sql
```

---

## 测试建议

### 单元测试

测试核心业务逻辑：
- 创建家庭组
- 生成邀请码（唯一性、有效期）
- 加入家庭组（各种边界条件）
- 权限验证（组长/成员）

### 集成测试

测试完整流程：
- 创建 → 邀请 → 加入 → 切换
- 组长管理流程
- 退出/解散流程

---

## 注意事项

1. **邀请码安全**
   - 邀请码应该足够随机，避免被猜测
   - 设置合理的有效期
   - 记录邀请历史

2. **并发控制**
   - 加入家庭组时检查成员数量（使用事务）
   - 生成邀请码时检查唯一性

3. **性能优化**
   - 家庭组成员列表使用缓存
   - 邀请码验证使用缓存

4. **用户体验**
   - 提供清晰的错误提示
   - 邀请码过期前提醒
   - 切换模式时给予反馈
