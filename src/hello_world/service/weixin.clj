(ns hello-world.service.weixin
  (:require
    [clj-http.client :as client]
    ;;[clojure.tools.logging :as log]
    [cheshire.core :as cheshire]
    [ring.util.codec :as encoder]
    [hello-world.util.response :as res]
    ))

(def APP_ID "wx97efadfe16b09ba3")
(def APP_SECRET "9f0ee1cf3e2d6b55b5d676e6fb170556")
(def SCOPE "snsapi_userinfo")
(def STATE "state")
(def TIMEOUT 200)

(defn get-weixin-accesstoken
  "Get weixin access token by code"
  [code]
  (try
    (let [result (client/get "https://api.weixin.qq.com/sns/oauth2/access_token"
                             {:query-params {:appid APP_ID
                                             :secret APP_SECRET
                                             :code code
                                             :grant_type "authorization_code"}
                              :timeout TIMEOUT
                              :as :json})
          weixin-response (:body result)]
      ;;(log/info "access token response: " weixin-response)
      (if (:errcode weixin-response)
        (do
          ;;(log/error "get access token failed: " (:errcode weixin-response))
          (res/failResponse weixin-response))
        (res/succResponse weixin-response)))
    (catch Exception e
      (do                                                   ;;(log/error "get access token error: " (.getMessage e))
          (res/failResponse 50100 (.getMessage e))))))

(defn get-weixin-info
  [access_token openid]
  (try
    (let [result (client/get "https://api.weixin.qq.com/sns/userinfo"
                             {:query-params {:access_token access_token
                                             :openid openid
                                             :lang "zh_CN"}
                              :timeout TIMEOUT
                              :as :json})
          weixin-response (:body result)]
      ;;(log/info "userinfo response: " weixin-response)
      (if (:errcode weixin-response)
        (do
          ;;(log/error "get open user info failed: " (:errcode weixin-response))
          (res/failResponse weixin-response))
        (res/succResponse weixin-response)))
    (catch Exception e
      ;;(log/error ("get open user info error: " (.getMessage e)))
      )))

(defn weixin-info
  [code state]
  (let [accessTokenResponse (get-weixin-accesstoken code)
        success (:isSuccess accessTokenResponse)
        result (:result accessTokenResponse)]
    (if success
      (get-weixin-info (:access_token result) (:openid result))
      accessTokenResponse)))

(defn weixin-auth-info
  []
  (let [query-params {:appid APP_ID
                      :redirect_uri (encoder/url-encode "http://192.168.88.48:3000/api/weixin-info")
                      :response_type "code"
                      :scope SCOPE
                      :state STATE}]
    (format "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=%s&scope=%s&state=%s#wechat_redirect"
            (:appid query-params)
            (:redirect_uri query-params)
            (:response_type query-params)
            (:scope query-params)
            (:state query-params))))


