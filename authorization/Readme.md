## Spring Authorization Serverの調査
* バージョン 0.2.3

### デフォルトのConfig
* `org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration`で実装されている。
### デフォルトのSecurity設定
* `org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer`をベースに設定されている。
* フレームワークで設定されているエンドポイントに認証を設定し、csrfトークンを除外する。
* `org.springframework.security.config.annotation.web.configuration.RegisterMissingBeanPostProcessor`をBeanに登録している

### クライアントシステムの認証
* `registeredClientRepository`から取得している

### 実際のユーザの認証
* `OAuth2AuthorizationService`トークンの情報を取得するサービスになっている
* `JdbcOAuth2AuthorizationService`でDBから取得できる
* `OAUth2TokenGenerator`でトークンの作成スタイルを変更できる
* この辺りの処理を見ていると確かにセンスがあるとは言えないな・・・
* すでにコードがスパゲッティになっている

### エンドポイントの設定
デフォルトの設定を記載する。外から設定できるようになっているので変更も可能だろう
| 名称 | URL |
| --- | --- |
| 認証エンドポイント | `/oauth2/authorize` |
| トークンエンドポイント | `/oauth2/token` |
| トークンイントロスペクションエンドポイント | `/oauth2/introspect` |
| トークン取り消しエンドポイント | `/oauth2/revoke` |
| OIDCメタデータエンドポイント | `/.well-known/oauth-authorization-server` |


Google
メタエンドポイント: https://accounts.google.com/.well-known/openid-configuration
JWKSエンドポイント: https://www.googleapis.com/oauth2/v3/certs

