(ns modern-cljs.core
  (:use ring.util.response)
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [modern-cljs.views :as views]
            [modern-cljs.repository.newsrepository :as newsrepo]
            [modern-cljs.model.model :as model]))

;; defroutes macro defines a function that chains individual route
;; functions together. The request map is passed to each function in
;; turn, until a non-nil response is returned.
(defroutes app-routes
           ; to serve document root address
           (GET "/" [] (views/news-list))
           (GET "/news" [] (views/news-list))
           (GET "/news/:id" [id] (views/browse-news id))
           (GET "/add-news" [] (views/add-news-page))

           (route/resources "/") ; to serve static pages saved in resources/public directory
           (route/not-found "Page not found")) ; if page is not found


(defroutes api-routes
           (context "/api" []
                    (POST "/add-news" {body :body} [body] {:status 200 :body (model/->News (.valAt body "purge_everything") "asdf" "asdf" "asdf" "asdf")})))

(defn wrap-log-request [handler]
  (fn [req] ; return handler function
    (println req) ; perform logging
    (handler req))) ; pass the request through to the inner handler

;; site function creates a handler suitable for a standard website,
;; adding a bunch of standard ring middleware to app-route:
(def app-handler
  (-> app-routes
      wrap-log-request))

(def api-handler
  (->
      (handler/api api-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))

(def handler
  (routes                                                   ;;got it here http://stackoverflow.com/questions/30303256/registering-multiple-handlers-while-running-server
    api-handler
    app-routes))

