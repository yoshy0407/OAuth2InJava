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


## 動作テストメモ
### リソースオーナー・パスワード・クレデンシャルズフロー
#### トークン取得
```
curl -i -u test-client:test-client-pass -X POST http://localhost:9090/oauth2/token -d "grant_type=password&username=test&password=password"
```

#### リソースサーバへのアクセス
```
curl -i -H "Authorization: Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwibmJmIjoxNjU0NjEzMjgyLCJpc3MiOm51bGwsImV4cCI6MTY1NDYxNTA4MiwiaWF0IjoxNjU0NjEzMjgyLCJqdGkiOiIxZDg5OWM1MC1iYTNiLTQ5OWYtYTBiMS0zYmM4YjdlODJlNDUifQ.fL072h5Ye7n99Qsq4i3acIrErWj0Ewl1qyP1vWTtYe4YG56_cG98B42vTwE_fys7ZWoCVB1f3OsDZdSzU_aAAHyQUHhsRuNQs3uNSQaetCZV5odXsqFRrHnQnSfdLO0TCRZQw5gysdu2jxMSJbjtQggnAnoqdU_iwW-9mPP5xdIgy5G14iKlRwpt2E236a1BdMmH4xqooDieCQRdJWEIX5fzS9dj_zVu6gcImCj-b2sGM-co4HK5O3-hm4lTmfGgZ5TgjpI_R935g5IqLhGUCaO21kjb2eniMcTUVsF3FgZmH_iiukUUGeGpgop7E-5vkwigK8plfoIZYLMg8V46aw" http://localhost:8081/list
```