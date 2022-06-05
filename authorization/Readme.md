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
| 名称 | URL | 実装 |
| --- | --- | ---- |
| 認証エンドポイント | `/oauth2/authorize` | 実装 |
| トークンエンドポイント | `/oauth2/token` | 実装 |
| トークンイントロスペクションエンドポイント | `/oauth2/introspect` | 実装 |
| トークン取り消しエンドポイント | `/oauth2/revoke` | 実装 |
| JWKエンドポイント | `/oauth2/jwks` | 実装 |
| OIDCメタデータエンドポイント | `/.well-known/oauth-authorization-server` | 未実装 |
| 動的クライアント登録エンドポイント | `/oauth2/client` | 未実装 |
| ユーザ情報エンドポイント | -- | 未実装 |
| PARエンドポイント | -- | 未実装 |
| グラント管理エンドポイント | -- | 未実装 |


Google
メタエンドポイント: https://accounts.google.com/.well-known/openid-configuration
JWKSエンドポイント: https://www.googleapis.com/oauth2/v3/certs

### JWT周りの用語整理
#### JWT
* JSON Web Tokenの略称
* JSONをトークンとして渡す仕様の話
* ヘッダーとペイロードをBASE64でエンコードされたものの話
* 検証の仕組みがないので、改ざんができちゃう
#### JOSE
* JSON Object Signing and Encryptionの略称
* JWTが検証できないのを補う仕様のこと
##### JWS
* JSON Web Signatures
* JSONオブジェクトの署名の話
##### JWE
* JSON Web Encryotion
* 暗号化の話
##### JWK
* JSON Web Keys
* 鍵の格納フォーマット話

##### 検証の仕掛け
1. JWSを作る時に秘密鍵で署名したものを作る
2. 検証側は公開鍵でJWSを検証すると
