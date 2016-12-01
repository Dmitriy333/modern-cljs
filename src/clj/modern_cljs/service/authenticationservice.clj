(ns modern-cljs.service.authenticationservice
  (:require [ring.util.response :as response]
            [digest :as d]
            [modern-cljs.repository.userrepository :as userrepo]
            [modern-cljs.views.views :as views]
            [modern-cljs.views.layout :as layout]
            [modern-cljs.dsl.dsltaskexecutor :as de]
            [modern-cljs.repository.dslrepository :as dr]
            ))

(defn add-user-attribute-to-response [authdetails response request]
  (assoc response :session (-> (:session request)
                               (assoc :role (:role authdetails))
                               (assoc :login (:login authdetails))
                               (assoc :email (:email authdetails)))))

(defn login [request]
  (let [email (get-in request [:params :email])
        password (get-in request [:params :password])]
    (let [userRs (userrepo/find-by-email-and-hash userrepo/userRepositoryComponent email (d/md5 password))]
      (if (empty? userRs)
        (layout/application "Incorrect credentials" (views/login-view))
        (do
          (let [rule (dr/find-rule "user" "login")]
            (de/execute-task (:task rule ) "user" "dmitriybrashevetsgmailcom" "user has been logged in"))

          (-> (first userRs)
                (add-user-attribute-to-response (response/redirect "/") request))

          )))))

(defn logout [request]
  (add-user-attribute-to-response {} (response/redirect "/") request ))

