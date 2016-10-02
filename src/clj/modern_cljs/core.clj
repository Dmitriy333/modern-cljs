(ns modern-cljs.core
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [modern-cljs.views :as views]))

;; defroutes macro defines a function that chains individual route
;; functions together. The request map is passed to each function in
;; turn, until a non-nil response is returned.
(defroutes app-routes
           ; to serve document root address
           (GET "/" [] (views/news-list))
           (GET "/news" [] (views/news-list))
           (GET "/news/:id" [id] (views/browse-news id))

           (route/resources "/") ; to serve static pages saved in resources/public directory

           (route/not-found "Page not found")) ; if page is not found

(defn wrap-log-request [handler]
  (fn [req] ; return handler function
    (println req) ; perform logging
    (handler req))) ; pass the request through to the inner handler

;; site function creates a handler suitable for a standard website,
;; adding a bunch of standard ring middleware to app-route:
(def handler
  (handler/site (-> app-routes wrap-log-request)))

