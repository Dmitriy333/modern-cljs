(ns modern-cljs.core
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.java.jdbc :as sql]))

;; defroutes macro defines a function that chains individual route
;; functions together. The request map is passed to each function in
;; turn, until a non-nil response is returned.
(defroutes app-routes
           ; to serve document root address
           (GET "/" [] "<p>Hello from compojure. Hello world</p>")
           ; to serve static pages saved in resources/public directory
           (route/resources "/")
           ; if page is not found
           (route/not-found "Page not found"))

;; site function creates a handler suitable for a standard website,
;; adding a bunch of standard ring middleware to app-route:
(def handler
  (handler/site app-routes))

(def mysql-db {
         :classname "com.mysql.jdbc.Driver"
         :subprotocol "mysql"
         :subname "//127.0.0.1:3306/newsmanagement"
         :user "root"
         :password "root"
         })

(sql/query mysql-db
         ["select * from news where news_id = ?" "1"]
         {:row-fn :short_text})

;(defn list-users []
;  (sql/with-connection db
;   (sql/with-query-results rows
;     ["select * from news"]
;     (println rows))))




;(defn foo
;  "I don't do a whole lot."
;  [x]
;  (println x "Hello, World!"))
