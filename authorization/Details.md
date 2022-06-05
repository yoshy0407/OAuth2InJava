# OAuth認可サーバ
## 仕様一覧
* RFC6749
* OpenID Connect Core 1.0
* OAuth 2.0 Multiple Response Type Encoding Practices
* RFC7636
## エンドポイント一覧
| エンドポイント | パス |
| ------------ | ---- |
| 認可エンドポイント | `/oauth2/authorize` |
| トークンエンドポイント | `/oauth2/token` |
| イントロスペクションエンドポイント | `/oauth2/token/introspection` |
| 取り消しエンドポイント | `/oauth2/token/revoke` |
| JWKエンドポイント | `/oauth2/jwks` |
| 動的クライアント登録エンドポイント | `/oauth2/client/register` |
