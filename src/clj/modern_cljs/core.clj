(ns modern-cljs.core
  (:use ring.util.response)
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [compojure.route :as route]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.session :as ring-session]
            [ring.middleware.session.memory :as session-memory]
            [modern-cljs.views.views :as views]
            [modern-cljs.views.layout :as layout]
            [modern-cljs.service.browse-news-service :as browse-news-service]
            [modern-cljs.service.userservice :as userservice]
            [modern-cljs.service.add-news-service :as add-news-service]
            [modern-cljs.service.sessionservice :as sessionservice]
            [modern-cljs.controller.registrationcontroller :as registrationcontroller]))

;; defroutes macro defines a function that chains individual route
;; functions together. The request map is passed to each function in
;; turn, until a non-nil response is returned.
(defroutes app-routes
           ; to serve document root address
           (GET "/" [] (layout/application "Home" (views/news-list-view)))
           (GET "/news/:id" [id] (layout/application "Read News" (views/browse-news-view id)))
           (GET "/add-news" [] (layout/application "Add News Page" (views/add-news-view)))
           (GET "/registration" [] (layout/application "Registration Page" (views/registration-view)))

           (route/resources "/") ; to serve static pages saved in resources/public directory
           (route/not-found "Page not found")) ; if page is not found

(defroutes api-routes
   (context "/api" []
     (POST "/add-news" [] add-news-service/add-news)
     (POST "/add-comment" [] browse-news-service/add-comment)
     (POST "/delete-comment" [] browse-news-service/remove-comment)
     (POST "/register" [] registrationcontroller/registerUser)))

(defn wrap-log-request [handler]
  (fn [req] ; return handler function
    (println req) ; perform logging
    (handler req))) ; pass the request through to the inner handler

;(def session-atom (atom {}))

;; site function creates a handler suitable for a standard website,
;; adding a bunch of standard ring middleware to app-route:
(def app-handler
  (-> app-routes
      ;wrap-log-request
      (ring-session/wrap-session {:store (session-memory/memory-store (sessionservice/getSessionMemory))}))) ;http://stackoverflow.com/questions/16269537/how-to-debug-the-ring-session-store

(def api-handler
  (-> (handler/api api-routes)
      (ring-session/wrap-session {:store (session-memory/memory-store (sessionservice/getSessionMemory))})))

(def handler
  (routes                                                   ;;got it here http://stackoverflow.com/questions/30303256/registering-multiple-handlers-while-running-server
    api-handler
    app-handler
))

