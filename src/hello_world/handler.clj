(ns hello-world.handler
  (:require
            ;[compojure.core :refer :all]
            ;[compojure.route :as route]
            [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [hello-world.domain.schema :refer :all]
            [hello-world.service.weixin :as weixin]
            [hello-world.service.base :as base]
            [hello-world.service.user :as user]
            [schema.core :as s]
            ))

;(defroutes app-routes
;  (GET "/" [] "Hello World")
;  (GET "/weixin-info" []
;      :return Response
;      :tags ["weixin"]
;      :query-params [code :- String, state :- String]
;      :summary "微信回调接口"
;      (ok (weixin/weixin-info code state)))
;
;  (GET "/weixin-auth-info" []
;      :tags ["weixin"]
;      :summary "需要在微信客户端中运行"
;      (permanent-redirect (weixin/weixin-auth-info)))
;
;  (route/not-found "Not Found"))

(def reload-app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "Newbie"
                    :description "Jiliguala newbie project"}
             :tags [{:name "api", :description "Api"}
                    {:name "weixin", :description "Weixin api"}]}}}

    (context "/api" []
      :tags ["api"]

      (GET "/hello-world" []
        :return Response
        :summary "Hello World"
        (ok (base/hello-world))
        )

      (GET "/weixin-info" []
        :return Response
        :tags ["weixin"]
        :query-params [code :- String, state :- String]
        :summary "微信回调接口"
        (ok (weixin/weixin-info code state)))

      (GET "/weixin-auth-info" []
        :tags ["weixin"]
        :summary "需要在微信客户端中运行"
        (permanent-redirect (weixin/weixin-auth-info)))
      )

    (context "/user" []
      :tags ["user"]

      (POST "/register" []
        :summary "注册用户"
        :body [body {:openid s/Str
                     (s/optional-key :sex) s/Str
                     (s/optional-key :nickname) s/Str
                     (s/optional-key :country) s/Str
                     (s/optional-key :province) s/Str}]
        (ok (user/register body)))

      (POST "/login" []
        :summary "注册用户"
        :body [body {:openid s/Str}]
        (let [response (user/login (:openid body))]
          (if (:isSuccess response) (assoc-in (ok response) [:session :identity] body)
                                    (ok response))))

      (POST "/logout" []
        :summary "注销用户"
        :body [body {:openid s/Str}]
        (assoc-in (ok) [:session :identity] nil))

      (POST "/order" []
        :summary "下订单"
        :body [body {:openid s/Str
                     :pid s/Str}]
        (ok (user/order (:openid body) (:pid body))))
      )
    ))



;(def app
;  (wrap-defaults reload-app site-defaults)
;  )

(def app
  (wrap-reload #'reload-app))
