(ns modern-cljs.core
  (:use ring.util.response)
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [compojure.route :as route]
            [ring.middleware.cors :refer [wrap-cors]]
            [modern-cljs.views.views :as views]
            [modern-cljs.views.layout :as layout]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.session :as ss]
            [modern-cljs.service.authenticationservice :as authservice]))

(defroutes app-routes
     (GET "/" [] (layout/application "Home" (views/news-list-view)))
     (GET "/news/:id" [id] (layout/application "Read News" (views/browse-news-view id)))
     (GET "/add-news" [] (layout/application "Add News Page" (views/add-news-view)))
     (GET "/registration" [] (layout/application "Registration Page" (views/registration-view)))
     (GET "/login" [] (layout/application "Login Page" (views/login-view)))
     (GET "/logout" request (authservice/logout request))


     (POST "/login" request (authservice/login request))

     (route/resources "/")                            ; to serve static pages saved in resources/public directory
     (route/not-found "Page not found")               ; if page is not found
     )

(defn wrap-log-request [handler]
  (fn [req]                                                 ; return handler function
    (println req)                                           ; perform logging
    (handler req)))                                         ; pass the request through to the inner handler

(def handler
  (-> (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false))
      ;wrap-log-request
      (handler/site)
      (ss/wrap-session)))