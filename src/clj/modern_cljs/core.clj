(ns modern-cljs.core
  (:use ring.util.response)
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [ring.middleware.cors :refer [wrap-cors]]
            [modern-cljs.views :as views]
            [modern-cljs.views.layout :as layout]
            [cheshire.core :as json]
            [modern-cljs.model.model :as model]))

;; defroutes macro defines a function that chains individual route
;; functions together. The request map is passed to each function in
;; turn, until a non-nil response is returned.
(defroutes app-routes
           ; to serve document root address
           (GET "/" [] (layout/application "Home" (views/news-list)))
           (GET "/news" [] (views/news-list))
           (GET "/news/:id" [id] (views/browse-news id))
           (GET "/add-news" [] (views/add-news-page)
                               ;(POST "/add-news" req {:status 200 :body (:params req)})
                               ;(POST "/create-news" req (println req))
           )

           (route/resources "/") ; to serve static pages saved in resources/public directory
           (route/not-found "Page not found")) ; if page is not found

;(defroutes api-routes
; (context "/api" []
;  (POST "/add-news" [body] {:status 200 :body (views/browse-news (.valAt body "newsId") )
;
;
;
;                                         ;;(.add newsrepo/news-repo-component body)
;                                         ;;:body (model/->News (.valAt body "purge_everything") "asdf" "asdf" "asdf" "asdf")
;                                         })))

(defroutes api-routes
           (context "/api" []
             (POST "/add-news" {body :body} [body] {:status 200 :body (model/->News (.valAt body "purge_everything") "asdf" "asdf" "asdf" "asdf")})))


(defn wrap-log-request [handler]
  (fn [req] ; return handler function
    (println req) ; perform logging
    (handler req))) ; pass the request through to the inner handler

(defn allow-cross-origin
  "middleware function to allow cross origin"
  [handler]
  (fn [request]
    (let [response {}]
      (-> response
          (assoc-in [:headers "Access-Control-Allow-Origin"]  "*")
          (assoc-in [:headers "Access-Control-Allow-Methods"] "GET,PUT,POST,DELETE,OPTIONS")
          (assoc-in [:headers "Access-Control-Allow-Headers"] "X-Requested-With,Content-Type,Cache-Control")))))

(def cors-headers
  { "Access-Control-Allow-Origin" "*"
   "Access-Control-Allow-Headers" "Content-Type"
   "Access-Control-Allow-Methods" "GET,POST,OPTIONS" })

(defn all-cors
  "Allow requests from all origins"
  [handler]
  (fn [request]
    (let [response (handler request)]
      (update-in response [:headers]
                 merge cors-headers ))))

;; site function creates a handler suitable for a standard website,
;; adding a bunch of standard ring middleware to app-route:
(def app-handler
  (-> app-routes
      ;allow-cross-origin
      all-cors
      ;wrap-log-request
      ))


(defn my-header
  "Returns an updated Ring response with the specified header added."
  [resp name value]
  (assoc-in resp [:headers name] (str value)))

(defn my-content-type
  "Returns an updated Ring response with the a Content-Type header corresponding
  to the given content-type."
  [resp content-type]
  (my-header resp "Content-Type" content-type)
  (my-header resp "Access-Control-Allow-Origin" "*")
  (my-header resp "Access-Control-Allow-Headers" "Content-Type")
  (my-header resp "Access-Control-Allow-Headers" "GET,POST,OPTIONS,DELETE")
  )

(defn my-wrap-json-response
  "Middleware that converts responses with a map or a vector for a body into a
  JSON response.

  Accepts the following options:

  :pretty            - true if the JSON should be pretty-printed
  :escape-non-ascii  - true if non-ASCII characters should be escaped with \\u"
  {:arglists '([handler] [handler options])}
  [handler & [{:as options}]]
  (fn [request]
    (let [response (handler request)]
      (if (coll? (:body response))
        (let [json-response (update-in response [:body] json/generate-string options)]
          (if (contains? (:headers response) "Content-Type")
            json-response
            (my-content-type json-response "text/html; charset=utf-8")))
        response))))


(def api-handler
  (->
      (handler/api api-routes)
      ;(wrap-cors :access-control-allow-origin #".*"
      ;           :access-control-allow-methods [:get :put :post]
      ;           :access-control-allow-headers ["Content-Type"])
      ;(allow-cross-origin)
      ;(all-cors)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)
      ;(my-wrap-json-response)
      ;(wrap-cors routes #".*")
      )
  ;
  )


;(def allow-cross-origin-handler (-> app wrap-params allow-cross-origin))



;(def cors-handler
;  ;(wrap-cors routes #".*")
;  ;(wrap-cors api-routes #".*")
;  ;;(wrap-cors routes identity)
;  ;(wrap-cors api-routes :access-control-allow-origin [#"http://localhost:3000"]
;  ;           :access-control-allow-methods [:get :put :post :delete])
;  (wrap-cors api-routes :access-control-allow-origin [#".*"]
;             :access-control-allow-methods [:get :put :post :delete]
;             :access-control-allow-headers [:content-type]
;             :access-control-max-age [86400]
;
;             )
  ;(wrap-cors routes :access-control-allow-origin [#".*"]
  ;           :access-control-allow-methods [:get :put :post :delete])

;
;)

(def handler
  (routes                                                   ;;got it here http://stackoverflow.com/questions/30303256/registering-multiple-handlers-while-running-server
     api-handler
    app-handler
))

