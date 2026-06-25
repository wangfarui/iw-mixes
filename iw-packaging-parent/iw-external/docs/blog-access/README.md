# 静态博客文章访问密码校验

该能力用于给静态 Hexo 博客的受保护文章发放 AES-256-GCM 解密 key。按 IW 网关规范，对外接口必须带既有外部服务前缀：

```text
POST /external-service/api/blog/access/verify
```

网关会把 `/external-service/api/**` 转发到 `iw-external-service` 并保留前缀。接口成功时返回原始 JSON，不再包 `GeneralResponse`。

## 环境变量示例

生产环境建议通过环境变量或 Nacos 等安全配置中心注入，不要把真实密码、AES key、token secret 提交到代码仓库。

```bash
BLOG_ACCESS_ALLOWED_ORIGINS=https://blog.itwray.com,http://localhost:4000
BLOG_ACCESS_TOKEN_SECRET=replace-with-random-secret
BLOG_ACCESS_DEFAULT_EXPIRES_IN=86400

BLOG_ACCESS_SCOPE_LIFE_PASSWORD_HASH=<bcrypt-hash>
BLOG_ACCESS_SCOPE_LIFE_KEY=<base64url-32-byte-key>
```

配置结构支持多 scope。例如新增 `work` scope 时，配置：

```bash
BLOG_ACCESS_SCOPE_WORK_PASSWORD_HASH=<bcrypt-hash>
BLOG_ACCESS_SCOPE_WORK_KEY=<base64url-32-byte-key>
```

密码 hash 沿用项目现有 Hutool BCrypt 方案，服务端只保存 hash，不保存明文密码。AES key 必须和博客构建时使用的 `BLOG_ACCESS_KEY_<SCOPE>` 完全一致。

## 生成 AES key

```bash
node -e "console.log(require('crypto').randomBytes(32).toString('base64url'))"
```

示例输出应为 43 位 base64url 字符串。将同一个值配置给后端 scope key 和博客构建变量：

```bash
BLOG_ACCESS_SCOPE_LIFE_KEY=<base64url-32-byte-key>
BLOG_ACCESS_KEY_LIFE=<base64url-32-byte-key> npm run build
```

## 请求示例

```bash
curl -i 'https://your-api.example.com/external-service/api/blog/access/verify' \
  -H 'Content-Type: application/json' \
  -H 'Origin: https://blog.itwray.com' \
  --data '{
    "postId": "2026/02/24/monthly-202602",
    "path": "/2026/02/24/monthly-202602/",
    "scope": "life",
    "password": "用户输入的访问密码"
  }'
```

成功响应：

```json
{
  "ok": true,
  "access": "scope",
  "scope": "life",
  "postId": "2026/02/24/monthly-202602",
  "token": "短期访问令牌",
  "expiresAt": "2026-06-25T00:00:00+08:00",
  "key": "base64url-encoded-32-byte-aes-key"
}
```

失败响应统一为 HTTP 401，避免枚举 scope 或文章：

```json
{
  "ok": false,
  "message": "访问密码不正确"
}
```

## 博客侧配置

```yaml
article_access:
  enable: true
  endpoint: https://your-api.example.com/external-service/api/blog/access/verify
```

受保护文章 front-matter：

```yaml
access:
  protected: true
  scope: life
  hint: 请输入访问密码后继续阅读。
```

## 安全行为

- CORS 只允许 `BLOG_ACCESS_ALLOWED_ORIGINS` 中配置的 Origin。
- 响应头包含 `Cache-Control: no-store`。
- 同一 IP、同一 scope 的失败请求按 1 分钟 5 次限流。
- 审计日志记录 scope、postId、path、IP、User-Agent、是否成功和时间，不记录明文密码和 AES key。
